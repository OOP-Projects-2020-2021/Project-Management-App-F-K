package model.team;

import model.UnauthorisedOperationException;
import model.User;
import model.team.repository.TeamRepository;
import model.team.repository.TeamRepositoryFactory;

import java.sql.SQLException;
import java.util.List;

/**
 * TeamManager is responsible for executing all the commands needed for the application that are
 * related to teams.
 *
 * Remark that it is implemented with the singleton pattern, so only one instance of it exists.
 *
 * @author Bori Fazakas
 */
public class TeamManager {
  private static TeamManager instance = new TeamManager();
  private TeamRepository teamRepository =
      TeamRepositoryFactory.getTeamRepository(TeamRepositoryFactory.RepositoryType.SQLITE);

  private TeamManager() {}

  public static TeamManager getInstance() {
    return instance;
  }

  /**
   * Creates a new team with the given name, and with the manager being the currently logged in
   * user
   * @param name is the name of the new team.
   * @throws SQLException if the operation could not be performed in the database.
   */
  public void createNewTeam(String name) throws SQLException {
    // todo: get managerId from UserManager
    int managerId = 2;
    teamRepository.saveTeam(new Team(name, managerId, generateTeamCode()));
  }

  /**
   * Returns the teams in which the currently logged in user is a member.
   *
   * @return the list of teams having the current user as member.
   * @throws SQLException if the operation could not be performed in the database.
   */
  public List<Team> getTeamsOfCurrentUser() throws SQLException {
    User currentUser = new User(1, "", ""); // todo get from UserManager
    return teamRepository.getTeamsOfUser(currentUser);
  }

  /**
   * Generated a new, unique code for the team with the given id, and saves it.
   * Remark that if no team with the given id exists, nothing happend, no exception is thrown.
   *
   * @param teamId is the id of the team for which the new code is generated.
   * @throws SQLException if the operation could not be performed in the database.
   */
  public String regenerateTeamCode(int teamId) throws SQLException {
    String newCode = generateTeamCode();
    teamRepository.setNewCode(teamId, newCode);
    return newCode;
  }

  /**
   * Adds the current user as a member to the team with the given code, if it exists.
   *
   * @param code is the code provided by the user of the team to join.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if no team with this code exists.
   */
  public void joinTeam(String code) throws SQLException, InexistentTeamException {
    Team team = teamRepository.getTeam(code);
    User currentUser = new User(2, "", ""); // todo get from UserManager
    if (team == null) {
      throw new InexistentTeamException(code);
    } else {
      teamRepository.joinTeam(currentUser.getId().get(), team.getId().get());
    }
  }

  /**
   * Removes the current user from the team with the given id, if the user was a member of it. If
   * not, nothing happens, no exception is thrown.
   *
   * @param teamId is the id of the team to join.
   * @throws SQLException if the operation could not be performed in the database.
   */
  public void leaveTeam(int teamId) throws SQLException {
    User currentUser = new User(1, "", ""); // todo get from UserManager
    teamRepository.leaveTeam(currentUser.getId().get(), teamId);
  }

  /**
   * Sets the manager of the team with the given id to be the user with the given name, but only
   * if the current user is at the moment the manager of the team.
   *
   * @param teamId is the id of the team for which the manager is changes.
   * @param newManagerName is the nam eof the new manager as specified by the current one.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if there is no team with id teamId.
   * @throws UnauthorisedOperationException if the current user is not the manager of the team,
   * so they are not authorised to change the manager.
   */
  public void passManagerPosition(int teamId, String newManagerName)
      throws SQLException, InexistentTeamException, UnauthorisedOperationException {
    Team team = teamRepository.getTeam(teamId);
    if (team == null) {
      throw new InexistentTeamException(teamId);
    }
    User currentUser = new User(1, "", ""); // todo get from UserManager
    if (currentUser.getId().isEmpty() || team.getManagerId() != currentUser.getId().get()) {
      throw new UnauthorisedOperationException(
          currentUser.getId().get(),
          " pass manager " + "position",
          "this user is not the manager of the project");
    }
    User newManager = new User(2, newManagerName, ""); // todo get user with name UserManager
    if (newManager == null) {
      // todo throw new InexistentUserException
    }
    teamRepository.setNewManagerPosition(teamId, newManager.getId().get());
  }

  /**
   * Generates a new teamcode with the length specidied in Team.CODE_LENGTH, consisting of digits
   * only. This code must be unique, meaning that it is not used by any other team.
   *
   * @return the newly generated code.
   * @throws SQLException if any exception occurs which accessing the database.
   */
  private String generateTeamCode() throws SQLException {
    boolean found = false;
    String code = null;
    while (!found) {
      int randomNumber = (int) (Math.random() * (int) (Math.pow(10, Team.CODE_LENGTH) - 1) + 1);
      String format = "%0" + Team.CODE_LENGTH + "d";
      code = String.format(format, randomNumber);
      if (teamRepository.getTeam(code) == null) {
        found = true;
      }
    }
    return code;
  }
}
