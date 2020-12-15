package model.team;

import model.InexistentDatabaseEntityException;
import model.Manager;
import model.UnauthorisedOperationException;
import model.user.InexistentUserException;
import model.user.NoSignedInUserException;
import model.user.User;

import java.sql.SQLException;
import java.util.List;

/**
 * TeamManager is responsible for executing all the commands needed for the application that are
 * related to teams.
 *
 * <p>Remark that it is implemented with the singleton pattern, so only one instance of it exists.
 *
 * @author Bori Fazakas
 */
public class TeamManager extends Manager {
  private static TeamManager instance = new TeamManager();

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
   * @throws InexistentDatabaseEntityException - should never occur.
   */
  public void createNewTeam(String name)
      throws SQLException, NoSignedInUserException, InexistentDatabaseEntityException {
    teamRepository.saveTeam(
        new Team.SavableTeam(name, getMandatoryCurrentUser().getId(), generateTeamCode()));
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
   * @throws InexistentDatabaseEntityException - should never occur.
   */
  public void deleteTeam(int teamId)
      throws SQLException, InexistentTeamException, UnauthorisedOperationException,
          NoSignedInUserException, InexistentDatabaseEntityException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
    guaranteeUserIsManager(team, currentUser, "delete the team");
    teamRepository.deleteTeam(teamId);
  }

  /**
   * Returns the teams in which the currently logged in user is a member.
   *
   * @return the list of teams having the current user as member.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if the user is not signed in.
   * @throws InexistentDatabaseEntityException - should never occur.
   */
  public List<Team> getTeamsOfCurrentUser()
      throws SQLException, NoSignedInUserException, InexistentDatabaseEntityException {
    User currentUser = getMandatoryCurrentUser();
    return teamRepository.getTeamsOfUser(currentUser.getId());
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
   * @throws InexistentDatabaseEntityException - should never occur.
   */
  public String regenerateTeamCode(int teamId)
      throws SQLException, InexistentTeamException, NoSignedInUserException,
          UnauthorisedOperationException, InexistentDatabaseEntityException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
    guaranteeUserIsManager(team, currentUser, "regenerate team code");
    String newCode = generateTeamCode();
    teamRepository.setNewCode(teamId, newCode);
    return newCode;
  }

  /**
   * Adds the current user as a member to the team with the given code, if it exists.
   * Requirement: The user should not alrady be a member of the team.
   *
   * @param code is the code provided by the user of the team to join.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if no team with this code exists.
   * @throws NoSignedInUserException if the user is not signed in.
   * @throws InexistentDatabaseEntityException - should never occur.
   * @throws AlreadyMemberException if the current user is already a member of the team.
   */
  public void joinTeam(String code)
          throws SQLException, InexistentTeamException, NoSignedInUserException,
          InexistentDatabaseEntityException, AlreadyMemberException {
    Team team = getMandatoryTeam(code);
    User currentUser = getMandatoryCurrentUser();
    if (teamRepository.isMemberOfTeam(team.getId(), currentUser.getId())) {
      throw new AlreadyMemberException(currentUser.getUsername(), team.getName());
    }
    teamRepository.addTeamMember(team.getId(), currentUser.getId());
  }

  /**
   * Removes the current user from the team with the given id, if the user was a member of it. If
   * not, nothing happens, no exception is thrown.
   *
   * @param teamId is the id of the team to join.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if the user is not signed in.
   * @throws InexistentDatabaseEntityException - should never occur.
   */
  public void leaveTeam(int teamId)
      throws SQLException, NoSignedInUserException, InexistentDatabaseEntityException {
    User currentUser = getMandatoryCurrentUser();
    teamRepository.removeTeamMember(teamId, currentUser.getId());
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
   * @throws InexistentUserException if the requested new member with userName does not exist.
   * @throws InexistentDatabaseEntityException - should never occur.
   * @throws AlreadyMemberException if the user with userName is already a member of the team.
   */
  public void addMemberToTeam(int teamId, String userName)
          throws SQLException, InexistentTeamException, UnauthorisedOperationException,
          NoSignedInUserException, InexistentUserException, InexistentDatabaseEntityException, AlreadyMemberException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
    guaranteeUserIsManager(team, currentUser, "add member to the team");
    User newMember = getMandatoryUser(userName);
    if (teamRepository.isMemberOfTeam(team.getId(), newMember.getId())) {
      throw new AlreadyMemberException(newMember.getUsername(), team.getName());
    }
    teamRepository.addTeamMember(team.getId(), newMember.getId());
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
   * @throws InexistentUserException if the requested new member with userName does not exist.
   * @throws InexistentDatabaseEntityException - should never occur.
   */
  public void removeTeamMember(int teamId, String userName)
      throws SQLException, InexistentTeamException, UnauthorisedOperationException,
          NoSignedInUserException, InexistentUserException, InexistentDatabaseEntityException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
    guaranteeUserIsManager(team, currentUser, "remove a member from the team");
    User toRemoveMember = getMandatoryUser(userName);
    teamRepository.removeTeamMember(team.getId(), toRemoveMember.getId());
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
   * @throws InexistentUserException if the requested new member with userName does not exist.
   */
  public void passManagerPosition(int teamId, String newManagerName)
      throws SQLException, InexistentTeamException, UnauthorisedOperationException,
          NoSignedInUserException, InexistentUserException, InexistentDatabaseEntityException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
    guaranteeUserIsManager(team, currentUser, "pass manager position of team to someone else");
    User newManager = getMandatoryUser(newManagerName);
    if (!teamRepository.isMemberOfTeam(teamId, newManager.getId())) {
      throw new IllegalArgumentException(
          "The user with name "
              + newManagerName
              + " can't be the "
              + "manager of a team he/she is not a member of");
    }
    teamRepository.setNewManagerPosition(teamId, newManager.getId());
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
   * @throws InexistentDatabaseEntityException - should never occur.
   */
  public void setNewName(int teamId, String newTeamName)
      throws SQLException, InexistentTeamException, UnauthorisedOperationException,
          NoSignedInUserException, InexistentDatabaseEntityException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
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

  /**
   * Throws UnauthorisedOperationException if user is not the manager of team.
   *
   * @throws InexistentDatabaseEntityException - should never occur.
   */
  private void guaranteeUserIsManager(Team team, User user, String operation)
      throws UnauthorisedOperationException, InexistentDatabaseEntityException {
    if (!userIsManager(team, user)) {
      throw new UnauthorisedOperationException(
          user.getId(), operation, "this user is not the manager of the project");
    }
  }

  /** @throws InexistentDatabaseEntityException - should never occur. */
  private boolean userIsManager(Team team, User user) throws InexistentDatabaseEntityException {
    return team.getManagerId() == user.getId();
  }
}
