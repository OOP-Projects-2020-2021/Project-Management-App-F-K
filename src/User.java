import java.io.*;

/**
 * Represents a User and contains its account information.
 *
 * @author Beata Keresztes
 */
public class User implements Serializable {

  private String username;
  private String password;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  /**
   * Validates the username and password introduced by the user.
   *
   * @param username uniquely identifies the user
   * @param password is only checked if the username is valid
   * @return boolean
   */
  public boolean isValidLogInData(String username, String password) {

    return true;
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
}
