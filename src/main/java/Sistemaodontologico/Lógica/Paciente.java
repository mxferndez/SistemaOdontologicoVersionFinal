package Sistemaodontologico.Lógica;

// Un paciente tiene datos personales y datos propios de su atención en la clínica.
public class Paciente {

    private Persona persona = new Persona();
    private String documentoTutor;
    private String estado = "ACTIVO";

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getDocumentoTutor() {
        return documentoTutor;
    }

    public void setDocumentoTutor(String documentoTutor) {
        this.documentoTutor = documentoTutor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreCompleto() {
        String segundoApellido = persona.getApellidoMaterno();
        return (persona.getNombres() + " " + persona.getApellidoPaterno()
                + (segundoApellido == null || segundoApellido.isBlank() ? "" : " " + segundoApellido)).trim();
    }
}
