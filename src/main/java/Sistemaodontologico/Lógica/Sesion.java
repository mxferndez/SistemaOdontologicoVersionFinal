package Sistemaodontologico.Lógica;

// Guarda quién inició sesión y con qué rol está usando el sistema.
public class Sesion {

    // La aplicación usa una sola sesión mientras está abierta.
    private static final Sesion INSTANCIA = new Sesion();

    private Usuario usuario;
    private String rolActivo;

    // Solo esta clase puede crear la sesión.
    private Sesion() {
    }

    public static Sesion getInstancia() {
        return INSTANCIA;
    }

    public void iniciar(Usuario usuario, String rolActivo) {
        this.usuario = usuario;
        this.rolActivo = rolActivo;
    }

    public void cerrar() {
        this.usuario = null;
        this.rolActivo = null;
    }

    public boolean haySesionActiva() {
        return usuario != null;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getRolActivo() {
        return rolActivo;
    }

    public long getIdUsuario() {
        return usuario.getIdUsuario();
    }

    // Devuelve las primeras letras del nombre y el apellido.
    public String obtenerIniciales() {
        if (usuario == null) {
            return "";
        }

        String nombreCompleto = usuario.getNombreCompleto();
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            return "";
        }

        String[] palabras = nombreCompleto.trim().split("\\s+");
        StringBuilder iniciales = new StringBuilder();

        for (int i = 0; i < palabras.length && i < 2; i++) {
            iniciales.append(palabras[i].substring(0, 1).toUpperCase());
        }

        return iniciales.toString();
    }
}
