package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.AgendaAdministracionDAO;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class AgendaAdministracionControladora {

    private final AgendaAdministracionDAO agenda = new AgendaAdministracionDAO();

    public ArrayList<Object[]> disponibilidad() throws PersistenciaException {
        return agenda.listarDisponibilidad();
    }

    public void guardarDisponibilidad(long id, long odontologo, int consultorio, int dia, LocalTime inicio,
            LocalTime fin, int maximo, LocalDate desde, LocalDate hasta, boolean activo) throws PersistenciaException {
        agenda.guardarDisponibilidad(id, odontologo, consultorio, dia, inicio, fin, maximo, desde, hasta, activo);
    }

    public void desactivarDisponibilidad(long id) throws PersistenciaException {
        agenda.desactivarDisponibilidad(id);
    }

    public ArrayList<Object[]> excepciones() throws PersistenciaException {
        return agenda.listarExcepciones();
    }

    public void crearExcepcion(long odontologo, String tipo, LocalDateTime inicio, LocalDateTime fin, String motivo)
            throws PersistenciaException {
        agenda.crearExcepcion(odontologo, tipo, inicio, fin, motivo, Sesion.getInstancia().getIdUsuario());
    }

    public void anularExcepcion(long id) throws PersistenciaException {
        agenda.anularExcepcion(id);
    }
}
