package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.ControladoraPersistencia;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.util.ArrayList;

// Aplica las reglas sencillas para buscar, registrar y actualizar pacientes.
public class PacienteControladora {

    private final ControladoraPersistencia persistencia = new ControladoraPersistencia();

    public ArrayList<Paciente> listarPacientes(String texto, String estado) throws PersistenciaException {
        return persistencia.listarPacientes(texto, estado);
    }

    public Paciente buscarPaciente(long idPersona) throws PersistenciaException {
        return persistencia.buscarPaciente(idPersona);
    }

    public Persona buscarPersona(String tipoDocumento, String numeroDocumento)
            throws PersistenciaException {
        return persistencia.buscarPersona(tipoDocumento, numeroDocumento);
    }

    public void crearPaciente(Paciente paciente) throws PersistenciaException {
        validar(paciente);
        persistencia.crearPaciente(paciente);
    }

    public void editarPaciente(Paciente paciente) throws PersistenciaException {
        validar(paciente);
        persistencia.editarPaciente(paciente);
    }

    private void validar(Paciente paciente) {
        if (paciente == null || paciente.getPersona() == null) {
            throw new IllegalArgumentException("Ingrese los datos del paciente.");
        }

        Persona persona = paciente.getPersona();
        if (estaVacio(persona.getTipoDocumento())) {
            throw new IllegalArgumentException("Seleccione el tipo de documento.");
        }
        if (estaVacio(persona.getNumeroDocumento())) {
            throw new IllegalArgumentException("Ingrese el número de documento.");
        }
        if (estaVacio(persona.getNombres())) {
            throw new IllegalArgumentException("Ingrese los nombres.");
        }
        if (estaVacio(persona.getApellidoPaterno())) {
            throw new IllegalArgumentException("Ingrese el apellido paterno.");
        }
        if (!"ACTIVO".equals(paciente.getEstado()) && !"INACTIVO".equals(paciente.getEstado())) {
            throw new IllegalArgumentException("Seleccione un estado válido para el paciente.");
        }
    }

    private boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
