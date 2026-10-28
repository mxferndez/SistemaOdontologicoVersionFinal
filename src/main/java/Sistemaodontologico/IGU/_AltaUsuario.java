package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Controladora;
import Sistemaodontologico.Lógica.PacienteControladora;
import Sistemaodontologico.Lógica.Persona;
import Sistemaodontologico.Lógica.Rol;
import Sistemaodontologico.Lógica.Usuario;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class _AltaUsuario extends javax.swing.JPanel {

    AdVentanas dash;
    private Persona personaEncontrada;

    public _AltaUsuario(AdVentanas dashboard) {
        initComponents();
        this.dash = dashboard;
        PanelP.putClientProperty("FlatLaf.style", "arc: 30");
        txtColegiatura.setEnabled(false);
        configurarFormulario();
    }

    private void configurarFormulario() {
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"DNI", "CE", "PASAPORTE", "OTRO"}));
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"ACTIVO", "BAJA"}));
        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Odontólogo", "Recepcionista", "Administración", "Otro"}));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"HABILITADO", "BLOQUEADO"}));
        jComboBox2.setSelectedItem("ACTIVO");
        jComboBox5.setSelectedItem("HABILITADO");
        jButton5.addActionListener(e -> buscarPersona());
        jButton6.addActionListener(e -> limpiarFormulario());
    }

    private void buscarPersona() {
        try {
            String documento = jTextField8.getText().trim();
            if (documento.isEmpty()) {
                throw new IllegalArgumentException("Escriba el documento que desea buscar.");
            }
            personaEncontrada = new PacienteControladora().buscarPersona(String.valueOf(jComboBox1.getSelectedItem()), documento);
            if (personaEncontrada == null) {
                jTextField1.setText(documento);
                JOptionPane.showMessageDialog(this, "No se encontró la persona. Complete los datos para crearla.",
                        "Persona no encontrada", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            jComboBox1.setSelectedItem(personaEncontrada.getTipoDocumento());
            jTextField1.setText(personaEncontrada.getNumeroDocumento());
            jTextField2.setText(personaEncontrada.getNombres());
            jTextField3.setText(personaEncontrada.getApellidoPaterno());
            jTextField4.setText(personaEncontrada.getApellidoMaterno());
            jTextField5.setText(personaEncontrada.getTelefono());
            jTextField6.setText(personaEncontrada.getCorreo());
            jTextField7.setText(personaEncontrada.getDireccion());
            if (personaEncontrada.getFechaNacimiento() != null) {
                jDateChooser1.setDate(java.util.Date.from(personaEncontrada.getFechaNacimiento()
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }
            JOptionPane.showMessageDialog(this, "Se cargaron los datos existentes de la persona.",
                    "Persona encontrada", JOptionPane.INFORMATION_MESSAGE);
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo buscar la persona",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        personaEncontrada = null;
        for (javax.swing.JTextField campo : new javax.swing.JTextField[]{jTextField1, jTextField2, jTextField3,
            jTextField4, jTextField5, jTextField6, jTextField7, jTextField8, jTextField10, txtColegiatura}) {
            campo.setText("");
        }
        jPasswordField1.setText("");
        jDateChooser1.setDate(null);
        jDateChooser2.setDate(null);
        jDateChooser3.setDate(null);
        jCheckBox1.setSelected(false);
        chkOdontologo.setSelected(false);
        jCheckBox3.setSelected(false);
        txtColegiatura.setEnabled(false);
        jComboBox2.setSelectedItem("ACTIVO");
        jComboBox5.setSelectedItem("HABILITADO");
    }

    private void crearUsuario() {
        try {
            Persona persona = personaEncontrada == null ? new Persona() : personaEncontrada;
            persona.setTipoDocumento(String.valueOf(jComboBox1.getSelectedItem()));
            persona.setNumeroDocumento(jTextField1.getText().trim());
            persona.setNombres(jTextField2.getText().trim());
            persona.setApellidoPaterno(jTextField3.getText().trim());
            persona.setApellidoMaterno(jTextField4.getText().trim());
            persona.setFechaNacimiento(jDateChooser1.getDate() == null ? null
                    : jDateChooser1.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            persona.setTelefono(jTextField5.getText().trim());
            persona.setCorreo(jTextField6.getText().trim());
            persona.setDireccion(jTextField7.getText().trim());
            Usuario usuario = new Usuario();
            usuario.setPersona(persona);
            usuario.setNombreUsuario(jTextField10.getText().trim());
            usuario.setCargoLaboral(String.valueOf(jComboBox3.getSelectedItem()));
            usuario.setEstadoLaboral(String.valueOf(jComboBox2.getSelectedItem()));
            usuario.setEstadoAcceso(String.valueOf(jComboBox5.getSelectedItem()));
            usuario.setFechaIngreso(fecha(jDateChooser3));
            usuario.setFechaBaja(fecha(jDateChooser2));
            usuario.setNumeroColegiatura(chkOdontologo.isSelected() ? txtColegiatura.getText().trim() : null);
            ArrayList<Rol> rolesDisponibles = new Controladora().traerRoles();
            ArrayList<Rol> seleccionados = new ArrayList<>();
            agregarRol(seleccionados, rolesDisponibles, "ADMINISTRADOR", jCheckBox1.isSelected());
            agregarRol(seleccionados, rolesDisponibles, "ODONTOLOGO", chkOdontologo.isSelected());
            agregarRol(seleccionados, rolesDisponibles, "RECEPCIONISTA", jCheckBox3.isSelected());
            usuario.setRoles(seleccionados);
            new Controladora().crearUsuario(usuario, new String(jPasswordField1.getPassword()));
            JOptionPane.showMessageDialog(this, "La cuenta se creó correctamente.", "Usuario creado",
                    JOptionPane.INFORMATION_MESSAGE);
            volver();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo crear la cuenta",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private LocalDate fecha(com.toedter.calendar.JDateChooser selector) {
        return selector.getDate() == null ? null
                : selector.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void agregarRol(ArrayList<Rol> destino, ArrayList<Rol> todos, String nombre, boolean seleccionado) {
        if (!seleccionado) {
            return;
        }
        todos.stream().filter(rol -> nombre.equalsIgnoreCase(rol.getNombre())).findFirst()
                .ifPresent(destino::add);
        if (destino.stream().noneMatch(rol -> nombre.equalsIgnoreCase(rol.getNombre()))) {
            throw new IllegalArgumentException("El rol " + nombre + " no está configurado.");
        }
    }

    private void volver() {
        dash.mostrarPanel(new Ad_Usuarios(dash));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PanelP = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jDateChooser2 = new com.toedter.calendar.JDateChooser();
        jLabel14 = new javax.swing.JLabel();
        jDateChooser3 = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel16 = new javax.swing.JLabel();
        txtColegiatura = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox<>();
        jPasswordField1 = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jCheckBox1 = new javax.swing.JCheckBox();
        chkOdontologo = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        lblTitulo2 = new javax.swing.JLabel();
        lblTitulo3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(860, 680));

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));

        PanelP.setBackground(new java.awt.Color(255, 255, 255));
        PanelP.setPreferredSize(new java.awt.Dimension(854, 540));
        PanelP.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel1.setText("Tipo de Documento");
        PanelP.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 174, 125, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PanelP.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 196, 180, 32));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setText("Número de Documento");
        PanelP.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 174, 145, -1));
        PanelP.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 196, 180, 32));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Nombres");
        PanelP.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 246, -1, -1));
        PanelP.add(jTextField2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 262, 180, 32));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Apellido Paterno");
        PanelP.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 246, -1, -1));

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        PanelP.add(jTextField3, new org.netbeans.lib.awtextra.AbsoluteConstraints(233, 262, 180, 32));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setText("Apellido Materno");
        PanelP.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 235, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel7.setText("Fecha Nac.");
        PanelP.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, 80, -1));
        PanelP.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 342, 180, 32));
        PanelP.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 180, 32));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("Teléfono");
        PanelP.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 386, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setText("Correo");
        PanelP.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 390, -1, -1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel10.setText("Dirección");
        PanelP.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 510, -1, -1));
        PanelP.add(jTextField5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 408, 180, 32));
        PanelP.add(jTextField6, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 410, 180, 32));
        PanelP.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 530, 391, 32));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/person_search_28dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jLabel11.setText("Buscar persona");
        PanelP.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 10, -1, -1));

        jTextField8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField8ActionPerformed(evt);
            }
        });
        PanelP.add(jTextField8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, 433, 32));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Cargo laboral");
        PanelP.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(641, 174, -1, -1));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setText("Fecha de ingreso");
        PanelP.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 370, -1, -1));
        PanelP.add(jDateChooser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 450, 180, 32));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Fecha de baja");
        PanelP.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 430, -1, -1));
        PanelP.add(jDateChooser3, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 390, 180, 32));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setText("Estado laboral");
        PanelP.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 430, -1, -1));

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PanelP.add(jComboBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(641, 196, 180, 32));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setText("N° de colegiatura(Odontólogo)");
        PanelP.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 450, -1, -1));
        PanelP.add(txtColegiatura, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 470, 180, 34));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setText("Estado de acceso");
        PanelP.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 370, -1, -1));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setText("Nombre de usuario");
        PanelP.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 170, -1, -1));
        PanelP.add(jTextField10, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 190, 180, 32));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setText("Permisos de acceso");
        PanelP.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(641, 246, -1, -1));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PanelP.add(jComboBox5, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 390, 180, 32));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setText("Contraseña inicial");
        PanelP.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 240, -1, -1));

        jButton5.setBackground(new java.awt.Color(0, 143, 168));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));
        jButton5.setText("Buscar");
        PanelP.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 40, -1, 36));

        jButton6.setBackground(new java.awt.Color(0, 143, 168));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Nuevo");
        PanelP.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 40, -1, 36));

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(29, 41, 57));
        jButton4.setText("Cancelar");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        PanelP.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(488, 564, 90, 35));

        jButton2.setBackground(new java.awt.Color(0, 143, 168));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/add_24dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton2.setText("Crear cuenta");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        PanelP.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(608, 564, 144, 35));

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        PanelP.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 450, 177, 32));
        PanelP.add(jPasswordField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 260, 180, 38));
        PanelP.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1, 159, 420, -1));
        PanelP.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 596, 420, -1));

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelP.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(419, 159, -1, 440));

        jCheckBox1.setText("Administrador");
        PanelP.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 270, -1, -1));

        chkOdontologo.setText("Odontólogo");
        chkOdontologo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkOdontologoActionPerformed(evt);
            }
        });
        PanelP.add(chkOdontologo, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 300, -1, -1));

        jCheckBox3.setText("Recepcionista");
        PanelP.add(jCheckBox3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 330, -1, -1));
        PanelP.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 150, 370, 10));

        jSeparator5.setOrientation(javax.swing.SwingConstants.VERTICAL);
        PanelP.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 150, -1, 360));
        PanelP.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 510, 370, -1));

        lblTitulo2.setFont(new java.awt.Font("Perpetua", 1, 24)); // NOI18N
        lblTitulo2.setForeground(new java.awt.Color(18, 18, 18));
        lblTitulo2.setText("Datos personales");
        PanelP.add(lblTitulo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, 180, 40));

        lblTitulo3.setFont(new java.awt.Font("Perpetua", 1, 24)); // NOI18N
        lblTitulo3.setForeground(new java.awt.Color(18, 18, 18));
        lblTitulo3.setText("Usuario");
        PanelP.add(lblTitulo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, 90, 40));

        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(18, 18, 18));
        jLabel2.setText("Crear usuario");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(PanelP, javax.swing.GroupLayout.DEFAULT_SIZE, 821, Short.MAX_VALUE)
                        .addGap(84, 84, 84))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(745, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelP, javax.swing.GroupLayout.PREFERRED_SIZE, 650, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(75, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        volver();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        crearUsuario();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField8ActionPerformed
        buscarPersona();
    }//GEN-LAST:event_jTextField8ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void chkOdontologoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkOdontologoActionPerformed
        txtColegiatura.setEnabled(chkOdontologo.isSelected());
        if (!chkOdontologo.isSelected()) {
            txtColegiatura.setText("");
        }
    }//GEN-LAST:event_chkOdontologoActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelP;
    private javax.swing.JCheckBox chkOdontologo;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox5;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private com.toedter.calendar.JDateChooser jDateChooser2;
    private com.toedter.calendar.JDateChooser jDateChooser3;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JLabel lblTitulo2;
    private javax.swing.JLabel lblTitulo3;
    private javax.swing.JTextField txtColegiatura;
    // End of variables declaration//GEN-END:variables
}
