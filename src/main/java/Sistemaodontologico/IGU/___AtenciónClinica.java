package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.AtencionControladora;
import Sistemaodontologico.Lógica.Cita;
import Sistemaodontologico.Lógica.CitaControladora;
import Sistemaodontologico.Lógica.PlanTratamientoControladora;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.awt.Component;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.table.DefaultTableModel;

public class ___AtenciónClinica extends javax.swing.JPanel {

    ODVentanas dashboardV;
    private final Cita cita;
    private final AtencionControladora atenciones = new AtencionControladora();
    private long idAtencion;
    private String piezaSeleccionada;

    public ___AtenciónClinica(ODVentanas dashboard) {
        this(dashboard, null);
    }

    public ___AtenciónClinica(ODVentanas dashboard, Cita cita) {
        initComponents();
        this.dashboardV = dashboard;
        this.cita = cita;
        configurarAcciones();
        Dbloqueo.setValue(1);
        if (cita == null) {
            JOptionPane.showMessageDialog(this, "Abra esta pantalla desde una cita seleccionada.",
                    "Falta seleccionar una cita", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cargarDatosAtencion();
    }

    private void configurarAcciones() {
        jButton1.addActionListener(e -> ejecutar(this::eliminarHallazgo));
        jButton2.addActionListener(e -> ejecutar(this::eliminarMedicamento));
        jButton3.addActionListener(e -> ejecutar(this::eliminarProcedimiento));
        jButton5.addActionListener(e -> ejecutar(this::registrarHallazgo));
        jButton6.addActionListener(e -> ejecutar(this::registrarProcedimientoPieza));
        jButton7.addActionListener(e -> ejecutar(this::registrarDiagnosticoGeneral));
        jButton9.addActionListener(e -> ejecutar(this::registrarProcedimientoGeneral));
        jButton10.addActionListener(e -> ejecutar(this::registrarMedicamento));
        jButton12.addActionListener(e -> ejecutar(this::eliminarDiagnosticoPieza));
        jComboBox3.addActionListener(e -> mostrarPrecio(jComboBox3, jTextField3));
        jComboBox6.addActionListener(e -> mostrarPrecio(jComboBox6, jTextField6));
        for (Component componente : jPanel2.getComponents()) {
            if (componente instanceof JToggleButton boton && boton.getActionListeners().length == 0) {
                boton.addActionListener(e -> seleccionarPieza(boton));
            }
        }
        jTextField4.setToolTipText("Escriba el ID numérico del diagnóstico del catálogo.");
    }

    private void cargarDatosAtencion() {
        try {
            idAtencion = atenciones.idPorCita(cita.getIdCita());
            jLabel8.setText(cita.getPaciente());
            jLabel33.setText(cita.getDocumentoPaciente());
            jLabel35.setText(cita.getInicio().format(DateTimeFormatter.ofPattern("dd/MM HH:mm")));
            jLabel36.setText(cita.getOdontologo());
            jLabel37.setText(cita.getConsultorio());
            jLabel38.setText(cita.getEstado());
            jTextArea1.setText(atenciones.buscarNotas(cita.getIdCita())[1]);
            jTextArea2.setText(atenciones.buscarNotas(cita.getIdCita())[0]);
            jComboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"Oclusal", "Mesial", "Distal",
                "Vestibular", "Lingual / palatina", "Sin superficie"}));
            jComboBox2.setModel(new DefaultComboBoxModel<>(atenciones.diagnosticosCatalogo().toArray(String[]::new)));
            jComboBox2.setSelectedIndex(-1);
            String[] servicios = atenciones.servicios().toArray(String[]::new);
            jComboBox3.setModel(new DefaultComboBoxModel<>(servicios));
            jComboBox6.setModel(new DefaultComboBoxModel<>(servicios));
            cargarTablas();
            mostrarPrecio(jComboBox3, jTextField3);
            mostrarPrecio(jComboBox6, jTextField6);
        } catch (PersistenciaException | RuntimeException error) {
            mostrarError(error);
            deshabilitarEdicion();
        }
    }

    private void cargarTablas() throws PersistenciaException {
        cargarTabla(jTable1, new String[]{"ID", "Pieza", "Superficie", "Hallazgo"}, atenciones.listarHallazgos(cita.getIdCita()));
        cargarTabla(jTable2, new String[]{"ID", "Código", "Diagnóstico"},
                atenciones.listarDiagnosticos(cita.getIdCita(), "GENERAL"));
        cargarTabla(jTable3, new String[]{"ID", "Código", "Diagnóstico", "Pieza"},
                atenciones.listarDiagnosticos(cita.getIdCita(), "PIEZA"));
        cargarTabla(jTable4, new String[]{"ID", "Procedimiento", "Pieza", "Superficie", "Cantidad", "Precio unitario"},
                atenciones.listarProcedimientos(cita.getIdCita()));
        cargarTabla(jTable5, new String[]{"ID", "Medicamento", "Dosis y frecuencia", "Duración", "Indicaciones"},
                atenciones.listarMedicamentos(cita.getIdCita()));
        for (javax.swing.JTable tabla : new javax.swing.JTable[]{jTable1, jTable2, jTable3, jTable4, jTable5}) {
            tabla.getColumnModel().getColumn(0).setMinWidth(0);
            tabla.getColumnModel().getColumn(0).setMaxWidth(0);
            tabla.getColumnModel().getColumn(0).setWidth(0);
        }
    }

    private void cargarTabla(javax.swing.JTable tabla, String[] columnas, ArrayList<Object[]> filas) {
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        for (Object[] fila : filas) {
            modelo.addRow(fila);
        }
        tabla.setModel(modelo);
    }

    private void ejecutar(AccionClinica accion) {
        if (cita == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita antes de registrar información.",
                    "Sin cita", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            accion.ejecutar();
        } catch (Exception error) {
            mostrarError(error);
        }
    }

    private void mostrarError(Exception error) {
        JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo completar la acción",
                JOptionPane.ERROR_MESSAGE);
    }

    private void deshabilitarEdicion() {
        for (Component componente : jPanel5.getComponents()) {
            componente.setEnabled(false);
        }
        jTabbedPane2.setEnabled(false);
    }

    private int idSeleccionado(javax.swing.JComboBox<String> combo, String tipo) {
        Object opcion = combo.getSelectedItem();
        if (opcion == null) {
            throw new IllegalArgumentException("Seleccione " + tipo + ".");
        }
        String valor = opcion.toString().trim();
        int separador = valor.indexOf(" - ");
        if (separador < 1) {
            throw new IllegalArgumentException("Seleccione " + tipo + " válido.");
        }
        return Integer.parseInt(valor.substring(0, separador));
    }

    private String superficieSeleccionada() {
        String superficie = String.valueOf(jComboBox1.getSelectedItem());
        return "Sin superficie".equals(superficie) ? null : superficie;
    }

    private void seleccionarPieza(JToggleButton boton) {
        piezaSeleccionada = boton.isSelected() ? boton.getText().trim() : null;
        jLabel16.setText(piezaSeleccionada == null ? "—" : piezaSeleccionada);
        jLabel18.setText(piezaSeleccionada == null ? "—" : piezaSeleccionada);
    }

    private void mostrarPrecio(javax.swing.JComboBox<String> combo, javax.swing.JTextField campo) {
        Object seleccionado = combo.getSelectedItem();
        if (seleccionado == null) {
            campo.setText("");
            return;
        }
        String opcion = seleccionado.toString();
        int inicioPrecio = opcion.lastIndexOf("(");
        campo.setText(inicioPrecio >= 0 ? opcion.substring(inicioPrecio + 1, opcion.length() - 1) : "");
    }

    private long idFila(javax.swing.JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            throw new IllegalArgumentException("Seleccione un registro de la tabla.");
        }
        return ((Number) tabla.getModel().getValueAt(tabla.convertRowIndexToModel(fila), 0)).longValue();
    }

    private void registrarHallazgo() throws PersistenciaException {
        if (piezaSeleccionada == null) {
            throw new IllegalArgumentException("Seleccione una pieza en el odontograma.");
        }
        atenciones.agregarHallazgo(idAtencion, piezaSeleccionada, superficieSeleccionada(), jTextField2.getText());
        if (jComboBox2.getSelectedItem() != null) {
            atenciones.agregarDiagnostico(idAtencion, idSeleccionado(jComboBox2, "un diagnóstico"), piezaSeleccionada);
        }
        jTextField2.setText("");
        cargarTablas();
    }

    private void registrarProcedimientoPieza() throws PersistenciaException {
        if (piezaSeleccionada == null) {
            throw new IllegalArgumentException("Seleccione una pieza en el odontograma.");
        }
        atenciones.agregarProcedimiento(idAtencion, idSeleccionado(jComboBox3, "un procedimiento"),
                piezaSeleccionada, superficieSeleccionada());
        cargarTablas();
    }

    private void registrarDiagnosticoGeneral() throws PersistenciaException {
        String texto = jTextField4.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Ingrese el ID del diagnóstico general.");
        }
        int separador = texto.indexOf(" - ");
        int id = Integer.parseInt(separador > 0 ? texto.substring(0, separador) : texto);
        atenciones.agregarDiagnostico(idAtencion, id, null);
        jTextField4.setText("");
        cargarTablas();
    }

    private void registrarProcedimientoGeneral() throws PersistenciaException {
        int cantidad = ((Number) Dbloqueo.getValue()).intValue();
        if (cantidad < 1) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
        int servicio = idSeleccionado(jComboBox6, "un procedimiento");
        for (int i = 0; i < cantidad; i++) {
            atenciones.agregarProcedimiento(idAtencion, servicio, null, null);
        }
        cargarTablas();
    }

    private void registrarMedicamento() throws PersistenciaException {
        atenciones.agregarMedicamento(idAtencion, jTextField8.getText(), jTextField5.getText(),
                jTextField7.getText(), jTextArea3.getText());
        jTextField8.setText("");
        jTextField5.setText("");
        jTextField7.setText("");
        cargarTablas();
    }

    private void eliminarHallazgo() throws PersistenciaException {
        atenciones.quitarHallazgo(idFila(jTable1));
        cargarTablas();
    }

    private void eliminarMedicamento() throws PersistenciaException {
        atenciones.quitarMedicamento(idFila(jTable5));
        cargarTablas();
    }

    private void eliminarProcedimiento() throws PersistenciaException {
        atenciones.quitarProcedimiento(idFila(jTable4));
        cargarTablas();
    }

    private void eliminarDiagnosticoPieza() throws PersistenciaException {
        atenciones.quitarDiagnostico(idFila(jTable3));
        cargarTablas();
    }

    @FunctionalInterface
    private interface AccionClinica {

        void ejecutar() throws Exception;
    }

    private void volver() {
        dashboardV.mostrarPanel(new OD_Principal(dashboardV));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jToggleButton10 = new javax.swing.JToggleButton();
        jToggleButton11 = new javax.swing.JToggleButton();
        jToggleButton12 = new javax.swing.JToggleButton();
        jToggleButton13 = new javax.swing.JToggleButton();
        jToggleButton14 = new javax.swing.JToggleButton();
        jToggleButton15 = new javax.swing.JToggleButton();
        jToggleButton9 = new javax.swing.JToggleButton();
        jToggleButton17 = new javax.swing.JToggleButton();
        jToggleButton18 = new javax.swing.JToggleButton();
        jToggleButton19 = new javax.swing.JToggleButton();
        jToggleButton20 = new javax.swing.JToggleButton();
        jToggleButton21 = new javax.swing.JToggleButton();
        jToggleButton22 = new javax.swing.JToggleButton();
        jToggleButton16 = new javax.swing.JToggleButton();
        jToggleButton23 = new javax.swing.JToggleButton();
        jToggleButton24 = new javax.swing.JToggleButton();
        jToggleButton26 = new javax.swing.JToggleButton();
        jToggleButton27 = new javax.swing.JToggleButton();
        jToggleButton25 = new javax.swing.JToggleButton();
        jToggleButton28 = new javax.swing.JToggleButton();
        jToggleButton29 = new javax.swing.JToggleButton();
        jToggleButton30 = new javax.swing.JToggleButton();
        jToggleButton32 = new javax.swing.JToggleButton();
        jToggleButton33 = new javax.swing.JToggleButton();
        jToggleButton31 = new javax.swing.JToggleButton();
        jLabel14 = new javax.swing.JLabel();
        jToggleButton34 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton35 = new javax.swing.JToggleButton();
        jToggleButton38 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField4 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton12 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jComboBox6 = new javax.swing.JComboBox<>();
        jButton9 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        Dbloqueo = new javax.swing.JSpinner();
        jLabel27 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton3 = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jButton2 = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(245, 247, 250));

        jButton11.setBackground(new java.awt.Color(0, 143, 168));
        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("Finalizar atención");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(29, 41, 57));
        jButton4.setText("Proponer tratamiento");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(18, 18, 18));
        jLabel12.setText("Nota evolución clínica");

        jLabel13.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(18, 18, 18));
        jLabel13.setText("Descripción del problema");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel13)
                    .addComponent(jLabel12)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap(441, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(199, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Motivo y evolución", jPanel3);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        buttonGroup1.add(jToggleButton1);
        jToggleButton1.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/18_tercer_molar_superior_derecho.png"))); // NOI18N
        jToggleButton1.setText(" 18");
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/17_segundo_molar_superior_derecho.png"))); // NOI18N
        jToggleButton2.setText(" 17");
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        buttonGroup1.add(jToggleButton8);
        jToggleButton8.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/12_incisivo_lateral_superior_derecho.png"))); // NOI18N
        jToggleButton8.setText(" 12");
        jToggleButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        buttonGroup1.add(jToggleButton10);
        jToggleButton10.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/11_incisivo_central_superior_derecho.png"))); // NOI18N
        jToggleButton10.setText(" 11");
        jToggleButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        buttonGroup1.add(jToggleButton11);
        jToggleButton11.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/21_incisivo_central_superior_izquierdo.png"))); // NOI18N
        jToggleButton11.setText(" 21");
        jToggleButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        buttonGroup1.add(jToggleButton12);
        jToggleButton12.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/22_incisivo_lateral_superior_izquierdo.png"))); // NOI18N
        jToggleButton12.setText(" 22");
        jToggleButton12.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton12.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton12ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton13);
        jToggleButton13.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/23_canino_superior_izquierdo.png"))); // NOI18N
        jToggleButton13.setText(" 23");
        jToggleButton13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton13.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton13ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton14);
        jToggleButton14.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/24_primer_premolar_superior_izquierdo.png"))); // NOI18N
        jToggleButton14.setText(" 24");
        jToggleButton14.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton14.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton14ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton15);
        jToggleButton15.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/25_segundo_premolar_superior_izquierdo.png"))); // NOI18N
        jToggleButton15.setText(" 25");
        jToggleButton15.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton15.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton15ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton9);
        jToggleButton9.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/26_primer_molar_superior_izquierdo.png"))); // NOI18N
        jToggleButton9.setText(" 26");
        jToggleButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton9ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton17);
        jToggleButton17.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/27_segundo_molar_superior_izquierdo.png"))); // NOI18N
        jToggleButton17.setText(" 27");
        jToggleButton17.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton17.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton17ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton18);
        jToggleButton18.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/28_tercer_molar_superior_izquierdo.png"))); // NOI18N
        jToggleButton18.setText(" 28");
        jToggleButton18.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton18.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton18ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton19);
        jToggleButton19.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/28_tercer_molar_superior_izquierdo.png"))); // NOI18N
        jToggleButton19.setText(" 48");
        jToggleButton19.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton19.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton19ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton20);
        jToggleButton20.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/47_segundo_molar_inferior_derecho.png"))); // NOI18N
        jToggleButton20.setText(" 47");
        jToggleButton20.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton20.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton20ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton21);
        jToggleButton21.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/46_primer_molar_inferior_derecho.png"))); // NOI18N
        jToggleButton21.setText(" 46");
        jToggleButton21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton21.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton21ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton22);
        jToggleButton22.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton22.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/45_segundo_premolar_inferior_derecho.png"))); // NOI18N
        jToggleButton22.setText(" 45");
        jToggleButton22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton22.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton22ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton16);
        jToggleButton16.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/44_primer_premolar_inferior_derecho.png"))); // NOI18N
        jToggleButton16.setText(" 44");
        jToggleButton16.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton16.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton16ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton23);
        jToggleButton23.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/43_canino_inferior_derecho.png"))); // NOI18N
        jToggleButton23.setText(" 43");
        jToggleButton23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton23.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton23ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton24);
        jToggleButton24.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/42_incisivo_lateral_inferior_derecho.png"))); // NOI18N
        jToggleButton24.setText(" 42");
        jToggleButton24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton24.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton24ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton26);
        jToggleButton26.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/31_incisivo_central_inferior_izquierdo.png"))); // NOI18N
        jToggleButton26.setText(" 31");
        jToggleButton26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton26.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton26ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton27);
        jToggleButton27.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/32_incisivo_lateral_inferior_izquierdo.png"))); // NOI18N
        jToggleButton27.setText(" 32");
        jToggleButton27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton27.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton27ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton25);
        jToggleButton25.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/41_incisivo_central_inferior_derecho.png"))); // NOI18N
        jToggleButton25.setText(" 41");
        jToggleButton25.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton25.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton25ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton28);
        jToggleButton28.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/33_canino_inferior_izquierdo.png"))); // NOI18N
        jToggleButton28.setText(" 33");
        jToggleButton28.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton28.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton28ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton29);
        jToggleButton29.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/34_primer_premolar_inferior_izquierdo.png"))); // NOI18N
        jToggleButton29.setText(" 34");
        jToggleButton29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton29.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton29ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton30);
        jToggleButton30.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/35_segundo_premolar_inferior_izquierdo.png"))); // NOI18N
        jToggleButton30.setText(" 35");
        jToggleButton30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton30.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton30ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton32);
        jToggleButton32.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/36_primer_molar_inferior_izquierdo.png"))); // NOI18N
        jToggleButton32.setText(" 36");
        jToggleButton32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton32.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton32ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton33);
        jToggleButton33.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/38_tercer_molar_inferior_izquierdo.png"))); // NOI18N
        jToggleButton33.setText(" 38");
        jToggleButton33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton33.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton33ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton31);
        jToggleButton31.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/37_segundo_molar_inferior_izquierdo.png"))); // NOI18N
        jToggleButton31.setText(" 37");
        jToggleButton31.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton31.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton31ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(18, 18, 18));
        jLabel14.setText("N° de Pieza :");

        buttonGroup1.add(jToggleButton34);
        jToggleButton34.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/14_primer_premolar_superior_derecho.png"))); // NOI18N
        jToggleButton34.setText("14");
        jToggleButton34.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton34.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        buttonGroup1.add(jToggleButton4);
        jToggleButton4.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/16_primer_molar_superior_derecho.png"))); // NOI18N
        jToggleButton4.setText("16");
        jToggleButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        buttonGroup1.add(jToggleButton35);
        jToggleButton35.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/15_segundo_premolar_superior_derecho.png"))); // NOI18N
        jToggleButton35.setText(" 15");
        jToggleButton35.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton35.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton35ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton38);
        jToggleButton38.setFont(new java.awt.Font("Perpetua", 1, 12)); // NOI18N
        jToggleButton38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/14_primer_premolar_superior_derecho.png"))); // NOI18N
        jToggleButton38.setText(" 13");
        jToggleButton38.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton38.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel16.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(18, 18, 18));
        jLabel16.setText("numero");

        jLabel15.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(18, 18, 18));
        jLabel15.setText("Ficha de la pieza seleccionada  N°:");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable1);

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        jLabel17.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(18, 18, 18));
        jLabel17.setText("Registrar en la pieza:");

        jLabel18.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(18, 18, 18));
        jLabel18.setText("num");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Hallazgo");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Diagnóstico de esta pieza");

        jButton5.setBackground(new java.awt.Color(0, 143, 168));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Registrar hallazgo");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setText("Procedimiento en la pieza");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Superficie");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Precio");

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(255, 255, 255));
        jTextField3.setText("jTextField2");

        jButton6.setBackground(new java.awt.Color(0, 143, 168));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Registrar Procedimiento");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 435, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel18)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(278, 278, 278)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton5)
                        .addGap(203, 203, 203))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton6)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jTextField2))
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel20)
                        .addGap(2, 2, 2)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setText("Borrar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 913, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap(9, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jToggleButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(3, 3, 3)
                                .addComponent(jToggleButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jToggleButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jToggleButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToggleButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel14))
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton34)
                            .addComponent(jToggleButton38)
                            .addComponent(jToggleButton8)
                            .addComponent(jToggleButton10)
                            .addComponent(jToggleButton11)
                            .addComponent(jToggleButton12)
                            .addComponent(jToggleButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton14)
                            .addComponent(jToggleButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jToggleButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton23)
                            .addComponent(jToggleButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton28)
                            .addComponent(jToggleButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jToggleButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jToggleButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Odontograma y pieza", jPanel2);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setText("Diagnósticos  ");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setText("Diagnóstico general");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(jTable2);

        jButton7.setBackground(new java.awt.Color(0, 143, 168));
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton7.setForeground(new java.awt.Color(255, 255, 255));
        jButton7.setText("Agregar");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setText("Diagnósticos de piezas ");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(jTable3);

        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton12.setText("Borrar");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane6)
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                            .addComponent(jTextField4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7)
                            .addComponent(jButton12))))
                .addContainerGap(366, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );

        jTabbedPane2.addTab("Diagnóstico general", jPanel4);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jButton9.setBackground(new java.awt.Color(0, 143, 168));
        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Agregar");

        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(255, 255, 255));
        jTextField6.setText("jTextField2");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setText("Precio");

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setText("Procedimientos");

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setText("Cantidad");

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setText("Procedimiento general");

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable4);

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("Borrar");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                                    .addComponent(jComboBox6, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jLabel27, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Dbloqueo, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jButton9))
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(312, Short.MAX_VALUE))))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(jLabel24)
                    .addComponent(jLabel27))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTextField6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                        .addComponent(Dbloqueo, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComboBox6, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(46, 46, 46)
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Procedimientos", jPanel6);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setText("Medicamento");

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setText("Docis y frecuencia");

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setText("Duración");

        jButton10.setBackground(new java.awt.Color(0, 143, 168));
        jButton10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("Agregar");

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(jTable5);

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setText("Medicamentos");

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel32.setText("Indicaciones al paciente");

        jTextArea3.setColumns(20);
        jTextArea3.setRows(5);
        jScrollPane8.setViewportView(jTextArea3);

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setText("Borrar");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(116, 116, 116)))
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton10)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(483, 483, 483))))
                    .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
                            .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane8))
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel30)))
                .addGap(2, 2, 2)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(86, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Receta", jPanel8);

        jPanel11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel1.setText("Paciente");

        jLabel3.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel3.setText("DNI");

        jLabel4.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel4.setText("Cita");

        jLabel5.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel5.setText("Odontólogo");

        jLabel6.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel6.setText("Estado");

        jLabel7.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel7.setText("Inicio");

        jLabel8.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel8.setText("name");

        jLabel33.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel33.setText("num");

        jLabel35.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel35.setText("horainicial");

        jLabel36.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel36.setText("odAtencion");

        jLabel37.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel37.setText("Inicio");

        jLabel38.setFont(new java.awt.Font("Perpetua", 1, 16)); // NOI18N
        jLabel38.setText("EstConsulta");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(12, 12, 12)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel33)
                    .addComponent(jLabel35)
                    .addComponent(jLabel37)
                    .addComponent(jLabel38)
                    .addComponent(jLabel36))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 41, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 596, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jToggleButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton9ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton9ActionPerformed

    private void jToggleButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton12ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton12ActionPerformed

    private void jToggleButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton13ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton13ActionPerformed

    private void jToggleButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton14ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton14ActionPerformed

    private void jToggleButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton15ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton15ActionPerformed

    private void jToggleButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton16ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton16ActionPerformed

    private void jToggleButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton17ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton17ActionPerformed

    private void jToggleButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton18ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton18ActionPerformed

    private void jToggleButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton19ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton19ActionPerformed

    private void jToggleButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton20ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton20ActionPerformed

    private void jToggleButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton21ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton21ActionPerformed

    private void jToggleButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton22ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton22ActionPerformed

    private void jToggleButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton23ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton23ActionPerformed

    private void jToggleButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton24ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton24ActionPerformed

    private void jToggleButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton25ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton25ActionPerformed

    private void jToggleButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton26ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton26ActionPerformed

    private void jToggleButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton27ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton27ActionPerformed

    private void jToggleButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton28ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton28ActionPerformed

    private void jToggleButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton29ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton29ActionPerformed

    private void jToggleButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton30ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton30ActionPerformed

    private void jToggleButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton31ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton31ActionPerformed

    private void jToggleButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton32ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton32ActionPerformed

    private void jToggleButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton33ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton33ActionPerformed

    private void jToggleButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton35ActionPerformed
        seleccionarPieza((JToggleButton) evt.getSource());
    }//GEN-LAST:event_jToggleButton35ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        ejecutar(() -> {
            atenciones.guardarNotas(cita.getIdCita(), jTextArea2.getText(), jTextArea1.getText());
            atenciones.finalizar(cita.getIdCita());
            JOptionPane.showMessageDialog(this, "La atención se finalizó correctamente.",
                    "Atención finalizada", JOptionPane.INFORMATION_MESSAGE);
            volver();
        });
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        ejecutar(() -> {
            String valor = JOptionPane.showInputDialog(this,
                    "Cantidad de sesiones estimadas (1 a 24):", "1");
            if (valor == null) {
                return;
            }
            int sesiones = Integer.parseInt(valor.trim());
            new PlanTratamientoControladora().proponer(idAtencion, sesiones);
            JOptionPane.showMessageDialog(this, "La propuesta de tratamiento se guardó.",
                    "Plan propuesto", JOptionPane.INFORMATION_MESSAGE);
        });
    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner Dbloqueo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.JToggleButton jToggleButton11;
    private javax.swing.JToggleButton jToggleButton12;
    private javax.swing.JToggleButton jToggleButton13;
    private javax.swing.JToggleButton jToggleButton14;
    private javax.swing.JToggleButton jToggleButton15;
    private javax.swing.JToggleButton jToggleButton16;
    private javax.swing.JToggleButton jToggleButton17;
    private javax.swing.JToggleButton jToggleButton18;
    private javax.swing.JToggleButton jToggleButton19;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton20;
    private javax.swing.JToggleButton jToggleButton21;
    private javax.swing.JToggleButton jToggleButton22;
    private javax.swing.JToggleButton jToggleButton23;
    private javax.swing.JToggleButton jToggleButton24;
    private javax.swing.JToggleButton jToggleButton25;
    private javax.swing.JToggleButton jToggleButton26;
    private javax.swing.JToggleButton jToggleButton27;
    private javax.swing.JToggleButton jToggleButton28;
    private javax.swing.JToggleButton jToggleButton29;
    private javax.swing.JToggleButton jToggleButton30;
    private javax.swing.JToggleButton jToggleButton31;
    private javax.swing.JToggleButton jToggleButton32;
    private javax.swing.JToggleButton jToggleButton33;
    private javax.swing.JToggleButton jToggleButton34;
    private javax.swing.JToggleButton jToggleButton35;
    private javax.swing.JToggleButton jToggleButton38;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton9;
    // End of variables declaration//GEN-END:variables
}
