package Sistemaodontologico.Persistencia.excepciones;

// Error general de acceso a datos. La interfaz solo muestra getMessage().
public class PersistenciaException extends Exception {

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
