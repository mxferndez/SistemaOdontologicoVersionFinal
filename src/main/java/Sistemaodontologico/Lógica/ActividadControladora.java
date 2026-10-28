package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.ActividadDAO;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.util.ArrayList;

public class ActividadControladora {

    private final ActividadDAO actividades = new ActividadDAO();

    public ArrayList<Object[]> listarHoy() throws PersistenciaException {
        return actividades.listarHoy();
    }

    public void registrar(Long idUsuario, Long idPaciente, Long idCita, Long idPago,
            String tipoEvento, String descripcion) throws PersistenciaException {
        actividades.registrar(idUsuario, idPaciente, idCita, idPago, tipoEvento, descripcion);
    }
}
