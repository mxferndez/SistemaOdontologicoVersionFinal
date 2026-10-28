package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import Sistemaodontologico.Lógica.Sesion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

// Guarda los horarios semanales y los bloqueos especiales del personal dental.
public class AgendaAdministracionDAO {

    public ArrayList<Object[]> listarDisponibilidad() throws PersistenciaException {
        ArrayList<Object[]> filas = new ArrayList<>();
        String sql = "SELECT d.id_disponibilidad, d.id_odontologo, "
                + "CONCAT_WS(' ', p.nombres, p.apellido_paterno) AS odontologo, "
                + "d.id_consultorio, co.nombre AS consultorio, d.dia_semana, d.hora_inicio, d.hora_fin, "
                + "d.maximo_citas, d.vigente_desde, d.vigente_hasta, d.activo "
                + "FROM disponibilidad_habitual d JOIN usuarios u ON u.id_usuario = d.id_odontologo "
                + "JOIN personas p ON p.id_persona = u.id_persona JOIN consultorios co ON co.id_consultorio = d.id_consultorio "
                + "ORDER BY p.apellido_paterno, d.dia_semana, d.hora_inicio";
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(sql); ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                filas.add(new Object[]{resultado.getLong("id_disponibilidad"),
                    resultado.getLong("id_odontologo"), resultado.getString("odontologo"),
                    resultado.getInt("id_consultorio"), resultado.getString("consultorio"),
                    resultado.getInt("dia_semana"), resultado.getTime("hora_inicio").toLocalTime(),
                    resultado.getTime("hora_fin").toLocalTime(), resultado.getInt("maximo_citas"),
                    resultado.getDate("vigente_desde").toLocalDate(),
                    resultado.getDate("vigente_hasta") == null ? null : resultado.getDate("vigente_hasta").toLocalDate(),
                    resultado.getBoolean("activo")});
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar la disponibilidad semanal.", error);
        }
        return filas;
    }

    public void guardarDisponibilidad(long id, long odontologo, int consultorio, int dia,
            LocalTime inicio, LocalTime fin, int maximo, LocalDate desde, LocalDate hasta, boolean activo)
            throws PersistenciaException {
        if (odontologo <= 0 || consultorio <= 0) {
            throw new IllegalArgumentException("Seleccione un odontólogo y un consultorio.");
        }
        if (dia < 1 || dia > 7) {
            throw new IllegalArgumentException("Seleccione un día válido.");
        }
        if (inicio == null || fin == null || !fin.isAfter(inicio)) {
            throw new IllegalArgumentException("La hora final debe ser posterior a la inicial.");
        }
        if (maximo < 1) {
            throw new IllegalArgumentException("El máximo de citas debe ser mayor que cero.");
        }
        if (desde == null || (hasta != null && hasta.isBefore(desde))) {
            throw new IllegalArgumentException("Revise el periodo de vigencia.");
        }
        if (activo) {
            validarCruce(id, odontologo, consultorio, dia, inicio, fin, desde, hasta);
        }
        String sql = id == 0
                ? "INSERT INTO disponibilidad_habitual (id_odontologo, id_consultorio, dia_semana, hora_inicio, hora_fin, maximo_citas, vigente_desde, vigente_hasta, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                : "UPDATE disponibilidad_habitual SET id_odontologo = ?, id_consultorio = ?, dia_semana = ?, hora_inicio = ?, hora_fin = ?, maximo_citas = ?, vigente_desde = ?, vigente_hasta = ?, activo = ? WHERE id_disponibilidad = ?";
        try (Connection conexion = Conexion.obtener(); PreparedStatement guardar = conexion.prepareStatement(sql)) {
            guardar.setLong(1, odontologo);
            guardar.setInt(2, consultorio);
            guardar.setInt(3, dia);
            guardar.setTime(4, Time.valueOf(inicio));
            guardar.setTime(5, Time.valueOf(fin));
            guardar.setInt(6, maximo);
            guardar.setDate(7, Date.valueOf(desde));
            if (hasta == null) {
                guardar.setNull(8, java.sql.Types.DATE);
            } else {
                guardar.setDate(8, Date.valueOf(hasta));
            }
            guardar.setBoolean(9, activo);
            if (id != 0) {
                guardar.setLong(10, id);
            }
            if (guardar.executeUpdate() == 0 && id != 0) {
                throw new IllegalArgumentException("Ese horario ya no existe.");
            }
            registrarActividad(conexion, (id == 0 ? "Se creó" : "Se actualizó")
                    + " la disponibilidad del odontólogo " + odontologo + " para el día " + dia + ".");
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo guardar el horario habitual.", error);
        }
    }

    private void validarCruce(long id, long odontologo, int consultorio, int dia, LocalTime inicio,
            LocalTime fin, LocalDate desde, LocalDate hasta) throws PersistenciaException {
        String sql = "SELECT COUNT(*) FROM disponibilidad_habitual WHERE activo = TRUE "
                + "AND (id_odontologo = ? OR id_consultorio = ?) AND dia_semana = ? "
                + "AND hora_inicio < ? AND hora_fin > ? AND (vigente_hasta IS NULL OR vigente_hasta >= ?) "
                + "AND (? IS NULL OR vigente_desde <= ?)" + (id == 0 ? "" : " AND id_disponibilidad <> ?");
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(sql)) {
            int i = 1;
            buscar.setLong(i++, odontologo);
            buscar.setInt(i++, consultorio);
            buscar.setInt(i++, dia);
            buscar.setTime(i++, Time.valueOf(fin));
            buscar.setTime(i++, Time.valueOf(inicio));
            buscar.setDate(i++, Date.valueOf(desde));
            if (hasta == null) {
                buscar.setNull(i++, java.sql.Types.DATE);
            } else {
                buscar.setDate(i++, Date.valueOf(hasta));
            }
            if (hasta == null) {
                buscar.setNull(i++, java.sql.Types.DATE);
            } else {
                buscar.setDate(i++, Date.valueOf(hasta));
            }
            if (id != 0) {
                buscar.setLong(i, id);
            }
            try (ResultSet resultado = buscar.executeQuery()) {
                resultado.next();
                if (resultado.getInt(1) > 0) {
                    throw new IllegalArgumentException("Ese horario se cruza con otro horario activo del odontólogo o consultorio.");
                }
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo revisar el cruce de horarios.", error);
        }
    }

    public void desactivarDisponibilidad(long id) throws PersistenciaException {
        actualizarEstado("UPDATE disponibilidad_habitual SET activo = FALSE WHERE id_disponibilidad = ?", id,
                "No se pudo desactivar el horario.");
    }

    public ArrayList<Object[]> listarExcepciones() throws PersistenciaException {
        ArrayList<Object[]> filas = new ArrayList<>();
        String sql = "SELECT e.id_excepcion, e.id_odontologo, "
                + "CONCAT_WS(' ', p.nombres, p.apellido_paterno) AS odontologo, "
                + "e.tipo, e.fecha_hora_inicio, e.fecha_hora_fin, e.motivo, e.estado "
                + "FROM excepciones_agenda e JOIN usuarios u ON u.id_usuario = e.id_odontologo "
                + "JOIN personas p ON p.id_persona = u.id_persona ORDER BY e.fecha_hora_inicio DESC";
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(sql); ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                filas.add(new Object[]{resultado.getLong("id_excepcion"),
                    resultado.getLong("id_odontologo"), resultado.getString("odontologo"), resultado.getString("tipo"),
                    resultado.getTimestamp("fecha_hora_inicio").toLocalDateTime(),
                    resultado.getTimestamp("fecha_hora_fin").toLocalDateTime(), resultado.getString("motivo"),
                    resultado.getString("estado")});
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar las excepciones de agenda.", error);
        }
        return filas;
    }

    public void crearExcepcion(long odontologo, String tipo, LocalDateTime inicio, LocalDateTime fin,
            String motivo, long idUsuario) throws PersistenciaException {
        if (odontologo <= 0) {
            throw new IllegalArgumentException("Seleccione un odontólogo.");
        }
        if (!"BLOQUEO".equals(tipo) && !"HORARIO_ESPECIAL".equals(tipo)) {
            throw new IllegalArgumentException("Seleccione el tipo de excepción.");
        }
        if (inicio == null || fin == null || !fin.isAfter(inicio)) {
            throw new IllegalArgumentException("La fecha y hora final debe ser posterior al inicio.");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Escriba el motivo.");
        }
        String sql = "INSERT INTO excepciones_agenda (id_odontologo, tipo, fecha_hora_inicio, fecha_hora_fin, motivo, creado_por_usuario) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = Conexion.obtener(); PreparedStatement guardar = conexion.prepareStatement(sql)) {
            guardar.setLong(1, odontologo);
            guardar.setString(2, tipo);
            guardar.setTimestamp(3, Timestamp.valueOf(inicio));
            guardar.setTimestamp(4, Timestamp.valueOf(fin));
            guardar.setString(5, motivo.trim());
            guardar.setLong(6, idUsuario);
            guardar.executeUpdate();
            registrarActividad(conexion, idUsuario, "Se agregó una excepción " + tipo + " a la agenda del odontólogo " + odontologo + ".");
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo guardar la excepción de agenda.", error);
        }
    }

    public void anularExcepcion(long id) throws PersistenciaException {
        actualizarEstado("UPDATE excepciones_agenda SET estado = 'ANULADA' WHERE id_excepcion = ? AND estado = 'ACTIVA'", id,
                "No se pudo anular la excepción.");
    }

    private void actualizarEstado(String sql, long id, String mensaje) throws PersistenciaException {
        try (Connection conexion = Conexion.obtener(); PreparedStatement actualizar = conexion.prepareStatement(sql)) {
            actualizar.setLong(1, id);
            if (actualizar.executeUpdate() == 0) {
                throw new IllegalArgumentException("El registro ya no está activo.");
            }
            registrarActividad(conexion, "Se actualizó el estado del registro de agenda " + id + ".");
        } catch (SQLException error) {
            throw new PersistenciaException(mensaje, error);
        }
    }

    private void registrarActividad(Connection conexion, String descripcion) throws SQLException {
        registrarActividad(conexion, Sesion.getInstancia().getIdUsuario(), descripcion);
    }

    private void registrarActividad(Connection conexion, long idUsuario, String descripcion) throws SQLException {
        try (PreparedStatement actividad = conexion.prepareStatement(
                "INSERT INTO actividades (id_usuario, tipo_evento, descripcion) VALUES (?, 'OTRO', ?)")) {
            actividad.setLong(1, idUsuario);
            actividad.setString(2, descripcion);
            actividad.executeUpdate();
        }
    }
}
