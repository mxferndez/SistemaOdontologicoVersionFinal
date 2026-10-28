package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.AgendaAdministracionControladora;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import javax.swing.JOptionPane;

public class _DisponibilidadHabitual extends javax.swing.JPanel {

    AdVentanas dash;

    public _DisponibilidadHabitual(AdVentanas dashboard) {
        initComponents();
        this.dash = dashboard;
        PanelP2.putClientProperty("FlatLaf.style", "arc: 30");
        configurarFormulario();
    }

    private void configurarFormulario() {
        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"1 - Lunes", "2 - Martes",
            "3 - Miércoles", "4 - Jueves", "5 - Viernes", "6 - Sábado", "7 - Domingo"}));
        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Activo", "Inactivo"}));
        Mcitas.setValue(1);
        jDateChooser1.setDate(java.util.Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        timePicker1.setTime(LocalTime.of(9, 0));
        timePicker2.setTime(LocalTime.of(18, 0));
        try {
            AgendaFormSupport.cargarOdontologos(jComboBox5);
            AgendaFormSupport.cargarConsultorios(jComboBox6);
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudieron cargar las opciones",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardar() throws PersistenciaException {
        int odontologo = AgendaFormSupport.idSeleccionado(jComboBox5, "un odontólogo");
        int consultorio = AgendaFormSupport.idSeleccionado(jComboBox6, "un consultorio");
        String diaElegido = String.valueOf(jComboBox7.getSelectedItem());
        int dia = Integer.parseInt(diaElegido.substring(0, diaElegido.indexOf(" - ")));
        int maximo = ((Number) Mcitas.getValue()).intValue();
        LocalDate desde = AgendaFormSupport.fechaSql(jDateChooser1.getDate(), "la fecha de inicio", true)
                .toLocalDate();
        java.sql.Date hastaSql = AgendaFormSupport.fechaSql(jDateChooser2.getDate(), "la fecha de fin", false);
        LocalDate hasta = hastaSql == null ? null : hastaSql.toLocalDate();
        boolean activo = "Activo".equals(jComboBox8.getSelectedItem());
        new AgendaAdministracionControladora().guardarDisponibilidad(0, odontologo, consultorio, dia,
                timePicker1.getTime(), timePicker2.getTime(), maximo, desde, hasta, activo);
    }

    private void volver() {
        dash.mostrarPanel(new Ad_DisponibilidadO(dash));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        PanelP2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox6 = new javax.swing.JComboBox<>();
        jLabel28 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox<>();
        jComboBox8 = new javax.swing.JComboBox<>();
        timePicker1 = new com.github.lgooddatepicker.components.TimePicker();
        timePicker2 = new com.github.lgooddatepicker.components.TimePicker();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        Mcitas = new javax.swing.JSpinner();

        panel.setBackground(new java.awt.Color(245, 247, 250));

        PanelP2.setBackground(new java.awt.Color(255, 255, 255));
        PanelP2.setPreferredSize(new java.awt.Dimension(854, 540));

        jLabel17.setText("Odontólogo");
        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel18.setText("Consultorio");
        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel19.setText("Día de la semana");
        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel20.setText("Hora de inicio");
        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel21.setText("Hora fin");
        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel22.setText("Vigente desde ");
        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel23.setText("Vigente hasta (opcional)");
        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel24.setText("Estado");
        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jButton4.setText("Cancelar");
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(29, 41, 57));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/add_24dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton2.setText("Guardar");
        jButton2.setBackground(new java.awt.Color(0, 143, 168));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel28.setText("Disponibilidad habitual");
        jLabel28.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(18, 18, 18));

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Máximo de citas ");

        javax.swing.GroupLayout PanelP2Layout = new javax.swing.GroupLayout(PanelP2);
        PanelP2.setLayout(PanelP2Layout);
        PanelP2Layout.setHorizontalGroup(
            PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelP2Layout.createSequentialGroup()
                        .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox8, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox7, 0, 180, Short.MAX_VALUE)
                            .addComponent(jLabel23)
                            .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                            .addComponent(jLabel19)
                            .addComponent(timePicker2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelP2Layout.createSequentialGroup()
                                .addGap(209, 209, 209)
                                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(PanelP2Layout.createSequentialGroup()
                                .addGap(206, 206, 206)
                                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(timePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel24)))
                            .addGroup(PanelP2Layout.createSequentialGroup()
                                .addGap(206, 206, 206)
                                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Mcitas, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel28)
                    .addGroup(PanelP2Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(109, Short.MAX_VALUE))
        );
        PanelP2Layout.setVerticalGroup(
            PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelP2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel28)
                .addGap(51, 51, 51)
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelP2Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelP2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(35, 35, 35)
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(timePicker2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(28, 28, 28)
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jDateChooser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Mcitas, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelP2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(127, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(PanelP2, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                .addGap(84, 84, 84))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(PanelP2, javax.swing.GroupLayout.PREFERRED_SIZE, 678, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(123, 123, 123))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        volver();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            guardar();
            JOptionPane.showMessageDialog(this, "El horario habitual se guardó.", "Horario guardado",
                    JOptionPane.INFORMATION_MESSAGE);
            volver();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo guardar el horario",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner Mcitas;
    private javax.swing.JPanel PanelP2;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JComboBox<String> jComboBox5;
    private javax.swing.JComboBox<String> jComboBox6;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JComboBox<String> jComboBox8;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JPanel panel;
    private com.github.lgooddatepicker.components.TimePicker timePicker1;
    private com.github.lgooddatepicker.components.TimePicker timePicker2;
    // End of variables declaration//GEN-END:variables
}
