package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.PlanTratamiento;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

// Guarda propuestas y respuestas de los planes.
public class PlanTratamientoDAO {

    private static final String SELECT_PLAN = "SELECT pl.id_plan, c.id_paciente, pl.id_odontologo_proponente, "
            + "CONCAT_WS(' ', pa.nombres, pa.apellido_paterno, pa.apellido_materno) AS paciente, "
            + "CONCAT_WS(' ', pa.tipo_documento, pa.numero_documento) AS documento, "
            + "CONCAT_WS(' ', po.nombres, po.apellido_paterno) AS odontologo, "
            + "pl.estado, COALESCE(pl.numero_sesiones_estimadas, 1) AS sesiones, "
            + "COALESCE(SUM(d.cantidad * d.precio_unitario), 0) AS total, "
            + "(SELECT COALESCE(MAX(sp.numero_sesion), 0) + 1 FROM sesiones_plan sp WHERE sp.id_plan = pl.id_plan) AS siguiente "
            + "FROM planes_tratamiento pl JOIN atenciones a ON a.id_atencion = pl.id_atencion "
            + "JOIN citas c ON c.id_cita = a.id_cita JOIN personas pa ON pa.id_persona = c.id_paciente "
            + "JOIN usuarios u ON u.id_usuario = pl.id_odontologo_proponente "
            + "JOIN personas po ON po.id_persona = u.id_persona "
            + "LEFT JOIN plan_tratamiento_detalles d ON d.id_plan = pl.id_plan ";
    private static final String GROUP_PLAN = " GROUP BY pl.id_plan, c.id_paciente, pl.id_odontologo_proponente, "
            + "pa.nombres, pa.apellido_paterno, pa.apellido_materno, pa.tipo_documento, pa.numero_documento, "
            + "po.nombres, po.apellido_paterno, po.apellido_materno, pl.estado, pl.numero_sesiones_estimadas, pl.propuesto_en";

    public long proponerDesdeAtencion(long idAtencion, long idOdontologo, int sesiones)
            throws PersistenciaException {
        if (sesiones < 1 || sesiones > 24) {
            throw new IllegalArgumentException("El plan debe tener entre 1 y 24 sesiones estimadas.");
        }
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                validarAtencion(conexion, idAtencion, idOdontologo);
                try (PreparedStatement buscar = conexion.prepareStatement(
                        "SELECT COUNT(*) FROM planes_tratamiento WHERE id_atencion = ? "
                        + "AND estado IN ('PENDIENTE', 'ACEPTADO', 'EN_CURSO')")) {
                    buscar.setLong(1, idAtencion);
                    try (ResultSet resultado = buscar.executeQuery()) {
                        resultado.next();
                        if (resultado.getInt(1) > 0) {
                            throw new IllegalArgumentException("Esta atención ya tiene un plan pendiente o activo.");
                        }
                    }
                }

                long idPlan;
                String insertarPlan = "INSERT INTO planes_tratamiento "
                        + "(id_atencion, id_odontologo_proponente, numero_sesiones_estimadas, estado) "
                        + "VALUES (?, ?, ?, 'PENDIENTE')";
                try (PreparedStatement insertar = conexion.prepareStatement(insertarPlan, Statement.RETURN_GENERATED_KEYS)) {
                    insertar.setLong(1, idAtencion);
                    insertar.setLong(2, idOdontologo);
                    insertar.setInt(3, sesiones);
                    insertar.executeUpdate();
                    try (ResultSet claves = insertar.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("No se recibió el número del plan.");
                        }
                        idPlan = claves.getLong(1);
                    }
                }

                String copiarProcedimientos = "INSERT INTO plan_tratamiento_detalles "
                        + "(id_plan, id_servicio, pieza_fdi, superficie, sesion_sugerida, cantidad, precio_unitario) "
                        + "SELECT ?, id_servicio, pieza_fdi, superficie, NULL, cantidad, precio_unitario "
                        + "FROM procedimientos_atencion WHERE id_atencion = ?";
                try (PreparedStatement copiar = conexion.prepareStatement(copiarProcedimientos)) {
                    copiar.setLong(1, idPlan);
                    copiar.setLong(2, idAtencion);
                    if (copiar.executeUpdate() == 0) {
                        throw new IllegalArgumentException("Agregue al menos un procedimiento antes de proponer el plan.");
                    }
                }
                conexion.commit();
                return idPlan;
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo crear la propuesta de tratamiento.", error);
        }
    }

    private void validarAtencion(Connection conexion, long idAtencion, long idOdontologo) throws SQLException {
        String consulta = "SELECT a.estado, c.id_odontologo, c.estado AS estado_cita "
                + "FROM atenciones a JOIN citas c ON c.id_cita = a.id_cita "
                + "WHERE a.id_atencion = ? FOR UPDATE";
        try (PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, idAtencion);
            try (ResultSet resultado = buscar.executeQuery()) {
                if (!resultado.next() || !"ABIERTA".equals(resultado.getString("estado"))) {
                    throw new IllegalArgumentException("La atención ya terminó o no existe.");
                }
                if (resultado.getLong("id_odontologo") != idOdontologo) {
                    throw new IllegalArgumentException("Solo el odontólogo de la cita puede proponer el plan.");
                }
                if (!"EN_ATENCION".equals(resultado.getString("estado_cita"))) {
                    throw new IllegalArgumentException("La cita ya no está en atención.");
                }
            }
        }
    }

    public ArrayList<PlanTratamiento> listarDelOdontologo(long idOdontologo) throws PersistenciaException {
        return listar("WHERE pl.id_odontologo_proponente = ?", idOdontologo);
    }

    public ArrayList<PlanTratamiento> listarPendientes() throws PersistenciaException {
        return listar("WHERE pl.estado = 'PENDIENTE'", null);
    }

    private ArrayList<PlanTratamiento> listar(String filtro, Long idOdontologo) throws PersistenciaException {
        ArrayList<PlanTratamiento> planes = new ArrayList<>();
        String consulta = SELECT_PLAN + filtro + GROUP_PLAN + " ORDER BY pl.propuesto_en DESC";
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            if (idOdontologo != null) {
                buscar.setLong(1, idOdontologo);
            }
            try (ResultSet resultado = buscar.executeQuery()) {
                while (resultado.next()) {
                    planes.add(mapear(resultado));
                }
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar los planes de tratamiento.", error);
        }
        return planes;
    }

    public void responder(long idPlan, boolean aceptar, long idUsuario) throws PersistenciaException {
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                int sesiones;
                long idPaciente;
                BigDecimal total;
                try (PreparedStatement buscar = conexion.prepareStatement(
                        "SELECT COALESCE(pl.numero_sesiones_estimadas, 1) AS sesiones, c.id_paciente "
                        + "FROM planes_tratamiento pl JOIN atenciones a ON a.id_atencion = pl.id_atencion "
                        + "JOIN citas c ON c.id_cita = a.id_cita "
                        + "WHERE pl.id_plan = ? AND pl.estado = 'PENDIENTE' FOR UPDATE")) {
                    buscar.setLong(1, idPlan);
                    try (ResultSet resultado = buscar.executeQuery()) {
                        if (!resultado.next()) {
                            throw new IllegalArgumentException("La propuesta ya fue respondida o no existe.");
                        }
                        sesiones = resultado.getInt("sesiones");
                        idPaciente = resultado.getLong("id_paciente");
                    }
                }
                try (PreparedStatement buscar = conexion.prepareStatement(
                        "SELECT COALESCE(SUM(cantidad * precio_unitario), 0) FROM plan_tratamiento_detalles WHERE id_plan = ?")) {
                    buscar.setLong(1, idPlan);
                    try (ResultSet resultado = buscar.executeQuery()) {
                        resultado.next();
                        total = resultado.getBigDecimal(1);
                    }
                }
                String estado = aceptar ? "ACEPTADO" : "RECHAZADO";
                try (PreparedStatement actualizar = conexion.prepareStatement(
                        "UPDATE planes_tratamiento SET estado = ?, id_usuario_respuesta = ?, respondido_en = CURRENT_TIMESTAMP "
                        + "WHERE id_plan = ? AND estado = 'PENDIENTE'")) {
                    actualizar.setString(1, estado);
                    actualizar.setLong(2, idUsuario);
                    actualizar.setLong(3, idPlan);
                    if (actualizar.executeUpdate() != 1) {
                        throw new IllegalArgumentException("No se pudo responder la propuesta.");
                    }
                }
                try (PreparedStatement actividad = conexion.prepareStatement(
                        "INSERT INTO actividades (id_usuario, id_paciente, tipo_evento, descripcion) VALUES (?, ?, 'OTRO', ?)")) {
                    actividad.setLong(1, idUsuario);
                    actividad.setLong(2, idPaciente);
                    actividad.setString(3, "Plan " + idPlan + " " + (aceptar ? "aceptado" : "rechazado") + " por recepción.");
                    actividad.executeUpdate();
                }
                if (aceptar && total != null && total.signum() > 0) {
                    crearCuotas(conexion, idPlan, total, sesiones);
                }
                conexion.commit();
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo guardar la respuesta del plan.", error);
        }
    }

    private void crearCuotas(Connection conexion, long idPlan, BigDecimal total, int sesiones) throws SQLException {
        long centavos = total.movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValueExact();
        int cantidad = (int) Math.min(Math.max(1, sesiones), centavos);
        if (cantidad < 1) {
            return;
        }
        long base = centavos / cantidad;
        long sobrantes = centavos % cantidad;
        String insertarSql = "INSERT INTO cuotas_plan (id_plan, numero_cuota, fecha_vencimiento, monto, estado) "
                + "VALUES (?, ?, ?, ?, 'PENDIENTE')";
        try (PreparedStatement insertar = conexion.prepareStatement(insertarSql)) {
            for (int i = 1; i <= cantidad; i++) {
                long montoCentavos = base + (i <= sobrantes ? 1 : 0);
                insertar.setLong(1, idPlan);
                insertar.setInt(2, i);
                insertar.setDate(3, Date.valueOf(LocalDate.now().plusDays(30L * i)));
                insertar.setBigDecimal(4, BigDecimal.valueOf(montoCentavos, 2));
                insertar.addBatch();
            }
            insertar.executeBatch();
        }
    }

    private PlanTratamiento mapear(ResultSet resultado) throws SQLException {
        PlanTratamiento plan = new PlanTratamiento();
        plan.setIdPlan(resultado.getLong("id_plan"));
        plan.setIdPaciente(resultado.getLong("id_paciente"));
        plan.setIdOdontologo(resultado.getLong("id_odontologo_proponente"));
        plan.setPaciente(resultado.getString("paciente"));
        plan.setDocumento(resultado.getString("documento"));
        plan.setOdontologo(resultado.getString("odontologo"));
        plan.setEstado(resultado.getString("estado"));
        plan.setSesionesEstimadas(resultado.getInt("sesiones"));
        plan.setSiguienteSesion(resultado.getInt("siguiente"));
        plan.setTotal(resultado.getBigDecimal("total"));
        return plan;
    }
}
