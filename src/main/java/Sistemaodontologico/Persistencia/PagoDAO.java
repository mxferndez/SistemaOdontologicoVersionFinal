package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Pago;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

// Guarda pagos y muestra los pagos de una persona.
public class PagoDAO {

    public ArrayList<Pago> listarPorPaciente(long idPaciente, String concepto, String estado)
            throws PersistenciaException {
        String consulta = "SELECT p.id_pago, p.id_cita, p.id_cuota, p.fecha_pago, p.monto, "
                + "p.metodo_pago, p.referencia, p.estado, "
                + "CASE WHEN p.id_cita IS NOT NULL THEN CONCAT('Cita #', p.id_cita) "
                + "ELSE CONCAT('Cuota #', q.numero_cuota) END AS concepto "
                + "FROM pagos p LEFT JOIN citas c ON c.id_cita = p.id_cita "
                + "LEFT JOIN cuotas_plan q ON q.id_cuota = p.id_cuota "
                + "LEFT JOIN planes_tratamiento pl ON pl.id_plan = q.id_plan "
                + "LEFT JOIN atenciones a ON a.id_atencion = pl.id_atencion "
                + "LEFT JOIN citas cp ON cp.id_cita = a.id_cita "
                + "WHERE (c.id_paciente = ? OR cp.id_paciente = ?) ";
        if (concepto != null && !concepto.equals("TODOS")) {
            consulta += concepto.equals("CITAS") ? "AND p.id_cita IS NOT NULL " : "AND p.id_cuota IS NOT NULL ";
        }
        if (estado != null && !estado.equals("TODOS")) {
            consulta += "AND p.estado = ? ";
        }
        consulta += "ORDER BY p.fecha_pago DESC, p.id_pago DESC";
        ArrayList<Pago> pagos = new ArrayList<>();

        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, idPaciente);
            buscar.setLong(2, idPaciente);
            if (estado != null && !estado.equals("TODOS")) {
                buscar.setString(3, estado);
            }
            try (ResultSet resultado = buscar.executeQuery()) {
                while (resultado.next()) {
                    Pago pago = new Pago();
                    pago.setIdPago(resultado.getLong("id_pago"));
                    pago.setIdCita(resultado.getLong("id_cita"));
                    pago.setIdCuota(resultado.getLong("id_cuota"));
                    Timestamp fecha = resultado.getTimestamp("fecha_pago");
                    pago.setFecha(fecha == null ? null : fecha.toLocalDateTime());
                    pago.setMonto(resultado.getBigDecimal("monto"));
                    pago.setMetodo(resultado.getString("metodo_pago"));
                    pago.setReferencia(resultado.getString("referencia"));
                    pago.setEstado(resultado.getString("estado"));
                    pago.setConcepto(resultado.getString("concepto"));
                    pagos.add(pago);
                }
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar el estado de cuenta.", error);
        }
        return pagos;
    }

    public ArrayList<Object[]> listarDelDia(java.time.LocalDate fecha) throws PersistenciaException {
        ArrayList<Object[]> filas = new ArrayList<>();
        String consulta = "SELECT p.id_pago, p.fecha_pago, p.monto, p.metodo_pago, p.estado, "
                + "CONCAT_WS(' ', persona.nombres, persona.apellido_paterno, persona.apellido_materno) AS paciente, "
                + "CASE WHEN p.id_cita IS NOT NULL THEN CONCAT('Cita #', p.id_cita) "
                + "ELSE CONCAT('Cuota #', q.numero_cuota) END AS concepto "
                + "FROM pagos p LEFT JOIN citas c ON c.id_cita = p.id_cita "
                + "LEFT JOIN cuotas_plan q ON q.id_cuota = p.id_cuota "
                + "LEFT JOIN planes_tratamiento pl ON pl.id_plan = q.id_plan "
                + "LEFT JOIN atenciones a ON a.id_atencion = pl.id_atencion "
                + "LEFT JOIN citas cp ON cp.id_cita = a.id_cita "
                + "LEFT JOIN personas persona ON persona.id_persona = COALESCE(c.id_paciente, cp.id_paciente) "
                + "WHERE DATE(p.fecha_pago) = ? ORDER BY p.fecha_pago DESC";
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setDate(1, java.sql.Date.valueOf(fecha));
            try (ResultSet resultado = buscar.executeQuery()) {
                while (resultado.next()) {
                    filas.add(new Object[]{resultado.getLong("id_pago"), resultado.getTimestamp("fecha_pago"),
                        resultado.getString("paciente"), resultado.getString("concepto"),
                        resultado.getBigDecimal("monto"), resultado.getString("metodo_pago"), resultado.getString("estado")});
                }
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar los pagos del día.", error);
        }
        return filas;
    }

    public void crear(long idUsuario, Pago pago) throws PersistenciaException {
        if ((pago.getIdCita() > 0) == (pago.getIdCuota() > 0)) {
            throw new IllegalArgumentException("El pago debe corresponder a una cita o a una cuota.");
        }
        if (pago.getMonto() == null || pago.getMonto().signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        String insertarSql = "INSERT INTO pagos (id_usuario_registra, id_cita, id_cuota, monto, "
                + "metodo_pago, referencia, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                if (pago.getIdCita() > 0) {
                    validarSaldoCita(conexion, pago);
                } else {
                    validarSaldoCuota(conexion, pago);
                }

                try (PreparedStatement insertar = conexion.prepareStatement(insertarSql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    insertar.setLong(1, idUsuario);
                    if (pago.getIdCita() > 0) {
                        insertar.setLong(2, pago.getIdCita());
                        insertar.setNull(3, java.sql.Types.BIGINT);
                    } else {
                        insertar.setNull(2, java.sql.Types.BIGINT);
                        insertar.setLong(3, pago.getIdCuota());
                    }
                    insertar.setBigDecimal(4, pago.getMonto());
                    insertar.setString(5, pago.getMetodo());
                    insertar.setString(6, pago.getReferencia());
                    insertar.setString(7, "Pago registrado desde caja.");
                    insertar.executeUpdate();
                    try (ResultSet claves = insertar.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("No se recibió el número del pago.");
                        }
                        long idPago = claves.getLong(1);
                        long idPaciente = pacienteDePago(conexion, pago);
                        try (PreparedStatement actividad = conexion.prepareStatement(
                                "INSERT INTO actividades (id_usuario, id_paciente, id_cita, id_pago, tipo_evento, descripcion) VALUES (?, ?, ?, ?, 'PAGO_REGISTRADO', ?)")) {
                            actividad.setLong(1, idUsuario);
                            actividad.setLong(2, idPaciente);
                            if (pago.getIdCita() > 0) {
                                actividad.setLong(3, pago.getIdCita());
                            } else {
                                actividad.setNull(3, java.sql.Types.BIGINT);
                            }
                            actividad.setLong(4, idPago);
                            actividad.setString(5, "Se registró un pago por " + pago.getMonto() + ".");
                            actividad.executeUpdate();
                        }
                    }
                }
                if (pago.getIdCuota() > 0) {
                    actualizarEstadoCuota(conexion, pago.getIdCuota());
                }
                conexion.commit();
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo registrar el pago.", error);
        }
    }

    private long pacienteDePago(Connection conexion, Pago pago) throws SQLException {
        String consulta = pago.getIdCita() > 0
                ? "SELECT id_paciente FROM citas WHERE id_cita = ?"
                : "SELECT c.id_paciente FROM cuotas_plan q JOIN planes_tratamiento pl ON pl.id_plan = q.id_plan "
                + "JOIN atenciones a ON a.id_atencion = pl.id_atencion JOIN citas c ON c.id_cita = a.id_cita WHERE q.id_cuota = ?";
        try (PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, pago.getIdCita() > 0 ? pago.getIdCita() : pago.getIdCuota());
            try (ResultSet resultado = buscar.executeQuery()) {
                if (!resultado.next()) {
                    throw new SQLException("No se encontró al paciente asociado al pago.");
                }
                return resultado.getLong(1);
            }
        }
    }

    private void validarSaldoCita(Connection conexion, Pago pago) throws SQLException {
        String consulta = "SELECT GREATEST(COALESCE((SELECT SUM(pr.cantidad * pr.precio_unitario) "
                + "FROM procedimientos_atencion pr JOIN atenciones a ON a.id_atencion = pr.id_atencion "
                + "WHERE a.id_cita = c.id_cita), s.precio, 0) - COALESCE((SELECT SUM(pg.monto) "
                + "FROM pagos pg WHERE pg.id_cita = c.id_cita AND pg.estado = 'REGISTRADO'), 0), 0) AS saldo "
                + "FROM citas c LEFT JOIN servicios s ON s.id_servicio = c.id_servicio "
                + "WHERE c.id_cita = ? AND c.estado = 'ATENDIDA' FOR UPDATE";
        try (PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, pago.getIdCita());
            try (ResultSet resultado = buscar.executeQuery()) {
                if (!resultado.next()) {
                    throw new IllegalArgumentException("La cita no está lista para cobrar.");
                }
                if (resultado.getBigDecimal("saldo").compareTo(pago.getMonto()) < 0) {
                    throw new IllegalArgumentException("El monto supera el saldo pendiente de la cita.");
                }
            }
        }
    }

    private void validarSaldoCuota(Connection conexion, Pago pago) throws SQLException {
        String consulta = "SELECT q.monto - COALESCE((SELECT SUM(pg.monto) FROM pagos pg "
                + "WHERE pg.id_cuota = q.id_cuota AND pg.estado = 'REGISTRADO'), 0) AS saldo "
                + "FROM cuotas_plan q WHERE q.id_cuota = ? FOR UPDATE";
        try (PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, pago.getIdCuota());
            try (ResultSet resultado = buscar.executeQuery()) {
                if (!resultado.next()) {
                    throw new IllegalArgumentException("La cuota no existe.");
                }
                if (resultado.getBigDecimal("saldo").compareTo(pago.getMonto()) < 0) {
                    throw new IllegalArgumentException("El monto supera el saldo pendiente de la cuota.");
                }
            }
        }
    }

    private void actualizarEstadoCuota(Connection conexion, long idCuota) throws SQLException {
        String consulta = "UPDATE cuotas_plan SET estado = CASE "
                + "WHEN (SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE id_cuota = ? AND estado = 'REGISTRADO') >= monto "
                + "THEN 'PAGADA' WHEN (SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE id_cuota = ? AND estado = 'REGISTRADO') > 0 "
                + "THEN 'PARCIAL' ELSE 'PENDIENTE' END WHERE id_cuota = ?";
        try (PreparedStatement actualizar = conexion.prepareStatement(consulta)) {
            actualizar.setLong(1, idCuota);
            actualizar.setLong(2, idCuota);
            actualizar.setLong(3, idCuota);
            actualizar.executeUpdate();
        }
    }
}
