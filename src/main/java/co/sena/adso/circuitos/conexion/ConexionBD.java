package co.sena.adso.circuitos.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conexión a MySQL — lee variables de entorno de Railway automáticamente.
 * GA7-220501096-AA3-EV02 — Michael Olivares — SENA Ficha 3118306 — Abril 2026
 */
public class ConexionBD {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    private static final String HOST = getEnv("MYSQLHOST",     "localhost");
    private static final String PORT = getEnv("MYSQLPORT",     "3306");
    private static final String DB   = getEnv("MYSQLDATABASE", "sistema_diseno_circuitos");
    private static final String USER = getEnv("MYSQLUSER",     "root");
    private static final String PASS = getEnv("MYSQLPASSWORD", "");

    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DB
        + "?useSSL=false&serverTimezone=America/Bogota&allowPublicKeyRetrieval=true";

    private static Connection instancia;
    private ConexionBD() {}

    public static Connection getConexion() throws SQLException {
        try {
            if (instancia == null || instancia.isClosed()) {
                Class.forName(DRIVER);
                instancia = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado: " + e.getMessage(), e);
        }
        return instancia;
    }

    public static void cerrarConexion() {
        try {
            if (instancia != null && !instancia.isClosed()) {
                instancia.close();
                instancia = null;
            }
        } catch (SQLException e) {
            System.err.println("[ConexionBD] Error al cerrar: " + e.getMessage());
        }
    }

    private static String getEnv(String nombre, String defecto) {
        String val = System.getenv(nombre);
        return (val != null && !val.isBlank()) ? val : defecto;
    }
}
