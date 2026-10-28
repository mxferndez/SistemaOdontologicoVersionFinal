package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.AtencionDAO;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.util.ArrayList;

// Ayuda a guardar la información de la consulta dental.
public class AtencionControladora {

    private final AtencionDAO atenciones = new AtencionDAO();

    public long idPorCita(long idCita) throws PersistenciaException {
        return atenciones.idPorCita(idCita);
    }

    public String[] buscarNotas(long idCita) throws PersistenciaException {
        return atenciones.buscarNotas(idCita);
    }

    public void guardarNotas(long idCita, String problema, String evolucion)
            throws PersistenciaException {
        atenciones.guardarNotas(idCita, problema, evolucion);
    }

    public void agregarHallazgo(long idAtencion, String pieza, String superficie, String hallazgo)
            throws PersistenciaException {
        atenciones.registrarHallazgo(idAtencion, pieza, superficie, hallazgo);
    }

    public ArrayList<Object[]> listarHallazgos(long idCita) throws PersistenciaException {
        return atenciones.listarHallazgos(idCita);
    }

    public void quitarHallazgo(long idHallazgo) throws PersistenciaException {
        atenciones.eliminarHallazgo(idHallazgo);
    }

    public ArrayList<String> diagnosticosCatalogo() throws PersistenciaException {
        return atenciones.listarDiagnosticosCatalogo();
    }

    public void agregarDiagnostico(long idAtencion, int idDiagnostico, String pieza)
            throws PersistenciaException {
        atenciones.registrarDiagnostico(idAtencion, idDiagnostico, pieza);
    }

    public ArrayList<Object[]> listarDiagnosticos(long idCita, String tipo) throws PersistenciaException {
        return atenciones.listarDiagnosticos(idCita, tipo);
    }

    public void quitarDiagnostico(long idDiagnostico) throws PersistenciaException {
        atenciones.eliminarDiagnostico(idDiagnostico);
    }

    public ArrayList<String> servicios() throws PersistenciaException {
        return atenciones.listarServicios();
    }

    public void agregarProcedimiento(long idAtencion, int idServicio, String pieza, String superficie)
            throws PersistenciaException {
        atenciones.registrarProcedimiento(idAtencion, idServicio, pieza, superficie);
    }

    public ArrayList<Object[]> listarProcedimientos(long idCita) throws PersistenciaException {
        return atenciones.listarProcedimientos(idCita);
    }

    public void quitarProcedimiento(long idProcedimiento) throws PersistenciaException {
        atenciones.eliminarProcedimiento(idProcedimiento);
    }

    public void agregarMedicamento(long idAtencion, String nombre, String dosis, String duracion,
            String indicaciones) throws PersistenciaException {
        atenciones.agregarMedicamento(idAtencion, nombre, dosis, duracion, indicaciones);
    }

    public ArrayList<Object[]> listarMedicamentos(long idCita) throws PersistenciaException {
        return atenciones.listarMedicamentos(idCita);
    }

    public void quitarMedicamento(long idDetalle) throws PersistenciaException {
        atenciones.eliminarMedicamento(idDetalle);
    }

    public void finalizar(long idCita) throws PersistenciaException {
        atenciones.finalizar(idCita);
    }
}
