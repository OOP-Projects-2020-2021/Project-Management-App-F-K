package model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * SqliteDatabaseConnectionFactory is responsible for creating exactly one instance of database
 * connection (remark thhe singleton pattern), used in all repositories. If more connections would
 * be used, the database would get locked and updates would not be possible.
 *
 * @author Bori Fazakas
 */
public class SqliteDatabaseConnectionFactory {

  public static Connection getConnection() {
    try {
      Class.forName("org.sqlite.JDBC");
      return DriverManager.getConnection(
          "jdbc:sqlite:project_management_app" + ".db" + "?foreign_keys=on");
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
    return null;
  }
}
