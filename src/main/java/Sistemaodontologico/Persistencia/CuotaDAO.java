package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Cuota;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Busca las cuotas que todavía tienen saldo.
public class CuotaDAO {

    public ArrayList<Cuota> listarPendientes() throws PersistenciaException {
        String consulta = "SELECT q.id_cuota, q.id_plan, q.numero_cuota, q.fecha_vencimiento, q.monto, "
                + "q.id_plan, c.id_paciente, p.tipo_documento, p.numero_documento, "
                + "CONCAT_WS(' ', p.nombres, p.apellido_paterno, p.apellido_materno) AS paciente, "
                + "q.monto - COALESCE((SELECT SUM(pg.monto) FROM pagos pg WHERE pg.id_cuota = q.id_cuota "
                + "AND pg.estado = 'REGISTRADO'), 0) AS saldo "
                + "FROM cuotas_plan q JOIN planes_tratamiento pl ON pl.id_plan = q.id_plan "
                + "JOIN atenciones a ON a.id_atencion = pl.id_atencion JOIN citas c ON c.id_cita = a.id_cita "
                + "JOIN pacientes pa ON pa.id_persona = c.id_paciente JOIN personas p ON p.id_persona = c.id_paciente "
                + "WHERE q.estado IN ('PENDIENTE','PARCIAL') HAVING saldo > 0 "
                + "ORDER BY q.fecha_vencimiento, q.id_plan, q.numero_cuota";
        ArrayList<Cuota> cuotas = new ArrayList<>();
        try (Connection conexion = Conexion.obtener(); PreparedStatement buscar = conexion.prepareStatement(consulta); ResultSet resultado = buscar.executeQuery()) {
            while (resultado.next()) {
                Cuota cuota = new Cuota();
                cuota.setIdCuota(resultado.getLong("id_cuota"));
                cuota.setIdPlan(resultado.getLong("id_plan"));
                cuota.setNumero(resultado.getInt("numero_cuota"));
                cuota.setIdPaciente(resultado.getLong("id_paciente"));
                cuota.setPaciente(resultado.getString("paciente"));
                cuota.setDocumento(resultado.getString("tipo_documento") + " " + resultado.getString("numero_documento"));
                Date fecha = resultado.getDate("fecha_vencimiento");
                cuota.setVencimiento(fecha == null ? null : fecha.toLocalDate());
                cuota.setMonto(resultado.getBigDecimal("monto"));
                cuota.setSaldo(resultado.getBigDecimal("saldo"));
                cuotas.add(cuota);
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudieron cargar las cuotas pendientes.", error);
        }
        return cuotas;
    }
}
