package ORM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/SpiaggiaFacile"; // Replace with your DB URL
    private static final String DB_USER = "Utente"; // Replace with your username
    private static final String DB_PASSWORD = "spiaggiafacile"; // Replace with your password

    private DatabaseConnection() {
        // Private constructor to prevent instantiation
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load the PostgreSQL JDBC driver
                Class.forName("org.postgresql.Driver"); // Important for older JDKs or specific setups

                // Establish the connection
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("Connection to database established.");
            } catch (ClassNotFoundException e) {
                System.err.println("PostgreSQL JDBC driver not found.");
                throw new SQLException("PostgreSQL JDBC driver not found.", e); // Re-throw as SQLException for better handling
            } catch (SQLException e) {
                System.err.println("Error connecting to the database: " + e.getMessage());
                throw e; // Re-throw the SQLException
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Connection to database closed.");
        }
    }
}