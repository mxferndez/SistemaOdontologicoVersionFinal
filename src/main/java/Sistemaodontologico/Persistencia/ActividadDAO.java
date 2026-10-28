package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Consulta y registra la bitácora de actividad de la clínica.
 */
public class ActividadDAO {

    public ArrayList<Object[]> listarHoy() throws PersistenciaException {
        String consulta = "SELECT a.id_actividad, a.fecha_hora, a.tipo_evento, a.descripcion, "
                + "COALESCE(NULLIF(CONCAT_WS(' ', p.nombres, p.apellido_paterno), ''), u.nombre_usuario, 'Sistema') AS usuario, "
                + "CONCAT_WS(' ', pa.nombres, pa.apellido_paterno) AS paciente "
                + "FROM actividades a LEFT JOIN usuarios u ON u.id_usuario = a.id_usuario "
                + "LEFT JOIN personas p ON p.id_persona = u.id_persona "
                + "LEFT JOIN personas pa ON pa.id_persona = a.id_paciente "
                + "WHERE a.fecha_hora >= CURDATE() AND a.fecha_hora < CURDATE() + INTERVAL 1 DAY "
                + "ORDER BY a.fecha_hora DESC, a.id_actividad DESC";
        ArrayList<Object[]> filas = new ArrayList<>();
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta); ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                filas.add(new Object[]{resultado.getLong("id_actividad"),
                    resultado.getTimestamp("fecha_hora"), resultado.getString("tipo_evento"),
                    resultado.getString("descripcion"), resultado.getString("usuario"), resultado.getString("paciente")});
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar la actividad de hoy.", error);
        }
        return filas;
    }

    public void registrar(Long idUsuario, Long idPaciente, Long idCita, Long idPago,
            String tipoEvento, String descripcion) throws PersistenciaException {
        if (tipoEvento == null || tipoEvento.isBlank() || descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La actividad necesita tipo y descripción.");
        }
        String consulta = "INSERT INTO actividades (id_usuario, id_paciente, id_cita, id_pago, tipo_evento, descripcion) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = Conexion.obtener(); PreparedStatement insertar = conexion.prepareStatement(consulta)) {
            if (idUsuario == null) {
                insertar.setNull(1, java.sql.Types.BIGINT);
            } else {
                insertar.setLong(1, idUsuario);
            }
            if (idPaciente == null) {
                insertar.setNull(2, java.sql.Types.BIGINT);
            } else {
                insertar.setLong(2, idPaciente);
            }
            if (idCita == null) {
                insertar.setNull(3, java.sql.Types.BIGINT);
            } else {
                insertar.setLong(3, idCita);
            }
            if (idPago == null) {
                insertar.setNull(4, java.sql.Types.BIGINT);
            } else {
                insertar.setLong(4, idPago);
            }
            insertar.setString(5, tipoEvento);
            insertar.setString(6, descripcion.trim());
            insertar.executeUpdate();
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo guardar la actividad.", error);
        }
    }
}
