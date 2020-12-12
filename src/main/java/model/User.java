package main.java.model;

/**
 * Represents a User and contains its account information.
 *
 * @author Beata Keresztes
 */
public class User {

  /** The id identifies the user in the database. */
  private Integer id;
  /** Each user has a unique username. */
  private String username;
  /** Each user has a password used for authentication. */
  private String password;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
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

  public Integer getId() {
    return id;
  }
}
