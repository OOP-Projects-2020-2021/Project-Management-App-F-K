package main.java.model.team.repository.impl;

import main.java.model.User;
import main.java.model.team.Team;
import main.java.model.team.repository.TeamRepository;

import javax.print.DocFlavor;
import java.sql.*;

public class SqliteTeamRepository implements TeamRepository {
    private Connection c;

    private static final String GET_TEAM_WITH_CODE_QUERY = "Select * from Team WHERE Code = ?";
    private PreparedStatement getTeamWithCode;
    private static final String ADD_TEAM_MEMBERSHIP_QUERY = "INSERT Into MemberToTeam (MemberId, " +
            "TeamId) VALUES (?, (SELECT TeamId " +
            "FROM Team WHERE Code = ?))";
    private PreparedStatement addTeamMembership;
    private static final String REMOVE_TEAM_MEMBERSHIP_QUERY = "DELETE FROM MemberToTeam WHERE  " +
            "MemberId = ? AND TeamId = (SELECT TeamId " +
            "FROM Team WHERE Code = ?)";
    private PreparedStatement removeTeamMembership;


    SqliteTeamRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:project_management_app.db");
            prepareStatements();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatements() throws SQLException {
        getTeamWithCode = c.prepareStatement(GET_TEAM_WITH_CODE_QUERY);
        addTeamMembership = c.prepareStatement(ADD_TEAM_MEMBERSHIP_QUERY);
        removeTeamMembership = c.prepareStatement(REMOVE_TEAM_MEMBERSHIP_QUERY);
    }

    @Override
    public void createTeam(Team team) {

    }

    @Override
    public Team getTeam(String code) throws SQLException {
        getTeamWithCode.setString(1, code);
        ResultSet result = getTeamWithCode.executeQuery();
        result.next();
        int id = result.getInt("TeamId");
        String teamName = result.getString("TeamName");
        int managerId = result.getInt("ManagerId");
        String teamCode = result.getString("Code");
        return new Team(id, teamName, new User("", ""), teamCode);
    }

  @Override
  public void deleteTeam(Team team) {}

    @Override
    public void joinTeam(User user, String teamCode) throws SQLException {
        addTeamMembership.setInt(1, 5);
        addTeamMembership.setString(2, teamCode);
        addTeamMembership.execute();
    }

    @Override
    public void leaveTeam(User user, Team team) throws SQLException {
        removeTeamMembership.setInt(1, 1);
        removeTeamMembership.setString(2, team.getCode());
        removeTeamMembership.execute();
    }

    public static void main(String[] args) throws SQLException {
        TeamRepository repository = new SqliteTeamRepository();
        Team team = repository.getTeam("895621");
        System.out.println("Team with name " + team.getName() + " and id " + team.getId());
        repository.joinTeam(new User("Anna", "pass"), "895621");
        repository.leaveTeam(new User("Anna", "pass"), new Team(1, "aa", new User("", ""),
                "895621"));
    }
}
