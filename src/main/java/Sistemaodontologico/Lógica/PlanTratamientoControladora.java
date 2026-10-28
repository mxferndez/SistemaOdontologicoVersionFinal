package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.PlanTratamientoDAO;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.util.ArrayList;

// Lleva las acciones de los planes entre las pantallas y la base de datos.
public class PlanTratamientoControladora {

    private final PlanTratamientoDAO planes = new PlanTratamientoDAO();

    public long proponer(long idAtencion, int sesiones) throws PersistenciaException {
        return planes.proponerDesdeAtencion(idAtencion, Sesion.getInstancia().getIdUsuario(), sesiones);
    }

    public ArrayList<PlanTratamiento> listarDelOdontologo() throws PersistenciaException {
        return planes.listarDelOdontologo(Sesion.getInstancia().getIdUsuario());
    }

    public ArrayList<PlanTratamiento> listarPendientes() throws PersistenciaException {
        return planes.listarPendientes();
    }

    public void responder(long idPlan, boolean aceptar) throws PersistenciaException {
        planes.responder(idPlan, aceptar, Sesion.getInstancia().getIdUsuario());
    }
}
