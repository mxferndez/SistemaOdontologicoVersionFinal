package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Persona;
import Sistemaodontologico.Persistencia.excepciones.PersistenciaException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

// Guarda y lee los datos personales de una persona.
public class PersonaDAO {

    public Persona buscarPorDocumento(String tipoDocumento, String numeroDocumento)
            throws PersistenciaException {
        String consulta = "SELECT id_persona, tipo_documento, numero_documento, nombres, "
                + "apellido_paterno, apellido_materno, fecha_nacimiento, telefono, correo, direccion "
                + "FROM personas WHERE tipo_documento = ? AND numero_documento = ?";

        try (Connection conexion = Conexion.obtener(); PreparedStatement buscarPersona = conexion.prepareStatement(consulta)) {
            buscarPersona.setString(1, tipoDocumento);
            buscarPersona.setString(2, numeroDocumento);

            try (ResultSet resultado = buscarPersona.executeQuery()) {
                if (!resultado.next()) {
                    return null;
                }
                return mapear(resultado);
            }
        } catch (SQLException error) {
            throw new PersistenciaException("No se pudo buscar la persona.", error);
        }
    }

    long insertar(Connection conexion, Persona persona) throws SQLException {
        String consulta = "INSERT INTO personas (tipo_documento, numero_documento, nombres, apellido_paterno, "
                + "apellido_materno, fecha_nacimiento, telefono, correo, direccion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement insertarPersona = conexion.prepareStatement(
                consulta, Statement.RETURN_GENERATED_KEYS)) {
            insertarPersona.setString(1, persona.getTipoDocumento());
            insertarPersona.setString(2, persona.getNumeroDocumento());
            insertarPersona.setString(3, persona.getNombres());
            insertarPersona.setString(4, persona.getApellidoPaterno());
            insertarPersona.setString(5, persona.getApellidoMaterno());
            guardarFecha(insertarPersona, 6, persona.getFechaNacimiento());
            insertarPersona.setString(7, persona.getTelefono());
            insertarPersona.setString(8, persona.getCorreo());
            insertarPersona.setString(9, persona.getDireccion());
            insertarPersona.executeUpdate();

            try (ResultSet resultado = insertarPersona.getGeneratedKeys()) {
                resultado.next();
                return resultado.getLong(1);
            }
        }
    }

    void actualizar(Connection conexion, Persona persona) throws SQLException {
        String consulta = "UPDATE personas SET tipo_documento = ?, numero_documento = ?, nombres = ?, "
                + "apellido_paterno = ?, apellido_materno = ?, fecha_nacimiento = ?, telefono = ?, "
                + "correo = ?, direccion = ? WHERE id_persona = ?";

        try (PreparedStatement actualizarPersona = conexion.prepareStatement(consulta)) {
            actualizarPersona.setString(1, persona.getTipoDocumento());
            actualizarPersona.setString(2, persona.getNumeroDocumento());
            actualizarPersona.setString(3, persona.getNombres());
            actualizarPersona.setString(4, persona.getApellidoPaterno());
            actualizarPersona.setString(5, persona.getApellidoMaterno());
            guardarFecha(actualizarPersona, 6, persona.getFechaNacimiento());
            actualizarPersona.setString(7, persona.getTelefono());
            actualizarPersona.setString(8, persona.getCorreo());
            actualizarPersona.setString(9, persona.getDireccion());
            actualizarPersona.setLong(10, persona.getIdPersona());
            actualizarPersona.executeUpdate();
        }
    }

    Persona mapear(ResultSet resultado) throws SQLException {
        Persona persona = new Persona();
        persona.setIdPersona(resultado.getLong("id_persona"));
        persona.setTipoDocumento(resultado.getString("tipo_documento"));
        persona.setNumeroDocumento(resultado.getString("numero_documento"));
        persona.setNombres(resultado.getString("nombres"));
        persona.setApellidoPaterno(resultado.getString("apellido_paterno"));
        persona.setApellidoMaterno(resultado.getString("apellido_materno"));
        persona.setFechaNacimiento(leerFecha(resultado, "fecha_nacimiento"));
        persona.setTelefono(resultado.getString("telefono"));
        persona.setCorreo(resultado.getString("correo"));
        persona.setDireccion(resultado.getString("direccion"));
        return persona;
    }

    private java.time.LocalDate leerFecha(ResultSet resultado, String columna) throws SQLException {
        Date fecha = resultado.getDate(columna);
        if (fecha == null) {
            return null;
        }
        return fecha.toLocalDate();
    }

    private void guardarFecha(PreparedStatement consulta, int posicion, java.time.LocalDate fecha)
            throws SQLException {
        if (fecha == null) {
            consulta.setNull(posicion, Types.DATE);
        } else {
            consulta.setDate(posicion, Date.valueOf(fecha));
        }
    }
}
