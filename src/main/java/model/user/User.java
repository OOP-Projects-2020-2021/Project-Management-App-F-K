package model.user;

import model.InexistentDatabaseEntityException;
import model.project.Project;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Represents a User and contains its account information.
 *
 * @author Beata Keresztes
 */
public class User {

  /** The id identifies the user in the database. */
  private int id;
  /** Each user has a unique username. */
  private String username;
  /** Each user has a password used for authentication. */
  private String password;

  /**
   * This class is used only when the user instance is created to be saved in the database, but
   * does not have a valid id yet.
   */
  public static class SavableUser extends User {
    public SavableUser(String username, String password) {
      super(-1, username, password);
    }

    @Override
    public int getId() throws InexistentDatabaseEntityException {
      throw new InexistentDatabaseEntityException(this);
    }
  }

  public User(int id, String username, String password) {
    this.username = username;
    this.password = password;
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getId() throws InexistentDatabaseEntityException {
    return id;
  }
}
