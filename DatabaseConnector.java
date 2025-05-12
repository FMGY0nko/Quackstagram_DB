import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String url = "jdbc:mysql://localhost:3306/q_db";
    private static final String user = "server";
    private static final String password = "server";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
