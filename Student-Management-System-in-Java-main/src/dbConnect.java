import java.sql.*;

public class dbConnect {
    private dbConnect() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Add the MySQL Connector/J dependency.", e);
        }

        String url = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/studata");
        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "1236");

        return DriverManager.getConnection(url, user, password);
    }
}
