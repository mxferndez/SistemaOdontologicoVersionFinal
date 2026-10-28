package Sistemaodontologico.Lógica;

import java.time.LocalDateTime;
import java.math.BigDecimal;

// Datos que se muestran y guardan para una cita.
public class Cita {

    private long idCita;
    private long idPaciente;
    private long idOdontologo;
    private long idConsultorio;
    private long idServicio;
    private long idCitaAnterior;
    private String documentoPaciente;
    private String paciente;
    private String odontologo;
    private String consultorio;
    private LocalDateTime inicio;
    private LocalDateTime horaLlegada;
    private int duracionMinutos;
    private String canal;
    private String tipo;
    private String estado;
    private String observaciones;
    private BigDecimal saldoPendiente;

    public long getIdCita() {
        return idCita;
    }

    public void setIdCita(long idCita) {
        this.idCita = idCita;
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

    public long getIdConsultorio() {
        return idConsultorio;
    }

    public void setIdConsultorio(long idConsultorio) {
        this.idConsultorio = idConsultorio;
    }

    public long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(long idServicio) {
        this.idServicio = idServicio;
    }

    public long getIdCitaAnterior() {
        return idCitaAnterior;
    }

    public void setIdCitaAnterior(long idCitaAnterior) {
        this.idCitaAnterior = idCitaAnterior;
    }

    public String getDocumentoPaciente() {
        return documentoPaciente;
    }

    public void setDocumentoPaciente(String documentoPaciente) {
        this.documentoPaciente = documentoPaciente;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getOdontologo() {
        return odontologo;
    }

    public void setOdontologo(String odontologo) {
        this.odontologo = odontologo;
    }

    public String getConsultorio() {
        return consultorio;
    }

    public void setConsultorio(String consultorio) {
        this.consultorio = consultorio;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getHoraLlegada() {
        return horaLlegada;
    }

    public void setHoraLlegada(LocalDateTime horaLlegada) {
        this.horaLlegada = horaLlegada;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getSaldoPendiente() {
        return saldoPendiente;
    }

    public void setSaldoPendiente(BigDecimal saldoPendiente) {
        this.saldoPendiente = saldoPendiente;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
