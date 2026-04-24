package co.sena.adso.circuitos.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    public static Connection getConexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver MySQL no encontrado", e);
        }

        String privateUrl = System.getenv("MYSQL_PRIVATE_URL");
        if (privateUrl != null && !privateUrl.isEmpty()) {
            return DriverManager.getConnection(privateUrl);
        }

        String host = System.getenv("MYSQLHOST") != null ? System.getenv("MYSQLHOST") : "localhost";
        String port = System.getenv("MYSQLPORT") != null ? System.getenv("MYSQLPORT") : "3306";
        String db   = System.getenv("MYSQLDATABASE") != null ? System.getenv("MYSQLDATABASE") : "sistema_circuitos";
        String user = System.getenv("MYSQLUSER") != null ? System.getenv("MYSQLUSER") : "root";
        String pass = System.getenv("MYSQLPASSWORD") != null ? System.getenv("MYSQLPASSWORD") : "";

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db
                   + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        return DriverManager.getConnection(url, user, pass);
    }
}