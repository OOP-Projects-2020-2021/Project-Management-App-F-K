package model.user.repository.impl;

import model.user.InexistentUserException;
import model.user.repository.UserRepository;
import model.user.User;
import org.jetbrains.annotations.Nullable;
import java.sql.*;

public class SqliteUserRepository implements UserRepository {

  private Connection connection;
  private PreparedStatement saveUserStatement;
  private PreparedStatement getUserIdStatement;
  private PreparedStatement getUserByIdStatement;
  private PreparedStatement getUserByUsernameStatement;
  private PreparedStatement updateUserStatement;
  private PreparedStatement getUserStatement;

  private static final String SAVE_USER_STATEMENT =
      "INSERT INTO User (UserName,Password) VALUES (?,?)";
  private static final String GET_USER_ID_STATEMENT =
      "SELECT UserId FROM User WHERE (Username = ? and Password = ?);";
  private static final String GET_USER_BY_ID_STATEMENT = "SELECT * FROM User WHERE UserId = ?;";
  private static final String GET_USER_BY_USERNAME_STATEMENT =
      "SELECT * FROM User WHERE Username = ?;";
  private static final String UPDATE_USER_STATEMENT = "UPDATE User SET UserName = ?, Password = ? WHERE UserId = ?;";
  private static final String GET_USER_STATEMENT = "SELECT * FROM User WHERE UserName = ?, Password = ?;";

  public SqliteUserRepository() {
    try {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:project_management_app.db");
      prepareStatements();
    } catch (ClassNotFoundException | SQLException e) {
      e.printStackTrace();
      // if the connection to the database fails exit the program
      System.exit(1);
    }
  }

  private void prepareStatements() throws SQLException {
    saveUserStatement = connection.prepareStatement(SAVE_USER_STATEMENT);
    getUserIdStatement = connection.prepareStatement(GET_USER_ID_STATEMENT);
    getUserByIdStatement = connection.prepareStatement(GET_USER_BY_ID_STATEMENT);
    getUserByUsernameStatement = connection.prepareStatement(GET_USER_BY_USERNAME_STATEMENT);
    updateUserStatement = connection.prepareStatement(UPDATE_USER_STATEMENT);
  }

  /** Saves the user in the database. */
  public void saveUser(User user) throws SQLException {
    saveUserStatement.setString(1, user.getUsername());
    saveUserStatement.setString(2, user.getPassword());
    saveUserStatement.execute();
    // check if the user was saved
    User savedUser = getUserByUsername(user.getUsername());
    if (savedUser == null) {
      throw new SQLException("User could not be saved.");
    }
  }
  /** Updates information about an existing user. */
  public void updateUser(int id,String username,String password) throws SQLException {
    updateUserStatement.setString(1,username);
    updateUserStatement.setString(2,password);
    updateUserStatement.setInt(3,id);
    updateUserStatement.execute();

  }

  /** Get the user's id based on the username and password, used for validating the sign-in. */
  public int getUserId(String username, String password) throws SQLException {
    getUserIdStatement.setString(1, username);
    getUserIdStatement.setString(2, password);
    ResultSet result = getUserIdStatement.executeQuery();
    if (result.next()) {
      return result.getInt("UserId");
    }
    return -1;
  }

  /** Access the user's data based on the id of the user. */
  @Nullable
  public User getUserById(int id) throws SQLException {
    getUserByIdStatement.setInt(1, id);
    ResultSet result = getUserByIdStatement.executeQuery();
    if (result.next()) {
      String username = result.getString("UserName");
      String password = result.getString("Password");
      return new User(id, username, password);
    } else {
      return null;
    }
  }

  /**
   * Access the user's data based on the username of the user, used when managing the members of a
   * team.
   */
  @Nullable
  public User getUserByUsername(String username) throws SQLException {
    getUserByUsernameStatement.setString(1, username);
    ResultSet result = getUserByUsernameStatement.executeQuery();
    if (result.next()) {
      int id = result.getInt("UserId");
      String password = result.getString("Password");
      return new User(id, username, password);
    } else {
      return null;
    }
  }
}
