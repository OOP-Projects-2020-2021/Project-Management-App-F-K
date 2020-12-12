package model.team.repository;

import model.User;
import model.team.Team;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

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
  int saveTeam(Team team) throws SQLException;

  /**
   * Returns the Team with the specified code from the database, if it exists. Othwerwise it returns
   * null.
   *
   * @param code is the code of the searched team.
   * @return the team with the given code, or null if it doesn't exist.
   * @throws SQLException if the operation could not be performed in the database.
   */
  @Nullable
  Team getTeam(String code) throws SQLException;

  /**
   * Returns the Team with the specified id from the database, if it exists. Othwerwise it returns
   * null.
   *
   * @param teamId is the id of the searched team.
   * @return the team with the given code, or null if it doesn't exist.
   * @throws SQLException if the operation could not be performed in the database.
   */
  @Nullable
  Team getTeam(int teamId) throws SQLException;

  /**
   * Searches for and returns all the teams in which the user is a member.
   *
   * @param user is the member whose teams are searched.
   * @return the list of teams in which the user is a member.
   * @throws SQLException if the operation could not be performed in the database.
   */
  List<Team> getTeamsOfUser(User user) throws SQLException;

  void deleteTeam(Team team);

  /**
   * Adds user to the members of the team with id teamID
   *
   * @param teamId is the id of the team which user wants to join. Requirement: The team must exist
   *     in the database.
   * @param userId is the new member's id. Requirement: the user must exist in the database.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void joinTeam(int userId, int teamId) throws SQLException;

  /**
   * Removes user from the members of the team with id teamID, if the user was a member previously.
   * Otherwise nothing happens.
   *
   * @param teamId is the id of the team which user wants to leave.
   * @param userId is the member's id who leaves the team.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void leaveTeam(int userId, int teamId) throws SQLException;

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
}
