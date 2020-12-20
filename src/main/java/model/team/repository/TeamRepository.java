package model.team.repository;

import model.InexistentDatabaseEntityException;
import model.team.Team;
import model.team.exceptions.InexistentTeamException;
import model.user.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * TeamRepository specifies the methods required from any class implementing database access for
 * team-related updates, insertions, deletions and queries.
 *
 * @author Bori Fazakas
 */
public interface TeamRepository {

  /**
   * Saves the new team in the database and returns the id of the team in the database. It also sets
   * the id of the team object.
   *
   * @param team holds the data of the new team to be saved. The id of team should be null.
   * @return the id of the newly saved team.
   * @throws SQLException if the operation could not be performed in the database.
   */
  int saveTeam(Team.SavableTeam team) throws SQLException, InexistentDatabaseEntityException;

  /**
   * Deletes the team with the specified id from the database. It also deletes all the memberships
   * related to it. // todo: delete projects of the team too
   *
   * @param teamId is the id of the team to delete.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void deleteTeam(int teamId) throws SQLException;

  /**
   * Returns the Team with the specified code from the database, if it exists, wrapped in an
   * Optional.
   *
   * @param code is the code of the searched team.
   * @return the team with the given code, or null if it doesn't exist.
   * @throws SQLException if the operation could not be performed in the database.
   */
  Optional<Team> getTeam(String code) throws SQLException;

  /**
   * Returns the Team with the specified id from the database, if it exists, wrapped in an Optional.
   *
   * @param teamId is the id of the searched team.
   * @return the team with the given code, or null if it doesn't exist.
   * @throws SQLException if the operation could not be performed in the database.
   */
  Optional<Team> getTeam(int teamId) throws SQLException;

  /**
   * Searches for and returns all the teams in which the user is a member.
   *
   * @param userId is the id of the user whose teams are listed.
   * @return the list of teams in which the user is a member.
   * @throws SQLException if the operation could not be performed in the database.
   */
  List<Team> getTeamsOfUser(int userId) throws SQLException;

  /**
   * Adds user to the members of the team with id teamID
   *
   * @param teamId is the id of the team which user wants to join. Requirement: The team must exist
   *     in the database.
   * @param userId is the new member's id. Requirement: the user must exist in the database.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void addTeamMember(int teamId, int userId) throws SQLException;

  /**
   * Removes user from the members of the team with id teamID, if the user was a member previously.
   * Otherwise nothing happens.
   *
   * @param teamId is the id of the team which user wants to leave.
   * @param userId is the member's id who leaves the team.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void removeTeamMember(int teamId, int userId) throws SQLException;

  /**
   * Chekcs whether a particular user is the member of a particular team.
   *
   * @param teamId is the id of the team.
   * @param userId is the id of the user whose membership is checked.
   * @return true if and only if the user with userId is member of team with teamId.
   * @throws SQLException if the operation could not be performed in the database.
   */
  boolean isMemberOfTeam(int teamId, int userId) throws SQLException;

  /**
   * Sets the new code for the specified team, if it exists.
   *
   * @param teamId is the id of the team to update.
   * @param newCode is the new code to set.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void setNewCode(int teamId, String newCode) throws SQLException;

  /**
   * Sets the manager of a team to be another existing user, if the team exists. The existence of
   * the user with managerId is not checked.
   *
   * @param teamId is the id of the team which gets a new manager.
   * @param managerId is the id of the new manager. REMARK: It must exist in the database.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void setNewManagerPosition(int teamId, int managerId) throws SQLException;

  /**
   * Change the name of the team with the given id to the new name.
   *
   * @param teamId is the id of the team which gets a new team.
   * @param newTeamName is the new name of the team.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void setNewName(int teamId, String newTeamName) throws SQLException;

  /**
   * Returns the members of the given team.
   *
   * @param teamId specifies the team
   * @return a list of members
   * @throws SQLException if an error occurs during processing the data in the database
   */
  List<User> getMembersOfTeam(int teamId) throws SQLException;
}
