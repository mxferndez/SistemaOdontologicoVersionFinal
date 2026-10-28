package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Cita;
import Sistemaodontologico.Lógica.CitaControladora;
import Sistemaodontologico.Lógica.Sesion;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class OD_Agenda extends javax.swing.JPanel {

    private LocalDate fecha = LocalDate.now();

    public OD_Agenda() {
        initComponents();
        txtDianterior.addActionListener(e -> {
            fecha = fecha.minusDays(1);
            cargarAgenda();
        });
        btnDia_siguiente.addActionListener(e -> {
            if (fecha.isBefore(LocalDate.now())) {
                fecha = fecha.plusDays(1);
                cargarAgenda();
            }
            fecha = fecha.plusDays(1);
            cargarAgenda();
        });
        cargarAgenda();
    }

    private void cargarAgenda() {
        txtDía.setText(fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Hora", "Paciente", "Consultorio", "Tipo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tableCitas.setModel(modelo);
        try {
            ArrayList<Cita> citas = new CitaControladora().listarDia(fecha, Sesion.getInstancia().getIdUsuario(), 0);
            DateTimeFormatter hora = DateTimeFormatter.ofPattern("HH:mm");
            for (Cita cita : citas) {
                modelo.addRow(new Object[]{cita.getIdCita(), cita.getInicio().format(hora),
                    cita.getPaciente(), cita.getConsultorio(), cita.getTipo(), cita.getEstado()});
            }
            tableCitas.getColumnModel().getColumn(0).setMinWidth(0);
            tableCitas.getColumnModel().getColumn(0).setMaxWidth(0);
            tableCitas.getColumnModel().getColumn(0).setWidth(0);
            btnDia_siguiente.setEnabled(fecha.isBefore(LocalDate.now()));
            btnDia_siguiente.setEnabled(true);

        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo cargar mi agenda", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtDianterior = new javax.swing.JButton();
        btnDia_siguiente = new javax.swing.JButton();
        txtDía = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        ScrollCitas = new javax.swing.JScrollPane();
        tableCitas = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));

        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(18, 18, 18));
        jLabel2.setText("Mi agenda");

        txtDianterior.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDianterior.setText("Día anterior");
        txtDianterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDianteriorActionPerformed(evt);
            }
        });

        btnDia_siguiente.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnDia_siguiente.setText("Día siguiente");

        txtDía.setEditable(false);
        txtDía.setBackground(new java.awt.Color(245, 247, 250));
        txtDía.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtDía.setText("jTextField1");
        txtDía.setBorder(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(18, 18, 18));
        jLabel1.setText("Citas  ");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel2)
                        .addGap(52, 722, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(ScrollCitas)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtDianterior)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtDía, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDia_siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel1))
                        .addGap(0, 347, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDianterior, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDía, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDia_siguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ScrollCitas, javax.swing.GroupLayout.DEFAULT_SIZE, 429, Short.MAX_VALUE)
                .addGap(46, 46, 46))
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

    private void txtDianteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDianteriorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDianteriorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane ScrollCitas;
    private javax.swing.JButton btnDia_siguiente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable tableCitas;
    private javax.swing.JButton txtDianterior;
    private javax.swing.JTextField txtDía;
    // End of variables declaration//GEN-END:variables
}
