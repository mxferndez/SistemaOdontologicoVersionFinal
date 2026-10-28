package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.ConfiguracionDAO;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalTime;

public class ConfiguracionControladora {

    private final ConfiguracionDAO configuracion = new ConfiguracionDAO();

    public Object[] buscar() throws PersistenciaException {
        return configuracion.buscar();
    }

    public void guardar(String moneda, LocalTime hora, int diasBloqueo, int tolerancia,
            int horasCancelacion, int margenLimpieza) throws PersistenciaException {
        configuracion.guardar(moneda, hora, diasBloqueo, tolerancia, horasCancelacion,
                margenLimpieza, Sesion.getInstancia().getIdUsuario());
    }
}
