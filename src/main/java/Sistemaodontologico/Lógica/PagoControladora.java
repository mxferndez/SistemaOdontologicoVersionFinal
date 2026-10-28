package Sistemaodontologico.Lógica;

import Sistemaodontologico.Persistencia.ControladoraPersistencia;
import Sistemaodontologico.Persistencia.PagoDAO;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.util.ArrayList;
import java.time.LocalDate;

// Revisa los datos simples de un pago antes de guardarlo.
public class PagoControladora {

    private final PagoDAO pagos = new PagoDAO();
    private final ControladoraPersistencia persistencia = new ControladoraPersistencia();

    public ArrayList<Cuota> listarCuotasPendientes() throws PersistenciaException {
        return persistencia.listarCuotasPendientes();
    }

    public ArrayList<Pago> listarPorPaciente(long idPaciente, String concepto, String estado)
            throws PersistenciaException {
        return persistencia.listarPagosPorPaciente(idPaciente, concepto, estado);
    }

    public ArrayList<Object[]> listarDelDia(LocalDate fecha) throws PersistenciaException {
        return persistencia.listarPagosDelDia(fecha);
    }

    public void crear(Pago pago) throws PersistenciaException {
        if (!"EFECTIVO".equals(pago.getMetodo()) && !"TARJETA".equals(pago.getMetodo())
                && !"TRANSFERENCIA".equals(pago.getMetodo())
                && !"BILLETERA_DIGITAL".equals(pago.getMetodo()) && !"OTRO".equals(pago.getMetodo())) {
            throw new IllegalArgumentException("Seleccione un método de pago válido.");
        }
        persistencia.crearPago(Sesion.getInstancia().getIdUsuario(), pago);
    }
}
