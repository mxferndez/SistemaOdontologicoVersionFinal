package Sistemaodontologico.Lógica;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Un pago puede corresponder a una cita o a una cuota.
public class Pago {

    private long idPago;
    private long idCita;
    private long idCuota;
    private String concepto;
    private LocalDateTime fecha;
    private BigDecimal monto;
    private String metodo;
    private String referencia;
    private String estado;

    public long getIdPago() {
        return idPago;
    }

    public void setIdPago(long idPago) {
        this.idPago = idPago;
    }

    public long getIdCita() {
        return idCita;
    }

    public void setIdCita(long idCita) {
        this.idCita = idCita;
    }

    public long getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(long idCuota) {
        this.idCuota = idCuota;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
