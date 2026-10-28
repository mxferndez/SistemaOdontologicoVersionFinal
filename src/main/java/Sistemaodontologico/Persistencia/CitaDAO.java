package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Cita;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

// Lee las citas y aplica reglas básicas para reservar y cambiar su estado.
public class CitaDAO {

    private static final String SELECT_CITA = "SELECT c.id_cita, c.id_paciente, c.id_odontologo, "
            + "c.id_consultorio, c.id_servicio, c.id_cita_anterior, c.fecha_hora_inicio, c.hora_llegada, c.duracion_min, c.canal, "
            + "c.tipo_cita, c.estado, c.observaciones, p.tipo_documento, p.numero_documento, "
            + "CONCAT_WS(' ', p.nombres, p.apellido_paterno, p.apellido_materno) AS paciente, "
            + "CONCAT_WS(' ', po.nombres, po.apellido_paterno) AS odontologo, "
            + "co.nombre AS consultorio FROM citas c "
            + "JOIN personas p ON p.id_persona = c.id_paciente "
            + "JOIN usuarios u ON u.id_usuario = c.id_odontologo "
            + "JOIN personas po ON po.id_persona = u.id_persona "
            + "JOIN consultorios co ON co.id_consultorio = c.id_consultorio ";

    public ArrayList<Cita> listarPendientesCobro() throws PersistenciaException {
        String consulta = "SELECT c.id_cita, c.id_paciente, c.id_odontologo, c.id_consultorio, c.id_cita_anterior, c.observaciones, "
                + "c.fecha_hora_inicio, c.hora_llegada, c.duracion_min, c.id_servicio, c.canal, c.tipo_cita, c.estado, "
                + "p.tipo_documento, p.numero_documento, CONCAT_WS(' ', p.nombres, p.apellido_paterno, p.apellido_materno) AS paciente, "
                + "CONCAT_WS(' ', po.nombres, po.apellido_paterno) AS odontologo, co.nombre AS consultorio, "
                + "GREATEST(COALESCE((SELECT SUM(pr.cantidad * pr.precio_unitario) FROM procedimientos_atencion pr "
                + "JOIN atenciones at ON at.id_atencion = pr.id_atencion WHERE at.id_cita = c.id_cita), s.precio, 0) "
                + "- COALESCE((SELECT SUM(pg.monto) FROM pagos pg WHERE pg.id_cita = c.id_cita AND pg.estado = 'REGISTRADO'), 0), 0) AS saldo "
                + "FROM citas c JOIN personas p ON p.id_persona = c.id_paciente "
                + "JOIN usuarios u ON u.id_usuario = c.id_odontologo JOIN personas po ON po.id_persona = u.id_persona "
                + "JOIN consultorios co ON co.id_consultorio = c.id_consultorio LEFT JOIN servicios s ON s.id_servicio = c.id_servicio "
                + "WHERE c.estado = 'ATENDIDA' HAVING saldo > 0 ORDER BY c.fecha_hora_inicio";
        ArrayList<Cita> citas = new ArrayList<>();
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta); ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                Cita cita = mapear(resultado);
                cita.setSaldoPendiente(resultado.getBigDecimal("saldo"));
                citas.add(cita);
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar los cobros pendientes.", error);
        }
        return citas;
    }

    public Cita buscarPorId(long idCita) throws PersistenciaException {
        String consulta = SELECT_CITA + "WHERE c.id_cita = ?";
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, idCita);
            try (ResultSet resultado = buscar.executeQuery()) {
                if (!resultado.next()) {
                    return null;
                }
                return mapear(resultado);
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo buscar la cita.", error);
        }
    }

    public ArrayList<Cita> listarDia(LocalDate fecha, long idOdontologo, long idConsultorio)
            throws PersistenciaException {
        String consulta = SELECT_CITA + "WHERE DATE(c.fecha_hora_inicio) = ? ";
        if (idOdontologo > 0) {
            consulta += "AND c.id_odontologo = ? ";
        }
        if (idConsultorio > 0) {
            consulta += "AND c.id_consultorio = ? ";
        }
        consulta += "ORDER BY c.fecha_hora_inicio";
        ArrayList<Cita> citas = new ArrayList<>();
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            int posicion = 1;
            buscar.setDate(posicion++, Date.valueOf(fecha));
            if (idOdontologo > 0) {
                buscar.setLong(posicion++, idOdontologo);
            }
            if (idConsultorio > 0) {
                buscar.setLong(posicion, idConsultorio);
            }
            try (ResultSet resultado = buscar.executeQuery()) {
                while (resultado.next()) {
                    citas.add(mapear(resultado));
                }
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar la agenda.", error);
        }
        return citas;
    }

    public ArrayList<String> listarPacientesActivos() throws PersistenciaException {
        return listarOpciones("SELECT p.id_persona, CONCAT(p.tipo_documento, ' ', p.numero_documento, ' - ', "
                + "p.nombres, ' ', p.apellido_paterno, ' ', COALESCE(p.apellido_materno, '')) AS nombre "
                + "FROM pacientes pa JOIN personas p ON p.id_persona = pa.id_persona "
                + "WHERE pa.estado = 'ACTIVO' ORDER BY p.apellido_paterno, p.nombres");
    }

    public ArrayList<String> listarOdontologosActivos() throws PersistenciaException {
        return listarOpciones("SELECT o.id_usuario, CONCAT_WS(' ', p.nombres, p.apellido_paterno) AS nombre "
                + "FROM odontologos o JOIN usuarios u ON u.id_usuario = o.id_usuario "
                + "JOIN personas p ON p.id_persona = u.id_persona WHERE u.estado_laboral = 'ACTIVO' "
                + "AND u.estado_acceso = 'HABILITADO' ORDER BY p.apellido_paterno, p.nombres");
    }

    public ArrayList<String> listarConsultoriosActivos() throws PersistenciaException {
        return listarOpciones("SELECT id_consultorio, nombre FROM consultorios "
                + "WHERE estado = 'ACTIVO' ORDER BY nombre");
    }

    private ArrayList<String> listarOpciones(String consulta) throws PersistenciaException {
        ArrayList<String> opciones = new ArrayList<>();
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta); ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                opciones.add(resultado.getLong(1) + " - " + resultado.getString(2));
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar las opciones de la agenda.", error);
        }
        return opciones;
    }

    public void crear(Cita cita, long idUsuario) throws PersistenciaException {
        validarDatos(cita);
        String consulta = "INSERT INTO citas (id_paciente, id_odontologo, id_consultorio, "
                + "id_cita_anterior, creado_por_usuario, fecha_hora_inicio, duracion_min, canal, "
                + "tipo_cita, estado, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 'PROGRAMADA', ?)";
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                validarHorario(conexion, cita);
                try (PreparedStatement insertar = conexion.prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS)) {
                    insertar.setLong(1, cita.getIdPaciente());
                    insertar.setLong(2, cita.getIdOdontologo());
                    insertar.setLong(3, cita.getIdConsultorio());
                    if (cita.getIdCitaAnterior() > 0) {
                        insertar.setLong(4, cita.getIdCitaAnterior());
                    } else {
                        insertar.setNull(4, java.sql.Types.BIGINT);
                    }
                    insertar.setLong(5, idUsuario);
                    insertar.setTimestamp(6, Timestamp.valueOf(cita.getInicio()));
                    insertar.setInt(7, cita.getDuracionMinutos());
                    insertar.setString(8, normalizarCanal(cita.getCanal()));
                    insertar.setString(9, normalizarTipo(cita.getTipo()));
                    insertar.setString(10, cita.getObservaciones());
                    insertar.executeUpdate();
                    try (ResultSet claves = insertar.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("No se recibió el número de la cita.");
                        }
                        long idCita = claves.getLong(1);
                        insertarActividad(conexion, idUsuario, cita.getIdPaciente(), idCita, "CITA_CREADA",
                                "Se registró una cita para " + cita.getInicio().toString().replace('T', ' '));
                    }
                }
                conexion.commit();
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo registrar la cita. Revise la fecha y la disponibilidad.", error);
        }
    }

    public long crearSesionPlan(Cita cita, long idUsuario, long idPlan) throws PersistenciaException {
        validarDatos(cita);
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                int sesionesEstimadas;
                String estadoPlan;
                long pacientePlan;
                long odontologoPlan;
                String planSql = "SELECT pl.estado, COALESCE(pl.numero_sesiones_estimadas, 1) AS sesiones, "
                        + "c.id_paciente, c.id_odontologo FROM planes_tratamiento pl "
                        + "JOIN atenciones a ON a.id_atencion = pl.id_atencion "
                        + "JOIN citas c ON c.id_cita = a.id_cita WHERE pl.id_plan = ? FOR UPDATE";
                try (PreparedStatement buscar = conexion.prepareStatement(planSql)) {
                    buscar.setLong(1, idPlan);
                    try (ResultSet resultado = buscar.executeQuery()) {
                        if (!resultado.next()) {
                            throw new IllegalArgumentException("El plan no existe.");
                        }
                        estadoPlan = resultado.getString("estado");
                        sesionesEstimadas = resultado.getInt("sesiones");
                        pacientePlan = resultado.getLong("id_paciente");
                        odontologoPlan = resultado.getLong("id_odontologo");
                    }
                }
                if (!"ACEPTADO".equals(estadoPlan) && !"EN_CURSO".equals(estadoPlan)) {
                    throw new IllegalArgumentException("Solo se pueden programar sesiones de un plan aceptado.");
                }
                if (cita.getIdPaciente() != pacientePlan || cita.getIdOdontologo() != odontologoPlan) {
                    throw new IllegalArgumentException("La nueva cita debe usar el paciente y odontólogo del plan.");
                }
                int numeroSesion;
                try (PreparedStatement buscar = conexion.prepareStatement(
                        "SELECT COALESCE(MAX(numero_sesion), 0) + 1 FROM sesiones_plan WHERE id_plan = ?")) {
                    buscar.setLong(1, idPlan);
                    try (ResultSet resultado = buscar.executeQuery()) {
                        resultado.next();
                        numeroSesion = resultado.getInt(1);
                    }
                }
                if (numeroSesion > sesionesEstimadas) {
                    throw new IllegalArgumentException("Ya se programaron todas las sesiones estimadas del plan.");
                }
                validarHorario(conexion, cita);

                long idCita;
                String insertarSql = "INSERT INTO citas (id_paciente, id_odontologo, id_consultorio, creado_por_usuario, "
                        + "fecha_hora_inicio, duracion_min, canal, tipo_cita, estado, observaciones) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'PROGRAMADA', ?)";
                try (PreparedStatement insertar = conexion.prepareStatement(insertarSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertar.setLong(1, cita.getIdPaciente());
                    insertar.setLong(2, cita.getIdOdontologo());
                    insertar.setLong(3, cita.getIdConsultorio());
                    insertar.setLong(4, idUsuario);
                    insertar.setTimestamp(5, Timestamp.valueOf(cita.getInicio()));
                    insertar.setInt(6, cita.getDuracionMinutos());
                    insertar.setString(7, normalizarCanal(cita.getCanal()));
                    insertar.setString(8, "TRATAMIENTO");
                    insertar.setString(9, cita.getObservaciones());
                    insertar.executeUpdate();
                    try (ResultSet claves = insertar.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("No se recibió el número de la cita.");
                        }
                        idCita = claves.getLong(1);
                    }
                }
                try (PreparedStatement vincular = conexion.prepareStatement(
                        "INSERT INTO sesiones_plan (id_plan, id_cita, numero_sesion) VALUES (?, ?, ?)")) {
                    vincular.setLong(1, idPlan);
                    vincular.setLong(2, idCita);
                    vincular.setInt(3, numeroSesion);
                    vincular.executeUpdate();
                }
                try (PreparedStatement actualizar = conexion.prepareStatement(
                        "UPDATE planes_tratamiento SET estado = 'EN_CURSO' WHERE id_plan = ? AND estado = 'ACEPTADO'")) {
                    actualizar.setLong(1, idPlan);
                    actualizar.executeUpdate();
                }
                conexion.commit();
                return idCita;
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo programar la sesión. Revise la disponibilidad.", error);
        }
    }

    public long reprogramar(long idCitaAnterior, Cita nuevaCita, long idUsuario, String motivo)
            throws PersistenciaException {
        validarDatos(nuevaCita);
        if (idCitaAnterior <= 0) {
            throw new IllegalArgumentException("Seleccione la cita que desea reprogramar.");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Escriba el motivo de la reprogramación.");
        }
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                long pacienteAnterior;
                long odontologoAnterior;
                long servicioAnterior;
                String estadoAnterior;
                try (PreparedStatement buscar = conexion.prepareStatement(
                        "SELECT id_paciente, id_odontologo, id_servicio, estado FROM citas WHERE id_cita = ? FOR UPDATE")) {
                    buscar.setLong(1, idCitaAnterior);
                    try (ResultSet resultado = buscar.executeQuery()) {
                        if (!resultado.next()) {
                            throw new IllegalArgumentException("La cita original no existe.");
                        }
                        pacienteAnterior = resultado.getLong("id_paciente");
                        odontologoAnterior = resultado.getLong("id_odontologo");
                        servicioAnterior = resultado.getLong("id_servicio");
                        if (resultado.wasNull()) {
                            servicioAnterior = 0;
                        }
                        estadoAnterior = resultado.getString("estado");
                    }
                }
                if (!"PROGRAMADA".equals(estadoAnterior) && !"CONFIRMADA".equals(estadoAnterior)) {
                    throw new IllegalArgumentException("Solo se pueden reprogramar citas programadas o confirmadas.");
                }
                nuevaCita.setIdPaciente(pacienteAnterior);
                nuevaCita.setIdOdontologo(odontologoAnterior);
                nuevaCita.setIdServicio(servicioAnterior);
                nuevaCita.setIdCitaAnterior(idCitaAnterior);
                validarHorario(conexion, nuevaCita);

                long idNuevaCita;
                String insertarSql = "INSERT INTO citas (id_paciente, id_odontologo, id_consultorio, id_servicio, id_cita_anterior, "
                        + "creado_por_usuario, fecha_hora_inicio, duracion_min, canal, tipo_cita, estado, observaciones) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'PROGRAMADA', ?)";
                try (PreparedStatement insertar = conexion.prepareStatement(insertarSql, Statement.RETURN_GENERATED_KEYS)) {
                    insertar.setLong(1, pacienteAnterior);
                    insertar.setLong(2, odontologoAnterior);
                    insertar.setLong(3, nuevaCita.getIdConsultorio());
                    if (servicioAnterior > 0) {
                        insertar.setLong(4, servicioAnterior);
                    } else {
                        insertar.setNull(4, java.sql.Types.INTEGER);
                    }
                    insertar.setLong(5, idCitaAnterior);
                    insertar.setLong(6, idUsuario);
                    insertar.setTimestamp(7, Timestamp.valueOf(nuevaCita.getInicio()));
                    insertar.setInt(8, nuevaCita.getDuracionMinutos());
                    insertar.setString(9, normalizarCanal(nuevaCita.getCanal()));
                    insertar.setString(10, normalizarTipo(nuevaCita.getTipo()));
                    insertar.setString(11, motivo.trim());
                    insertar.executeUpdate();
                    try (ResultSet claves = insertar.getGeneratedKeys()) {
                        if (!claves.next()) {
                            throw new SQLException("No se recibió el número de la nueva cita.");
                        }
                        idNuevaCita = claves.getLong(1);
                    }
                }
                try (PreparedStatement actualizar = conexion.prepareStatement(
                        "UPDATE citas SET estado = 'REPROGRAMADA', observaciones = ? WHERE id_cita = ? "
                        + "AND estado IN ('PROGRAMADA', 'CONFIRMADA')")) {
                    actualizar.setString(1, "Reprogramada: " + motivo.trim());
                    actualizar.setLong(2, idCitaAnterior);
                    if (actualizar.executeUpdate() != 1) {
                        throw new IllegalArgumentException("La cita ya cambió de estado.");
                    }
                }
                insertarActividad(conexion, idUsuario, pacienteAnterior, idNuevaCita, "CITA_REPROGRAMADA",
                        "Se reprogramó la cita " + idCitaAnterior + ". " + motivo.trim());
                try (PreparedStatement moverSesion = conexion.prepareStatement(
                        "UPDATE sesiones_plan SET id_cita = ? WHERE id_cita = ?")) {
                    moverSesion.setLong(1, idNuevaCita);
                    moverSesion.setLong(2, idCitaAnterior);
                    moverSesion.executeUpdate();
                }
                conexion.commit();
                return idNuevaCita;
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo reprogramar la cita. Revise la disponibilidad.", error);
        }
    }

    private void validarDatos(Cita cita) {
        if (cita == null || cita.getIdPaciente() <= 0 || cita.getIdOdontologo() <= 0
                || cita.getIdConsultorio() <= 0 || cita.getInicio() == null) {
            throw new IllegalArgumentException("Complete el paciente, el odontólogo, el consultorio y el horario.");
        }
        if (cita.getDuracionMinutos() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        normalizarCanal(cita.getCanal());
        normalizarTipo(cita.getTipo());
        if (cita.getInicio().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La cita debe tener una fecha y hora futuras.");
        }
    }

    private String normalizarCanal(String canal) {
        String valor = canal == null ? "VENTANILLA" : canal.trim().toUpperCase(java.util.Locale.ROOT);
        if (!java.util.Set.of("VENTANILLA", "TELEFONO", "WHATSAPP").contains(valor)) {
            throw new IllegalArgumentException("El canal debe ser VENTANILLA, TELEFONO o WHATSAPP.");
        }
        return valor;
    }

    private String normalizarTipo(String tipo) {
        String valor = tipo == null ? "CONSULTA" : tipo.trim().toUpperCase(java.util.Locale.ROOT);
        if (!java.util.Set.of("CONSULTA", "CONTROL", "TRATAMIENTO", "EMERGENCIA").contains(valor)) {
            throw new IllegalArgumentException("El tipo debe ser CONSULTA, CONTROL, TRATAMIENTO o EMERGENCIA.");
        }
        return valor;
    }

    private void validarHorario(Connection conexion, Cita cita) throws SQLException {
        String pacienteActivo = "SELECT estado FROM pacientes WHERE id_persona = ? FOR UPDATE";
        try (PreparedStatement buscar = conexion.prepareStatement(pacienteActivo)) {
            buscar.setLong(1, cita.getIdPaciente());
            try (ResultSet resultado = buscar.executeQuery()) {
                if (!resultado.next() || !"ACTIVO".equals(resultado.getString(1))) {
                    throw new IllegalArgumentException("El paciente no existe o está inactivo.");
                }
            }
        }

        int diaSemana = cita.getInicio().getDayOfWeek().getValue();
        String horario = "SELECT id_disponibilidad, hora_inicio, hora_fin, maximo_citas "
                + "FROM disponibilidad_habitual WHERE id_odontologo = ? AND id_consultorio = ? "
                + "AND dia_semana = ? AND activo = TRUE AND vigente_desde <= ? "
                + "AND (vigente_hasta IS NULL OR vigente_hasta >= ?) "
                + "AND hora_inicio <= ? AND hora_fin >= ? LIMIT 1 FOR UPDATE";
        boolean dentroDelHorario = false;
        int maximoCitas = 0;
        try (PreparedStatement buscar = conexion.prepareStatement(horario)) {
            buscar.setLong(1, cita.getIdOdontologo());
            buscar.setLong(2, cita.getIdConsultorio());
            buscar.setInt(3, diaSemana);
            buscar.setDate(4, Date.valueOf(cita.getInicio().toLocalDate()));
            buscar.setDate(5, Date.valueOf(cita.getInicio().toLocalDate()));
            buscar.setTime(6, java.sql.Time.valueOf(cita.getInicio().toLocalTime()));
            buscar.setTime(7, java.sql.Time.valueOf(cita.getInicio().toLocalTime().plusMinutes(cita.getDuracionMinutos())));
            try (ResultSet resultado = buscar.executeQuery()) {
                if (resultado.next()) {
                    dentroDelHorario = true;
                    maximoCitas = resultado.getInt("maximo_citas");
                }
            }
        }
        if (!dentroDelHorario && !tieneHorarioEspecial(conexion, cita)) {
            throw new IllegalArgumentException("El odontólogo no atiende en ese horario y consultorio.");
        }

        String bloqueo = "SELECT COUNT(*) FROM excepciones_agenda WHERE id_odontologo = ? "
                + "AND tipo = 'BLOQUEO' AND estado = 'ACTIVA' AND fecha_hora_inicio < ? AND fecha_hora_fin > ?";
        try (PreparedStatement buscar = conexion.prepareStatement(bloqueo)) {
            buscar.setLong(1, cita.getIdOdontologo());
            buscar.setTimestamp(2, Timestamp.valueOf(cita.getInicio().plusMinutes(cita.getDuracionMinutos())));
            buscar.setTimestamp(3, Timestamp.valueOf(cita.getInicio()));
            try (ResultSet resultado = buscar.executeQuery()) {
                resultado.next();
                if (resultado.getInt(1) > 0) {
                    throw new IllegalArgumentException("El odontólogo tiene un bloqueo de agenda en ese horario.");
                }
            }
        }

        String margenConsulta = "SELECT margen_limpieza_minutos FROM configuracion_clinica WHERE id_configuracion = 1";
        int margen = 0;
        try (PreparedStatement buscar = conexion.prepareStatement(margenConsulta); ResultSet resultado = buscar.executeQuery()) {
            if (resultado.next()) {
                margen = resultado.getInt(1);
            }
        }
        String cruce = "SELECT COUNT(*) FROM citas WHERE estado IN "
                + "('PROGRAMADA', 'CONFIRMADA', 'EN_ESPERA', 'EN_ATENCION') "
                + "AND (id_odontologo = ? OR id_consultorio = ?) "
                + "AND fecha_hora_inicio < TIMESTAMPADD(MINUTE, ?, ?) "
                + "AND TIMESTAMPADD(MINUTE, duracion_min + ?, fecha_hora_inicio) > ? "
                + "AND (? = 0 OR id_cita <> ?)";
        try (PreparedStatement buscar = conexion.prepareStatement(cruce)) {
            buscar.setLong(1, cita.getIdOdontologo());
            buscar.setLong(2, cita.getIdConsultorio());
            buscar.setInt(3, cita.getDuracionMinutos() + margen);
            buscar.setTimestamp(4, Timestamp.valueOf(cita.getInicio()));
            buscar.setInt(5, margen);
            buscar.setTimestamp(6, Timestamp.valueOf(cita.getInicio().minusMinutes(margen)));
            buscar.setLong(7, cita.getIdCitaAnterior());
            buscar.setLong(8, cita.getIdCitaAnterior());
            try (ResultSet resultado = buscar.executeQuery()) {
                resultado.next();
                if (resultado.getInt(1) > 0) {
                    throw new IllegalArgumentException("Ese horario se cruza con otra cita del odontólogo o del consultorio.");
                }
            }
        }
        if (dentroDelHorario && maximoCitas > 0) {
            String cuenta = "SELECT COUNT(*) FROM citas WHERE id_odontologo = ? AND id_consultorio = ? "
                    + "AND DATE(fecha_hora_inicio) = ? AND estado IN ('PROGRAMADA','CONFIRMADA','EN_ESPERA','EN_ATENCION') "
                    + "AND (? = 0 OR id_cita <> ?)";
            try (PreparedStatement buscar = conexion.prepareStatement(cuenta)) {
                buscar.setLong(1, cita.getIdOdontologo());
                buscar.setLong(2, cita.getIdConsultorio());
                buscar.setDate(3, Date.valueOf(cita.getInicio().toLocalDate()));
                buscar.setLong(4, cita.getIdCitaAnterior());
                buscar.setLong(5, cita.getIdCitaAnterior());
                try (ResultSet resultado = buscar.executeQuery()) {
                    resultado.next();
                    if (resultado.getInt(1) >= maximoCitas) {
                        throw new IllegalArgumentException("El odontólogo ya llegó al máximo de citas para ese día.");
                    }
                }
            }
        }
    }

    private boolean tieneHorarioEspecial(Connection conexion, Cita cita) throws SQLException {
        String consulta = "SELECT COUNT(*) FROM excepciones_agenda WHERE id_odontologo = ? "
                + "AND tipo = 'HORARIO_ESPECIAL' AND estado = 'ACTIVA' "
                + "AND fecha_hora_inicio <= ? AND fecha_hora_fin >= ?";
        try (PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, cita.getIdOdontologo());
            buscar.setTimestamp(2, Timestamp.valueOf(cita.getInicio()));
            buscar.setTimestamp(3, Timestamp.valueOf(cita.getInicio().plusMinutes(cita.getDuracionMinutos())));
            try (ResultSet resultado = buscar.executeQuery()) {
                resultado.next();
                return resultado.getInt(1) > 0;
            }
        }
    }

    public void cambiarEstado(long idCita, String estado, long idUsuario, String detalle)
            throws PersistenciaException {
        String consulta;
        if ("CANCELADA".equals(estado)) {
            consulta = "UPDATE citas SET estado = ?, cancelado_por_usuario = ?, motivo_cancelacion = ? "
                    + "WHERE id_cita = ? AND estado IN ('PROGRAMADA','CONFIRMADA')";
        } else if ("EN_ESPERA".equals(estado)) {
            consulta = "UPDATE citas SET estado = ?, hora_llegada = CURRENT_TIMESTAMP "
                    + "WHERE id_cita = ? AND estado IN ('PROGRAMADA','CONFIRMADA')";
        } else if ("AUSENTE".equals(estado)) {
            consulta = "UPDATE citas SET estado = ?, observaciones = ? WHERE id_cita = ? "
                    + "AND estado IN ('PROGRAMADA','CONFIRMADA') AND fecha_hora_inicio <= CURRENT_TIMESTAMP";
        } else if ("CONFIRMADA".equals(estado)) {
            consulta = "UPDATE citas SET estado = ? WHERE id_cita = ? AND estado = 'PROGRAMADA'";
        } else {
            throw new IllegalArgumentException("Ese cambio de estado no está permitido.");
        }
        try (Connection conexion = Conexion.obtener(); PreparedStatement actualizar = conexion.prepareStatement(consulta)) {
            actualizar.setString(1, estado);
            if ("CANCELADA".equals(estado)) {
                actualizar.setLong(2, idUsuario);
                actualizar.setString(3, detalle);
                actualizar.setLong(4, idCita);
            } else if ("EN_ESPERA".equals(estado)) {
                actualizar.setLong(2, idCita);
            } else if ("AUSENTE".equals(estado)) {
                actualizar.setString(2, detalle);
                actualizar.setLong(3, idCita);
            } else {
                actualizar.setLong(2, idCita);
            }
            if (actualizar.executeUpdate() == 0) {
                throw new IllegalArgumentException("No se puede cambiar el estado de esa cita.");
            }
            long idPaciente;
            try (PreparedStatement buscar = conexion.prepareStatement("SELECT id_paciente FROM citas WHERE id_cita = ?")) {
                buscar.setLong(1, idCita);
                try (ResultSet resultado = buscar.executeQuery()) {
                    if (!resultado.next()) {
                        throw new IllegalArgumentException("La cita no existe.");
                    }
                    idPaciente = resultado.getLong(1);
                }
            }
            String evento = "CANCELADA".equals(estado) ? "CITA_CANCELADA" : "OTRO";
            String descripcion = "Cita " + idCita + " cambió a " + estado + (detalle == null || detalle.isBlank() ? "" : ": " + detalle.trim());
            insertarActividad(conexion, idUsuario, idPaciente, idCita, evento, descripcion);
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo actualizar la cita.", error);
        }
    }

    public void iniciarAtencion(long idCita) throws PersistenciaException {
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                String actualizar = "UPDATE citas SET estado = 'EN_ATENCION' "
                        + "WHERE id_cita = ? AND estado = 'EN_ESPERA'";
                int cambiadas;
                try (PreparedStatement iniciar = conexion.prepareStatement(actualizar)) {
                    iniciar.setLong(1, idCita);
                    cambiadas = iniciar.executeUpdate();
                    if (cambiadas == 0 && !estaEnAtencion(conexion, idCita)) {
                        throw new IllegalArgumentException("La cita debe estar en espera para iniciar la atención.");
                    }
                }
                String atencion = "INSERT INTO atenciones (id_cita, estado, inicio_atencion) "
                        + "SELECT ?, 'ABIERTA', CURRENT_TIMESTAMP WHERE NOT EXISTS "
                        + "(SELECT 1 FROM atenciones WHERE id_cita = ?)";
                try (PreparedStatement crear = conexion.prepareStatement(atencion)) {
                    crear.setLong(1, idCita);
                    crear.setLong(2, idCita);
                    crear.executeUpdate();
                }
                if (cambiadas > 0) {
                    try (PreparedStatement buscar = conexion.prepareStatement(
                            "SELECT id_paciente, id_odontologo FROM citas WHERE id_cita = ?")) {
                        buscar.setLong(1, idCita);
                        try (ResultSet resultado = buscar.executeQuery()) {
                            if (resultado.next()) {
                                insertarActividad(conexion, resultado.getLong("id_odontologo"),
                                        resultado.getLong("id_paciente"), idCita, "ATENCION_INICIADA",
                                        "Se inició la atención de la cita " + idCita + ".");
                            }
                        }
                    }
                }
                conexion.commit();
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo iniciar la atención.", error);
        }
    }

    private boolean estaEnAtencion(Connection conexion, long idCita) throws SQLException {
        try (PreparedStatement buscar = conexion.prepareStatement(
                "SELECT estado FROM citas WHERE id_cita = ?")) {
            buscar.setLong(1, idCita);
            try (ResultSet resultado = buscar.executeQuery()) {
                return resultado.next() && "EN_ATENCION".equals(resultado.getString(1));
            }
        }
    }

    private void insertarActividad(Connection conexion, long idUsuario, long idPaciente, long idCita,
            String evento, String descripcion) throws SQLException {
        String consulta = "INSERT INTO actividades (id_usuario, id_paciente, id_cita, tipo_evento, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insertar = conexion.prepareStatement(consulta)) {
            insertar.setLong(1, idUsuario);
            insertar.setLong(2, idPaciente);
            insertar.setLong(3, idCita);
            insertar.setString(4, evento);
            insertar.setString(5, descripcion);
            insertar.executeUpdate();
        }
    }

    private Cita mapear(ResultSet resultado) throws SQLException {
        Cita cita = new Cita();
        cita.setIdCita(resultado.getLong("id_cita"));
        cita.setIdPaciente(resultado.getLong("id_paciente"));
        cita.setIdOdontologo(resultado.getLong("id_odontologo"));
        cita.setIdConsultorio(resultado.getLong("id_consultorio"));
        cita.setIdServicio(resultado.getLong("id_servicio"));
        cita.setIdCitaAnterior(resultado.getLong("id_cita_anterior"));
        cita.setInicio(resultado.getTimestamp("fecha_hora_inicio").toLocalDateTime());
        Timestamp llegada = resultado.getTimestamp("hora_llegada");
        cita.setHoraLlegada(llegada == null ? null : llegada.toLocalDateTime());
        cita.setDuracionMinutos(resultado.getInt("duracion_min"));
        cita.setCanal(resultado.getString("canal"));
        cita.setTipo(resultado.getString("tipo_cita"));
        cita.setEstado(resultado.getString("estado"));
        cita.setObservaciones(resultado.getString("observaciones"));
        cita.setDocumentoPaciente(resultado.getString("tipo_documento") + " " + resultado.getString("numero_documento"));
        cita.setPaciente(resultado.getString("paciente"));
        cita.setOdontologo(resultado.getString("odontologo"));
        cita.setConsultorio(resultado.getString("consultorio"));
        return cita;
    }
}
