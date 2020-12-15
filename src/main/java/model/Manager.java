package model;

import model.project.repository.ProjectRepository;
import model.project.repository.impl.SqliteProjectRepository;
import model.team.InexistentTeamException;
import model.team.Team;
import model.team.repository.TeamRepository;
import model.team.repository.impl.SqliteTeamRepository;
import model.user.InexistentUserException;
import model.user.NoSignedInUserException;
import model.user.User;
import model.user.UserManager;
import model.user.repository.UserRepository;
import model.user.repository.impl.SqliteUserRepository;

import java.sql.SQLException;
import java.util.Optional;

public abstract class Manager {
  protected static ProjectRepository projectRepository = SqliteProjectRepository.getInstance();
  protected static TeamRepository teamRepository = SqliteTeamRepository.getInstance();
  protected static UserRepository userRepository = SqliteUserRepository.getInstance();

  /** Returns the current user if it exists, and throws NoSignedInUserException otherwise. */
  protected User getMandatoryCurrentUser() throws NoSignedInUserException {
    if (UserManager.getInstance().getCurrentUser().isEmpty()) {
      throw new NoSignedInUserException();
    }
    return UserManager.getInstance().getCurrentUser().get();
  }

  /**
   * Returns the team with the given id if it exists in the database, and throws
   * InexistentTeamException otherwise. The returned team is guaranteed to have an id.
   */
  protected Team getMandatoryTeam(int teamId) throws InexistentTeamException, SQLException {
    Optional<Team> team = teamRepository.getTeam(teamId);
    if (team.isEmpty()) {
      throw new InexistentTeamException(teamId);
    }
    return team.get();
  }

  /**
   * Returns the team with the given code if it exists in the database, and throws
   * InexistentTeamException otherwise. The returned team is guaranteed to have an id.
   */
  protected Team getMandatoryTeam(String teamCode) throws InexistentTeamException, SQLException {
    Optional<Team> team = teamRepository.getTeam(teamCode);
    if (team.isEmpty()) {
      throw new InexistentTeamException(teamCode);
    }
    return team.get();
  }

  /** Returns the user with the given id if it exists, otherwise throws InexistentUserEXception. */
  protected User getMandatoryUser(int userId) throws SQLException, InexistentUserException {
    User user = userRepository.getUserById(userId);
    if (user == null) {
      throw new InexistentUserException(userId);
    }
    return user;
  }

  /**
   * Returns the user with the given name if it exists, otherwise throws InexistentUserEXception.
   */
  protected User getMandatoryUser(String userName) throws SQLException, InexistentUserException {
    User user = userRepository.getUserByUsername(userName);
    if (user == null) {
      throw new InexistentUserException(userName);
    }
    return user;
  }
}
