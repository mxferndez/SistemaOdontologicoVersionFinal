package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Cita;
import Sistemaodontologico.Lógica.CitaControladora;
import Sistemaodontologico.Lógica.ActividadControladora;
import Sistemaodontologico.Lógica.Controladora;
import Sistemaodontologico.Lógica.PagoControladora;
import Sistemaodontologico.Lógica.PacienteControladora;
import Sistemaodontologico.Lógica.ConfiguracionControladora;
import Sistemaodontologico.Lógica.Sesion;
import Sistemaodontologico.Lógica.Usuario;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Ad_principal extends javax.swing.JPanel {

    public Ad_principal() {
        initComponents();

        // diseño de los paneles principales de la información 
        PanelCitas.putClientProperty("FlatLaf.style",
                "arc: 30; background: #FFFFFF; border: 1,1,1,1,#E5E7EB,1,20");
        PanelCA.putClientProperty("FlatLaf.style",
                "arc: 30; background: #FFFFFF; border: 1,1,1,1,#E5E7EB,1,20");
        Panel_Ingresos.putClientProperty("FlatLaf.style",
                "arc: 30; background: #FFFFFF; border: 1,1,1,1,#E5E7EB,1,20");
        Panel_PTurno.putClientProperty("FlatLaf.style",
                "arc: 30; background: #FFFFFF; border: 1,1,1,1,#E5E7EB,1,20");
        Usuario usuarioActivo = Sesion.getInstancia().getUsuario();
        jTextField1.setText(usuarioActivo == null ? "" : usuarioActivo.getNombreCompleto());
        cargarCitasDelDia();
        cargarResumenDiario();

    }

    private void cargarCitasDelDia() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Hora", "Paciente", "Odontólogo", "Consultorio", "Tipo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tableCitas.setModel(modelo);
        try {
            ArrayList<Cita> citas = new CitaControladora().listarDia(LocalDate.now(), 0, 0);
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            int pendientes = 0;
            int enEspera = 0;
            for (Cita cita : citas) {
                modelo.addRow(new Object[]{cita.getIdCita(), cita.getInicio().format(formatoHora), cita.getPaciente(),
                    cita.getOdontologo(), cita.getConsultorio(), cita.getTipo(), cita.getEstado()});
                if ("PROGRAMADA".equals(cita.getEstado()) || "CONFIRMADA".equals(cita.getEstado())) {
                    pendientes++;
                }
                if ("EN_ESPERA".equals(cita.getEstado())) {
                    enEspera++;
                }
            }
            tableCitas.getColumnModel().getColumn(0).setMinWidth(0);
            tableCitas.getColumnModel().getColumn(0).setMaxWidth(0);
            tableCitas.getColumnModel().getColumn(0).setWidth(0);
            InfoNumeroCitas.setText(String.valueOf(citas.size()));
            InfocitasConfirmadas.setText(pendientes + " programadas/confirmadas");
            InfoCitasEspera.setText(enEspera + " en espera");
            DefaultTableModel modeloActividad = new DefaultTableModel(new Object[]{"Hora", "Evento", "Descripción", "Usuario", "Paciente"}, 0) {
                @Override
                public boolean isCellEditable(int fila, int columna) {
                    return false;
                }
            };
            tableActividad.setModel(modeloActividad);
            DateTimeFormatter horaActividad = DateTimeFormatter.ofPattern("HH:mm");
            for (Object[] actividad : new ActividadControladora().listarHoy()) {
                java.sql.Timestamp marca = (java.sql.Timestamp) actividad[1];
                modeloActividad.addRow(new Object[]{marca.toLocalDateTime().format(horaActividad), actividad[2],
                    actividad[3], actividad[4], actividad[5]});
            }
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudieron cargar las citas de hoy", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarResumenDiario() {
        try {
            jLabel5.setText(String.valueOf(new PacienteControladora().listarPacientes("", "ACTIVO").size()));
            jLabel6.setText("pacientes activos en la base");
            java.math.BigDecimal totalPagos = java.math.BigDecimal.ZERO;
            int cantidadPagos = 0;
            for (Object[] pago : new PagoControladora().listarDelDia(LocalDate.now())) {
                if ("REGISTRADO".equals(pago[6])) {
                    totalPagos = totalPagos.add((java.math.BigDecimal) pago[4]);
                    cantidadPagos++;
                }
            }
            String moneda = String.valueOf(new ConfiguracionControladora().buscar()[0]);
            jLabel9.setText(moneda + " " + totalPagos.toPlainString());
            jLabel10.setText(cantidadPagos + " pagos registrados hoy");
            int personalActivo = 0;
            int odontologos = 0;
            int recepcionistas = 0;
            for (Usuario usuario : new Controladora().traerUsuarios()) {
                if (!"ACTIVO".equals(usuario.getEstadoLaboral()) || !"HABILITADO".equals(usuario.getEstadoAcceso())) {
                    continue;
                }
                personalActivo++;
                if (usuario.tieneRol("ODONTOLOGO")) {
                    odontologos++;
                }
                if (usuario.tieneRol("RECEPCIONISTA")) {
                    recepcionistas++;
                }
            }
            jLabel13.setText(String.valueOf(personalActivo));
            jLabel14.setText(odontologos + " odontólogos");
            jLabel15.setText(recepcionistas + " recepcionistas");
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo cargar el resumen del día", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ScrollCitas = new javax.swing.JScrollPane();
        tableCitas = new javax.swing.JTable();
        ScrollActividad = new javax.swing.JScrollPane();
        tableActividad = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
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
        Panel_PTurno = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(860, 680));

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));

        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(18, 18, 18));
        jLabel2.setText("Bienvenido,  ");

        jLabel1.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(18, 18, 18));
        jLabel1.setText("Citas del día ");

        jLabel3.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(18, 18, 18));
        jLabel3.setText("Actividad de hoy");

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

        tableActividad.setModel(new javax.swing.table.DefaultTableModel(
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
        ScrollActividad.setViewportView(tableActividad);

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(245, 247, 250));
        jTextField1.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jTextField1.setForeground(new java.awt.Color(18, 18, 18));
        jTextField1.setText("jTextField1");
        jTextField1.setBorder(null);

        PanelCitas.setBackground(new java.awt.Color(255, 255, 255));

        tituloCitas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tituloCitas.setText("Citas");

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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(IconCitas)
                        .addGap(19, 19, 19))
                    .addGroup(PanelCitasLayout.createSequentialGroup()
                        .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(InfoCitasEspera)
                            .addComponent(InfocitasConfirmadas)
                            .addComponent(InfoNumeroCitas))
                        .addContainerGap(71, Short.MAX_VALUE))))
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
        jLabel4.setText("Pacientes activos");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("120");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(31, 111, 102));
        jLabel6.setText("18 pacientes nuevos");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/badge_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

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
                .addContainerGap(24, Short.MAX_VALUE))
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
        jLabel8.setText("Pagos del día");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setText("1250");

        jLabel10.setForeground(new java.awt.Color(31, 111, 102));
        jLabel10.setText("7 pagos registrados");

        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/payments_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        javax.swing.GroupLayout Panel_IngresosLayout = new javax.swing.GroupLayout(Panel_Ingresos);
        Panel_Ingresos.setLayout(Panel_IngresosLayout);
        Panel_IngresosLayout.setHorizontalGroup(
            Panel_IngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_IngresosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_IngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_IngresosLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11))
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        Panel_IngresosLayout.setVerticalGroup(
            Panel_IngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_IngresosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_IngresosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(jLabel10)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Panel_PTurno.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Personal en turno ");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("4");

        jLabel14.setForeground(new java.awt.Color(31, 111, 102));
        jLabel14.setText("3 Odontólogos");

        jLabel15.setForeground(new java.awt.Color(31, 111, 102));
        jLabel15.setText("1 Recepcionista");

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/person_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        javax.swing.GroupLayout Panel_PTurnoLayout = new javax.swing.GroupLayout(Panel_PTurno);
        Panel_PTurno.setLayout(Panel_PTurnoLayout);
        Panel_PTurnoLayout.setHorizontalGroup(
            Panel_PTurnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_PTurnoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_PTurnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_PTurnoLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16))
                    .addComponent(jLabel14)
                    .addComponent(jLabel15)
                    .addComponent(jLabel13))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        Panel_PTurnoLayout.setVerticalGroup(
            Panel_PTurnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_PTurnoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_PTurnoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel16))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                            .addComponent(PanelCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(PanelCA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)
                                .addComponent(Panel_Ingresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(ScrollCitas, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(ScrollActividad, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(Panel_PTurno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(0, 155, Short.MAX_VALUE)))
                .addGap(15, 15, 15))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelCitas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel_Ingresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel_PTurno, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PanelCA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ScrollCitas, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ScrollActividad))
                .addGap(25, 25, 25))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IconCitas;
    private javax.swing.JLabel InfoCitasEspera;
    private javax.swing.JLabel InfoNumeroCitas;
    private javax.swing.JLabel InfocitasConfirmadas;
    private javax.swing.JPanel PanelCA;
    private javax.swing.JPanel PanelCitas;
    private javax.swing.JPanel Panel_Ingresos;
    private javax.swing.JPanel Panel_PTurno;
    private javax.swing.JScrollPane ScrollActividad;
    private javax.swing.JScrollPane ScrollCitas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTable tableActividad;
    private javax.swing.JTable tableCitas;
    private javax.swing.JLabel tituloCitas;
    // End of variables declaration//GEN-END:variables
}
