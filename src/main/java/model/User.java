package main.java.model;

import java.util.Optional;

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
    this.id = null;
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

  public Optional<Integer> getId() {
    return Optional.of(id);
  }
}
