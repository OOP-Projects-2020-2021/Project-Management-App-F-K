package model.user;

import model.Project;
import model.Team;
import model.user.repository.UserRepository;
import model.user.repository.impl.SqliteUserRepository;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public class UserManager {

  private static UserRepository userRepository;
  /** The current user which has signed in to the application. */
  @Nullable private User currentUser;

  public UserManager() {
    userRepository = new SqliteUserRepository();
    currentUser = null;
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
   * @throws SQLException if the data could not be accessed from the database
   */
  public void signIn(String username, String password) throws SQLException, InvalidSignInException {
    currentUser = userRepository.getUser(username, password);
    if (currentUser == null) throw new InvalidSignInException();
  }

  @Nullable
  public User getCurrentUser() {
    return currentUser;
  }

  public void logOut() {
    currentUser = null;
  }

  public List<Project> getUsersAssignments() {
    if (currentUser != null) {
      // TODO access ProjectRepository of current user
      List<Project> assignmentsOfCurrentUser;
    }
    return null;
  }

  public List<Project> getUsersSupervisedProjects() {
    List<Team> teamsOfCurrentUser;
    List<Project> projectsOfCurrentUser;
    if (currentUser != null) {
      // TODO first get the teams in which the user is in,
      // then get the projects where the current user is the supervisor, from the above listed teams
    }
    return null;
  }
}
