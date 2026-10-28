package Sistemaodontologico.Persistencia.excepciones;

// Se lanza cuando MySQL rechaza un valor repetido (clave UNIQUE, error 1062).
public class RegistroDuplicadoException extends PersistenciaException {

    public RegistroDuplicadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
