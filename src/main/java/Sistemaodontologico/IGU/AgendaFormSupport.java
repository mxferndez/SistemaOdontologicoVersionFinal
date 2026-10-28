package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.CitaControladora;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.JComboBox;

final class AgendaFormSupport {

    private AgendaFormSupport() {
    }

    static void cargarOdontologos(JComboBox<String> combo) throws PersistenciaException {
        combo.removeAllItems();
        for (String opcion : new CitaControladora().odontologosActivos()) {
            combo.addItem(opcion);
        }
    }

    static void cargarConsultorios(JComboBox<String> combo) throws PersistenciaException {
        combo.removeAllItems();
        for (String opcion : new CitaControladora().consultoriosActivos()) {
            combo.addItem(opcion);
        }
    }

    static int idSeleccionado(JComboBox<String> combo, String entidad) {
        Object seleccionado = combo.getSelectedItem();
        if (seleccionado == null) {
            throw new IllegalArgumentException("Seleccione " + entidad + ".");
        }
        String texto = seleccionado.toString();
        int separador = texto.indexOf(" - ");
        if (separador < 1) {
            throw new IllegalArgumentException("La opción de " + entidad + " no es válida.");
        }
        return Integer.parseInt(texto.substring(0, separador));
    }

    static void seleccionarId(JComboBox<String> combo, long id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = combo.getItemAt(i);
            if (item.startsWith(id + " - ")) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }

    static java.sql.Date fechaSql(Date fecha, String campo, boolean requerida) {
        if (fecha == null) {
            if (requerida) {
                throw new IllegalArgumentException("Seleccione " + campo + ".");
            }
            return null;
        }
        return new java.sql.Date(fecha.getTime());
    }

    static Date fechaUi(LocalDate fecha) {
        return fecha == null ? null : Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
