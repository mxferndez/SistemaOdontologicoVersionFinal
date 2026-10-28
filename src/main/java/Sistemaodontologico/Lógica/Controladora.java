package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.ControladoraPersistencia;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.util.ArrayList;
import org.mindrot.jbcrypt.BCrypt;

// Esta clase contiene las reglas que usa la aplicación.
public class Controladora {

    private final ControladoraPersistencia persistencia = new ControladoraPersistencia();

    // Busca al usuario y revisa que pueda entrar.
    // Devuelve null cuando el usuario, la contraseña o la cuenta no son válidos.
    public Usuario validarUsuario(String nombreUsuario, String contrasena) throws PersistenciaException {
        if (nombreUsuario == null || nombreUsuario.isBlank() || contrasena == null) {
            return null;
        }

        Usuario usuario = persistencia.buscarUsuarioPorNombre(nombreUsuario);

        if (usuario == null) {
            return null;
        }
        if (!puedeIniciarSesion(usuario)) {
            return null;
        }
        String passwordHash = usuario.getPasswordHash();
        if (passwordHash == null || !BCrypt.checkpw(contrasena, passwordHash)) {
            return null;
        }

        return usuario;
    }

    public ArrayList<Usuario> traerUsuarios() throws PersistenciaException {
        return persistencia.traerUsuarios();
    }

    public Usuario traerUsuario(long idUsuario) throws PersistenciaException {
        return persistencia.traerUsuario(idUsuario);
    }

    public ArrayList<Rol> traerRoles() throws PersistenciaException {
        return persistencia.traerRoles();
    }

    // La contraseña escrita se convierte en un hash antes de guardar al usuario.
    public void crearUsuario(Usuario usuario, String contrasenaPlana) throws PersistenciaException {
        validar(usuario);
        usuario.setPasswordHash(encriptarContrasena(contrasenaPlana));
        persistencia.crearUsuario(usuario);
    }

    // Si la nueva contraseña está vacía, la base de datos conserva la actual.
    public void editarUsuario(Usuario usuario, String nuevaContrasena) throws PersistenciaException {
        validar(usuario);

        if (nuevaContrasena == null || nuevaContrasena.isEmpty()) {
            usuario.setPasswordHash(null);
        } else {
            usuario.setPasswordHash(encriptarContrasena(nuevaContrasena));
        }

        persistencia.editarUsuario(usuario);
    }

    public void darDeBajaUsuario(long idUsuario) throws PersistenciaException {
        persistencia.darDeBajaUsuario(idUsuario);
    }

    public void cambiarEstadoAcceso(long idUsuario, String estado) throws PersistenciaException {
        persistencia.cambiarEstadoAccesoUsuario(idUsuario, estado);
    }

    private boolean puedeIniciarSesion(Usuario usuario) {
        boolean accesoHabilitado = "HABILITADO".equals(usuario.getEstadoAcceso());
        boolean trabajaActualmente = "ACTIVO".equals(usuario.getEstadoLaboral());
        return accesoHabilitado && trabajaActualmente;
    }

    private String encriptarContrasena(String contrasena) {
        if (contrasena == null || contrasena.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        return BCrypt.hashpw(contrasena, BCrypt.gensalt(10));
    }

    // Cada comprobación explica qué dato necesita el usuario.
    private void validar(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Ingrese los datos del usuario.");
        }
        if (usuario.getNombreUsuario() == null || usuario.getNombreUsuario().isBlank()) {
            throw new IllegalArgumentException("Ingrese el nombre de usuario.");
        }
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
            throw new IllegalArgumentException("Seleccione al menos un rol.");
        }
        if (usuario.tieneRol("ODONTOLOGO")
                && (usuario.getNumeroColegiatura() == null || usuario.getNumeroColegiatura().isBlank())) {
            throw new IllegalArgumentException("Ingrese el número de colegiatura del odontólogo.");
        }
        if (usuario.getFechaIngreso() != null
                && usuario.getFechaBaja() != null
                && usuario.getFechaBaja().isBefore(usuario.getFechaIngreso())) {
            throw new IllegalArgumentException("La fecha de baja no puede ser anterior a la de ingreso.");
        }
    }
}
