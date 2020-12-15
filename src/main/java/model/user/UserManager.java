package model.user;

import model.InexistentDatabaseEntityException;
import model.Manager;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

/** Singleton class UserManager. */
public class UserManager extends Manager {

  private static UserManager instance;
  /** The current user which has signed in to the application. */
  private User currentUser;

  private UserManager() {}

  /** The instance of the UserManager class is only created when it is required. */
  public static UserManager getInstance() {
    if (instance == null) {
      instance = new UserManager();
    }
    return instance;
  }

  /**
   * Creates a new user in the database.
   *
   * @param username = the username of the user
   * @param password = the password set by the user
   * @throws SQLException = in case the user could not be saved
   */
  public void signUp(String username, String password) throws SQLException {
    User user = new User.SavableUser(username, password);
    userRepository.saveUser(user);
  }

  /**
   * Validates the sign-in, by searching for the username in the database, then comparing the
   * corresponding password with the one introduced by the user, and it sets the currentUser on
   * successful sign-in, otherwise currentUser will be null.
   *
   * @param username = username introduced by the user at sign-in
   * @param password = password introduced by the user at sign-in
   * @return boolean = true if the username and password were found in the database.
   * @throws SQLException if a database error occurred
   */
  public boolean signIn(String username, String password) throws SQLException {
    currentUser = userRepository.getUserByUsername(username);
    return (currentUser != null && currentUser.getPassword().equals(password));
  }

  public Optional<User> getCurrentUser() {
    return Optional.ofNullable(currentUser);
  }

  /**
   * Validates the password introduced by the user when attempting to change the password. This is
   * an additional security step, to protect the account information of the user.
   *
   * @param password = password introduced by the user that will be compared to the current password
   * @return boolean = true if the password matches
   */
  public boolean validatePassword(String password) {
    return currentUser.getPassword().equals(password);
  }

  /**
   * Updates the user's account information, saving the new username and password. Whenever the user
   * changes the account data from the settings view, the currentUser instance is updated as well.
   *
   * @param username = new username
   * @param password = new password
   */
  public void updateUser(String username, String password)
      throws SQLException, NoSignedInUserException {
    try {
      int id = currentUser.getId();
      currentUser = new User(id, username, password);
      userRepository.updateUser(currentUser);
    } catch (NoSuchElementException | InexistentDatabaseEntityException noSuchElementException) {
      throw new NoSignedInUserException();
    }
  }

  public void logOut() {
    currentUser = null;
  }
}
