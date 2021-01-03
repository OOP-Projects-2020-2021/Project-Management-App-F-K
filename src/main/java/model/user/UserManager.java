package model.user;

import model.InexistentDatabaseEntityException;
import model.Manager;
import model.user.exceptions.DuplicateUsernameException;
import model.user.exceptions.EmptyFieldsException;
import model.user.exceptions.NoSignedInUserException;
import org.jetbrains.annotations.Nullable;

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

  public static final String UPDATE_ACCOUNT_PROPERTY = "Updated account data";

  public Optional<User> getCurrentUser() {
    return Optional.ofNullable(currentUser);
  }

  /**
   * Creates a new user in the database.
   *
   * @param username = the username of the user
   * @param password = the password set by the user
   * @throws SQLException = in case the user could not be saved
   */
  public void signUp(String username, String password)
      throws SQLException, DuplicateUsernameException, EmptyFieldsException {
    // check if the given username is already taken
    if (isMissingCredentials(username, password)) {
      throw new EmptyFieldsException();
    }
    User existingUser = userRepository.getUserByUsername(username);
    if (existingUser != null) {
      throw new DuplicateUsernameException(username);
    }
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
   * @throws EmptyFieldsException if a user didn't introduce all the required data when signing in
   */
  public boolean signIn(String username, String password)
      throws SQLException, EmptyFieldsException {
    if (isMissingCredentials(username, password)) throw new EmptyFieldsException();
    currentUser = userRepository.getUserByUsername(username);
    return (currentUser != null && currentUser.getPassword().equals(password));
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
      throws SQLException, NoSignedInUserException, DuplicateUsernameException,
          EmptyFieldsException {
    User oldUser = getMandatoryCurrentUser();
    try {
      User existingUser = userRepository.getUserByUsername(username);
      if (existingUser != null) {
        throw new DuplicateUsernameException(username);
      }
      if (isMissingCredentials(username, password)) throw new EmptyFieldsException();
      int id = currentUser.getId();
      currentUser = new User(id, username, password);
      userRepository.updateUser(currentUser);
    } catch (NoSuchElementException | InexistentDatabaseEntityException noSuchElementException) {
      throw new NoSignedInUserException();
    }
    support.firePropertyChange(UPDATE_ACCOUNT_PROPERTY, oldUser, currentUser);
  }

  /**
   * Finds and returns the user with the specified id from the database.
   *
   * @param id is the id of the user to search for.
   * @return the user with the given id.
   * @throws SQLException if the data could not be retrieved from the database.
   */
  @Nullable
  public User getUserById(int id) throws SQLException {
    return userRepository.getUserById(id);
  }

  public void logOut() {
    currentUser = null;
  }

  private boolean isEmptyText(String text) {
    return text == null || text.isEmpty();
  }

  /**
   * Check if all the required data has been introduced by the user.
   *
   * @param username = the username of the user
   * @param password = the password of the user
   * @return true if some credentials are missing, otherwise false
   */
  private boolean isMissingCredentials(String username, String password) {
    return isEmptyText(username) || isEmptyText(password);
  }
}
