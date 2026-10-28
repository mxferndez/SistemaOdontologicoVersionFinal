package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Paciente;
import Sistemaodontologico.Lógica.Cuota;
import Sistemaodontologico.Lógica.Cita;
import Sistemaodontologico.Lógica.Pago;
import Sistemaodontologico.Lógica.Persona;
import Sistemaodontologico.Lógica.Rol;
import Sistemaodontologico.Lógica.Usuario;
import Sistemaodontologico.Lógica.Sesion;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.util.ArrayList;

// La capa lógica usa esta clase para pedir datos a los DAO.
public class ControladoraPersistencia {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RolDAO rolDAO = new RolDAO();
    private final PersonaDAO personaDAO = new PersonaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final CitaDAO citaDAO = new CitaDAO();
    private final PagoDAO pagoDAO = new PagoDAO();
    private final AtencionDAO atencionDAO = new AtencionDAO();
    private final CuotaDAO cuotaDAO = new CuotaDAO();

    public Usuario buscarUsuarioPorNombre(String nombreUsuario) throws PersistenciaException {
        return usuarioDAO.buscarPorNombreUsuario(nombreUsuario);
    }

    public Usuario traerUsuario(long idUsuario) throws PersistenciaException {
        return usuarioDAO.buscarPorId(idUsuario);
    }

    public ArrayList<Usuario> traerUsuarios() throws PersistenciaException {
        return usuarioDAO.listar();
    }

    public void crearUsuario(Usuario usuario) throws PersistenciaException {
        usuarioDAO.crear(usuario);
    }

    public void editarUsuario(Usuario usuario) throws PersistenciaException {
        usuarioDAO.editar(usuario);
    }

    public void darDeBajaUsuario(long idUsuario) throws PersistenciaException {
        usuarioDAO.darDeBaja(idUsuario);
    }

    public void cambiarEstadoAccesoUsuario(long idUsuario, String estado) throws PersistenciaException {
        usuarioDAO.cambiarEstadoAcceso(idUsuario, estado);
    }

    public ArrayList<Paciente> listarPacientes(String texto, String estado) throws PersistenciaException {
        return pacienteDAO.listar(texto, estado);
    }

    public Paciente buscarPaciente(long idPersona) throws PersistenciaException {
        return pacienteDAO.buscarPorId(idPersona);
    }

    public Persona buscarPersona(String tipoDocumento, String numeroDocumento)
            throws PersistenciaException {
        return personaDAO.buscarPorDocumento(tipoDocumento, numeroDocumento);
    }

    public void crearPaciente(Paciente paciente) throws PersistenciaException {
        pacienteDAO.crear(paciente, Sesion.getInstancia().getIdUsuario());
    }

    public void editarPaciente(Paciente paciente) throws PersistenciaException {
        pacienteDAO.editar(paciente);
    }

    public ArrayList<Cita> listarCitasPendientesCobro() throws PersistenciaException {
        return citaDAO.listarPendientesCobro();
    }

    public Cita buscarCita(long idCita) throws PersistenciaException {
        return citaDAO.buscarPorId(idCita);
    }

    public ArrayList<Cita> listarCitasDelDia(java.time.LocalDate fecha, long idOdontologo, long idConsultorio)
            throws PersistenciaException {
        return citaDAO.listarDia(fecha, idOdontologo, idConsultorio);
    }

    public ArrayList<String> listarPacientesActivos() throws PersistenciaException {
        return citaDAO.listarPacientesActivos();
    }

    public ArrayList<String> listarOdontologosActivos() throws PersistenciaException {
        return citaDAO.listarOdontologosActivos();
    }

    public ArrayList<String> listarConsultoriosActivos() throws PersistenciaException {
        return citaDAO.listarConsultoriosActivos();
    }

    public void crearCita(Cita cita, long idUsuario) throws PersistenciaException {
        citaDAO.crear(cita, idUsuario);
    }

    public long crearSesionDePlan(Cita cita, long idUsuario, long idPlan) throws PersistenciaException {
        return citaDAO.crearSesionPlan(cita, idUsuario, idPlan);
    }

    public long reprogramarCita(long idAnterior, Cita nuevaCita, long idUsuario, String motivo)
            throws PersistenciaException {
        return citaDAO.reprogramar(idAnterior, nuevaCita, idUsuario, motivo);
    }

    public void cambiarEstadoCita(long idCita, String estado, long idUsuario, String detalle)
            throws PersistenciaException {
        citaDAO.cambiarEstado(idCita, estado, idUsuario, detalle);
    }

    public void iniciarAtencion(long idCita) throws PersistenciaException {
        citaDAO.iniciarAtencion(idCita);
    }

    public ArrayList<Cuota> listarCuotasPendientes() throws PersistenciaException {
        return cuotaDAO.listarPendientes();
    }

    public ArrayList<Object[]> listarPagosDelDia(java.time.LocalDate fecha) throws PersistenciaException {
        return pagoDAO.listarDelDia(fecha);
    }

    public ArrayList<Pago> listarPagosPorPaciente(long idPaciente, String concepto, String estado)
            throws PersistenciaException {
        return pagoDAO.listarPorPaciente(idPaciente, concepto, estado);
    }

    public void crearPago(long idUsuario, Pago pago) throws PersistenciaException {
        pagoDAO.crear(idUsuario, pago);
    }

    public long buscarAtencionPorCita(long idCita) throws PersistenciaException {
        return atencionDAO.idPorCita(idCita);
    }

    public void guardarNotasAtencion(long idCita, String problema, String evolucion)
            throws PersistenciaException {
        atencionDAO.guardarNotas(idCita, problema, evolucion);
    }

    public void registrarHallazgo(long idAtencion, String pieza, String superficie, String hallazgo)
            throws PersistenciaException {
        atencionDAO.registrarHallazgo(idAtencion, pieza, superficie, hallazgo);
    }

    public ArrayList<Object[]> listarHallazgos(long idCita) throws PersistenciaException {
        return atencionDAO.listarHallazgos(idCita);
    }

    public void eliminarHallazgo(long idHallazgo) throws PersistenciaException {
        atencionDAO.eliminarHallazgo(idHallazgo);
    }

    public void finalizarAtencion(long idCita) throws PersistenciaException {
        atencionDAO.finalizar(idCita);
    }

    public ArrayList<Rol> traerRoles() throws PersistenciaException {
        return rolDAO.listar();
    }
}
