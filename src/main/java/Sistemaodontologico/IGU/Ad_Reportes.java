
package Sistemaodontologico.IGU;


public class Ad_Reportes extends javax.swing.JPanel {

    /**
     * Creates new form Av_Reportes
     */
    public Ad_Reportes() {
        initComponents();
        PanelAtenciones.putClientProperty("FlatLaf.style", "arc: 30");
        PaneL_Inasistencias.putClientProperty("FlatLaf.style", "arc: 30");
        Panel_Ingresos.putClientProperty("FlatLaf.style", "arc: 30");
        Panel_PlanesCurso.putClientProperty("FlatLaf.style", "arc: 30");
        PanelCitas.putClientProperty("FlatLaf.style", "arc: 30");
        Grafica1.putClientProperty("FlatLaf.style", "arc: 30");
        Grafica2.putClientProperty("FlatLaf.style", "arc: 30");
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        PanelAtenciones = new javax.swing.JPanel();
        tituloCitas = new javax.swing.JLabel();
        InfoNumeroCitas = new javax.swing.JLabel();
        InfocitasConfirmadas = new javax.swing.JLabel();
        IconCitas = new javax.swing.JLabel();
        InfoCitasEspera = new javax.swing.JLabel();
        PaneL_Inasistencias = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        Panel_Ingresos = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        Panel_PlanesCurso = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        PanelCitas = new javax.swing.JPanel();
        tituloCitas1 = new javax.swing.JLabel();
        InfoNumeroCitas1 = new javax.swing.JLabel();
        InfocitasConfirmadas1 = new javax.swing.JLabel();
        IconCitas1 = new javax.swing.JLabel();
        InfoCitasEspera1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Grafica1 = new javax.swing.JPanel();
        Grafica2 = new javax.swing.JPanel();

        setPreferredSize(new java.awt.Dimension(860, 680));

        jPanel1.setBackground(new java.awt.Color(245, 247, 250));

        PanelAtenciones.setBackground(new java.awt.Color(255, 255, 255));

        tituloCitas.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tituloCitas.setText("Citas atendidas");

        InfoNumeroCitas.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        InfoNumeroCitas.setText("8");

        InfocitasConfirmadas.setForeground(new java.awt.Color(31, 111, 102));
        InfocitasConfirmadas.setText("6 confirmadas");

        IconCitas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/health_and_safety_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        InfoCitasEspera.setForeground(new java.awt.Color(102, 204, 255));
        InfoCitasEspera.setText("2 Espera");

        javax.swing.GroupLayout PanelAtencionesLayout = new javax.swing.GroupLayout(PanelAtenciones);
        PanelAtenciones.setLayout(PanelAtencionesLayout);
        PanelAtencionesLayout.setHorizontalGroup(
            PanelAtencionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAtencionesLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(PanelAtencionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelAtencionesLayout.createSequentialGroup()
                        .addComponent(tituloCitas)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(IconCitas)
                        .addGap(19, 19, 19))
                    .addGroup(PanelAtencionesLayout.createSequentialGroup()
                        .addGroup(PanelAtencionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(InfoCitasEspera)
                            .addComponent(InfocitasConfirmadas)
                            .addComponent(InfoNumeroCitas))
                        .addContainerGap(81, Short.MAX_VALUE))))
        );
        PanelAtencionesLayout.setVerticalGroup(
            PanelAtencionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelAtencionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelAtencionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloCitas)
                    .addComponent(IconCitas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(InfoNumeroCitas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(InfocitasConfirmadas)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(InfoCitasEspera)
                .addGap(12, 12, 12))
        );

        PaneL_Inasistencias.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel4.setText("Pacientes inasistentes");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("120");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setForeground(new java.awt.Color(31, 111, 102));
        jLabel6.setText("18 pacientes nuevos");

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/person_off_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        javax.swing.GroupLayout PaneL_InasistenciasLayout = new javax.swing.GroupLayout(PaneL_Inasistencias);
        PaneL_Inasistencias.setLayout(PaneL_InasistenciasLayout);
        PaneL_InasistenciasLayout.setHorizontalGroup(
            PaneL_InasistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PaneL_InasistenciasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PaneL_InasistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PaneL_InasistenciasLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PaneL_InasistenciasLayout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        PaneL_InasistenciasLayout.setVerticalGroup(
            PaneL_InasistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PaneL_InasistenciasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PaneL_InasistenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PaneL_InasistenciasLayout.createSequentialGroup()
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
        jLabel8.setText("Pagos del mes");

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
                .addContainerGap(57, Short.MAX_VALUE))
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

        Panel_PlanesCurso.setBackground(new java.awt.Color(255, 255, 255));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Planes en curso ");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel13.setText("1250");

        jLabel14.setForeground(new java.awt.Color(31, 111, 102));
        jLabel14.setText("7 pagos registrados");

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/assignment_turned_in_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        javax.swing.GroupLayout Panel_PlanesCursoLayout = new javax.swing.GroupLayout(Panel_PlanesCurso);
        Panel_PlanesCurso.setLayout(Panel_PlanesCursoLayout);
        Panel_PlanesCursoLayout.setHorizontalGroup(
            Panel_PlanesCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_PlanesCursoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_PlanesCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_PlanesCursoLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel15))
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        Panel_PlanesCursoLayout.setVerticalGroup(
            Panel_PlanesCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_PlanesCursoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_PlanesCursoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addGap(18, 18, 18)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PanelCitas.setBackground(new java.awt.Color(255, 255, 255));

        tituloCitas1.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tituloCitas1.setText("Citas");

        InfoNumeroCitas1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        InfoNumeroCitas1.setText("8");

        InfocitasConfirmadas1.setForeground(new java.awt.Color(31, 111, 102));
        InfocitasConfirmadas1.setText("6 confirmadas");

        IconCitas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/point_of_sale_24dp_1F2937_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N

        InfoCitasEspera1.setForeground(new java.awt.Color(102, 204, 255));
        InfoCitasEspera1.setText("2 Espera");

        javax.swing.GroupLayout PanelCitasLayout = new javax.swing.GroupLayout(PanelCitas);
        PanelCitas.setLayout(PanelCitasLayout);
        PanelCitasLayout.setHorizontalGroup(
            PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCitasLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelCitasLayout.createSequentialGroup()
                        .addComponent(InfocitasConfirmadas1)
                        .addGap(0, 97, Short.MAX_VALUE))
                    .addGroup(PanelCitasLayout.createSequentialGroup()
                        .addComponent(tituloCitas1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(IconCitas1)
                        .addGap(19, 19, 19))
                    .addGroup(PanelCitasLayout.createSequentialGroup()
                        .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(InfoCitasEspera1)
                            .addComponent(InfoNumeroCitas1))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        PanelCitasLayout.setVerticalGroup(
            PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelCitasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelCitasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tituloCitas1)
                    .addComponent(IconCitas1))
                .addGap(51, 51, 51)
                .addComponent(InfoNumeroCitas1)
                .addGap(36, 36, 36)
                .addComponent(InfocitasConfirmadas1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(InfoCitasEspera1)
                .addGap(12, 12, 12))
        );

        jLabel1.setFont(new java.awt.Font("Perpetua", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(18, 18, 18));
        jLabel1.setText("Resumen de caja");

        Grafica1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout Grafica1Layout = new javax.swing.GroupLayout(Grafica1);
        Grafica1.setLayout(Grafica1Layout);
        Grafica1Layout.setHorizontalGroup(
            Grafica1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        Grafica1Layout.setVerticalGroup(
            Grafica1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        Grafica2.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout Grafica2Layout = new javax.swing.GroupLayout(Grafica2);
        Grafica2.setLayout(Grafica2Layout);
        Grafica2Layout.setHorizontalGroup(
            Grafica2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        Grafica2Layout.setVerticalGroup(
            Grafica2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(PanelAtenciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PaneL_Inasistencias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Panel_Ingresos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(Panel_PlanesCurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Grafica1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Grafica2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PanelCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addGap(56, 56, 56))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PanelAtenciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(PaneL_Inasistencias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel_Ingresos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel_PlanesCurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(120, 120, 120)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PanelCitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(152, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(Grafica1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Grafica2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(12, 12, 12))))
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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Grafica1;
    private javax.swing.JPanel Grafica2;
    private javax.swing.JLabel IconCitas;
    private javax.swing.JLabel IconCitas1;
    private javax.swing.JLabel InfoCitasEspera;
    private javax.swing.JLabel InfoCitasEspera1;
    private javax.swing.JLabel InfoNumeroCitas;
    private javax.swing.JLabel InfoNumeroCitas1;
    private javax.swing.JLabel InfocitasConfirmadas;
    private javax.swing.JLabel InfocitasConfirmadas1;
    private javax.swing.JPanel PaneL_Inasistencias;
    private javax.swing.JPanel PanelAtenciones;
    private javax.swing.JPanel PanelCitas;
    private javax.swing.JPanel Panel_Ingresos;
    private javax.swing.JPanel Panel_PlanesCurso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel tituloCitas;
    private javax.swing.JLabel tituloCitas1;
    // End of variables declaration//GEN-END:variables
}
