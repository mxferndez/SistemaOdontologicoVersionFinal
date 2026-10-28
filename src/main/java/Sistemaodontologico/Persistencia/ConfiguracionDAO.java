package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Sesion;

import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;

// Lee y guarda las reglas generales de la clínica.
public class ConfiguracionDAO {

    public Object[] buscar() throws PersistenciaException {
        String sql = "SELECT moneda, hora_limite_reserva_remota, dias_bloqueo_infraccion, "
                + "tolerancia_llegada_minutos, horas_limite_cancelar_cambiar, margen_limpieza_minutos "
                + "FROM configuracion_clinica WHERE id_configuracion = 1";
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(sql); ResultSet resultado = buscar.executeQuery()) {
            if (!resultado.next()) {
                throw new IllegalArgumentException("Falta configurar la clínica en la base de datos.");
            }
            return new Object[]{resultado.getString("moneda"), resultado.getTime("hora_limite_reserva_remota").toLocalTime(),
                resultado.getInt("dias_bloqueo_infraccion"), resultado.getInt("tolerancia_llegada_minutos"),
                resultado.getInt("horas_limite_cancelar_cambiar"), resultado.getInt("margen_limpieza_minutos")};
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar la configuración de la clínica.", error);
        }
    }

    public void guardar(String moneda, LocalTime hora, int diasBloqueo, int tolerancia,
            int horasCancelacion, int margenLimpieza, long idUsuario) throws PersistenciaException {
        if (moneda == null || moneda.length() != 3) {
            throw new IllegalArgumentException("Seleccione una moneda válida.");
        }
        if (hora == null) {
            throw new IllegalArgumentException("Seleccione la hora límite de reserva.");
        }
        if (diasBloqueo < 0 || tolerancia < 0 || horasCancelacion < 0 || margenLimpieza < 0) {
            throw new IllegalArgumentException("Los minutos, horas y días no pueden ser negativos.");
        }
        String sql = "UPDATE configuracion_clinica SET moneda = ?, hora_limite_reserva_remota = ?, "
                + "dias_bloqueo_infraccion = ?, tolerancia_llegada_minutos = ?, "
                + "horas_limite_cancelar_cambiar = ?, margen_limpieza_minutos = ?, modificado_por_usuario = ? "
                + "WHERE id_configuracion = 1";
        try (Connection conexion = Conexion.obtener(); PreparedStatement guardar = conexion.prepareStatement(sql)) {
            guardar.setString(1, moneda);
            guardar.setTime(2, Time.valueOf(hora));
            guardar.setInt(3, diasBloqueo);
            guardar.setInt(4, tolerancia);
            guardar.setInt(5, horasCancelacion);
            guardar.setInt(6, margenLimpieza);
            guardar.setLong(7, idUsuario);
            if (guardar.executeUpdate() != 1) {
                throw new IllegalArgumentException("No existe la fila de configuración de la clínica.");
            }
            try (PreparedStatement actividad = conexion.prepareStatement(
                    "INSERT INTO actividades (id_usuario, tipo_evento, descripcion) VALUES (?, 'OTRO', ?)")) {
                actividad.setLong(1, idUsuario > 0 ? idUsuario : Sesion.getInstancia().getIdUsuario());
                actividad.setString(2, "Se actualizó la configuración general de la clínica.");
                actividad.executeUpdate();
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo guardar la configuración de la clínica.", error);
        }
    }
}
