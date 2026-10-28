package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.ControladoraPersistencia;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

// Valida una cita antes de pedirle a la base de datos que la guarde.
public class CitaControladora {

    private final ControladoraPersistencia persistencia = new ControladoraPersistencia();

    public ArrayList<Cita> pendientesCobro() throws PersistenciaException {
        return persistencia.listarCitasPendientesCobro();
    }

    public Cita buscar(long idCita) throws PersistenciaException {
        return persistencia.buscarCita(idCita);
    }

    public ArrayList<Cita> listarDia(LocalDate fecha, long odontologo, long consultorio)
            throws PersistenciaException {
        return persistencia.listarCitasDelDia(fecha, odontologo, consultorio);
    }

    public ArrayList<String> pacientesActivos() throws PersistenciaException {
        return persistencia.listarPacientesActivos();
    }

    public ArrayList<String> odontologosActivos() throws PersistenciaException {
        return persistencia.listarOdontologosActivos();
    }

    public ArrayList<String> consultoriosActivos() throws PersistenciaException {
        return persistencia.listarConsultoriosActivos();
    }

    public void crear(Cita cita) throws PersistenciaException {
        if (cita == null || cita.getInicio() == null) {
            throw new IllegalArgumentException("Elija la fecha y la hora de la cita.");
        }
        if (cita.getCanal() == null || cita.getCanal().isBlank()) {
            cita.setCanal("VENTANILLA");
        }
        if (cita.getTipo() == null || cita.getTipo().isBlank()) {
            cita.setTipo("CONSULTA");
        }
        persistencia.crearCita(cita, Sesion.getInstancia().getIdUsuario());
    }

    public long programarSesionDePlan(Cita cita, long idPlan) throws PersistenciaException {
        if (cita.getCanal() == null || cita.getCanal().isBlank()) {
            cita.setCanal("VENTANILLA");
        }
        cita.setTipo("TRATAMIENTO");
        return persistencia.crearSesionDePlan(cita, Sesion.getInstancia().getIdUsuario(), idPlan);
    }

    public long reprogramar(long idAnterior, Cita nuevaCita, String motivo) throws PersistenciaException {
        if (nuevaCita == null || nuevaCita.getInicio() == null) {
            throw new IllegalArgumentException("Elija la fecha y la hora nuevas.");
        }
        if (nuevaCita.getCanal() == null || nuevaCita.getCanal().isBlank()) {
            nuevaCita.setCanal("VENTANILLA");
        }
        if (nuevaCita.getTipo() == null || nuevaCita.getTipo().isBlank()) {
            nuevaCita.setTipo("CONSULTA");
        }
        return persistencia.reprogramarCita(idAnterior, nuevaCita,
                Sesion.getInstancia().getIdUsuario(), motivo);
    }

    public void cambiarEstado(long idCita, String estado, String detalle)
            throws PersistenciaException {
        persistencia.cambiarEstadoCita(idCita, estado, Sesion.getInstancia().getIdUsuario(), detalle);
    }

    public void iniciarAtencion(long idCita) throws PersistenciaException {
        persistencia.iniciarAtencion(idCita);
    }
}
