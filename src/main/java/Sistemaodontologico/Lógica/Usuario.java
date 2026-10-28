package Sistemaodontologico.Lógica;

import java.time.LocalDate;
import java.util.ArrayList;

// Reúne los datos de acceso, trabajo y persona de un usuario.
public class Usuario {

    private long idUsuario;
    private Persona persona = new Persona();
    private String cargoLaboral;
    private LocalDate fechaIngreso;
    private LocalDate fechaBaja;
    private String estadoLaboral = "ACTIVO";
    private String estadoAcceso = "HABILITADO";
    private String nombreUsuario;
    private String passwordHash; // Se guarda el hash, nunca la contraseña escrita.
    private ArrayList<Rol> roles = new ArrayList<>();
    private String numeroColegiatura; // Se usa cuando la persona tiene el rol ODONTOLOGO.

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getCargoLaboral() {
        return cargoLaboral;
    }

    public void setCargoLaboral(String cargoLaboral) {
        this.cargoLaboral = cargoLaboral;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public String getEstadoLaboral() {
        return estadoLaboral;
    }

    public void setEstadoLaboral(String estadoLaboral) {
        this.estadoLaboral = estadoLaboral;
    }

    public String getEstadoAcceso() {
        return estadoAcceso;
    }

    public void setEstadoAcceso(String estadoAcceso) {
        this.estadoAcceso = estadoAcceso;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public ArrayList<Rol> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Rol> roles) {
        this.roles = roles;
    }

    public String getNumeroColegiatura() {
        return numeroColegiatura;
    }

    public void setNumeroColegiatura(String numeroColegiatura) {
        this.numeroColegiatura = numeroColegiatura;
    }

    // Une el nombre y el primer apellido para mostrarlo en pantalla.
    public String getNombreCompleto() {
        return persona.getNombres() + " " + persona.getApellidoPaterno();
    }

    // Devuelve una lista sencilla con los nombres de sus roles.
    public ArrayList<String> getNombresRoles() {
        ArrayList<String> nombres = new ArrayList<>();
        for (Rol rol : roles) {
            nombres.add(rol.getNombre());
        }
        return nombres;
    }

    // Revisa si el usuario tiene un rol con ese nombre.
    public boolean tieneRol(String nombreRol) {
        for (Rol rol : roles) {
            if (rol.getNombre().equals(nombreRol)) {
                return true;
            }
        }
        return false;
    }
}
