package Sistemaodontologico.IGU;

import Sistemaodontologico.Lógica.Cita;
import Sistemaodontologico.Lógica.CitaControladora;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class REC_Principal extends javax.swing.JPanel {

    RECVentanas dashboardV;

    public REC_Principal(RECVentanas dashboard) {
        initComponents();
        this.dashboardV = dashboard;

        // diseño de los paneles principales de la información 
        PanelCitas.putClientProperty("FlatLaf.style",
                "arc: 30; background: #FFFFFF; border: 1,1,1,1,#E5E7EB,1,20");
        PanelCA.putClientProperty("FlatLaf.style",
                "arc: 30; background: #FFFFFF; border: 1,1,1,1,#E5E7EB,1,20");
        Panel_Ingresos.putClientProperty("FlatLaf.style",
                "arc: 30; background: #FFFFFF; border: 1,1,1,1,#E5E7EB,1,20");
        configurarAgendaDiaria();
    }

    private void configurarAgendaDiaria() {
        jComboBox1.removeAllItems();
        jComboBox1.addItem("Todos los odontólogos");
        try {
            for (String opcion : new CitaControladora().odontologosActivos()) {
                jComboBox1.addItem(opcion);
            }
        } catch (PersistenciaException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudieron cargar odontólogos", JOptionPane.ERROR_MESSAGE);
        }
        jComboBox1.addActionListener(e -> cargarCitasDelDia());
        jButton2.addActionListener(e -> marcarLlegada());
        cargarCitasDelDia();
    }

    private long odontologoFiltro() {
        Object seleccion = jComboBox1.getSelectedItem();
        if (seleccion == null || "Todos los odontólogos".equals(seleccion.toString())) {
            return 0;
        }
        String texto = seleccion.toString();
        return Long.parseLong(texto.substring(0, texto.indexOf(" - ")));
    }

    private void cargarCitasDelDia() {
        DefaultTableModel modeloCitas = modeloAgenda();
        DefaultTableModel modeloCola = new DefaultTableModel(new Object[]{"ID", "Llegó", "Paciente", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
        tableCitas.setModel(modeloCitas);
        tableActividad.setModel(modeloCola);
        try {
            ArrayList<Cita> citas = new CitaControladora().listarDia(LocalDate.now(), odontologoFiltro(), 0);
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");
            ArrayList<Cita> cola = new ArrayList<>();
            int enEspera = 0;
            int confirmadas = 0;
            for (Cita cita : citas) {
                Object[] fila = {cita.getIdCita(), cita.getInicio().format(formatoHora), cita.getPaciente(),
                    cita.getOdontologo(), cita.getConsultorio(), cita.getTipo(), cita.getEstado()};
                modeloCitas.addRow(fila);
                if ("EN_ESPERA".equals(cita.getEstado())) {
                    cola.add(cita);
                    enEspera++;
                }
                if ("CONFIRMADA".equals(cita.getEstado()) || "PROGRAMADA".equals(cita.getEstado())) {
                    confirmadas++;
                }
            }
            cola.sort(Comparator.comparing(Cita::getHoraLlegada, Comparator.nullsLast(Comparator.naturalOrder())));
            for (Cita cita : cola) {
                modeloCola.addRow(new Object[]{cita.getIdCita(),
                    cita.getHoraLlegada() == null ? "" : cita.getHoraLlegada().format(formatoHora), cita.getPaciente(), cita.getEstado()});
            }
            ocultarId(tableCitas);
            ocultarId(tableActividad);
            InfoNumeroCitas.setText(String.valueOf(citas.size()));
            InfocitasConfirmadas.setText(confirmadas + " pendientes/confirmadas");
            InfoCitasEspera.setText(enEspera + " en cola");
            jLabel5.setText(String.valueOf(enEspera));
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudieron cargar las citas de hoy", JOptionPane.ERROR_MESSAGE);
        }
    }

    private DefaultTableModel modeloAgenda() {
        return new DefaultTableModel(new Object[]{"ID", "Hora", "Paciente", "Odontólogo", "Consultorio", "Tipo", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    private void ocultarId(javax.swing.JTable tabla) {
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setWidth(0);
    }

    private void marcarLlegada() {
        try {
            int fila = tableCitas.getSelectedRow();
            if (fila < 0) {
                throw new IllegalArgumentException("Seleccione primero una cita.");
            }
            int modelo = tableCitas.convertRowIndexToModel(fila);
            long idCita = ((Number) tableCitas.getModel().getValueAt(modelo, 0)).longValue();
            new CitaControladora().cambiarEstado(idCita, "EN_ESPERA", null);
            cargarCitasDelDia();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo marcar la llegada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Cita seleccionarCita() throws PersistenciaException {
        int fila = tableCitas.getSelectedRow();
        if (fila < 0) {
            throw new IllegalArgumentException("Seleccione primero una cita de hoy.");
        }
        int modelo = tableCitas.convertRowIndexToModel(fila);
        long idCita = ((Number) tableCitas.getModel().getValueAt(modelo, 0)).longValue();
        Cita cita = new CitaControladora().buscar(idCita);
        if (cita == null) {
            throw new IllegalArgumentException("La cita seleccionada ya no existe.");
        }
        return cita;
    }

    private void cancelarCita() {
        try {
            Cita cita = seleccionarCita();
            String motivo = JOptionPane.showInputDialog(this, "Motivo de cancelación:", "Cancelar cita", JOptionPane.QUESTION_MESSAGE);
            if (motivo == null) {
                return;
            }
            if (motivo.isBlank()) {
                throw new IllegalArgumentException("Escriba el motivo de cancelación.");
            }
            new CitaControladora().cambiarEstado(cita.getIdCita(), "CANCELADA", motivo.trim());
            cargarCitasDelDia();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo cancelar la cita", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarInasistencia() {
        try {
            Cita cita = seleccionarCita();
            String observacion = JOptionPane.showInputDialog(this, "Observación de inasistencia:", "Registrar inasistencia", JOptionPane.QUESTION_MESSAGE);
            if (observacion == null) {
                return;
            }
            new CitaControladora().cambiarEstado(cita.getIdCita(), "AUSENTE", observacion.trim());
            cargarCitasDelDia();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo registrar la inasistencia", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void reprogramarCita() {
        try {
            Cita anterior = seleccionarCita();
            String fechaHora = JOptionPane.showInputDialog(this,
                    "Nueva fecha y hora (AAAA-MM-DDTHH:MM):", "Reprogramar cita",
                    JOptionPane.QUESTION_MESSAGE);
            if (fechaHora == null) {
                return;
            }
            LocalDateTime inicio = LocalDateTime.parse(fechaHora.trim());
            ArrayList<String> consultorios = new CitaControladora().consultoriosActivos();
            if (consultorios.isEmpty()) {
                throw new IllegalArgumentException("No hay consultorios activos.");
            }
            Object consultorio = JOptionPane.showInputDialog(this, "Seleccione el consultorio", "Reprogramar cita",
                    JOptionPane.QUESTION_MESSAGE, null, consultorios.toArray(), consultorios.get(0));
            if (consultorio == null) {
                return;
            }
            String opcion = consultorio.toString();
            long idConsultorio = Long.parseLong(opcion.substring(0, opcion.indexOf(" - ")));
            String motivo = JOptionPane.showInputDialog(this, "Motivo de la reprogramación:", "Reprogramar cita", JOptionPane.QUESTION_MESSAGE);
            if (motivo == null) {
                return;
            }
            Cita nueva = new Cita();
            nueva.setIdPaciente(anterior.getIdPaciente());
            nueva.setIdOdontologo(anterior.getIdOdontologo());
            nueva.setIdConsultorio(idConsultorio);
            nueva.setInicio(inicio);
            nueva.setDuracionMinutos(anterior.getDuracionMinutos());
            nueva.setCanal(anterior.getCanal());
            nueva.setTipo(anterior.getTipo());
            new CitaControladora().reprogramar(anterior.getIdCita(), nueva, motivo.trim());
            JOptionPane.showMessageDialog(this, "La cita fue reprogramada.", "Agenda", JOptionPane.INFORMATION_MESSAGE);
            cargarCitasDelDia();
        } catch (PersistenciaException | RuntimeException error) {
            JOptionPane.showMessageDialog(this, error.getMessage(), "No se pudo reprogramar la cita", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        ScrollCitas = new javax.swing.JScrollPane();
        tableCitas = new javax.swing.JTable();
        ScrollActividad = new javax.swing.JScrollPane();
        tableActividad = new javax.swing.JTable();
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
        jButton1 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));

        jLabel2.setText("Panel de recepción");
        jLabel2.setFont(new java.awt.Font("Perpetua", 1, 28)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(18, 18, 18));

        jLabel3.setText("Cola de hoy");
        jLabel3.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(18, 18, 18));

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

        PanelCitas.setBackground(new java.awt.Color(255, 255, 255));

        tituloCitas.setText("Citas de hoy");
        tituloCitas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        InfoNumeroCitas.setText("8");
        InfoNumeroCitas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        InfocitasConfirmadas.setText("6 confirmadas");
        InfocitasConfirmadas.setForeground(new java.awt.Color(31, 111, 102));

        IconCitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/event_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        InfoCitasEspera.setText("2 Espera");
        InfoCitasEspera.setForeground(new java.awt.Color(102, 204, 255));

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

        jLabel4.setText("En sala de espera");
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel5.setText("120");
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel6.setText("18 pacientes nuevos");
        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(31, 111, 102));

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
                .addContainerGap(22, Short.MAX_VALUE))
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

        jLabel8.setText("Cupos disponibles");
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N

        jLabel9.setText("1250");
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel10.setText("7 pagos registrados");
        jLabel10.setForeground(new java.awt.Color(31, 111, 102));

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
                        .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
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

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/add_24dp_FFFFFF_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        jButton1.setText("Nueva cita");
        jButton1.setBackground(new java.awt.Color(0, 143, 168));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel12.setText("Citas del día ");
        jLabel12.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(18, 18, 18));

        jButton2.setText("Marcar llegada");
        jButton2.setBackground(new java.awt.Color(0, 143, 168));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));

        jButton3.setText("Reprogramar");
        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Cancelar");
        jButton4.setBackground(new java.awt.Color(255, 240, 235));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton4.setForeground(new java.awt.Color(255, 51, 102));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Inasistencia");
        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

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
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel12)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(PanelCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(PanelCA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)
                                        .addComponent(Panel_Ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(23, 23, 23))))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5))
                            .addComponent(ScrollCitas, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 583, Short.MAX_VALUE))))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ScrollActividad, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton1))))
                .addGap(15, 15, 15))
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
                            .addComponent(PanelCitas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Panel_Ingresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(PanelCA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(45, 45, 45))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ScrollCitas, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addComponent(ScrollActividad, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE))
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(23, Short.MAX_VALUE))
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
        reprogramarCita();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dashboardV.mostrarPanel(new __NuevaCita(dashboardV));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        cancelarCita();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        registrarInasistencia();
    }//GEN-LAST:event_jButton5ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel IconCitas;
    private javax.swing.JLabel InfoCitasEspera;
    private javax.swing.JLabel InfoNumeroCitas;
    private javax.swing.JLabel InfocitasConfirmadas;
    private javax.swing.JPanel PanelCA;
    private javax.swing.JPanel PanelCitas;
    private javax.swing.JPanel Panel_Ingresos;
    private javax.swing.JScrollPane ScrollActividad;
    private javax.swing.JScrollPane ScrollCitas;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTable tableActividad;
    private javax.swing.JTable tableCitas;
    private javax.swing.JLabel tituloCitas;
    // End of variables declaration//GEN-END:variables
}
