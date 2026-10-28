package Sistemaodontologico.Lógica;

import java.math.BigDecimal;
import java.time.LocalDate;

// Una cuota del plan de tratamiento y lo que todavía falta pagar.
public class Cuota {

    private long idCuota;
    private long idPlan;
    private int numero;
    private long idPaciente;
    private String paciente;
    private String documento;
    private LocalDate vencimiento;
    private BigDecimal monto;
    private BigDecimal saldo;

    public long getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(long idCuota) {
        this.idCuota = idCuota;
    }

    public long getIdPlan() {
        return idPlan;
    }

    public void setIdPlan(long idPlan) {
        this.idPlan = idPlan;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public long getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(long idPaciente) {
        this.idPaciente = idPaciente;
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

    public LocalDate getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(LocalDate vencimiento) {
        this.vencimiento = vencimiento;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
