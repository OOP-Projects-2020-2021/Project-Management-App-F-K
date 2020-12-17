package model.user.repository.impl;

import model.InexistentDatabaseEntityException;
import model.database.Repository;
import model.user.exceptions.DuplicateUsernameException;
import model.user.repository.UserRepository;
import model.user.User;
import org.jetbrains.annotations.Nullable;
import java.sql.*;

public class SqliteUserRepository extends Repository implements UserRepository {
  protected static SqliteUserRepository instance;

  private PreparedStatement saveUserStatement;
  private PreparedStatement getUserIdStatement;
  private PreparedStatement getUserByIdStatement;
  private PreparedStatement getUserByUsernameStatement;
  private PreparedStatement updateUserStatement;

  private static final String SAVE_USER_STATEMENT =
      "INSERT INTO User (UserName,Password) VALUES (?,?)";
  private static final String GET_USER_ID_STATEMENT =
      "SELECT UserId FROM User WHERE (Username = ? and Password = ?);";
  private static final String GET_USER_BY_ID_STATEMENT = "SELECT * FROM User WHERE UserId = ?;";
  private static final String GET_USER_BY_USERNAME_STATEMENT =
      "SELECT * FROM User WHERE Username = ?;";
  private static final String UPDATE_USER_STATEMENT =
      "UPDATE User SET UserName = ?, Password = ? WHERE UserId = ?;";

  private SqliteUserRepository() {}

  public static SqliteUserRepository getInstance() {
    if (instance == null) {
      instance = new SqliteUserRepository();
    }
    return instance;
  }

  protected void prepareStatements() throws SQLException {
    saveUserStatement = c.prepareStatement(SAVE_USER_STATEMENT);
    getUserIdStatement = c.prepareStatement(GET_USER_ID_STATEMENT);
    getUserByIdStatement = c.prepareStatement(GET_USER_BY_ID_STATEMENT);
    getUserByUsernameStatement = c.prepareStatement(GET_USER_BY_USERNAME_STATEMENT);
    updateUserStatement = c.prepareStatement(UPDATE_USER_STATEMENT);
  }

  /** Saves the user in the database. */
  public void saveUser(User user) throws SQLException, DuplicateUsernameException {
    // check if the given username is already taken
    User existingUser = getUserByUsername(user.getUsername());
    if (existingUser != null) {
      throw new DuplicateUsernameException(user.getUsername());
    } else {
      saveUserStatement.setString(1, user.getUsername());
      saveUserStatement.setString(2, user.getPassword());
      saveUserStatement.execute();
      // check if the user was saved
      User savedUser = getUserByUsername(user.getUsername());
      if (savedUser == null) {
        throw new SQLException("User could not be saved.");
      }
    }
  }
  /** Updates information about an existing user. */
  public void updateUser(User user) throws SQLException, InexistentDatabaseEntityException {
    updateUserStatement.setString(1, user.getUsername());
    updateUserStatement.setString(2, user.getPassword());
    updateUserStatement.setInt(3, user.getId());
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
