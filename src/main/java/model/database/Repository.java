package model.database;


import java.sql.Connection;
import java.sql.SQLException;

/**
 * Repository is an abstract class which constructs a repository by getting the database
 * connection and preparing all the statements.
 *
 * @author Bori Fazakas
 */
public abstract class Repository {
  protected Connection c;

  protected Repository() {
    try {
      c = SqliteDatabaseConnectionFactory.getConnection();
      prepareStatements();
    } catch (SQLException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }

  protected abstract void prepareStatements() throws SQLException;
}
