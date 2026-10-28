package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import Sistemaodontologico.Lógica.Sesion;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Administra los servicios y consultorios que usa la clínica.
public class CatalogoDAO {

    public ArrayList<Object[]> listarServicios() throws PersistenciaException {
        ArrayList<Object[]> filas = new ArrayList<>();
        String consulta = "SELECT s.id_servicio, s.id_categoria, c.nombre AS categoria, s.nombre, "
                + "s.descripcion, s.precio, s.duracion_min, s.activo FROM servicios s "
                + "JOIN categorias_servicio c ON c.id_categoria = s.id_categoria ORDER BY s.nombre";
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta); ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                filas.add(new Object[]{resultado.getInt("id_servicio"),
                    resultado.getInt("id_categoria"), resultado.getString("categoria"), resultado.getString("nombre"),
                    resultado.getString("descripcion"), resultado.getBigDecimal("precio"),
                    resultado.getInt("duracion_min"), resultado.getBoolean("activo")});
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar los servicios.", error);
        }
        return filas;
    }

    public ArrayList<String> listarCategorias() throws PersistenciaException {
        ArrayList<String> categorias = new ArrayList<>();
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(
                "SELECT id_categoria, nombre FROM categorias_servicio WHERE activo = TRUE ORDER BY nombre"); ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                categorias.add(resultado.getInt("id_categoria") + " - " + resultado.getString("nombre"));
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar las categorías de servicio.", error);
        }
        return categorias;
    }

    public void guardarServicio(int idServicio, int idCategoria, String nombre, String descripcion,
            BigDecimal precio, int duracion, boolean activo) throws PersistenciaException {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Escriba el nombre del servicio.");
        }
        if (idCategoria <= 0) {
            throw new IllegalArgumentException("Seleccione una categoría.");
        }
        if (precio == null || precio.signum() < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
        if (duracion < 1) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero.");
        }
        String consulta = idServicio == 0
                ? "INSERT INTO servicios (id_categoria, nombre, descripcion, precio, duracion_min, activo) VALUES (?, ?, ?, ?, ?, ?)"
                : "UPDATE servicios SET id_categoria = ?, nombre = ?, descripcion = ?, precio = ?, duracion_min = ?, activo = ? WHERE id_servicio = ?";
        try (Connection conexion = Conexion.obtener(); PreparedStatement guardar = conexion.prepareStatement(consulta)) {
            guardar.setInt(1, idCategoria);
            guardar.setString(2, nombre.trim());
            guardar.setString(3, descripcion == null || descripcion.isBlank() ? null : descripcion.trim());
            guardar.setBigDecimal(4, precio);
            guardar.setInt(5, duracion);
            guardar.setBoolean(6, activo);
            if (idServicio != 0) {
                guardar.setInt(7, idServicio);
            }
            if (guardar.executeUpdate() == 0 && idServicio != 0) {
                throw new IllegalArgumentException("El servicio ya no existe.");
            }
            registrarActividad(conexion, (idServicio == 0 ? "Se creó" : "Se actualizó")
                    + " el servicio " + nombre.trim() + (activo ? "." : " (inactivo)."));
        } catch (SQLException error) {
            if (error.getErrorCode() == 1062) {
                throw new PersistenciaException("Ya existe un servicio con ese nombre en la categoría.", error);
            }
            throw new PersistenciaException("No se pudo guardar el servicio.", error);
        }
    }

    public ArrayList<Object[]> listarConsultorios() throws PersistenciaException {
        ArrayList<Object[]> filas = new ArrayList<>();
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(
                "SELECT id_consultorio, nombre, estado FROM consultorios ORDER BY nombre"); ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                filas.add(new Object[]{resultado.getInt("id_consultorio"),
                    resultado.getString("nombre"), resultado.getString("estado")});
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar los consultorios.", error);
        }
        return filas;
    }

    public void guardarConsultorio(int idConsultorio, String nombre, String estado) throws PersistenciaException {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Escriba el nombre del consultorio.");
        }
        String consulta = idConsultorio == 0
                ? "INSERT INTO consultorios (nombre, estado) VALUES (?, ?)"
                : "UPDATE consultorios SET nombre = ?, estado = ? WHERE id_consultorio = ?";
        try (Connection conexion = Conexion.obtener(); PreparedStatement guardar = conexion.prepareStatement(consulta)) {
            guardar.setString(1, nombre.trim());
            if (idConsultorio != 0) {
                guardar.setString(2, estado);
                guardar.setInt(3, idConsultorio);
            } else {
                guardar.setString(2, estado);
            }
            if (guardar.executeUpdate() == 0 && idConsultorio != 0) {
                throw new IllegalArgumentException("El consultorio ya no existe.");
            }
            registrarActividad(conexion, (idConsultorio == 0 ? "Se creó" : "Se actualizó")
                    + " el consultorio " + nombre.trim() + " (" + estado + ").");
        } catch (SQLException error) {
            if (error.getErrorCode() == 1062) {
                throw new PersistenciaException("Ya existe un consultorio con ese nombre.", error);
            }
            throw new PersistenciaException("No se pudo guardar el consultorio.", error);
        }
    }

    private void registrarActividad(Connection conexion, String descripcion) throws SQLException {
        try (PreparedStatement actividad = conexion.prepareStatement(
                "INSERT INTO actividades (id_usuario, tipo_evento, descripcion) VALUES (?, 'OTRO', ?)")) {
            actividad.setLong(1, Sesion.getInstancia().getIdUsuario());
            actividad.setString(2, descripcion);
            actividad.executeUpdate();
        }
    }
}
