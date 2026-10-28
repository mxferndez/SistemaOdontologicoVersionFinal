package Sistemaodontologico.Persistencia;

import Sistemaodontologico.Lógica.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Guarda el número de colegiatura del usuario que tiene el rol ODONTOLOGO.
public class OdontologoDAO {

    void guardar(Connection conexion, Usuario usuario) throws SQLException {
        if (!usuario.tieneRol("ODONTOLOGO")) {
            return;
        }

        boolean yaExiste = existeOdontologo(conexion, usuario.getIdUsuario());
        if (yaExiste) {
            actualizarColegiatura(conexion, usuario);
        } else {
            insertarColegiatura(conexion, usuario);
        }
    }

    private boolean existeOdontologo(Connection conexion, long idUsuario) throws SQLException {
        String consulta = "SELECT 1 FROM odontologos WHERE id_usuario = ?";

        try (PreparedStatement buscarOdontologo = conexion.prepareStatement(consulta)) {
            buscarOdontologo.setLong(1, idUsuario);

            try (ResultSet resultado = buscarOdontologo.executeQuery()) {
                return resultado.next();
            }
        }
    }

    private void actualizarColegiatura(Connection conexion, Usuario usuario) throws SQLException {
        String consulta = "UPDATE odontologos SET numero_colegiatura = ? WHERE id_usuario = ?";

        try (PreparedStatement actualizarOdontologo = conexion.prepareStatement(consulta)) {
            actualizarOdontologo.setString(1, usuario.getNumeroColegiatura());
            actualizarOdontologo.setLong(2, usuario.getIdUsuario());
            actualizarOdontologo.executeUpdate();
        }
    }

    private void insertarColegiatura(Connection conexion, Usuario usuario) throws SQLException {
        String consulta = "INSERT INTO odontologos (numero_colegiatura, id_usuario) VALUES (?, ?)";

        try (PreparedStatement insertarOdontologo = conexion.prepareStatement(consulta)) {
            insertarOdontologo.setString(1, usuario.getNumeroColegiatura());
            insertarOdontologo.setLong(2, usuario.getIdUsuario());
            insertarOdontologo.executeUpdate();
        }
    }
}
