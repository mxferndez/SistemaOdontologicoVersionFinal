package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Rol;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

// Busca roles en el catálogo y en la relación usuario-rol.
public class RolDAO {

    // Carga todos los roles para mostrarlos en una lista o un combo.
    public ArrayList<Rol> listar() throws PersistenciaException {
        String consulta = "SELECT id_rol, nombre, descripcion FROM roles ORDER BY nombre";
        ArrayList<Rol> roles = new ArrayList<>();

        try (Connection conexion = Conexion.obtener(); PreparedStatement consultaRoles = conexion.prepareStatement(consulta); ResultSet resultado = consultaRoles.executeQuery()) {

            while (resultado.next()) {
                roles.add(mapearRol(resultado));
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar los roles.", error);
        }

        return roles;
    }

    // Estos métodos reciben una conexión para usar la misma transacción del usuario.
    ArrayList<Rol> listarPorUsuario(Connection conexion, long idUsuario) throws SQLException {
        String consulta = "SELECT r.id_rol, r.nombre, r.descripcion "
                + "FROM usuario_roles ur JOIN roles r ON r.id_rol = ur.id_rol "
                + "WHERE ur.id_usuario = ? ORDER BY r.nombre";
        ArrayList<Rol> roles = new ArrayList<>();

        try (PreparedStatement consultaRoles = conexion.prepareStatement(consulta)) {
            consultaRoles.setLong(1, idUsuario);

            try (ResultSet resultado = consultaRoles.executeQuery()) {
                while (resultado.next()) {
                    roles.add(mapearRol(resultado));
                }
            }
        }

        return roles;
    }

    // Carga los roles de todos los usuarios con una sola consulta.
    HashMap<Long, ArrayList<Rol>> listarTodosPorUsuario(Connection conexion) throws SQLException {
        String consulta = "SELECT ur.id_usuario, r.id_rol, r.nombre, r.descripcion "
                + "FROM usuario_roles ur JOIN roles r ON r.id_rol = ur.id_rol "
                + "ORDER BY r.nombre";
        HashMap<Long, ArrayList<Rol>> rolesPorUsuario = new HashMap<>();

        try (PreparedStatement consultaRoles = conexion.prepareStatement(consulta); ResultSet resultado = consultaRoles.executeQuery()) {

            while (resultado.next()) {
                long idUsuario = resultado.getLong("id_usuario");
                ArrayList<Rol> roles = rolesPorUsuario.get(idUsuario);

                if (roles == null) {
                    roles = new ArrayList<>();
                    rolesPorUsuario.put(idUsuario, roles);
                }

                roles.add(mapearRol(resultado));
            }
        }

        return rolesPorUsuario;
    }

    private Rol mapearRol(ResultSet resultado) throws SQLException {
        Rol rol = new Rol();
        rol.setIdRol(resultado.getInt("id_rol"));
        rol.setNombre(resultado.getString("nombre"));
        rol.setDescripcion(resultado.getString("descripcion"));
        return rol;
    }
}
