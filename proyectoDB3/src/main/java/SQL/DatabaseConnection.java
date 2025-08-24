package SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url = "jdbc:mysql://localhost:3306/clinicdb";
    private static final String user = "root";
    private static final String password = "AnahiNiurka123";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(url, user, password);
    }
}
