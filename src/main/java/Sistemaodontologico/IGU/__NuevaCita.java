package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Cita;
import Sistemaodontologico.Lógica.CitaControladora;
import Sistemaodontologico.Lógica.Paciente;
import Sistemaodontologico.Lógica.PacienteControladora;
import Sistemaodontologico.Lógica.Persona;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class __NuevaCita extends javax.swing.JPanel {

    RECVentanas dashV;
    private long pacienteSeleccionado;
    private long consultorioSeleccionado;

    public __NuevaCita(RECVentanas dashboard) {
        initComponents();
        this.dashV = dashboard;
        configurarFormulario();
    }

    private void volver() {
        dashV.mostrarPanel(new REC_Principal(dashV));
    }

    private void configurarFormulario() {
        jTextField10.setEditable(false);
        jTextField8.setEditable(false);
        jTextField9.setEditable(false);
        jTextField11.setEditable(false);
        jComboBox3.removeAllItems();
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Consulta", "Control", "Tratamiento", "Emergencia"}));
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Ventanilla", "Teléfono", "WhatsApp"}));
        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(30, 5, 240, 5));
        try {
            CitaControladora control = new CitaControladora();
            for (String odontologo : control.odontologosActivos()) {
                jComboBox3.addItem(odontologo);
            }
            ArrayList<String> consultorios = control.consultoriosActivos();
            if (consultorios.isEmpty()) {
                throw new IllegalArgumentException("No hay consultorios activos.");
            }
            Object elegido = JOptionPane.showInputDialog(this, "Seleccione el consultorio", "Consultorio",
                    JOptionPane.QUESTION_MESSAGE, null, consultorios.toArray(), consultorios.get(0));
            if (elegido == null) {
                volver();
                return;
            }
            String opcion = elegido.toString();
            consultorioSeleccionado = Long.parseLong(opcion.substring(0, opcion.indexOf(" - ")));
            jTextField11.setText(opcion.substring(opcion.indexOf(" - ") + 3));
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo cargar la agenda", JOptionPane.ERROR_MESSAGE);
        }
        jButton5.addActionListener(e -> buscarPaciente());
        jButton6.setText("Limpiar");
        jButton6.addActionListener(e -> limpiarPaciente());
    }

    private void limpiarPaciente() {
        pacienteSeleccionado = 0;
        jTextField14.setText("");
        jTextField7.setText("");
        jTextField10.setText("");
        jTextField8.setText("");
        jTextField9.setText("");
    }

    private void buscarPaciente() {
        try {
            String documento = jTextField14.getText().trim();
            if (documento.isEmpty()) {
                throw new IllegalArgumentException("Escriba el documento del paciente.");
            }
            PacienteControladora pacientes = new PacienteControladora();
            Persona persona = null;
            for (String tipo : new String[]{"DNI", "CE", "PASAPORTE", "OTRO"}) {
                persona = pacientes.buscarPersona(tipo, documento);
                if (persona != null) {
                    break;
                }
            }
            if (persona == null) {
                throw new IllegalArgumentException("No existe una persona con ese documento. Regístrela primero con Nuevo.");
            }
            Paciente paciente = pacientes.buscarPaciente(persona.getIdPersona());
            if (paciente == null || !"ACTIVO".equals(paciente.getEstado())) {
                throw new IllegalArgumentException("La persona no está registrada como paciente activo.");
            }
            pacienteSeleccionado = persona.getIdPersona();
            jTextField7.setText(persona.getNumeroDocumento());
            jTextField10.setText(persona.getNombres());
            jTextField8.setText(persona.getApellidoPaterno());
            jTextField9.setText(persona.getApellidoMaterno());
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo buscar el paciente", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarCita() {
        try {
            if (pacienteSeleccionado <= 0) {
                throw new IllegalArgumentException("Busque y seleccione un paciente.");
            }
            if (consultorioSeleccionado <= 0) {
                throw new IllegalArgumentException("Seleccione un consultorio.");
            }
            if (jDateChooser1.getDate() == null) {
                throw new IllegalArgumentException("Seleccione la fecha.");
            }
            if (timePicker1.getTime() == null) {
                throw new IllegalArgumentException("Seleccione la hora.");
            }
            String odontologo = (String) jComboBox3.getSelectedItem();
            if (odontologo == null) {
                throw new IllegalArgumentException("No hay odontólogos activos.");
            }
            Cita cita = new Cita();
            cita.setIdPaciente(pacienteSeleccionado);
            cita.setIdOdontologo(Long.parseLong(odontologo.substring(0, odontologo.indexOf(" - "))));
            cita.setIdConsultorio(consultorioSeleccionado);
            LocalDate fecha = jDateChooser1.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalTime hora = timePicker1.getTime();
            cita.setInicio(fecha.atTime(hora));
            cita.setDuracionMinutos((Integer) jSpinner1.getValue());
            cita.setCanal(canalCodigo(String.valueOf(jComboBox5.getSelectedItem())));
            cita.setTipo(tipoCodigo(String.valueOf(jComboBox4.getSelectedItem())));
            new CitaControladora().crear(cita);
            JOptionPane.showMessageDialog(this, "La cita fue registrada correctamente.", "Cita registrada", JOptionPane.INFORMATION_MESSAGE);
            volver();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo registrar la cita", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String canalCodigo(String etiqueta) {
        switch (etiqueta) {
            case "Teléfono":
                return "TELEFONO";
            case "WhatsApp":
                return "WHATSAPP";
            default:
                return "VENTANILLA";
        }
    }

    private String tipoCodigo(String etiqueta) {
        switch (etiqueta) {
            case "Control":
                return "CONTROL";
            case "Tratamiento":
                return "TRATAMIENTO";
            case "Emergencia":
                return "EMERGENCIA";
            default:
                return "CONSULTA";
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        PanelP = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox<>();
        jComboBox5 = new javax.swing.JComboBox<>();
        timePicker1 = new com.github.lgooddatepicker.components.TimePicker();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel28 = new javax.swing.JLabel();

        jPanel5.setBackground(new java.awt.Color(245, 247, 250));

        PanelP.setBackground(new java.awt.Color(255, 255, 255));
        PanelP.setPreferredSize(new java.awt.Dimension(854, 540));

        jLabel9.setText("Paciente");
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel10.setText("Número de Documento");
        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel11.setText("Odontólogo");
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel12.setText("Apellido Paterno");
        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });

        jLabel13.setText("Apellido Materno");
        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel14.setText("Fecha ");
        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel15.setText("Consultorio");
        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel16.setText("Canal");
        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel17.setText("Tipo");
        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jTextField11.setEditable(false);
        jTextField11.setBackground(new java.awt.Color(255, 255, 255));

        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/person_search_28dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });

        jButton5.setText("Buscar");
        jButton5.setBackground(new java.awt.Color(0, 143, 168));
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.setForeground(new java.awt.Color(255, 255, 255));

        jButton6.setText("Nuevo");
        jButton6.setBackground(new java.awt.Color(0, 143, 168));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));

        jButton4.setText("Cancelar");
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(29, 41, 57));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton2.setText("Registrar cita");
        jButton2.setBackground(new java.awt.Color(0, 143, 168));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel19.setText("Duración estimada min.");
        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel20.setText("Hora programada");
        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout PanelPLayout = new javax.swing.GroupLayout(PanelP);
        PanelP.setLayout(PanelPLayout);
        PanelPLayout.setHorizontalGroup(
            PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPLayout.createSequentialGroup()
                        .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                        .addComponent(jButton5)
                        .addGap(18, 18, 18)
                        .addComponent(jButton6)
                        .addContainerGap(47, Short.MAX_VALUE))
                    .addGroup(PanelPLayout.createSequentialGroup()
                        .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(PanelPLayout.createSequentialGroup()
                                    .addComponent(jLabel11)
                                    .addGap(594, 594, 594))
                                .addGroup(PanelPLayout.createSequentialGroup()
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(480, 480, 480))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, PanelPLayout.createSequentialGroup()
                                    .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTextField11, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(480, 480, 480)))
                            .addGroup(PanelPLayout.createSequentialGroup()
                                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel19)
                                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel12)
                                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(PanelPLayout.createSequentialGroup()
                                            .addComponent(jSpinner1)
                                            .addGap(172, 172, 172)
                                            .addComponent(jButton2)
                                            .addGap(29, 29, 29)
                                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(PanelPLayout.createSequentialGroup()
                                            .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(209, 209, 209)
                                            .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(jLabel16)
                                                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                                    .addComponent(jDateChooser1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addComponent(jLabel20)
                                                .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(timePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        PanelPLayout.setVerticalGroup(
            PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelPLayout.createSequentialGroup()
                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(jLabel18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(46, 46, 46)
                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addGap(6, 6, 6)
                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelPLayout.createSequentialGroup()
                        .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel11)))
                    .addGroup(PanelPLayout.createSequentialGroup()
                        .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jDateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabel16))
                .addGap(7, 7, 7)
                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PanelPLayout.createSequentialGroup()
                        .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(PanelPLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(timePicker1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(PanelPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSpinner1))
                .addContainerGap())
        );

        jLabel28.setText("Registrar cita");
        jLabel28.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(18, 18, 18));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(PanelP, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
                        .addGap(84, 84, 84))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addContainerGap(722, Short.MAX_VALUE))))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PanelP, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
                .addGap(111, 111, 111))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        volver();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        registrarCita();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel PanelP;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JComboBox<String> jComboBox4;
    private javax.swing.JComboBox<String> jComboBox5;
    private com.toedter.calendar.JDateChooser jDateChooser1;
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
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private com.github.lgooddatepicker.components.TimePicker timePicker1;
    // End of variables declaration//GEN-END:variables
}
