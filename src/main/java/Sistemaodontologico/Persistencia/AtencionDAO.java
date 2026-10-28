
package Sistemaodontologico.Persistencia;


import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Guarda notas y hallazgos de una atención.
public class AtencionDAO {
    public long idPorCita(long idCita) throws PersistenciaException {
        try (Connection conexion = Conexion.obtener();
                PreparedStatement buscar = conexion.prepareStatement(
                        "SELECT id_atencion FROM atenciones WHERE id_cita = ?")) {
            buscar.setLong(1, idCita);
            try (ResultSet resultado = buscar.executeQuery()) {
                if (!resultado.next()) throw new IllegalArgumentException("La cita todavía no tiene una atención iniciada.");
                return resultado.getLong(1);
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar la atención.", error);
        }
    }

    public String[] buscarNotas(long idCita) throws PersistenciaException {
        String consulta = "SELECT descripcion_problema, nota_evolucion FROM atenciones WHERE id_cita = ?";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, idCita);
            try (ResultSet resultado = buscar.executeQuery()) {
                if (!resultado.next()) return new String[] { "", "" };
                return new String[] { resultado.getString("descripcion_problema"), resultado.getString("nota_evolucion") };
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar las notas de la consulta.", error);
        }
    }
    public void guardarNotas(long idCita, String problema, String evolucion)
            throws PersistenciaException {
        String consulta = "UPDATE atenciones SET descripcion_problema = ?, nota_evolucion = ? "
                + "WHERE id_cita = ? AND estado = 'ABIERTA'";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement actualizar = conexion.prepareStatement(consulta)) {
            actualizar.setString(1, problema);
            actualizar.setString(2, evolucion);
            actualizar.setLong(3, idCita);
            if (actualizar.executeUpdate() == 0) {
                throw new IllegalArgumentException("La atención ya terminó o no se ha iniciado.");
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron guardar las notas.", error);
        }
    }

    public void registrarHallazgo(long idAtencion, String pieza, String superficie, String hallazgo)
            throws PersistenciaException {
        if (!esPiezaValida(pieza)) throw new IllegalArgumentException("Seleccione una pieza dental válida.");
        if (hallazgo == null || hallazgo.isBlank()) throw new IllegalArgumentException("Escriba el hallazgo.");
        String consulta = "INSERT INTO hallazgos_odontograma (id_atencion, pieza_fdi, superficie, hallazgo) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement insertar = conexion.prepareStatement(consulta)) {
            insertar.setLong(1, idAtencion);
            insertar.setString(2, pieza);
            insertar.setString(3, superficie == null || superficie.isBlank() ? null : superficie);
            insertar.setString(4, hallazgo.trim());
            insertar.executeUpdate();
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo guardar el hallazgo dental.", error);
        }
    }

    public ArrayList<Object[]> listarHallazgos(long idCita) throws PersistenciaException {
        ArrayList<Object[]> filas = new ArrayList<>();
        String consulta = "SELECT h.id_hallazgo, h.pieza_fdi, h.superficie, h.hallazgo FROM hallazgos_odontograma h "
                + "JOIN atenciones a ON a.id_atencion = h.id_atencion WHERE a.id_cita = ? "
                + "ORDER BY h.pieza_fdi, h.id_hallazgo";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, idCita);
            try (ResultSet resultado = buscar.executeQuery()) {
                while (resultado.next()) {
                    filas.add(new Object[] { resultado.getLong("id_hallazgo"), resultado.getString("pieza_fdi"),
                        resultado.getString("superficie"), resultado.getString("hallazgo") });
                }
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar el odontograma.", error);
        }
        return filas;
    }

    public void eliminarHallazgo(long idHallazgo) throws PersistenciaException {
        borrar("DELETE FROM hallazgos_odontograma WHERE id_hallazgo = ?", idHallazgo,
                "No se pudo eliminar el hallazgo.");
    }
    public ArrayList<String> listarDiagnosticosCatalogo() throws PersistenciaException {
        ArrayList<String> opciones = new ArrayList<>();
        String consulta = "SELECT id_diagnostico, codigo, nombre FROM diagnosticos WHERE activo = TRUE ORDER BY nombre";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement buscar = conexion.prepareStatement(consulta);
                ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                String codigo = resultado.getString("codigo");
                opciones.add(resultado.getInt("id_diagnostico") + " - "
                        + (codigo == null ? "" : codigo + " ") + resultado.getString("nombre"));
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar el catálogo de diagnósticos.", error);
        }
        return opciones;
    }

    public void registrarDiagnostico(long idAtencion, int idDiagnostico, String pieza)
            throws PersistenciaException {
        String tipo = pieza == null ? "GENERAL" : "PIEZA";
        String consulta = "INSERT INTO diagnosticos_atencion (id_atencion, id_diagnostico, tipo, pieza_fdi) "
                + "VALUES (?, ?, ?, ?)";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement insertar = conexion.prepareStatement(consulta)) {
            insertar.setLong(1, idAtencion);
            insertar.setInt(2, idDiagnostico);
            insertar.setString(3, tipo);
            insertar.setString(4, pieza);
            insertar.executeUpdate();
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo guardar el diagnóstico.", error);
        }
    }

    public ArrayList<Object[]> listarDiagnosticos(long idCita, String tipo) throws PersistenciaException {
        ArrayList<Object[]> filas = new ArrayList<>();
        String consulta = "SELECT da.id_diagnostico_atencion, d.codigo, d.nombre, da.pieza_fdi "
                + "FROM diagnosticos_atencion da JOIN atenciones a ON a.id_atencion = da.id_atencion "
                + "JOIN diagnosticos d ON d.id_diagnostico = da.id_diagnostico "
                + "WHERE a.id_cita = ? AND da.tipo = ? ORDER BY d.nombre";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, idCita);
            buscar.setString(2, tipo);
            try (ResultSet resultado = buscar.executeQuery()) {
                while (resultado.next()) {
                    filas.add(new Object[] { resultado.getLong("id_diagnostico_atencion"),
                        resultado.getString("codigo"), resultado.getString("nombre"), resultado.getString("pieza_fdi") });
                }
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar los diagnósticos.", error);
        }
        return filas;
    }

    public void eliminarDiagnostico(long idDiagnosticoAtencion) throws PersistenciaException {
        borrar("DELETE FROM diagnosticos_atencion WHERE id_diagnostico_atencion = ?", idDiagnosticoAtencion,
                "No se pudo eliminar el diagnóstico.");
    }

    public ArrayList<String> listarServicios() throws PersistenciaException {
        ArrayList<String> opciones = new ArrayList<>();
        String consulta = "SELECT s.id_servicio, s.nombre, s.precio, "
                + "COALESCE((SELECT moneda FROM configuracion_clinica WHERE id_configuracion = 1), 'PEN') AS moneda "
                + "FROM servicios s WHERE s.activo = TRUE ORDER BY s.nombre";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement buscar = conexion.prepareStatement(consulta);
                ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) opciones.add(resultado.getInt("id_servicio") + " - "
                    + resultado.getString("nombre") + " (" + resultado.getString("moneda") + " "
                    + resultado.getBigDecimal("precio") + ")");
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar la lista de servicios.", error);
        }
        return opciones;
    }

    public void registrarProcedimiento(long idAtencion, int idServicio, String pieza, String superficie)
            throws PersistenciaException {
        String consulta = "INSERT INTO procedimientos_atencion (id_atencion, id_servicio, pieza_fdi, superficie, "
                + "cantidad, precio_unitario) SELECT ?, id_servicio, ?, ?, 1.00, precio FROM servicios "
                + "WHERE id_servicio = ? AND activo = TRUE";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement insertar = conexion.prepareStatement(consulta)) {
            insertar.setLong(1, idAtencion);
            insertar.setString(2, pieza);
            insertar.setString(3, superficie == null || superficie.isBlank() ? null : superficie);
            insertar.setInt(4, idServicio);
            if (insertar.executeUpdate() == 0) throw new IllegalArgumentException("Seleccione un servicio activo.");
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo guardar el procedimiento.", error);
        }
    }

    public ArrayList<Object[]> listarProcedimientos(long idCita) throws PersistenciaException {
        ArrayList<Object[]> filas = new ArrayList<>();
        String consulta = "SELECT pr.id_procedimiento_atencion, s.nombre, pr.pieza_fdi, pr.superficie, "
                + "pr.cantidad, pr.precio_unitario FROM procedimientos_atencion pr "
                + "JOIN servicios s ON s.id_servicio = pr.id_servicio "
                + "JOIN atenciones a ON a.id_atencion = pr.id_atencion WHERE a.id_cita = ? "
                + "ORDER BY pr.id_procedimiento_atencion";
        try (Connection conexion = Conexion.obtener();
                PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, idCita);
            try (ResultSet resultado = buscar.executeQuery()) {
                while (resultado.next()) filas.add(new Object[] { resultado.getLong("id_procedimiento_atencion"),
                        resultado.getString("nombre"), resultado.getString("pieza_fdi"), resultado.getString("superficie"),
                        resultado.getBigDecimal("cantidad"), resultado.getBigDecimal("precio_unitario") });
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar los procedimientos.", error);
        }
        return filas;
    }

    public void eliminarProcedimiento(long idProcedimiento) throws PersistenciaException {
        borrar("DELETE FROM procedimientos_atencion WHERE id_procedimiento_atencion = ?", idProcedimiento,
                "No se pudo eliminar el procedimiento.");
    }

    public void agregarMedicamento(long idAtencion, String medicamento, String dosis,
            String duracion, String indicaciones) throws PersistenciaException {
        if (medicamento == null || medicamento.isBlank() || dosis == null || dosis.isBlank()) {
            throw new IllegalArgumentException("Escriba el medicamento y su dosis.");
        }
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                long idReceta = buscarOCrearReceta(conexion, idAtencion, indicaciones);
                String insertarSql = "INSERT INTO receta_detalles (id_receta, medicamento, dosis_frecuencia, duracion, indicaciones) "
                        + "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement insertar = conexion.prepareStatement(insertarSql)) {
                    insertar.setLong(1, idReceta);
                    insertar.setString(2, medicamento.trim());
                    insertar.setString(3, dosis.trim());
                    insertar.setString(4, duracion);
                    insertar.setString(5, indicaciones);
                    insertar.executeUpdate();
                }
                conexion.commit();
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo guardar la receta.", error);
        }
    }

    private long buscarOCrearReceta(Connection conexion, long idAtencion, String indicaciones) throws SQLException {
        try (PreparedStatement buscar = conexion.prepareStatement("SELECT id_receta FROM recetas WHERE id_atencion = ?")) {
            buscar.setLong(1, idAtencion);
            try (ResultSet resultado = buscar.executeQuery()) {
                if (resultado.next()) {
                    long idReceta = resultado.getLong(1);
                    try (PreparedStatement actualizar = conexion.prepareStatement(
                            "UPDATE recetas SET indicaciones = ? WHERE id_receta = ?")) {
                        actualizar.setString(1, indicaciones);
                        actualizar.setLong(2, idReceta);
                        actualizar.executeUpdate();
                    }
                    return idReceta;
                }
            }
        }
        try (PreparedStatement insertar = conexion.prepareStatement(
                "INSERT INTO recetas (id_atencion, indicaciones) VALUES (?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS)) {
            insertar.setLong(1, idAtencion);
            insertar.setString(2, indicaciones);
            insertar.executeUpdate();
            try (ResultSet claves = insertar.getGeneratedKeys()) { claves.next(); return claves.getLong(1); }
        }
    }
    public ArrayList<Object[]> listarMedicamentos(long idCita) throws PersistenciaException {
        ArrayList<Object[]> filas = new ArrayList<>();
        String consulta = "SELECT rd.id_detalle_receta, rd.medicamento, rd.dosis_frecuencia, rd.duracion, rd.indicaciones "
                + "FROM receta_detalles rd JOIN recetas r ON r.id_receta = rd.id_receta "
                + "JOIN atenciones a ON a.id_atencion = r.id_atencion WHERE a.id_cita = ? ORDER BY rd.id_detalle_receta";
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta)) {
            buscar.setLong(1, idCita);
            try (ResultSet resultado = buscar.executeQuery()) {
                while (resultado.next()) filas.add(new Object[] { resultado.getLong("id_detalle_receta"),
                        resultado.getString("medicamento"), resultado.getString("dosis_frecuencia"),
                        resultado.getString("duracion"), resultado.getString("indicaciones") });
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar los medicamentos.", error);
        }
        return filas;
    }

    public void eliminarMedicamento(long idDetalle) throws PersistenciaException {
        borrar("DELETE FROM receta_detalles WHERE id_detalle_receta = ?", idDetalle,
                "No se pudo eliminar el medicamento.");
    }

    private void borrar(String consulta, long id, String mensaje) throws PersistenciaException {
        try (Connection conexion = Conexion.obtener(); PreparedStatement borrar = conexion.prepareStatement(consulta)) {
            borrar.setLong(1, id);
            borrar.executeUpdate();
        } catch (SQLException error) {
            throw new PersistenciaException(mensaje, error);
        }
    }
    public void finalizar(long idCita) throws PersistenciaException {
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                String cerrarAtencion = "UPDATE atenciones SET estado = 'FINALIZADA', fin_atencion = CURRENT_TIMESTAMP "
                        + "WHERE id_cita = ? AND estado = 'ABIERTA'";
                try (PreparedStatement cerrar = conexion.prepareStatement(cerrarAtencion)) {
                    cerrar.setLong(1, idCita);
                    if (cerrar.executeUpdate() == 0) {
                        throw new IllegalArgumentException("La atención ya terminó o no se ha iniciado.");
                    }
                }
                try (PreparedStatement actualizarCita = conexion.prepareStatement(
                        "UPDATE citas SET estado = 'ATENDIDA' WHERE id_cita = ? AND estado = 'EN_ATENCION'")) {
                    actualizarCita.setLong(1, idCita);
                    if (actualizarCita.executeUpdate() == 0) throw new IllegalArgumentException("La cita no está en atención.");
                }
                try (PreparedStatement buscar = conexion.prepareStatement(
                        "SELECT id_paciente, id_odontologo FROM citas WHERE id_cita = ?")) {
                    buscar.setLong(1, idCita);
                    try (ResultSet resultado = buscar.executeQuery()) {
                        if (resultado.next()) {
                            try (PreparedStatement actividad = conexion.prepareStatement(
                                    "INSERT INTO actividades (id_usuario, id_paciente, id_cita, tipo_evento, descripcion) VALUES (?, ?, ?, 'ATENCION_FINALIZADA', ?)")) {
                                actividad.setLong(1, resultado.getLong("id_odontologo"));
                                actividad.setLong(2, resultado.getLong("id_paciente"));
                                actividad.setLong(3, idCita);
                                actividad.setString(4, "Se finalizó la atención de la cita " + idCita + ".");
                                actividad.executeUpdate();
                            }
                        }
                    }
                }
                conexion.commit();
            } catch (SQLException | RuntimeException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo finalizar la atención.", error);
        }
    }

    private boolean esPiezaValida(String pieza) {
        if (pieza == null) return false;
        return pieza.matches("1[1-8]|2[1-8]|3[1-8]|4[1-8]");
    }
}
