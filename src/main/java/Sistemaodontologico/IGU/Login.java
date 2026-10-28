package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Controladora;
import Sistemaodontologico.Lógica.Sesion;
import Sistemaodontologico.Lógica.Usuario;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {

    private final Controladora control = new Controladora();

    public Login() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitulo1 = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        panelPrincipal1 = new javax.swing.JPanel();
        lblSubtitulo = new javax.swing.JLabel();
        lblCorreo1 = new javax.swing.JLabel();
        txtCorreo1 = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnIniciarSesion = new javax.swing.JButton();
        lblSubtitulo1 = new javax.swing.JLabel();
        lblTitulo2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(81, 178, 186));

        lblTitulo1.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblTitulo1.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo1.setText("Sistema de Gestión ");

        lblTitulo.setBackground(new java.awt.Color(255, 255, 255));
        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblTitulo.setForeground(new java.awt.Color(255, 255, 255));
        lblTitulo.setText(" Odontológica");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/525fead2-eb35-4604-b5db-6507d18d1aa4.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 54, Short.MAX_VALUE)
                .addComponent(lblTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(lblTitulo1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelPrincipal1.setBackground(new java.awt.Color(255, 255, 255));
        panelPrincipal1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSubtitulo.setFont(new java.awt.Font("Perpetua", 0, 16)); // NOI18N
        lblSubtitulo.setText("de la clínica");
        panelPrincipal1.add(lblSubtitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 120, 80, 20));

        lblCorreo1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblCorreo1.setForeground(new java.awt.Color(18, 18, 18));
        lblCorreo1.setText("Usuario:");
        panelPrincipal1.add(lblCorreo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 160, 70, 30));

        txtCorreo1.setColumns(20);
        txtCorreo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCorreo1ActionPerformed(evt);
            }
        });
        panelPrincipal1.add(txtCorreo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 190, 280, 40));

        lblPassword.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblPassword.setForeground(new java.awt.Color(18, 18, 18));
        lblPassword.setText("Contraseña:");
        panelPrincipal1.add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 260, 120, 20));

        txtPassword.setColumns(20);
        panelPrincipal1.add(txtPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 280, 280, 40));

        btnIniciarSesion.setBackground(new java.awt.Color(0, 143, 168));
        btnIniciarSesion.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnIniciarSesion.setForeground(new java.awt.Color(255, 255, 255));
        btnIniciarSesion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/arrow_forward_24dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        btnIniciarSesion.setText("Iniciar sesión");
        btnIniciarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIniciarSesionActionPerformed(evt);
            }
        });
        panelPrincipal1.add(btnIniciarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 350, 230, 50));

        lblSubtitulo1.setBackground(new java.awt.Color(51, 51, 51));
        lblSubtitulo1.setFont(new java.awt.Font("Perpetua", 0, 16)); // NOI18N
        lblSubtitulo1.setText("Acceso seguro para el personal autorizado ");
        panelPrincipal1.add(lblSubtitulo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 270, 30));

        lblTitulo2.setFont(new java.awt.Font("Perpetua", 1, 30)); // NOI18N
        lblTitulo2.setForeground(new java.awt.Color(18, 18, 18));
        lblTitulo2.setText("Inicio de Sesión");
        panelPrincipal1.add(lblTitulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 210, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(2, 2, 2)
                .addComponent(panelPrincipal1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelPrincipal1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCorreo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCorreo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCorreo1ActionPerformed

    private void btnIniciarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIniciarSesionActionPerformed
        String nombreUsuario = txtCorreo1.getText().trim();
        String contrasena = new String(txtPassword.getPassword());

        try {
            Usuario usuario = control.validarUsuario(nombreUsuario, contrasena);
            if (usuario == null) {
                JOptionPane.showMessageDialog(this,
                        "El usuario o la contraseña no son válidos, o el acceso está deshabilitado.",
                        "No se pudo iniciar sesión",
                        JOptionPane.WARNING_MESSAGE);
                txtPassword.setText("");
                txtPassword.requestFocusInWindow();
                return;
            }

            ArrayList<String> roles = usuario.getNombresRoles();
            if (roles.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La cuenta no tiene un rol asignado.",
                        "Acceso no disponible",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            String rolActivo = roles.get(0);
            if (roles.size() > 1) {
                Object seleccionado = JOptionPane.showInputDialog(this,
                        "Selecciona el área con la que deseas ingresar:",
                        "Seleccionar área",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        roles.toArray(),
                        roles.get(0));
                if (seleccionado == null) {
                    return;
                }
                rolActivo = seleccionado.toString();
            }

            javax.swing.JFrame ventana;
            switch (rolActivo.trim().toUpperCase(java.util.Locale.ROOT)) {
                case "ADMINISTRADOR":
                    Sesion.getInstancia().iniciar(usuario, rolActivo);
                    ventana = new AdVentanas();
                    break;
                case "ODONTOLOGO":
                    Sesion.getInstancia().iniciar(usuario, rolActivo);
                    ventana = new ODVentanas();
                    break;
                case "RECEPCIONISTA":
                    Sesion.getInstancia().iniciar(usuario, rolActivo);
                    ventana = new RECVentanas();
                    break;
                default:
                    JOptionPane.showMessageDialog(this,
                            "El rol '" + rolActivo + "' no tiene una ventana asignada.",
                            "Acceso no disponible",
                            JOptionPane.WARNING_MESSAGE);
                    return;
            }

            ventana.setLocationRelativeTo(null);
            ventana.setVisible(true);
            this.dispose();
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo verificar el acceso: " + ex.getMessage(),
                    "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnIniciarSesionActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnIniciarSesion;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCorreo1;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblSubtitulo1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTitulo1;
    private javax.swing.JLabel lblTitulo2;
    private javax.swing.JPanel panelPrincipal1;
    private javax.swing.JTextField txtCorreo1;
    private javax.swing.JPasswordField txtPassword;
    // End of variables declaration//GEN-END:variables
}
