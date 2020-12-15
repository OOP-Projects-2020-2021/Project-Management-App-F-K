package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SqliteDatabaseConnectionFactory {
    private static Connection c;

    private static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:project_management_app.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Connection getConnection() {
        if (c == null) {
            connect();
        }
        return c;
    }
}
