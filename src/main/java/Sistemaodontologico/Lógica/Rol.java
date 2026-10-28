package Sistemaodontologico.Lógica;

// Representa un permiso o tipo de usuario del sistema.
public class Rol {

    private int idRol;
    private String nombre;
    private String descripcion;

    public Rol() {
    }

    public Rol(int idRol, String nombre, String descripcion) {
        this.idRol = idRol;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Muestra un nombre más fácil de leer en las listas y los combos.
    @Override
    public String toString() {
        String texto = nombre.toLowerCase();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }
}
