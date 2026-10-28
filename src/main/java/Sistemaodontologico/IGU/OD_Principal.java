package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Cita;
import Sistemaodontologico.Lógica.CitaControladora;
import Sistemaodontologico.Lógica.Sesion;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class OD_Principal extends javax.swing.JPanel {

    ODVentanas dashboardV;

    public OD_Principal(ODVentanas dashboard) {
        initComponents();
        this.dashboardV = dashboard;
        cargarCitasDelDia();
    }

    private void cargarCitasDelDia() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Hora", "Paciente", "Consultorio", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tableCitas.setModel(modelo);
        tableCitas.getColumnModel().getColumn(0).setMinWidth(0);
        tableCitas.getColumnModel().getColumn(0).setMaxWidth(0);
        tableCitas.getColumnModel().getColumn(0).setWidth(0);
        try {
            ArrayList<Cita> citas = new CitaControladora().listarDia(
                    LocalDate.now(), Sesion.getInstancia().getIdUsuario(), 0);
            citas.sort(Comparator.comparingInt((Cita cita) -> "EN_ESPERA".equals(cita.getEstado()) ? 0 : 1)
                    .thenComparing(cita -> "EN_ESPERA".equals(cita.getEstado()) && cita.getHoraLlegada() != null
                    ? cita.getHoraLlegada() : cita.getInicio()));
            DateTimeFormatter hora = DateTimeFormatter.ofPattern("HH:mm");
            int enEspera = 0;
            for (Cita cita : citas) {
                modelo.addRow(new Object[]{cita.getIdCita(), cita.getInicio().format(hora),
                    cita.getPaciente(), cita.getConsultorio(), cita.getEstado()});
                if ("EN_ESPERA".equals(cita.getEstado())) {
                    enEspera++;
                }
            }
            InfoNumeroCitas.setText(String.valueOf(citas.size()));
            InfocitasConfirmadas.setText(citas.size() + " citas hoy");
            InfoCitasEspera.setText(enEspera + " en espera");
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudieron cargar las citas",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private Cita citaSeleccionada() throws PersistenciaException {
        int fila = tableCitas.getSelectedRow();
        if (fila < 0) {
            throw new IllegalArgumentException("Seleccione una cita de la lista.");
        }
        int filaModelo = tableCitas.convertRowIndexToModel(fila);
        long idCita = ((Number) tableCitas.getModel().getValueAt(filaModelo, 0)).longValue();
        return new CitaControladora().buscar(idCita);
    }

    private void abrirAtencion(boolean iniciar) {
        try {
            Cita cita = citaSeleccionada();
            if (cita == null) {
                throw new IllegalArgumentException("La cita seleccionada ya no existe.");
            }
            CitaControladora controladora = new CitaControladora();
            if (iniciar) {
                if (!"EN_ESPERA".equals(cita.getEstado())) {
                    throw new IllegalArgumentException("Recepción debe marcar la llegada antes de iniciar la atención.");
                }
                int siguienteFila = -1;
                for (int i = 0; i < tableCitas.getRowCount(); i++) {
                    if ("EN_ESPERA".equals(String.valueOf(tableCitas.getValueAt(i, 4)))) {
                        siguienteFila = i;
                        break;
                    }
                }
                int filaSeleccionada = tableCitas.convertRowIndexToModel(tableCitas.getSelectedRow());
                if (siguienteFila >= 0 && filaSeleccionada != siguienteFila) {
                    throw new IllegalArgumentException("Atienda primero al siguiente paciente de la cola, según la hora de llegada programada.");
                }
                controladora.iniciarAtencion(cita.getIdCita());
                cita.setEstado("EN_ATENCION");
            } else if (!"EN_ATENCION".equals(cita.getEstado())) {
                throw new IllegalArgumentException("Solo se puede continuar una atención que ya está iniciada.");
            }
            dashboardV.mostrarPanel(new ___AtenciónClinica(dashboardV, cita));
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo abrir la atención",
                    JOptionPane.ERROR_MESSAGE);
            cargarCitasDelDia();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        ScrollCitas = new javax.swing.JScrollPane();
        tableCitas = new javax.swing.JTable();
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
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        Panel_Ingresos1 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));

        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(18, 18, 18));
        jLabel2.setText("Mi jornada");

        tableCitas.setModel(new javax.swing.table.DefaultTableModel(
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
        ScrollCitas.setViewportView(tableCitas);

        PanelCitas.setBackground(new java.awt.Color(255, 255, 255));

        tituloCitas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tituloCitas.setText("Citas  hoy");

        InfoNumeroCitas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        InfoNumeroCitas.setText("8");

        InfocitasConfirmadas.setForeground(new java.awt.Color(31, 111, 102));
        InfocitasConfirmadas.setText("6 confirmadas");

        IconCitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/event_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        InfoCitasEspera.setForeground(new java.awt.Color(102, 204, 255));
        InfoCitasEspera.setText("2 Espera");

        javax.swing.GroupLayout PanelCitasLayout = new javax.swing.GroupLayout(PanelCitas);
        PanelCitas.setLayout(PanelCitasLayout);
        PanelCitasLayout.setHorizontalGroup(
            PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCitasLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCitasLayout.createSequentialGroup()
                        .addComponent(tituloCitas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
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

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("En  espera");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("120");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(31, 111, 102));
        jLabel6.setText("18 pacientes nuevos");

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
                .addContainerGap(45, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel_Ingresos.setBackground(new java.awt.Color(255, 255, 255));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Atendidos");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("1250");

        jLabel10.setForeground(new java.awt.Color(31, 111, 102));
        jLabel10.setText("7 pagos registrados");

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
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel12.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(18, 18, 18));
        jLabel12.setText("Citas del día ");

        jButton2.setBackground(new java.awt.Color(0, 143, 168));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("Iniciar atención");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setText("Continuar atención");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        Panel_Ingresos1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Planes activos");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel14.setText("1250");

        jLabel15.setForeground(new java.awt.Color(31, 111, 102));
        jLabel15.setText("7 pagos registrados");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/event_available_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        javax.swing.GroupLayout Panel_Ingresos1Layout = new javax.swing.GroupLayout(Panel_Ingresos1);
        Panel_Ingresos1.setLayout(Panel_Ingresos1Layout);
        Panel_Ingresos1Layout.setHorizontalGroup(
            Panel_Ingresos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Ingresos1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_Ingresos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_Ingresos1Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
                    .addComponent(jLabel14)
                    .addComponent(jLabel15))
                .addGap(12, 12, 12))
        );
        Panel_Ingresos1Layout.setVerticalGroup(
            Panel_Ingresos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_Ingresos1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_Ingresos1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addGap(26, 26, 26)
                .addComponent(jLabel15)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(PanelCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(PanelCA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(Panel_Ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(Panel_Ingresos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ScrollCitas, javax.swing.GroupLayout.DEFAULT_SIZE, 659, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(59, 59, 59))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Panel_Ingresos1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelCitas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Panel_Ingresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelCA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(45, 45, 45))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollCitas, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(70, 70, 70))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        abrirAtencion(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        abrirAtencion(true);
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IconCitas;
    private javax.swing.JLabel InfoCitasEspera;
    private javax.swing.JLabel InfoNumeroCitas;
    private javax.swing.JLabel InfocitasConfirmadas;
    private javax.swing.JPanel PanelCA;
    private javax.swing.JPanel PanelCitas;
    private javax.swing.JPanel Panel_Ingresos;
    private javax.swing.JPanel Panel_Ingresos1;
    private javax.swing.JScrollPane ScrollCitas;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable tableCitas;
    private javax.swing.JLabel tituloCitas;
    // End of variables declaration//GEN-END:variables
}
