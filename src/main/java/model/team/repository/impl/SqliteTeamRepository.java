package model.team.repository.impl;

import model.InexistentDatabaseEntityException;
import model.database.Repository;
import model.team.Team;
import model.team.repository.TeamRepository;
import model.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SqliteTeamRepository is an implementation of TeamRepository which provides database access to an
 * sqlite database holding team-related data.
 *
 * @author Bori Fazakas
 */
public class SqliteTeamRepository extends Repository implements TeamRepository {
  protected static SqliteTeamRepository instance;

  private SqliteTeamRepository() {}

  /** Implemented with the singleton pattern. */
  public static SqliteTeamRepository getInstance() {
    if (instance == null) {
      instance = new SqliteTeamRepository();
    }
    return instance;
  }

  // Save a new team.
  private static final String SAVE_TEAM_STATEMENT =
      "INSERT INTO Team (TeamName, ManagerId, " + "Code) VALUES (?, ?, ?)";
  private PreparedStatement saveTeamSt;

  // Delete a team.
  private static final String DELETE_TEAM_STATEMENT = "DELETE from Team WHERE TeamId = ?";
  private PreparedStatement deleteTeamSt;

  // Get a team with a given id.
  private static final String GET_TEAM_WITH_CODE_QUERY = "SELECT * from Team WHERE Code = ?";
  private PreparedStatement getTeamWithCodeSt;

  // Get a team with a given code.
  private static final String GET_TEAM_WITH_ID_QUERY = "SELECT * from Team WHERE TeamId = ?";
  private PreparedStatement getTeamWithIdSt;

  // Get teams of a given user.
  private static final String GET_TEAMS_OF_USER_QUERY =
      "SELECT t.TeamId, t.TeamName, t"
          + ".ManagerId, t.Code FROM Team t JOIN MemberToTeam mt ON mt.TeamId = t.TeamId WHERE mt"
          + ".MemberId = ?";
  private PreparedStatement getTeamsOfUserSt;

  // Set a new code for a team.
  private static final String SET_NEW_TEAMCODE_STATEMENT =
      "UPDATE Team SET Code = ? WHERE " + "TeamId = ?";
  private PreparedStatement setNewCodeSt;

  // Add a new member to a team.
  private static final String ADD_TEAM_MEMBERSHIP_STATEMENT =
      "INSERT Into MemberToTeam (MemberId, " + "TeamId) VALUES (?, ?)";
  private PreparedStatement addTeamMembershipSt;

  // Remove a member from a team.
  private static final String REMOVE_TEAM_MEMBERSHIP_STATEMENT =
      "DELETE FROM MemberToTeam WHERE  " + "MemberId = ? AND TeamId = ?";
  private PreparedStatement removeTeamMembershipSt;

  // Remove all members of a team.
  private static final String REMOVE_ALL_TEAM_MEMBERS_STATEMENT =
      "DELETE FROM MemberToTeam WHERE TeamId = ?";
  private PreparedStatement removeAllTeamMembersSt;

  // Set new manager for team.
  private static final String SET_MANAGER_STATEMENT =
      "UPDATE Team SET ManagerId = ? WHERE TeamId = ?";
  private PreparedStatement setManagerSt;

  // Set new name for team.
  private static final String SET_NAME_STATEMENT = "UPDATE Team SET TeamName = ? WHERE TeamId = ?";
  private PreparedStatement setNameSt;

  // Check user's membership in team.
  private static final String IS_MEMBER_QUERY =
      "SELECT * FROM MemberToTeam WHERE TeamId = ? AND MemberId = ?";
  private PreparedStatement isMemberSt;

  // Get all members of a team.
  private static final String GET_TEAM_MEMBERS_QUERY =
      "SELECT * FROM User AS u JOIN MemberToTeam AS m ON u.UserId = m.MemberId WHERE"
          + " m.TeamId = ?";
  private PreparedStatement getTeamMembersSt;

  /**
   * The statements are prepared only once, when the reposiroy is constructed, because this way sql
   * parsing and creating a query plan is created only once, so query execution is faster.
   */
  protected void prepareStatements() throws SQLException {
    saveTeamSt = c.prepareStatement(SAVE_TEAM_STATEMENT);
    deleteTeamSt = c.prepareStatement(DELETE_TEAM_STATEMENT);
    getTeamWithCodeSt = c.prepareStatement(GET_TEAM_WITH_CODE_QUERY);
    getTeamWithIdSt = c.prepareStatement(GET_TEAM_WITH_ID_QUERY);
    getTeamsOfUserSt = c.prepareStatement(GET_TEAMS_OF_USER_QUERY);
    setNewCodeSt = c.prepareStatement(SET_NEW_TEAMCODE_STATEMENT);
    addTeamMembershipSt = c.prepareStatement(ADD_TEAM_MEMBERSHIP_STATEMENT);
    removeTeamMembershipSt = c.prepareStatement(REMOVE_TEAM_MEMBERSHIP_STATEMENT);
    removeAllTeamMembersSt = c.prepareStatement(REMOVE_ALL_TEAM_MEMBERS_STATEMENT);
    setManagerSt = c.prepareStatement(SET_MANAGER_STATEMENT);
    setNameSt = c.prepareStatement(SET_NAME_STATEMENT);
    isMemberSt = c.prepareStatement(IS_MEMBER_QUERY);
    getTeamMembersSt = c.prepareStatement(GET_TEAM_MEMBERS_QUERY);
  }

  @Override
  public int saveTeam(Team.SavableTeam team)
      throws SQLException, InexistentDatabaseEntityException {
    saveTeamSt.setString(1, team.getName());
    saveTeamSt.setInt(2, team.getManagerId());
    saveTeamSt.setString(3, team.getCode());
    saveTeamSt.execute();
    Optional<Team> savedTeam = getTeam(team.getCode());
    if (savedTeam.isPresent()) {
      return savedTeam.get().getId();
    } else {
      throw new SQLException("Saving team was unsuccessful");
    }
  }

  @Override
  public Optional<Team> getTeam(int teamId) throws SQLException {
    getTeamWithIdSt.setInt(1, teamId);
    ResultSet result = getTeamWithIdSt.executeQuery();
    if (result.next()) {
      int id = result.getInt("TeamId");
      String teamName = result.getString("TeamName");
      int managerId = result.getInt("ManagerId");
      String teamCode = result.getString("Code");
      return Optional.of(new Team(id, teamName, managerId, teamCode));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Team> getTeam(String code) throws SQLException {
    getTeamWithCodeSt.setString(1, code);
    ResultSet result = getTeamWithCodeSt.executeQuery();
    if (result.next()) {
      int id = result.getInt("TeamId");
      String teamName = result.getString("TeamName");
      int managerId = result.getInt("ManagerId");
      String teamCode = result.getString("Code");
      return Optional.of(new Team(id, teamName, managerId, teamCode));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public List<Team> getTeamsOfUser(int userId) throws SQLException {
    getTeamsOfUserSt.setInt(1, userId);
    ResultSet result = getTeamsOfUserSt.executeQuery();
    List<Team> usersTeams = new ArrayList<>();
    while (result.next()) {
      int id = result.getInt("TeamId");
      String teamName = result.getString("TeamName");
      int managerId = result.getInt("ManagerId");
      String teamCode = result.getString("Code");
      usersTeams.add(new Team(id, teamName, managerId, teamCode));
    }
    return usersTeams;
  }

  @Override
  public void deleteTeam(int teamId) throws SQLException {
    deleteAllMembersOfTeam(teamId);
    deleteTeamSt.setInt(1, teamId);
    deleteTeamSt.execute();
  }

  private void deleteAllMembersOfTeam(int teamId) throws SQLException {
    removeAllTeamMembersSt.setInt(1, teamId);
    removeAllTeamMembersSt.execute();
  }

  @Override
  public void addTeamMember(int teamId, int userId) throws SQLException {
    addTeamMembershipSt.setInt(1, userId);
    addTeamMembershipSt.setInt(2, teamId);
    addTeamMembershipSt.execute();
  }

  @Override
  public void removeTeamMember(int teamId, int userId) throws SQLException {
    removeTeamMembershipSt.setInt(1, userId);
    removeTeamMembershipSt.setInt(2, teamId);
    removeTeamMembershipSt.execute();
  }

  @Override
  public boolean isMemberOfTeam(int teamId, int userId) throws SQLException {
    isMemberSt.setInt(1, teamId);
    isMemberSt.setInt(2, userId);
    ResultSet result = isMemberSt.executeQuery();
    return result.next();
  }

  @Override
  public void setNewCode(int teamId, String newCode) throws SQLException {
    setNewCodeSt.setString(1, newCode);
    setNewCodeSt.setInt(2, teamId);
    setNewCodeSt.execute();
  }

  @Override
  public void setNewManagerPosition(int teamId, int managerId) throws SQLException {
    setManagerSt.setInt(1, managerId);
    setManagerSt.setInt(2, teamId);
    setManagerSt.execute();
  }

  @Override
  public void setNewName(int teamId, String newTeamName) throws SQLException {
    setNameSt.setString(1, newTeamName);
    setNameSt.setInt(2, teamId);
    setNameSt.execute();
  }

  @Override
  public List<User> getMembersOfTeam(int teamId) throws SQLException {
    List<User> members = new ArrayList<>();
    getTeamMembersSt.setInt(1, teamId);
    ResultSet resultSet = getTeamMembersSt.executeQuery();
    while (resultSet.next()) {
      User member =
          new User(
              resultSet.getInt("UserId"),
              resultSet.getString("UserName"),
              resultSet.getString("Password"));
      members.add(member);
    }
    return members;
  }
}
