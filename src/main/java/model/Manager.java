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

/**
 * Manager is a helper class for UserManager, TeamManager and ProjectManager. It instantiates the
 * repositories for data access and provides some frequently-used methods for getting and validating
 * data, all of which throw the necessary exceptions in case of invalid data.
 *
 * @author Bori Fazakas
 */
public abstract class Manager {
  protected static ProjectRepository projectRepository = SqliteProjectRepository.getInstance();
  protected static TeamRepository teamRepository = SqliteTeamRepository.getInstance();
  protected static UserRepository userRepository = SqliteUserRepository.getInstance();

  /**
   * @return the current user.
   * @throws NoSignedInUserException if there is no current user.
   */
  protected User getMandatoryCurrentUser() throws NoSignedInUserException {
    if (UserManager.getInstance().getCurrentUser().isEmpty()) {
      throw new NoSignedInUserException();
    }
    return UserManager.getInstance().getCurrentUser().get();
  }

  /**
   * @return the team with the given id if it exists in the database.
   * @throws InexistentTeamException if there is no team with this id.
   */
  protected Team getMandatoryTeam(int teamId) throws InexistentTeamException, SQLException {
    Optional<Team> team = teamRepository.getTeam(teamId);
    if (team.isEmpty()) {
      throw new InexistentTeamException(teamId);
    }
    return team.get();
  }

  /**
   * @return the team with the given teamCode if it exists in the database.
   * @throws InexistentTeamException if there is no team with this code.
   */
  protected Team getMandatoryTeam(String teamCode) throws InexistentTeamException, SQLException {
    Optional<Team> team = teamRepository.getTeam(teamCode);
    if (team.isEmpty()) {
      throw new InexistentTeamException(teamCode);
    }
    return team.get();
  }

  /**
   * @return the user with the given id if it exists in the database.
   * @throws InexistentUserException if there is no user with this id.
   */
  protected User getMandatoryUser(int userId) throws SQLException, InexistentUserException {
    User user = userRepository.getUserById(userId);
    if (user == null) {
      throw new InexistentUserException(userId);
    }
    return user;
  }

  /**
   * @return the user with the given name if it exists in the database.
   * @throws InexistentUserException if there is no user with this name.
   */
  protected User getMandatoryUser(String userName) throws SQLException, InexistentUserException {
    User user = userRepository.getUserByUsername(userName);
    if (user == null) {
      throw new InexistentUserException(userName);
    }
    return user;
  }
}
