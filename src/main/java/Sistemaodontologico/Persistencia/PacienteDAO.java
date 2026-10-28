package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Paciente;
import Sistemaodontologico.Lógica.Persona;
import Sistemaodontologico.Lógica.Sesion;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import Sistemaodontologico.Persistencia.excepciones.RegistroDuplicadoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Lee y guarda pacientes y sus datos personales.
public class PacienteDAO {

    private final PersonaDAO personaDAO = new PersonaDAO();

    private static final String SELECT_BASE
            = "SELECT p.id_persona, p.tipo_documento, p.numero_documento, p.nombres, "
            + "p.apellido_paterno, p.apellido_materno, p.fecha_nacimiento, p.telefono, "
            + "p.correo, p.direccion, pa.documento_tutor, pa.estado "
            + "FROM pacientes pa JOIN personas p ON p.id_persona = pa.id_persona ";

    public ArrayList<Paciente> listar(String texto, String estado) throws PersistenciaException {
        StringBuilder consulta = new StringBuilder(SELECT_BASE);
        ArrayList<String> parametros = new ArrayList<>();
        boolean tieneFiltro = false;

        if (texto != null && !texto.isBlank()) {
            consulta.append("WHERE (p.numero_documento LIKE ? OR p.nombres LIKE ? ")
                    .append("OR p.apellido_paterno LIKE ? OR p.apellido_materno LIKE ? ")
                    .append("OR p.telefono LIKE ? OR p.correo LIKE ?) ");
            String textoBuscado = "%" + texto.trim() + "%";
            for (int i = 0; i < 6; i++) {
                parametros.add(textoBuscado);
            }
            tieneFiltro = true;
        }

        if (estado != null && !estado.equals("TODOS")) {
            consulta.append(tieneFiltro ? "AND " : "WHERE ")
                    .append("pa.estado = ? ");
            parametros.add(estado);
        }

        consulta.append("ORDER BY p.apellido_paterno, p.apellido_materno, p.nombres");
        ArrayList<Paciente> pacientes = new ArrayList<>();

        try (Connection conexion = Conexion.obtener(); PreparedStatement buscarPacientes = conexion.prepareStatement(consulta.toString())) {
            for (int i = 0; i < parametros.size(); i++) {
                buscarPacientes.setString(i + 1, parametros.get(i));
            }

            try (ResultSet resultado = buscarPacientes.executeQuery()) {
                while (resultado.next()) {
                    pacientes.add(mapear(resultado));
                }
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo cargar la lista de pacientes.", error);
        }

        return pacientes;
    }

    public Paciente buscarPorId(long idPersona) throws PersistenciaException {
        String consulta = SELECT_BASE + "WHERE pa.id_persona = ?";

        try (Connection conexion = Conexion.obtener(); PreparedStatement buscarPaciente = conexion.prepareStatement(consulta)) {
            buscarPaciente.setLong(1, idPersona);

            try (ResultSet resultado = buscarPaciente.executeQuery()) {
                if (!resultado.next()) {
                    return null;
                }
                return mapear(resultado);
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo buscar al paciente.", error);
        }
    }

    public void crear(Paciente paciente, long idUsuario) throws PersistenciaException {
        Persona persona = paciente.getPersona();
        boolean personaNueva = persona.getIdPersona() == 0;

        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                if (personaNueva) {
                    persona.setIdPersona(personaDAO.insertar(conexion, persona));
                }

                String consulta = "INSERT INTO pacientes (id_persona, documento_tutor, estado) "
                        + "VALUES (?, ?, ?)";
                try (PreparedStatement insertarPaciente = conexion.prepareStatement(consulta)) {
                    insertarPaciente.setLong(1, persona.getIdPersona());
                    insertarPaciente.setString(2, paciente.getDocumentoTutor());
                    insertarPaciente.setString(3, paciente.getEstado());
                    insertarPaciente.executeUpdate();
                }
                try (PreparedStatement actividad = conexion.prepareStatement(
                        "INSERT INTO actividades (id_usuario, id_paciente, tipo_evento, descripcion) VALUES (?, ?, 'PACIENTE_REGISTRADO', ?)")) {
                    actividad.setLong(1, idUsuario);
                    actividad.setLong(2, persona.getIdPersona());
                    actividad.setString(3, "Se registró al paciente " + paciente.getNombreCompleto());
                    actividad.executeUpdate();
                }

                conexion.commit();
            } catch (SQLException error) {
                conexion.rollback();
                if (personaNueva) {
                    persona.setIdPersona(0);
                }
                throw error;
            }
        } catch (SQLException error) {
            throw traducirError(error, "No se pudo registrar al paciente.");
        }
    }

    public void editar(Paciente paciente) throws PersistenciaException {
        try (Connection conexion = Conexion.obtener()) {
            conexion.setAutoCommit(false);
            try {
                personaDAO.actualizar(conexion, paciente.getPersona());

                String consulta = "UPDATE pacientes SET documento_tutor = ?, estado = ? "
                        + "WHERE id_persona = ?";
                try (PreparedStatement actualizarPaciente = conexion.prepareStatement(consulta)) {
                    actualizarPaciente.setString(1, paciente.getDocumentoTutor());
                    actualizarPaciente.setString(2, paciente.getEstado());
                    actualizarPaciente.setLong(3, paciente.getPersona().getIdPersona());

                    if (actualizarPaciente.executeUpdate() == 0) {
                        throw new SQLException("El paciente no existe.");
                    }
                }
                try (PreparedStatement actividad = conexion.prepareStatement(
                        "INSERT INTO actividades (id_usuario, id_paciente, tipo_evento, descripcion) VALUES (?, ?, 'OTRO', ?)")) {
                    long idUsuario = Sesion.getInstancia().getIdUsuario();
                    if (idUsuario > 0) {
                        actividad.setLong(1, idUsuario);
                    } else {
                        actividad.setNull(1, java.sql.Types.BIGINT);
                    }
                    actividad.setLong(2, paciente.getPersona().getIdPersona());
                    actividad.setString(3, "Se actualizaron los datos de " + paciente.getNombreCompleto() + ".");
                    actividad.executeUpdate();
                }

                conexion.commit();
            } catch (SQLException error) {
                conexion.rollback();
                throw error;
            }
        } catch (SQLException error) {
            throw traducirError(error, "No se pudieron guardar los cambios del paciente.");
        }
    }

    private Paciente mapear(ResultSet resultado) throws SQLException {
        Paciente paciente = new Paciente();
        Persona persona = personaDAO.mapear(resultado);
        paciente.setPersona(persona);
        paciente.setDocumentoTutor(resultado.getString("documento_tutor"));
        paciente.setEstado(resultado.getString("estado"));
        return paciente;
    }

    private PersistenciaException traducirError(SQLException error, String mensaje) {
        if (error.getErrorCode() == 1062) {
            String detalle = String.valueOf(error.getMessage());
            if (detalle.contains("uq_personas_documento")) {
                return new RegistroDuplicadoException(
                        "Ya existe una persona con ese tipo y número de documento.", error);
            }
            return new RegistroDuplicadoException("El paciente ya está registrado.", error);
        }
        return new PersistenciaException(mensaje, error);
    }
}
