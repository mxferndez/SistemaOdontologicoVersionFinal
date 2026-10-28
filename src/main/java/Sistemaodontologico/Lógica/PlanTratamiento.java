package Sistemaodontologico.Lógica;

import java.math.BigDecimal;

// Datos sencillos para mostrar un plan de tratamiento.
public class PlanTratamiento {

    private long idPlan;
    private long idPaciente;
    private long idOdontologo;
    private String paciente;
    private String documento;
    private String odontologo;
    private String estado;
    private int sesionesEstimadas;
    private int siguienteSesion;
    private BigDecimal total = BigDecimal.ZERO;

    public long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(long idPlan) {
        this.idPlan = idPlan;
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        this.idPaciente = idPaciente;
    }

    public long getIdOdontologo() {
        return idOdontologo;
    }

    public void setIdOdontologo(long idOdontologo) {
        this.idOdontologo = idOdontologo;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(String odontologo) {
        this.odontologo = odontologo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getSesionesEstimadas() {
        return sesionesEstimadas;
    }

    public void setSesionesEstimadas(int sesionesEstimadas) {
        this.sesionesEstimadas = sesionesEstimadas;
    }

    public int getSiguienteSesion() {
        return siguienteSesion;
    }

    public void setSiguienteSesion(int siguienteSesion) {
        this.siguienteSesion = siguienteSesion;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total == null ? BigDecimal.ZERO : total;
    }
}
