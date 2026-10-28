package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.CatalogoDAO;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.math.BigDecimal;
import java.util.ArrayList;

// Pide a la base de datos los servicios y consultorios.
public class CatalogoControladora {

    private final CatalogoDAO catalogo = new CatalogoDAO();

    public ArrayList<Object[]> servicios() throws PersistenciaException {
        return catalogo.listarServicios();
    }

    public ArrayList<String> categorias() throws PersistenciaException {
        return catalogo.listarCategorias();
    }

    public void guardarServicio(int id, int categoria, String nombre, String descripcion,
            BigDecimal precio, int duracion, boolean activo) throws PersistenciaException {
        catalogo.guardarServicio(id, categoria, nombre, descripcion, precio, duracion, activo);
    }

    public ArrayList<Object[]> consultorios() throws PersistenciaException {
        return catalogo.listarConsultorios();
    }

    public void guardarConsultorio(int id, String nombre, String estado) throws PersistenciaException {
        catalogo.guardarConsultorio(id, nombre, estado);
    }
}
