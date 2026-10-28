package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Cita;
import Sistemaodontologico.Lógica.CitaControladora;
import Sistemaodontologico.Lógica.Cuota;
import Sistemaodontologico.Lógica.Pago;
import Sistemaodontologico.Lógica.PagoControladora;
import Sistemaodontologico.Lógica.ConfiguracionControladora;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class REC_Caja extends javax.swing.JPanel {

    RECVentanas dashboardV;
    private String moneda = "PEN";

    public REC_Caja(RECVentanas dashboard) {
        initComponents();
        this.dashboardV = dashboard;
        try {
            moneda = String.valueOf(new ConfiguracionControladora().buscar()[0]);
        } catch (PersistenciaException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "Configuración", JOptionPane.ERROR_MESSAGE);
        }
        jButton1.addActionListener(e -> registrarPago(false));
        jButton4.addActionListener(e -> registrarPago(true));
        jButton5.addActionListener(e -> cargarPendientes());
        jButton6.addActionListener(e -> {
            jTextField1.setText("");
            cargarPendientes();
        });
        jButton7.addActionListener(e -> verDetalleSeleccionado());
        cmbPorTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Todos", "Citas", "Cuotas"}));
        cmbPorTipo.addActionListener(e -> cargarPendientes());
        cmbPorOdontólogo.removeAllItems();
        cmbPorOdontólogo.addItem("Todos");
        try {
            for (String odontologo : new CitaControladora().odontologosActivos()) {
                cmbPorOdontólogo.addItem(odontologo);
            }
        } catch (PersistenciaException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo cargar el filtro", JOptionPane.ERROR_MESSAGE);
        }
        cmbPorOdontólogo.addActionListener(e -> cargarPendientes());
        cargarPendientes();
    }

    private void cargarPendientes() {
        DefaultTableModel citasModelo = new DefaultTableModel(new Object[]{"ID", "Documento", "Paciente", "Odontólogo", "Consultorio", "Saldo"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        DefaultTableModel cuotasModelo = new DefaultTableModel(new Object[]{"ID", "Documento", "Paciente", "Plan", "Cuota", "Vence", "Saldo"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        jTable1.setModel(citasModelo);
        jTable2.setModel(cuotasModelo);
        String texto = jTextField1.getText() == null ? "" : jTextField1.getText().trim().toLowerCase(java.util.Locale.ROOT);
        String filtroOdontologo = String.valueOf(cmbPorOdontólogo.getSelectedItem());
        String tipo = String.valueOf(cmbPorTipo.getSelectedItem());
        try {
            ArrayList<Cita> citas = new CitaControladora().pendientesCobro();
            for (Cita cita : ("Cuotas".equals(tipo) ? new ArrayList<Cita>() : citas)) {
                if (!filtroOdontologo.equals("Todos") && !filtroOdontologo.equals(cita.getIdOdontologo() + " - " + cita.getOdontologo())) {
                    continue;
                }
                if (!coincide(texto, cita.getDocumentoPaciente() + " " + cita.getPaciente() + " " + cita.getOdontologo())) {
                    continue;
                }
                citasModelo.addRow(new Object[]{cita.getIdCita(), cita.getDocumentoPaciente(), cita.getPaciente(),
                    cita.getOdontologo(), cita.getConsultorio(), cita.getSaldoPendiente()});
            }
            for (Cuota cuota : ("Citas".equals(tipo) ? new ArrayList<Cuota>() : new PagoControladora().listarCuotasPendientes())) {
                if (!coincide(texto, cuota.getDocumento() + " " + cuota.getPaciente())) {
                    continue;
                }
                cuotasModelo.addRow(new Object[]{cuota.getIdCuota(), cuota.getDocumento(), cuota.getPaciente(), cuota.getIdPlan(),
                    cuota.getNumero(), cuota.getVencimiento(), cuota.getSaldo()});
            }
            ocultarId(jTable1);
            ocultarId(jTable2);
            InfoNumeroCitas.setText(String.valueOf(citasModelo.getRowCount() + cuotasModelo.getRowCount()));
            InfocitasConfirmadas.setText(citasModelo.getRowCount() + " citas por cobrar");
            InfoCitasEspera.setText(cuotasModelo.getRowCount() + " cuotas pendientes");
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudieron cargar los saldos pendientes", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean coincide(String filtro, String valor) {
        return filtro.isBlank() || (valor != null && valor.toLowerCase(java.util.Locale.ROOT).contains(filtro));
    }

    private void ocultarId(javax.swing.JTable tabla) {
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);
    }

    private void registrarPago(boolean esCuota) {
        try {
            javax.swing.JTable tabla = esCuota ? jTable2 : jTable1;
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                throw new IllegalArgumentException("Seleccione primero el saldo que desea cobrar.");
            }
            int modelo = tabla.convertRowIndexToModel(fila);
            long idDestino = ((Number) tabla.getModel().getValueAt(modelo, 0)).longValue();
            BigDecimal saldo = new BigDecimal(String.valueOf(tabla.getModel().getValueAt(modelo, tabla.getModel().getColumnCount() - 1)));
            String montoTexto = JOptionPane.showInputDialog(this, "Saldo pendiente: " + moneda + " " + saldo + "\nMonto a registrar:", saldo.toPlainString());
            if (montoTexto == null) {
                return;
            }
            String[] metodos = {"EFECTIVO", "TARJETA", "TRANSFERENCIA", "BILLETERA_DIGITAL", "OTRO"};
            Object metodo = JOptionPane.showInputDialog(this, "Método de pago", "Registrar pago",
                    JOptionPane.QUESTION_MESSAGE, null, metodos, metodos[0]);
            if (metodo == null) {
                return;
            }
            Pago pago = new Pago();
            if (esCuota) {
                pago.setIdCuota(idDestino);
            } else {
                pago.setIdCita(idDestino);
            }
            pago.setMonto(new BigDecimal(montoTexto.trim()));
            pago.setMetodo(String.valueOf(metodo));
            new PagoControladora().crear(pago);
            JOptionPane.showMessageDialog(this, "Pago registrado.", "Caja", JOptionPane.INFORMATION_MESSAGE);
            cargarPendientes();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo registrar el pago", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void verDetalleSeleccionado() {
        javax.swing.JTable tabla = jTable1.getSelectedRow() >= 0 ? jTable1 : jTable2;
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una cita o cuota pendiente.", "Detalle de saldo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int modelo = tabla.convertRowIndexToModel(fila);
        StringBuilder detalle = new StringBuilder();
        for (int columna = 1; columna < tabla.getModel().getColumnCount(); columna++) {
            detalle.append(tabla.getColumnName(columna)).append(": ").append(tabla.getModel().getValueAt(modelo, columna)).append('\n');
        }
        JOptionPane.showMessageDialog(this, detalle.toString(), "Detalle de saldo", JOptionPane.INFORMATION_MESSAGE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        PanelCitas = new javax.swing.JPanel();
        tituloCitas = new javax.swing.JLabel();
        InfoNumeroCitas = new javax.swing.JLabel();
        InfocitasConfirmadas = new javax.swing.JLabel();
        IconCitas = new javax.swing.JLabel();
        InfoCitasEspera = new javax.swing.JLabel();
        PanelCA = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Panel_Ingresos = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        cmbPorTipo = new javax.swing.JComboBox<>();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        datePicker1 = new com.github.lgooddatepicker.components.DatePicker();
        cmbPorOdontólogo = new javax.swing.JComboBox<>();

        setBackground(new java.awt.Color(245, 247, 250));
        setPreferredSize(new java.awt.Dimension(860, 680));

        jLabel2.setText("Caja y cobros ");
        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(18, 18, 18));

        PanelCitas.setBackground(new java.awt.Color(255, 255, 255));

        tituloCitas.setText("Cobros pendientes");
        tituloCitas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        InfoNumeroCitas.setText("8");
        InfoNumeroCitas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        InfocitasConfirmadas.setText("6 confirmadas");
        InfocitasConfirmadas.setForeground(new java.awt.Color(31, 111, 102));

        IconCitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/event_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        InfoCitasEspera.setText("2 Espera");
        InfoCitasEspera.setForeground(new java.awt.Color(102, 204, 255));

        javax.swing.GroupLayout PanelCitasLayout = new javax.swing.GroupLayout(PanelCitas);
        PanelCitas.setLayout(PanelCitasLayout);
        PanelCitasLayout.setHorizontalGroup(
            PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCitasLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCitasLayout.createSequentialGroup()
                        .addComponent(tituloCitas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addComponent(IconCitas)
                        .addGap(19, 19, 19))
                    .addGroup(PanelCitasLayout.createSequentialGroup()
                        .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(InfoCitasEspera)
                            .addComponent(InfocitasConfirmadas)
                            .addComponent(InfoNumeroCitas))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        PanelCitasLayout.setVerticalGroup(
            PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCitasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloCitas)
                    .addComponent(IconCitas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(InfoNumeroCitas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(InfocitasConfirmadas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(InfoCitasEspera)
                .addGap(12, 12, 12))
        );

        PanelCA.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setText("cobrado hoy");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel5.setText("120");
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel6.setText("18 pacientes nuevos");
        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(31, 111, 102));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/schedule_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        javax.swing.GroupLayout PanelCALayout = new javax.swing.GroupLayout(PanelCA);
        PanelCA.setLayout(PanelCALayout);
        PanelCALayout.setHorizontalGroup(
            PanelCALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCALayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCALayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addContainerGap(46, Short.MAX_VALUE))
            .addGroup(PanelCALayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PanelCALayout.setVerticalGroup(
            PanelCALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCALayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCALayout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6))
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        Panel_Ingresos.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setText("Devoluciones");
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel9.setText("1250");
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel10.setText("7 pagos registrados");
        jLabel10.setForeground(new java.awt.Color(31, 111, 102));

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/event_available_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        javax.swing.GroupLayout Panel_IngresosLayout = new javax.swing.GroupLayout(Panel_Ingresos);
        Panel_Ingresos.setLayout(Panel_IngresosLayout);
        Panel_IngresosLayout.setHorizontalGroup(
            Panel_IngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_IngresosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_IngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_IngresosLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(12, 12, 12))
        );
        Panel_IngresosLayout.setVerticalGroup(
            Panel_IngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_IngresosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_IngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(26, 26, 26)
                .addComponent(jLabel10)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jLabel12.setText("Cobros pendientes");
        jLabel12.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(18, 18, 18));

        jLabel13.setText("Pagos realizados");
        jLabel13.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(18, 18, 18));

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
        jScrollPane1.setViewportView(jTable1);

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
        jScrollPane2.setViewportView(jTable2);

        jButton1.setText("Registrar pago");
        jButton1.setBackground(new java.awt.Color(0, 143, 168));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("Pagar cuotas");
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        cmbPorTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/person_search_28dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        jButton5.setText("Buscar");
        jButton5.setBackground(new java.awt.Color(0, 143, 168));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));

        jButton6.setText("Nuevo");
        jButton6.setBackground(new java.awt.Color(0, 143, 168));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));

        jButton7.setText("Ver detalle");
        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        cmbPorOdontólogo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(34, 34, 34)
                .addComponent(PanelCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(PanelCA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(Panel_Ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(datePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addComponent(jLabel12)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton6)
                                .addGap(18, 18, 18)
                                .addComponent(cmbPorTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbPorOdontólogo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(94, 94, 94))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(PanelCA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Panel_Ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(29, 29, 29)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbPorTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cmbPorOdontólogo, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(jLabel13))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(datePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

    }//GEN-LAST:event_jButton4ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IconCitas;
    private javax.swing.JLabel InfoCitasEspera;
    private javax.swing.JLabel InfoNumeroCitas;
    private javax.swing.JLabel InfocitasConfirmadas;
    private javax.swing.JPanel PanelCA;
    private javax.swing.JPanel PanelCitas;
    private javax.swing.JPanel Panel_Ingresos;
    private javax.swing.JComboBox<String> cmbPorOdontólogo;
    private javax.swing.JComboBox<String> cmbPorTipo;
    private com.github.lgooddatepicker.components.DatePicker datePicker1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel tituloCitas;
    // End of variables declaration//GEN-END:variables
}
