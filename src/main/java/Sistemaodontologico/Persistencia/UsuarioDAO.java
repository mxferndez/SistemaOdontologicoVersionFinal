package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Persona;
import Sistemaodontologico.Lógica.Rol;
import Sistemaodontologico.Lógica.Usuario;
import Sistemaodontologico.Lógica.Sesion;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import Sistemaodontologico.Persistencia.excepciones.RegistroDuplicadoException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class UsuarioDAO {

    private final RolDAO rolDAO = new RolDAO();
    private final PersonaDAO personaDAO = new PersonaDAO();
    private final OdontologoDAO odontologoDAO = new OdontologoDAO();

    private static final String SELECT_BASE
            = "SELECT u.id_usuario, u.id_persona, u.cargo_laboral, u.fecha_ingreso, u.fecha_baja, "
            + "u.estado_laboral, u.estado_acceso, u.nombre_usuario, u.password_hash, "
            + "p.tipo_documento, p.numero_documento, p.nombres, p.apellido_paterno, "
            + "p.apellido_materno, p.fecha_nacimiento, p.telefono, p.correo, p.direccion, "
            + "o.numero_colegiatura "
            + "FROM usuarios u "
            + "JOIN personas p ON p.id_persona = u.id_persona "
            + "LEFT JOIN odontologos o ON o.id_usuario = u.id_usuario ";

    // ------------------------------------------------------------------
    // CONSULTAS
    // ------------------------------------------------------------------
    // Para el login: devuelve null si el nombre de usuario no existe.
    // Solo trae datos; la verificación de la contraseña la hace la capa Lógica.
    public Usuario buscarPorNombreUsuario(String nombreUsuario) throws PersistenciaException {
        String consulta = SELECT_BASE + "WHERE u.nombre_usuario = ?";

        try (Connection conexion = Conexion.obtener(); PreparedStatement buscarUsuario = conexion.prepareStatement(consulta)) {
            buscarUsuario.setString(1, nombreUsuario);

            try (ResultSet resultado = buscarUsuario.executeQuery()) {
                if (!resultado.next()) {
                    return null;
                }

                Usuario usuario = mapearUsuario(resultado);
                usuario.setRoles(rolDAO.listarPorUsuario(conexion, usuario.getIdUsuario()));
                return usuario;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo consultar el usuario.", error);
        }
    }

    // Devuelve null si el id no existe.
    public Usuario buscarPorId(long idUsuario) throws PersistenciaException {
        String consulta = SELECT_BASE + "WHERE u.id_usuario = ?";

        try (Connection conexion = Conexion.obtener(); PreparedStatement buscarUsuario = conexion.prepareStatement(consulta)) {
            buscarUsuario.setLong(1, idUsuario);

            try (ResultSet resultado = buscarUsuario.executeQuery()) {
                if (!resultado.next()) {
                    return null;
                }

                Usuario usuario = mapearUsuario(resultado);
                usuario.setRoles(rolDAO.listarPorUsuario(conexion, usuario.getIdUsuario()));
                return usuario;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo consultar el usuario.", error);
        }
    }

    // Para llenar la tabla de usuarios del administrador
    public ArrayList<Usuario> listar() throws PersistenciaException {
        String consulta = SELECT_BASE + "ORDER BY p.apellido_paterno, p.nombres";
        ArrayList<Usuario> usuarios = new ArrayList<>();

        try (Connection conexion = Conexion.obtener(); PreparedStatement buscarUsuarios = conexion.prepareStatement(consulta); ResultSet resultado = buscarUsuarios.executeQuery()) {

            while (resultado.next()) {
                usuarios.add(mapearUsuario(resultado));
            }

            HashMap<Long, ArrayList<Rol>> rolesPorUsuario = rolDAO.listarTodosPorUsuario(conexion);
            for (Usuario usuario : usuarios) {
                ArrayList<Rol> roles = rolesPorUsuario.get(usuario.getIdUsuario());
                if (roles != null) {
                    usuario.setRoles(roles);
                }
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar la lista de usuarios.", error);
        }

        return usuarios;
    }

    // ------------------------------------------------------------------
    // ESCRITURA (cada operación es una transacción: o se guarda todo o nada)
    // ------------------------------------------------------------------
    // Orden: persona -> usuario -> roles -> odontólogo (cada paso necesita el id del anterior).
    // Si la persona ya existe (idPersona > 0) no se vuelve a insertar.
    public void crear(Usuario usuario) throws PersistenciaException {
        Persona persona = usuario.getPersona();
        boolean personaNueva = persona.getIdPersona() == 0;

        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                guardarNuevoUsuario(conexion, usuario);
                registrarActividad(conexion, "Se creó la cuenta de usuario " + usuario.getNombreUsuario() + ".");
                conexion.commit();
            } catch (SQLException error) {
                conexion.rollback();
                if (personaNueva) {
                    persona.setIdPersona(0);
                }
                throw error;
            }
        } catch (SQLException error) {
            throw traducir(error, "No se pudo crear el usuario.");
        }
    }

    private void guardarNuevoUsuario(Connection conexion, Usuario usuario) throws SQLException {
        Persona persona = usuario.getPersona();

        if (persona.getIdPersona() == 0) {
            persona.setIdPersona(personaDAO.insertar(conexion, persona));
        }

        usuario.setIdUsuario(insertarUsuario(conexion, usuario));
        insertarRoles(conexion, usuario);
        odontologoDAO.guardar(conexion, usuario);
    }

    // Si el hash de la contraseña es null, se conserva la contraseña actual.
    public void editar(Usuario usuario) throws PersistenciaException {
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                guardarCambiosUsuario(conexion, usuario);
                registrarActividad(conexion, "Se actualizaron los datos de usuario " + usuario.getNombreUsuario() + ".");
                conexion.commit();
            } catch (SQLException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw traducir(error, "No se pudo actualizar el usuario.");
        }
    }

    private void guardarCambiosUsuario(Connection conexion, Usuario usuario) throws SQLException {
        personaDAO.actualizar(conexion, usuario.getPersona());
        actualizarUsuario(conexion, usuario);

        // Los roles se reemplazan por la lista nueva.
        try (PreparedStatement consulta = conexion.prepareStatement(
                "DELETE FROM usuario_roles WHERE id_usuario = ?")) {
            consulta.setLong(1, usuario.getIdUsuario());
            consulta.executeUpdate();
        }

        insertarRoles(conexion, usuario);
        odontologoDAO.guardar(conexion, usuario);
    }

    // Un usuario con historial (citas, pagos, atenciones) no se borra: se da de baja.
    public void darDeBaja(long idUsuario) throws PersistenciaException {
        String consulta = "UPDATE usuarios SET estado_laboral = 'BAJA', estado_acceso = 'BLOQUEADO', "
                + "fecha_baja = CURDATE() WHERE id_usuario = ?";

        try (Connection conexion = Conexion.obtener(); PreparedStatement darDeBaja = conexion.prepareStatement(consulta)) {
            darDeBaja.setLong(1, idUsuario);

            if (darDeBaja.executeUpdate() == 0) {
                throw new PersistenciaException("El usuario no existe.");
            }
            registrarActividad(conexion, "Se dio de baja al usuario " + idUsuario + ".");
        } catch (SQLException error) {
            throw traducir(error, "No se pudo dar de baja al usuario.");
        }
    }

    public void cambiarEstadoAcceso(long idUsuario, String estado) throws PersistenciaException {
        if (!"HABILITADO".equals(estado) && !"BLOQUEADO".equals(estado)) {
            throw new IllegalArgumentException("Seleccione un estado de acceso válido.");
        }
        String consulta = "UPDATE usuarios SET estado_acceso = ? WHERE id_usuario = ?";
        try (Connection conexion = Conexion.obtener(); PreparedStatement actualizar = conexion.prepareStatement(consulta)) {
            actualizar.setString(1, estado);
            actualizar.setLong(2, idUsuario);
            if (actualizar.executeUpdate() == 0) {
                throw new PersistenciaException("El usuario no existe.");
            }
            registrarActividad(conexion, "Se cambió el acceso del usuario " + idUsuario + " a " + estado + ".");
        } catch (SQLException error) {
            throw traducir(error, "No se pudo cambiar el acceso del usuario.");
        }
    }

    // ------------------------------------------------------------------
    // MÉTODOS PRIVADOS
    // ------------------------------------------------------------------
    private void registrarActividad(Connection conexion, String descripcion) throws SQLException {
        try (PreparedStatement actividad = conexion.prepareStatement(
                "INSERT INTO actividades (id_usuario, tipo_evento, descripcion) VALUES (?, 'OTRO', ?)")) {
            long idUsuario = Sesion.getInstancia().getIdUsuario();
            if (idUsuario > 0) {
                actividad.setLong(1, idUsuario);
            } else {
                actividad.setNull(1, java.sql.Types.BIGINT);
            }
            actividad.setString(2, descripcion);
            actividad.executeUpdate();
        }
    }

    private long insertarUsuario(Connection conexion, Usuario usuario) throws SQLException {
        String consulta = "INSERT INTO usuarios (id_persona, cargo_laboral, fecha_ingreso, fecha_baja, "
                + "estado_laboral, estado_acceso, nombre_usuario, password_hash) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement insertarUsuario = conexion.prepareStatement(
                consulta, Statement.RETURN_GENERATED_KEYS)) {
            insertarUsuario.setLong(1, usuario.getPersona().getIdPersona());
            insertarUsuario.setString(2, usuario.getCargoLaboral());
            guardarFecha(insertarUsuario, 3, usuario.getFechaIngreso());
            guardarFecha(insertarUsuario, 4, usuario.getFechaBaja());
            insertarUsuario.setString(5, usuario.getEstadoLaboral());
            insertarUsuario.setString(6, usuario.getEstadoAcceso());
            insertarUsuario.setString(7, usuario.getNombreUsuario());
            insertarUsuario.setString(8, usuario.getPasswordHash());
            insertarUsuario.executeUpdate();
            return leerIdGenerado(insertarUsuario);
        }
    }

    private void actualizarUsuario(Connection conexion, Usuario usuario) throws SQLException {
        boolean cambiaContrasena = usuario.getPasswordHash() != null;
        String consulta = "UPDATE usuarios SET cargo_laboral = ?, fecha_ingreso = ?, fecha_baja = ?, "
                + "estado_laboral = ?, estado_acceso = ?, nombre_usuario = ?"
                + (cambiaContrasena ? ", password_hash = ?" : "")
                + " WHERE id_usuario = ?";

        try (PreparedStatement actualizarUsuario = conexion.prepareStatement(consulta)) {
            actualizarUsuario.setString(1, usuario.getCargoLaboral());
            guardarFecha(actualizarUsuario, 2, usuario.getFechaIngreso());
            guardarFecha(actualizarUsuario, 3, usuario.getFechaBaja());
            actualizarUsuario.setString(4, usuario.getEstadoLaboral());
            actualizarUsuario.setString(5, usuario.getEstadoAcceso());
            actualizarUsuario.setString(6, usuario.getNombreUsuario());

            int posicionParametro = 7;
            if (cambiaContrasena) {
                actualizarUsuario.setString(posicionParametro, usuario.getPasswordHash());
                posicionParametro++;
            }

            actualizarUsuario.setLong(posicionParametro, usuario.getIdUsuario());
            actualizarUsuario.executeUpdate();
        }
    }

    private void insertarRoles(Connection conexion, Usuario usuario) throws SQLException {
        String consulta = "INSERT INTO usuario_roles (id_usuario, id_rol) VALUES (?, ?)";

        try (PreparedStatement insertarRol = conexion.prepareStatement(consulta)) {
            for (Rol rol : usuario.getRoles()) {
                insertarRol.setLong(1, usuario.getIdUsuario());
                insertarRol.setInt(2, rol.getIdRol());
                insertarRol.addBatch();
            }
            insertarRol.executeBatch();
        }
    }

    private Usuario mapearUsuario(ResultSet resultado) throws SQLException {
        Persona persona = personaDAO.mapear(resultado);

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultado.getLong("id_usuario"));
        usuario.setPersona(persona);
        usuario.setCargoLaboral(resultado.getString("cargo_laboral"));
        usuario.setFechaIngreso(leerFecha(resultado, "fecha_ingreso"));
        usuario.setFechaBaja(leerFecha(resultado, "fecha_baja"));
        usuario.setEstadoLaboral(resultado.getString("estado_laboral"));
        usuario.setEstadoAcceso(resultado.getString("estado_acceso"));
        usuario.setNombreUsuario(resultado.getString("nombre_usuario"));
        usuario.setPasswordHash(resultado.getString("password_hash"));
        usuario.setNumeroColegiatura(resultado.getString("numero_colegiatura"));
        return usuario;
    }

    private long leerIdGenerado(PreparedStatement consulta) throws SQLException {
        try (ResultSet resultado = consulta.getGeneratedKeys()) {
            resultado.next();
            return resultado.getLong(1);
        }
    }

    private LocalDate leerFecha(ResultSet resultado, String columna) throws SQLException {
        Date fecha = resultado.getDate(columna);
        if (fecha == null) {
            return null;
        }
        return fecha.toLocalDate();
    }

    private void guardarFecha(PreparedStatement consulta, int posicion, LocalDate fecha) throws SQLException {
        if (fecha == null) {
            consulta.setNull(posicion, Types.DATE);
        } else {
            consulta.setDate(posicion, Date.valueOf(fecha));
        }
    }

    // Convierte los errores de MySQL en mensajes que la interfaz puede mostrar tal cual
    private PersistenciaException traducir(SQLException e, String mensajeGeneral) {
        if (e.getErrorCode() == 1062) {
            String detalle = String.valueOf(e.getMessage());
            if (detalle.contains("uq_usuarios_nombre")) {
                return new RegistroDuplicadoException("El nombre de usuario ya existe.", e);
            }
            if (detalle.contains("uq_personas_documento")) {
                return new RegistroDuplicadoException("Ya existe una persona con ese documento.", e);
            }
            if (detalle.contains("uq_odontologos_colegiatura")) {
                return new RegistroDuplicadoException("Ese número de colegiatura ya está registrado.", e);
            }
            if (detalle.contains("uq_usuarios_persona")) {
                return new RegistroDuplicadoException("Esa persona ya tiene un usuario.", e);
            }
            return new RegistroDuplicadoException("Ya existe un registro con esos datos.", e);
        }
        return new PersistenciaException(mensajeGeneral, e);
    }
}
