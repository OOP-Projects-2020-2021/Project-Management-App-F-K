package model.team;

import model.InexistentDatabaseEntityException;
import model.Manager;
import model.UnauthorisedOperationException;
import model.project.ProjectManager;
import model.user.exceptions.*;
import model.user.User;
import model.team.exceptions.*;

import java.sql.SQLException;
import java.util.List;

/**
 * TeamManager is responsible for executing all the commands needed for the application that are
 * related to teams.
 *
 * <p>Remark that it is implemented with the singleton pattern, so only one instance of it exists.
 *
 * @author Bori Fazakas, Beata Keresztes
 */
public class TeamManager extends Manager {
  private static TeamManager instance = new TeamManager();

  private TeamManager() {}

  public static TeamManager getInstance() {
    return instance;
  }

  public enum ChangablePropertyName {
    CURRENT_USER_TEAM_MEMBERSHIPS, // event is fired when the current user becomes member of a
    // team/looses
    // membership of a team
    CREATE_TEAM, // event is fired when a new team is created
    DELETE_TEAM, // event is fired when a team is deleted
    CHANGED_TEAM_NAME, // event is fired when the data of the name of the team has been changed
    CHANGED_TEAM_CODE, // event is fired when the code of the team has been changed
    CHANGED_TEAM_MANAGER, // event is fired when the manager passed it position to another member
    ADDED_TEAM_MEMBER, // event is fired when a new member has been added to the team
    REMOVED_TEAM_MEMBER // event is fired when an existing member has been removed from the team
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
    User currentUser = getMandatoryCurrentUser();
    int teamId =
        teamRepository.saveTeam(
            new Team.SavableTeam(name, currentUser.getId(), generateTeamCode()));
    teamRepository.addTeamMember(teamId, currentUser.getId());
    support.firePropertyChange(ChangablePropertyName.CREATE_TEAM.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Deletes the team with the specified id from the database, but only if the current user is its
   * manager. To do so, first, all the memberships and projects of the team are deleted.
   *
   * @param teamId is the id of the team to delete.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if no team with teamId exists in the database.
   * @throws UnauthorisedOperationException if the current user is not allowed to delete the team,
   *     because the user is not the manager of the team.
   * @throws NoSignedInUserException if the user is not signed in.
   * @throws InexistentDatabaseEntityException - should never occur.
   * @throws InexistentUserException should never occur.
   */
  public void deleteTeam(int teamId)
      throws SQLException, InexistentTeamException, UnauthorisedOperationException,
          NoSignedInUserException, InexistentDatabaseEntityException, InexistentUserException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
    guaranteeUserIsManager(team, currentUser, "delete the team");
    teamRepository.deleteAllMembersOfTeam(teamId);
    ProjectManager.getInstance().deleteAllProjectsOfTeam(teamId);
    teamRepository.deleteTeam(teamId);
    support.firePropertyChange(ChangablePropertyName.DELETE_TEAM.toString(), OLD_VALUE, NEW_VALUE);
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
    support.firePropertyChange(
        ChangablePropertyName.CHANGED_TEAM_CODE.toString(), OLD_VALUE, NEW_VALUE);
    return newCode;
  }

  /**
   * Adds the current user as a member to the team with the given code, if it exists. Requirement:
   * The user should not alrady be a member of the team.
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
    support.firePropertyChange(
        ChangablePropertyName.CURRENT_USER_TEAM_MEMBERSHIPS.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Removes the current user from the team with the given id. Requirement: the current user would
   * be the member of the team, but not the manager.
   *
   * @param teamId is the id of the team to join.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if the user is not signed in.
   * @throws InexistentDatabaseEntityException - should never occur.
   * @throws InexistentTeamException if the team to leave does not exist in the database.
   * @throws ManagerRemovalException if the user who wants to leave is the manager.
   * @throws UnregisteredMemberRemovalException if the user is not the member of the team.
   */
  public void leaveTeam(int teamId)
      throws SQLException, NoSignedInUserException, InexistentDatabaseEntityException,
          InexistentTeamException, ManagerRemovalException, UnregisteredMemberRemovalException {
    User currentUser = getMandatoryCurrentUser();
    Team team = getMandatoryTeam(teamId);
    if (!teamRepository.isMemberOfTeam(teamId, currentUser.getId())) {
      throw new UnregisteredMemberRemovalException(team.getName(), currentUser.getUsername());
    }
    if (userIsManager(team, currentUser)) {
      throw new ManagerRemovalException(team.getName(), currentUser.getUsername());
    }
    teamRepository.removeTeamMember(teamId, currentUser.getId());
    support.firePropertyChange(
        ChangablePropertyName.CURRENT_USER_TEAM_MEMBERSHIPS.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Adds the user with userName to the team with teamId if they both exist and the current user is
   * the manager of the team. The user to add should not already be a member.
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
          NoSignedInUserException, InexistentUserException, InexistentDatabaseEntityException,
          AlreadyMemberException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
    guaranteeUserIsManager(team, currentUser, "add member to the team");
    User newMember = getMandatoryUser(userName);
    if (teamRepository.isMemberOfTeam(team.getId(), newMember.getId())) {
      throw new AlreadyMemberException(newMember.getUsername(), team.getName());
    }
    teamRepository.addTeamMember(team.getId(), newMember.getId());
    support.firePropertyChange(
        ChangablePropertyName.ADDED_TEAM_MEMBER.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Removes the user with userName from the team with teamId if they both exist and the current
   * user is the manager of the team. Requirement: the user to remove should be the member of the
   * team, but not the manager.
   *
   * @param teamId is the id of the team which looses a member.
   * @param userName is the name of the user to be removed from the team.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentTeamException if no team with this teamId exists.
   * @throws UnauthorisedOperationException if the current user is no the manager of the team.
   * @throws NoSignedInUserException if the user is not signed in.
   * @throws InexistentUserException if the requested new member with userName does not exist.
   * @throws InexistentDatabaseEntityException - should never occur.
   * @throws UnregisteredMemberRemovalException if the user is not the member of the team.
   * @throws ManagerRemovalException if the member to remove is the manager of the team (i.e. the
   *     current user wants to leave the team).
   */
  public void removeTeamMember(int teamId, String userName)
      throws SQLException, InexistentTeamException, UnauthorisedOperationException,
          NoSignedInUserException, InexistentUserException, InexistentDatabaseEntityException,
          UnregisteredMemberRemovalException, ManagerRemovalException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
    if (!teamRepository.isMemberOfTeam(teamId, currentUser.getId())) {
      throw new UnregisteredMemberRemovalException(team.getName(), currentUser.getUsername());
    }
    guaranteeUserIsManager(team, currentUser, "remove a member from the team");
    User toRemoveMember = getMandatoryUser(userName);
    if (userIsManager(team, toRemoveMember)) {
      throw new ManagerRemovalException(team.getName(), toRemoveMember.getUsername());
    }
    teamRepository.removeTeamMember(team.getId(), toRemoveMember.getId());
    support.firePropertyChange(
        ChangablePropertyName.REMOVED_TEAM_MEMBER.toString(), OLD_VALUE, NEW_VALUE);
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
   * @throws UnregisteredMemberRoleException if the manager to be set is not the member of the team.
   * @throws NoSignedInUserException if the user is not signed in.
   * @throws InexistentUserException if the requested new member with userName does not exist.
   */
  public void passManagerPosition(int teamId, String newManagerName)
      throws SQLException, InexistentTeamException, UnauthorisedOperationException,
          NoSignedInUserException, InexistentUserException, InexistentDatabaseEntityException,
          UnregisteredMemberRoleException {
    Team team = getMandatoryTeam(teamId);
    User currentUser = getMandatoryCurrentUser();
    guaranteeUserIsManager(team, currentUser, "pass manager position of team to someone else");
    User newManager = getMandatoryUser(newManagerName);
    if (!teamRepository.isMemberOfTeam(teamId, newManager.getId())) {
      throw new UnregisteredMemberRoleException(newManagerName, team.getId(), "be manager");
    }
    teamRepository.setNewManagerPosition(teamId, newManager.getId());
    support.firePropertyChange(
        ChangablePropertyName.CHANGED_TEAM_MANAGER.toString(), OLD_VALUE, NEW_VALUE);
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
    support.firePropertyChange(
        ChangablePropertyName.CHANGED_TEAM_NAME.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Generates a new teamcode with the length specified in Team.CODE_LENGTH, consisting of digits
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
          user.getId(), operation, "this user is not the manager of the team");
    }
  }

  /** @throws InexistentDatabaseEntityException - should never occur. */
  private boolean userIsManager(Team team, User user) throws InexistentDatabaseEntityException {
    return team.getManagerId() == user.getId();
  }

  /**
   * Return the team specified by the teamId, which is selected by the user from the Teams list.
   *
   * @throws SQLException if a database error occurred
   * @throws InexistentTeamException if the team is was not found in the database
   */
  public Team getTeam(int teamId) throws SQLException, InexistentTeamException {
    return getMandatoryTeam(teamId);
  }

  /**
   * Gets the members of the specified team.
   *
   * @param teamId specifies the team
   * @return the list of members
   * @throws SQLException when a database error occurs
   */
  public List<User> getMembersOfTeam(int teamId) throws SQLException {
    return teamRepository.getMembersOfTeam(teamId);
  }
}
