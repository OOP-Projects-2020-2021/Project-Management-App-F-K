package model.team;

import model.UnauthorisedOperationException;
import model.user.NoSignedInUserException;
import model.user.User;
import model.team.repository.TeamRepository;
import model.team.repository.TeamRepositoryFactory;
import model.user.UserManager;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * TeamManager is responsible for executing all the commands needed for the application that are
 * related to teams.
 *
 * <p>Remark that it is implemented with the singleton pattern, so only one instance of it exists.
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
   * user.
   *
   * @param name is the name of the new team.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public void createNewTeam(String name) throws SQLException, NoSignedInUserException {
    teamRepository.saveTeam(new Team(name, getCurrentUser().getId().get(), generateTeamCode()));
  }

  /**
   * Deletes the team with the specified id from the database, but only if the current user is its
   * manager.
   *
   * @param teamId is the id of the team to delete.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if no team with teamId exists in the database.
   * @throws UnauthorisedOperationException if the current user is not allowed to delete the team,
   *     because the user is not the manager of the team.
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public void deleteTeam(int teamId)
          throws SQLException, InexistentTeamException, UnauthorisedOperationException, NoSignedInUserException {
    Team team = getTeam(teamId);
    User currentUser = getCurrentUser();
    guaranteeUserIsManager(team, currentUser, "delete the team");
    teamRepository.deleteTeam(teamId);
  }

  /**
   * Returns the teams in which the currently logged in user is a member.
   *
   * @return the list of teams having the current user as member.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public List<Team> getTeamsOfCurrentUser() throws SQLException, NoSignedInUserException {
    User currentUser = getCurrentUser();
    return teamRepository.getTeamsOfUser(currentUser.getId().get());
  }

  /**
   * Generated a new, unique code for the team with the given id, and saves it. Remark that if no
   * team with the given id exists, nothing happens, no exception is thrown.
   *
   * @param teamId is the id of the team for which the new code is generated.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if no team with teamId exists in the database.
   * @throws UnauthorisedOperationException if the current user is not allowed to delete the team,
   *     because the user is not the manager of the team.
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public String regenerateTeamCode(int teamId) throws SQLException, InexistentTeamException, NoSignedInUserException, UnauthorisedOperationException {
    Team team = getTeam(teamId);
    User currentUser = getCurrentUser();
    guaranteeUserIsManager(team, currentUser, "regenerate team code");
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
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public void joinTeam(String code) throws SQLException, InexistentTeamException, NoSignedInUserException {
    Team team = getTeam(code);
    User currentUser = getCurrentUser();
    teamRepository.removeTeamMember(team.getId().get(), currentUser.getId().get());
  }

  /**
   * Removes the current user from the team with the given id, if the user was a member of it. If
   * not, nothing happens, no exception is thrown.
   *
   * @param teamId is the id of the team to join.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public void leaveTeam(int teamId) throws SQLException, NoSignedInUserException {
    User currentUser = getCurrentUser();
    teamRepository.removeTeamMember(teamId, currentUser.getId().get());
  }

  /**
   * Adds the user with userName to the team with teamId if they both exist and the current user is
   * the manager of the team.
   *
   * @param teamId is the id of the team with the new member.
   * @param userName is the name of the user to be added to the team.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if no team with this teamId exists.
   * @throws UnauthorisedOperationException if the current user is no the manager of the team.
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public void addMemberToTeam(int teamId, String userName)
          throws SQLException, InexistentTeamException, UnauthorisedOperationException, NoSignedInUserException {
    Team team = getTeam(teamId);
    User currentUser = getCurrentUser();
    guaranteeUserIsManager(team, currentUser, "add member to the team");
    User newMember = new User(3, userName, ""); // todo get from UserManager
    teamRepository.addTeamMember(team.getId().get(), newMember.getId().get());
  }

  /**
   * Removes the user with userName from the team with teamId if they both exist and the current
   * user is the manager of the team. Remark that if the user is not the member of the team, nothing
   * happens.
   *
   * @param teamId is the id of the team which looses a member.
   * @param userName is the name of the user to be removed from the team.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if no team with this teamId exists.
   * @throws UnauthorisedOperationException if the current user is no the manager of the team.
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public void removeTeamMember(int teamId, String userName)
          throws SQLException, InexistentTeamException, UnauthorisedOperationException, NoSignedInUserException {
    Team team = getTeam(teamId);
    User currentUser = getCurrentUser();
    guaranteeUserIsManager(team, currentUser, "remove a member from the team");
    User toRemoveMember = new User(3, userName, ""); // todo get from UserManager
    teamRepository.removeTeamMember(team.getId().get(), toRemoveMember.getId().get());
  }

  /**
   * Sets the manager of the team with the given id to be the user with the given name, but only if
   * the current user is at the moment the manager of the team.
   *
   * @param teamId is the id of the team for which the manager is changes.
   * @param newManagerName is the name of the new manager as specified by the current one.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if there is no team with id teamId.
   * @throws UnauthorisedOperationException if the current user is not the manager of the team, so
   *     they are not authorised to change the manager.
   * @throws IllegalArgumentException if the user with newManagerName is not the member of the team
   *     in which he/she should become a manager.
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public void passManagerPosition(int teamId, String newManagerName)
          throws SQLException, InexistentTeamException, UnauthorisedOperationException, NoSignedInUserException {
    Team team = getTeam(teamId);
    User currentUser = getCurrentUser();
    guaranteeUserIsManager(team, currentUser, "pass manager position of team to someone else");
    User newManager = new User(3, newManagerName, ""); // todo get user with name UserManager
    if (newManager == null) {
      // todo throw new InexistentUserException
    }
    if (!teamRepository.isMemberOfTeam(teamId, newManager.getId().get())) {
      throw new IllegalArgumentException(
          "The user with name "
              + newManagerName
              + " can't be the "
              + "manager of a team he/she is not a member of");
    }
    teamRepository.setNewManagerPosition(teamId, newManager.getId().get());
  }

  /**
   * Sets a new name for the team with the given code, but only if the current user is the manager
   * of the team.
   *
   * @param teamId is the id of the team whose name is changed.
   * @param newTeamName is the new name of the team to set.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if there is no team with id teamId.
   * @throws UnauthorisedOperationException if the current user is not the manager of the team, so
   *     they are not authorised to change the name.
   * @throws NoSignedInUserException if the user is not signed in.
   */
  public void setNewName(int teamId, String newTeamName)
          throws SQLException, InexistentTeamException, UnauthorisedOperationException, NoSignedInUserException {
    Team team = getTeam(teamId);
    User currentUser = getCurrentUser();
    guaranteeUserIsManager(team, currentUser, "change the name of the team");
    teamRepository.setNewName(teamId, newTeamName);
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
      if (teamRepository.getTeam(code).isEmpty()) {
        found = true;
      }
    }
    return code;
  }

  /** Returns the current user if it exists, and throws NoSignedInUserException otherwise. */
  private User getCurrentUser() throws NoSignedInUserException {
    if (UserManager.getInstance().getCurrentUser().isEmpty()) {
      throw new NoSignedInUserException();
    }
    return UserManager.getInstance().getCurrentUser().get();
  }

  /**
   * Returns the team with the given id if it exists in the database, and throws
   * InexistentTeamException otherwise.
   * The returned team is guaranteed to have an id.
   */
  private Team getTeam(int teamId) throws InexistentTeamException, SQLException {
    Optional<Team> team = teamRepository.getTeam(teamId);
    if (team.isEmpty()) {
      throw new InexistentTeamException(teamId);
    }
    return team.get();
  }

  /**
   * Returns the team with the given code if it exists in the database, and throws
   * InexistentTeamException otherwise.
   * The returned team is guaranteed to have an id.
   */
  private Team getTeam(String teamCode) throws InexistentTeamException, SQLException {
    Optional<Team> team = teamRepository.getTeam(teamCode);
    if (team.isEmpty()) {
      throw new InexistentTeamException(teamCode);
    }
    return team.get();
  }

  /** Throws UnauthorisedOperationException if user is not the manager of team. */
  private void guaranteeUserIsManager(Team team, User user, String operation) throws UnauthorisedOperationException {
    if (team.getManagerId() != user.getId().get()) {
      throw new UnauthorisedOperationException(
              user.getId().get(),
              operation,
              "this user is not the manager of the project");
    }
  }
}
