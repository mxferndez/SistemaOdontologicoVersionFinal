package Sistemaodontologico.Persistencia;
// Lee la conexión de las variables de entorno de Windows.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static final String URL = valor("ODONTO_DB_URL",
            "jdbc:mysql://localhost:3306/sistema_odontologico?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima");
    private static final String USUARIO = valor("ODONTO_DB_USER", "root");
    private static final String PASSWORD = valor("ODONTO_DB_PASSWORD", "Rfrancolj");

    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, PASSWORD);
    }

    private static String valor(String nombre, String valorPorDefecto) {
        String valor = System.getenv(nombre);
        if (valor == null || valor.isBlank()) {
            return valorPorDefecto;
        }
        return valor;
    }
}
