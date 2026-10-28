package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Sesion;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.JOptionPane;

public class RECVentanas extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(RECVentanas.class.getName());

    public RECVentanas() {
        initComponents();
        SetDate();
        // Para mostrar iniciales del Usuario 
        Sistemaodontologico.Lógica.Usuario usuarioActivo = Sesion.getInstancia().getUsuario();
        jTextField1.setText(usuarioActivo == null ? "" : usuarioActivo.getNombreCompleto());
        jTextField4.setText(usuarioActivo == null ? "" : usuarioActivo.getCargoLaboral());
        jTextField1.setEditable(false);
        jTextField4.setEditable(false);
        txtUsu.setText("RC");   // aquí va la variable "iniciales" que calculaste
        txtUsu.setEditable(false);
        txtUsu.setFocusable(false);
        txtUsu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtUsu.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

        txtUsu.putClientProperty("JComponent.roundRect", true);
        txtUsu.putClientProperty("FlatLaf.style",
                "background: #CFEDE7; inactiveBackground: #CFEDE7;"
                + "foreground: #1F6F66;"
                + "borderWidth: 0; focusWidth: 0");

        // Iniciales abajo
        txtUsu1.setText("RC");   // aquí va la variable "iniciales" que calculaste
        txtUsu1.setEditable(false);
        txtUsu1.setFocusable(false);
        txtUsu1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtUsu1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

        txtUsu1.putClientProperty("JComponent.roundRect", true);
        txtUsu1.putClientProperty("FlatLaf.style",
                "background: #CFEDE7; inactiveBackground: #CFEDE7;"
                + "foreground: #1F6F66;"
                + "borderWidth: 0; focusWidth: 0");

        //Estado activo 
        txtActive.setText(" Sistema activo");
        txtActive.setEditable(false);
        txtActive.setFocusable(false);
        txtActive.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        txtActive.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        txtActive.putClientProperty("JComponent.roundRect", true);
        txtActive.putClientProperty("FlatLaf.style",
                "background: #CFEDE7; inactiveBackground: #CFEDE7;"
                + "foreground: #1F6F66;"
                + "borderWidth: 0; focusWidth: 0");

        PanelDashboard.setLayout(new java.awt.BorderLayout());
        mostrarPanel(new REC_Principal(this));

        marcarBoton(jButton1);

        jButton1.setFocusable(false);
        jButton3.setFocusable(false);
        jButton4.setFocusable(false);
        jButton5.setFocusable(false);
        jButton6.setFocusable(false);
        jButton9.setFocusable(false);
    }

    public void mostrarPanel(javax.swing.JPanel p) {
        PanelDashboard.removeAll();
        PanelDashboard.add(p, java.awt.BorderLayout.CENTER);
        PanelDashboard.revalidate();
        PanelDashboard.repaint();
    }

    private void SetDate() {
        LocalDate now = LocalDate.now();
        Locale spanishLocale = new Locale("es", "ES");
        dateText.setText(now.format(DateTimeFormatter.ofPattern(" EEEE dd 'de' MMMM 'de' yyyy", spanishLocale)));
    }

    private boolean editando = false;

    public void setEditando(boolean editando) {
        this.editando = editando;
    }

    private boolean irAMenu(javax.swing.JPanel p) {
        if (editando) {
            int r = javax.swing.JOptionPane.showConfirmDialog(this,
                    "Tienes cambios sin guardar. ¿Deseas salir?",
                    "Salir sin guardar",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE);
            if (r != javax.swing.JOptionPane.YES_OPTION) {
                return false;
            }
            editando = false;
        }
        mostrarPanel(p);
        return true;
    }

    private void marcarBoton(javax.swing.JButton activo) {
        javax.swing.JButton[] botones = {jButton1, jButton3, jButton4, jButton5, jButton6};

        for (int i = 0; i < botones.length; i++) {
            botones[i].putClientProperty("FlatLaf.style", "background: #FFFFFF");
        }

        activo.putClientProperty("FlatLaf.style", "background: #CFEDE7; foreground: #1F6F66");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Menu = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        txtUsu1 = new javax.swing.JTextField();
        jTextField1 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        Header = new javax.swing.JPanel();
        txtActive = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        txtUsu = new javax.swing.JTextField();
        dateText = new javax.swing.JLabel();
        PanelDashboard = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));
        jPanel1.setPreferredSize(new java.awt.Dimension(1130, 730));

        Menu.setBackground(new java.awt.Color(255, 255, 255));
        Menu.setPreferredSize(new java.awt.Dimension(1130, 715));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/Logo_Dental_Clinic_icono_150x150.png"))); // NOI18N

        jLabel2.setBackground(new java.awt.Color(150, 162, 183));
        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(150, 162, 183));
        jLabel2.setText("Recepción");

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(29, 41, 57));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/home_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton1.setText("Panel principal");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.setForeground(new java.awt.Color(29, 41, 57));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/calendar_month_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton3.setText("Agenda");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(29, 41, 57));
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/person_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton4.setText("Pacientes");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(29, 41, 57));
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/point_of_sale_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton5.setText("Caja y cobros ");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(29, 41, 57));
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/description_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton6.setText("Presupuestos");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/exit_to_app_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton9.setText("Cerrar sessión ");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jSeparator1.setAlignmentY(0.7F);

        txtUsu1.setEditable(false);
        txtUsu1.setText("MR");

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(18, 18, 18));
        jTextField1.setText("jTextField1");
        jTextField1.setBorder(null);

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(255, 255, 255));
        jTextField4.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jTextField4.setForeground(new java.awt.Color(18, 18, 18));
        jTextField4.setText("jTextField4");
        jTextField4.setBorder(null);

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu);
        Menu.setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(jLabel2)
                            .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                            .addGroup(MenuLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel1))
                            .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(MenuLayout.createSequentialGroup()
                                .addComponent(txtUsu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                    .addComponent(jTextField4))))))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MenuLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(MenuLayout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addComponent(txtUsu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        Header.setBackground(new java.awt.Color(255, 255, 255));

        txtActive.setEditable(false);
        txtActive.setText("Sistema activo ");
        txtActive.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtActiveActionPerformed(evt);
            }
        });

        jTextField2.setEditable(false);
        jTextField2.setBackground(new java.awt.Color(255, 255, 255));
        jTextField2.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jTextField2.setForeground(new java.awt.Color(18, 18, 18));
        jTextField2.setText("Usuario:");
        jTextField2.setBorder(null);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jTextField3.setEditable(false);
        jTextField3.setBackground(new java.awt.Color(255, 255, 255));
        jTextField3.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jTextField3.setForeground(new java.awt.Color(18, 18, 18));
        jTextField3.setText("Recepcionista");
        jTextField3.setBorder(null);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        txtUsu.setEditable(false);
        txtUsu.setText("MR");

        dateText.setBackground(new java.awt.Color(255, 255, 255));
        dateText.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        dateText.setForeground(new java.awt.Color(153, 153, 153));
        dateText.setText(" {dayname} {day} de {month} de {year}");

        javax.swing.GroupLayout HeaderLayout = new javax.swing.GroupLayout(Header);
        Header.setLayout(HeaderLayout);
        HeaderLayout.setHorizontalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addComponent(dateText, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(135, 135, 135)
                .addComponent(txtActive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtUsu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );
        HeaderLayout.setVerticalGroup(
            HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HeaderLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(HeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtActive, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateText, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        PanelDashboard.setBackground(new java.awt.Color(245, 247, 250));

        javax.swing.GroupLayout PanelDashboardLayout = new javax.swing.GroupLayout(PanelDashboard);
        PanelDashboard.setLayout(PanelDashboardLayout);
        PanelDashboardLayout.setHorizontalGroup(
            PanelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 854, Short.MAX_VALUE)
        );
        PanelDashboardLayout.setVerticalGroup(
            PanelDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelDashboard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Header, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(Header, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PanelDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (irAMenu(new REC_Principal(this))) {
            marcarBoton(jButton1);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (irAMenu(new REC_Agenda())) {
            marcarBoton(jButton3);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (irAMenu(new REC_Pacientes(this))) {
            marcarBoton(jButton4);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (irAMenu(new REC_Caja(this))) {
            marcarBoton(jButton5);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        if (irAMenu(new REC_Presupuestos())) {
            marcarBoton(jButton6);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        if (JOptionPane.showConfirmDialog(rootPane, "¿Desea cerrar la sesión?", "Cerrar sesión", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            Sesion.getInstancia().cerrar();
            this.dispose();
            Login login = new Login();
            login.setVisible(true);
            login.setLocationRelativeTo(null);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void txtActiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtActiveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtActiveActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Header;
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel PanelDashboard;
    private javax.swing.JLabel dateText;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField txtActive;
    private javax.swing.JTextField txtUsu;
    private javax.swing.JTextField txtUsu1;
    // End of variables declaration//GEN-END:variables
}
