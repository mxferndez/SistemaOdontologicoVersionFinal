package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Controladora;
import Sistemaodontologico.Lógica.Rol;
import Sistemaodontologico.Lógica.Usuario;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author rodri
 */
public class Ad_Usuarios extends javax.swing.JPanel {

    AdVentanas dashboardP;

    public Ad_Usuarios(AdVentanas dashboard) {
        initComponents();
        this.dashboardP = dashboard;
        configurarFiltros();
        jButton5.addActionListener(e -> cargarUsuarios());
        jButton6.addActionListener(e -> dashboardP.mostrarPanel(new _AltaUsuario(dashboardP)));
        cargarUsuarios();
    }

    private void configurarFiltros() {
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"TODOS", "ACTIVO", "BAJA"}));
        try {
            ArrayList<Rol> roles = new Controladora().traerRoles();
            jComboBox2.removeAllItems();
            jComboBox2.addItem("TODOS");
            for (Rol rol : roles) {
                jComboBox2.addItem(rol.getNombre());
            }
            jComboBox1.addActionListener(e -> cargarUsuarios());
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudieron cargar los filtros",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarUsuarios() {
        try {
            String texto = jTextField1.getText().trim().toLowerCase(Locale.ROOT);
            String estado = String.valueOf(jComboBox1.getSelectedItem());
            String rol = String.valueOf(jComboBox2.getSelectedItem());
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Documento",
                "Usuario", "Roles", "Cargo", "Estado laboral", "Acceso", "Colegiatura"}, 0) {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            for (Usuario usuario : new Controladora().traerUsuarios()) {
                String roles = String.join(", ", usuario.getNombresRoles());
                PersonaDatos datos = new PersonaDatos(usuario);
                String buscar = (datos.nombre + " " + datos.documento + " " + usuario.getNombreUsuario()
                        + " " + roles + " " + usuario.getCargoLaboral()).toLowerCase(Locale.ROOT);
                if (!texto.isEmpty() && !buscar.contains(texto)) {
                    continue;
                }
                if (!"TODOS".equals(estado) && !estado.equals(usuario.getEstadoLaboral())) {
                    continue;
                }
                if (!"TODOS".equals(rol) && !usuario.tieneRol(rol)) {
                    continue;
                }
                modelo.addRow(new Object[]{usuario.getIdUsuario(), datos.nombre, datos.documento,
                    usuario.getNombreUsuario(), roles, usuario.getCargoLaboral(), usuario.getEstadoLaboral(),
                    usuario.getEstadoAcceso(), usuario.getNumeroColegiatura()});
            }
            jTable1.setModel(modelo);
            jTable1.getColumnModel().getColumn(0).setMinWidth(0);
            jTable1.getColumnModel().getColumn(0).setMaxWidth(0);
            jTable1.getColumnModel().getColumn(0).setWidth(0);
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo cargar el personal",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static final class PersonaDatos {

        private final String nombre;
        private final String documento;

        private PersonaDatos(Usuario usuario) {
            nombre = usuario.getNombreCompleto();
            documento = usuario.getPersona().getTipoDocumento() + " " + usuario.getPersona().getNumeroDocumento();
        }
    }

    private long idUsuarioSeleccionado() {
        int fila = jTable1.getSelectedRow();
        if (fila < 0) {
            throw new IllegalArgumentException("Seleccione un usuario de la lista.");
        }
        return ((Number) jTable1.getModel().getValueAt(jTable1.convertRowIndexToModel(fila), 0)).longValue();
    }

    private void cambiarAccesoSeleccionado() {
        try {
            long id = idUsuarioSeleccionado();
            Usuario usuario = new Controladora().traerUsuario(id);
            if (usuario == null) {
                throw new IllegalArgumentException("El usuario seleccionado ya no existe.");
            }
            String nuevoEstado = "HABILITADO".equals(usuario.getEstadoAcceso()) ? "BLOQUEADO" : "HABILITADO";
            if ("HABILITADO".equals(nuevoEstado) && !"ACTIVO".equals(usuario.getEstadoLaboral())) {
                throw new IllegalArgumentException("No se puede habilitar el acceso de una persona dada de baja.");
            }
            new Controladora().cambiarEstadoAcceso(id, nuevoEstado);
            cargarUsuarios();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo cambiar el acceso",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void darDeBajaSeleccionado() {
        try {
            long id = idUsuarioSeleccionado();
            if (JOptionPane.showConfirmDialog(this, "¿Dar de baja al usuario seleccionado?",
                    "Confirmar baja", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
                return;
            }
            new Controladora().darDeBajaUsuario(id);
            cargarUsuarios();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo dar de baja",
                    JOptionPane.ERROR_MESSAGE);
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
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));

        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(18, 18, 18));
        jLabel2.setText("Usuarios y personal");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/person_search_28dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

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

        jLabel3.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(18, 18, 18));
        jLabel3.setText("Personal del sistema");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(29, 41, 57));
        jButton1.setText("Editar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(0, 143, 168));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/add_24dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton2.setText("Nuevo usuario");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(29, 41, 57));
        jButton3.setText("Desactivar acceso");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(29, 41, 57));
        jButton4.setText("Borrar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(0, 143, 168));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Buscar");

        jButton6.setBackground(new java.awt.Color(0, 143, 168));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Nuevo");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)
                        .addGap(12, 12, 12)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addComponent(jButton5)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel2)))
                        .addGap(0, 47, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jComboBox2, 0, 121, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68))
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

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        cargarUsuarios();
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        dashboardP.mostrarPanel(new _AltaUsuario(dashboardP));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        cambiarAccesoSeleccionado();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        darDeBajaSeleccionado();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            dashboardP.mostrarPanel(new _EditarUsuario(dashboardP, idUsuarioSeleccionado()));
        } catch (RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "Sin selección", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        cargarUsuarios();
    }//GEN-LAST:event_jComboBox2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
