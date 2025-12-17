package lab2_map.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/duck_socialnetwork";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Al3x1708";

    private static Connection connection;

    private DatabaseConnection() {
    }

    /*
        Returns a shared database connection instance.
        Ensures driver is loaded and reconnects if needed.
    */
    public static synchronized Connection getConnection() {

        try {
            if (connection == null || connection.isClosed()) {

                // Load PostgreSQL driver explicitly
                try {
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException ignored) {
                    System.out.println("PostgreSQL driver not found.");
                }

                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connected to PostgreSQL.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }

        return connection;
    }
}
