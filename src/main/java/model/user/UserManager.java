package model.user;

import model.user.repository.UserRepository;
import model.user.repository.impl.SqliteUserRepository;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Optional;

/** Singleton class UserManager. */
public class UserManager {

  private static UserManager instance;
  private UserRepository userRepository = new SqliteUserRepository();
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
    User user = new User(username, password);
    userRepository.saveUser(user);
  }

  /**
   * Validates the sign-in, and sets the currentUser on successful sign-in.
   *
   * @param username = username introduced by the user at sign-in
   * @param password = password introduced by the user at sign-in
   * @return boolean = true if the username and password were found in the database.
   * @throws SQLException if the data could not be accessed from the database
   */
  public boolean signIn(String username, String password) throws SQLException {
    int id = userRepository.getUserId(username, password);
    if (id >= 0) {
      currentUser = userRepository.getUserById(id);
      return true;
    }
    return false;
  }

  /**
   * Validates the password introduced by the user when attempting to change the password.
   * This is an additional security step, to protect the account information of the user.
   *
   * @param password = password introduced by the user that will be compared to the current password
   * @return boolean = true if the password matches
   */
  public boolean validatePassword(String password) {
    return currentUser.getPassword().equals(password);
  }

  /**
   * Updates the user's account information, saving the new username and password.
   * @param username = new username
   * @param password = new password
   */
  public void updateUser(String username,String password) throws SQLException,NoSignedInUserException,InexistentUserException{
    try {
      int id = currentUser.getId().get();
      userRepository.updateUser(id, username, password);
      setCurrentUser(id);
    }catch(NoSuchElementException noSuchElementException) {
      throw new NoSignedInUserException();
    }
  }

  private void setCurrentUser(int id) throws SQLException,InexistentUserException {
    try {
      currentUser = userRepository.getUserById(id);
    }catch(NoSuchElementException noSuchElementException) {
      throw new InexistentUserException(id);
    }
  }

  public Optional<User> getCurrentUser(){
    return Optional.of(currentUser);
  }

  public void logOut() {
    currentUser = null;
  }

}
