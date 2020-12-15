package model.user;

import model.Manager;
import model.project.Project;
import model.user.repository.UserRepository;
import model.user.repository.impl.SqliteUserRepository;
import java.sql.SQLException;
import java.util.List;
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

  public Optional<User> getCurrentUser() {
    // todo ofNullable
    return Optional.ofNullable(currentUser);
  }

  public void logOut() {
    currentUser = null;
  }

  public List<Project> getCurrentUsersAssignments() {
    // TODO move to ProjectRepository
    List<Project> assignmentsOfCurrentUser;
    return null;
  }

  public List<Project> getCurrentUsersSupervisedProjects() {
    // TODO move to ProjectRepository
    List<Project> projectsOfCurrentUser;
    return null;
  }
}
