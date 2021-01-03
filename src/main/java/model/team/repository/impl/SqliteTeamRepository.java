package model.team.repository.impl;

import model.InexistentDatabaseEntityException;
import model.database.SqliteDatabaseConnectionFactory;
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
public class SqliteTeamRepository implements TeamRepository {
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

  // Delete a team.
  private static final String DELETE_TEAM_STATEMENT = "DELETE from Team WHERE TeamId = ?";

  // Get a team with a given id.
  private static final String GET_TEAM_WITH_CODE_QUERY = "SELECT * from Team WHERE Code = ?";

  // Get a team with a given code.
  private static final String GET_TEAM_WITH_ID_QUERY = "SELECT * from Team WHERE TeamId = ?";

  // Get teams of a given user.
  private static final String GET_TEAMS_OF_USER_QUERY =
      "SELECT t.TeamId, t.TeamName, t"
          + ".ManagerId, t.Code FROM Team t JOIN MemberToTeam mt ON mt.TeamId = t.TeamId WHERE mt"
          + ".MemberId = ?";

  // Set a new code for a team.
  private static final String SET_NEW_TEAMCODE_STATEMENT =
      "UPDATE Team SET Code = ? WHERE " + "TeamId = ?";

  // Add a new member to a team.
  private static final String ADD_TEAM_MEMBERSHIP_STATEMENT =
      "INSERT Into MemberToTeam (MemberId, " + "TeamId) VALUES (?, ?)";

  // Remove a member from a team.
  private static final String REMOVE_TEAM_MEMBERSHIP_STATEMENT =
      "DELETE FROM MemberToTeam WHERE  " + "MemberId = ? AND TeamId = ?";

  // Remove all members of a team.
  private static final String REMOVE_ALL_TEAM_MEMBERS_STATEMENT =
      "DELETE FROM MemberToTeam WHERE TeamId = ?";

  // Set new manager for team.
  private static final String SET_MANAGER_STATEMENT =
      "UPDATE Team SET ManagerId = ? WHERE TeamId = ?";

  // Set new name for team.
  private static final String SET_NAME_STATEMENT = "UPDATE Team SET TeamName = ? WHERE TeamId = ?";

  // Check user's membership in team.
  private static final String IS_MEMBER_QUERY =
      "SELECT * FROM MemberToTeam WHERE TeamId = ? AND MemberId = ?";

  // Get all members of a team.
  private static final String GET_TEAM_MEMBERS_QUERY =
      "SELECT * FROM User AS u JOIN MemberToTeam AS m ON u.UserId = m.MemberId WHERE"
          + " m.TeamId = ?";

  @Override
  public int saveTeam(Team.SavableTeam team)
      throws SQLException, InexistentDatabaseEntityException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement saveTeamSt = c.prepareStatement(SAVE_TEAM_STATEMENT); ) {
      saveTeamSt.setString(1, team.getName());
      saveTeamSt.setInt(2, team.getManagerId());
      saveTeamSt.setString(3, team.getCode());
      saveTeamSt.executeUpdate();
      Optional<Team> savedTeam = getTeam(team.getCode());
      if (savedTeam.isPresent()) {
        return savedTeam.get().getId();
      } else {
        throw new SQLException("Saving team was unsuccessful");
      }
    }
  }

  @Override
  public Optional<Team> getTeam(int teamId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getTeamWithIdSt = c.prepareStatement(GET_TEAM_WITH_ID_QUERY); ) {
      getTeamWithIdSt.setInt(1, teamId);
      try (ResultSet result = getTeamWithIdSt.executeQuery()) {
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
    }
  }

  @Override
  public Optional<Team> getTeam(String code) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getTeamWithCodeSt = c.prepareStatement(GET_TEAM_WITH_CODE_QUERY)) {
      getTeamWithCodeSt.setString(1, code);
      try (ResultSet result = getTeamWithCodeSt.executeQuery()) {
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
    }
  }

  @Override
  public List<Team> getTeamsOfUser(int userId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getTeamsOfUserSt = c.prepareStatement(GET_TEAMS_OF_USER_QUERY)) {
      getTeamsOfUserSt.setInt(1, userId);
      try (ResultSet result = getTeamsOfUserSt.executeQuery()) {
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
    }
  }

  @Override
  public void deleteTeam(int teamId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement deleteTeamSt = c.prepareStatement(DELETE_TEAM_STATEMENT)) {
      deleteTeamSt.setInt(1, teamId);
      deleteTeamSt.executeUpdate();
    }
  }

  @Override
  public void deleteAllMembersOfTeam(int teamId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement removeAllTeamMembersSt =
            c.prepareStatement(REMOVE_ALL_TEAM_MEMBERS_STATEMENT)) {
      removeAllTeamMembersSt.setInt(1, teamId);
      removeAllTeamMembersSt.execute();
    }
  }

  @Override
  public void addTeamMember(int teamId, int userId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement addTeamMembershipSt = c.prepareStatement(ADD_TEAM_MEMBERSHIP_STATEMENT)) {
      addTeamMembershipSt.setInt(1, userId);
      addTeamMembershipSt.setInt(2, teamId);
      addTeamMembershipSt.executeUpdate();
    }
  }

  @Override
  public void removeTeamMember(int teamId, int userId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement removeTeamMembershipSt =
            c.prepareStatement(REMOVE_TEAM_MEMBERSHIP_STATEMENT)) {
      removeTeamMembershipSt.setInt(1, userId);
      removeTeamMembershipSt.setInt(2, teamId);
      removeTeamMembershipSt.executeUpdate();
    }
  }

  @Override
  public boolean isMemberOfTeam(int teamId, int userId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement isMemberSt = c.prepareStatement(IS_MEMBER_QUERY)) {
      isMemberSt.setInt(1, teamId);
      isMemberSt.setInt(2, userId);
      try (ResultSet result = isMemberSt.executeQuery()) {
        return result.next();
      }
    }
  }

  @Override
  public void setNewCode(int teamId, String newCode) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement setNewCodeSt = c.prepareStatement(SET_NEW_TEAMCODE_STATEMENT)) {
      setNewCodeSt.setString(1, newCode);
      setNewCodeSt.setInt(2, teamId);
      setNewCodeSt.executeUpdate();
    }
  }

  @Override
  public void setNewManagerPosition(int teamId, int managerId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement setManagerSt = c.prepareStatement(SET_MANAGER_STATEMENT)) {
      setManagerSt.setInt(1, managerId);
      setManagerSt.setInt(2, teamId);
      setManagerSt.executeUpdate();
    }
  }

  @Override
  public void setNewName(int teamId, String newTeamName) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement setNameSt = c.prepareStatement(SET_NAME_STATEMENT)) {
      setNameSt.setString(1, newTeamName);
      setNameSt.setInt(2, teamId);
      setNameSt.executeUpdate();
    }
  }

  @Override
  public List<User> getMembersOfTeam(int teamId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getTeamMembersSt = c.prepareStatement(GET_TEAM_MEMBERS_QUERY)) {
      List<User> members = new ArrayList<>();
      getTeamMembersSt.setInt(1, teamId);
      try (ResultSet resultSet = getTeamMembersSt.executeQuery()) {
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
  }
}
