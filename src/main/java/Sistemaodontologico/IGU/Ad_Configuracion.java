package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.ConfiguracionControladora;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalTime;
import javax.swing.JOptionPane;

public class Ad_Configuracion extends javax.swing.JPanel {

    public Ad_Configuracion() {
        initComponents();
        panelConfig.putClientProperty("FlatLaf.style", "arc: 40");
        Mlimpieza.putClientProperty("JComponent.roundRect", true);
        Tllegada.putClientProperty("JComponent.roundRect", true);
        Hcancelar.putClientProperty("JComponent.roundRect", true);
        Dbloqueo.putClientProperty("JComponent.roundRect", true);
        Hlimite.getComponentTimeTextField().putClientProperty("JComponent.roundRect", true);
        Hlimite.getComponentToggleTimeMenuButton().putClientProperty("JButton.buttonType", "roundRect");
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"PEN", "USD", "EUR"}));
        cargarConfiguracion();
    }

    private void cargarConfiguracion() {
        try {
            Object[] valores = new ConfiguracionControladora().buscar();
            jComboBox1.setSelectedItem(valores[0]);
            Hlimite.setTime((LocalTime) valores[1]);
            Dbloqueo.setValue(valores[2]);
            Tllegada.setValue(valores[3]);
            Hcancelar.setValue(valores[4]);
            Mlimpieza.setValue(valores[5]);
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo cargar la configuración",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        panelConfig = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Hlimite = new com.github.lgooddatepicker.components.TimePicker();
        jLabel5 = new javax.swing.JLabel();
        Dbloqueo = new javax.swing.JSpinner();
        jLabel6 = new javax.swing.JLabel();
        Tllegada = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        Hcancelar = new javax.swing.JSpinner();
        jLabel8 = new javax.swing.JLabel();
        Mlimpieza = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton6 = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(860, 680));

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(860, 680));

        jLabel2.setText("Reglas de reservas y atención");
        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(18, 18, 18));

        panelConfig.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("Moneda");
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel4.setText("Hora límite de reservas remotas");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel5.setText("Días de bloqueo por infracción");
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel6.setText("Tolerancia de llegada ");
        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel7.setText("horas para cancelar o cambiar");
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel8.setText("Margen de limpieza");
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout panelConfigLayout = new javax.swing.GroupLayout(panelConfig);
        panelConfig.setLayout(panelConfigLayout);
        panelConfigLayout.setHorizontalGroup(
            panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfigLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(Hlimite, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(Dbloqueo, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Tllegada)
                    .addComponent(Hcancelar)
                    .addComponent(Mlimpieza)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(266, Short.MAX_VALUE))
        );
        panelConfigLayout.setVerticalGroup(
            panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelConfigLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Tllegada, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(jComboBox1))
                .addGap(62, 62, 62)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Hlimite, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelConfigLayout.createSequentialGroup()
                        .addComponent(Hcancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addGap(78, 78, 78)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelConfigLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Mlimpieza, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(Dbloqueo))
                .addContainerGap(177, Short.MAX_VALUE))
        );

        jButton6.setText("Guardar cambios");
        jButton6.setBackground(new java.awt.Color(0, 143, 168));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 255, Short.MAX_VALUE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(panelConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(80, 80, 80))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(83, 83, 83))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 848, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            if (!(Dbloqueo.getValue() instanceof Number bloqueo)
                    || !(Tllegada.getValue() instanceof Number llegada)
                    || !(Hcancelar.getValue() instanceof Number cancelar)
                    || !(Mlimpieza.getValue() instanceof Number limpieza)) {
                throw new IllegalArgumentException("Revise los valores de días, horas y minutos.");
            }
            new ConfiguracionControladora().guardar(String.valueOf(jComboBox1.getSelectedItem()), Hlimite.getTime(),
                    bloqueo.intValue(), llegada.intValue(), cancelar.intValue(), limpieza.intValue());
            JOptionPane.showMessageDialog(this, "La configuración se guardó.", "Cambios guardados",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo guardar",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton6ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner Dbloqueo;
    private javax.swing.JSpinner Hcancelar;
    private com.github.lgooddatepicker.components.TimePicker Hlimite;
    private javax.swing.JSpinner Mlimpieza;
    private javax.swing.JSpinner Tllegada;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel panelConfig;
    // End of variables declaration//GEN-END:variables
}
