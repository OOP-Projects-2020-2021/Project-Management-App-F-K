package main.java.model.team.repository.impl;

import main.java.model.User;
import main.java.model.team.Team;
import main.java.model.team.TeamManager;
import main.java.model.team.repository.TeamRepository;
import org.jetbrains.annotations.Nullable;

import javax.print.DocFlavor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteTeamRepository implements TeamRepository {
    private Connection c;

    private static final String SAVE_TEAM_STATEMENT = "INSERT INTO Team (TeamName, ManagerId, " +
            "Code) VALUES (?, ?, ?)";
    private PreparedStatement saveTeamSt;
    private static final String GET_TEAM_WITH_CODE_QUERY = "SELECT * from Team WHERE Code = ?";
    private PreparedStatement getTeamWithCodeSt;
    private static final String GET_TEAM_OF_USER_QUERY = "SELECT t.TeamId, t.TeamName, t" +
            ".ManagerId, t.Code FROM Team t JOIN MemberToTeam mt ON mt.TeamId = t.TeamId WHERE mt" +
            ".MemberId = ?";
    private PreparedStatement getTeamsOfUserSt;
    private static final String ADD_TEAM_MEMBERSHIP_QUERY = "INSERT Into MemberToTeam (MemberId, " +
            "TeamId) VALUES (?, (SELECT TeamId " +
            "FROM Team WHERE Code = ?))";
    private PreparedStatement addTeamMembershipSt;
    private static final String REMOVE_TEAM_MEMBERSHIP_QUERY = "DELETE FROM MemberToTeam WHERE  " +
            "MemberId = ? AND TeamId = (SELECT TeamId " +
            "FROM Team WHERE Code = ?)";
    private PreparedStatement removeTeamMembershipSt;


    public SqliteTeamRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:project_management_app.db");
            prepareStatements();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void prepareStatements() throws SQLException {
        saveTeamSt = c.prepareStatement(SAVE_TEAM_STATEMENT);
        getTeamWithCodeSt = c.prepareStatement(GET_TEAM_WITH_CODE_QUERY);
        getTeamsOfUserSt = c.prepareStatement(GET_TEAM_OF_USER_QUERY);
        addTeamMembershipSt = c.prepareStatement(ADD_TEAM_MEMBERSHIP_QUERY);
        removeTeamMembershipSt = c.prepareStatement(REMOVE_TEAM_MEMBERSHIP_QUERY);
    }

    @Override
    public int saveTeam(Team team) throws SQLException {
        saveTeamSt.setString(1, team.getName());
        saveTeamSt.setInt(2, team.getManagerId());
        saveTeamSt.setString(3, team.getCode());
        saveTeamSt.execute();
        Team savedTeam = getTeam(team.getCode());
        if (savedTeam != null) {
            return savedTeam.getId().get();
        } else {
            throw new SQLException("Saving team was unsuccesful");
        }
    }

    @Nullable
    @Override
    public Team getTeam(String code) throws SQLException {
        getTeamWithCodeSt.setString(1, code);
        ResultSet result = getTeamWithCodeSt.executeQuery();
        if (result.next()) {
            int id = result.getInt("TeamId");
            String teamName = result.getString("TeamName");
            int managerId = result.getInt("ManagerId");
            String teamCode = result.getString("Code");
            return new Team(id, teamName, managerId, teamCode);
        } else {
            return null;
        }
    }

    @Override
    public List<Team> getTeamsOfUser(User user) throws SQLException {
        if (user.getId().isPresent()) {
            getTeamsOfUserSt.setInt(1, user.getId().get());
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
        } else {
            throw new IllegalArgumentException("The user whose teams are queried should have an " +
                    "id");
        }
    }

    @Override
  public void deleteTeam(Team team) {}

    @Override
    public void joinTeam(User user, String teamCode) throws SQLException {
        addTeamMembershipSt.setInt(1, 5);
        addTeamMembershipSt.setString(2, teamCode);
        addTeamMembershipSt.execute();
    }

    @Override
    public void leaveTeam(User user, Team team) throws SQLException {
        removeTeamMembershipSt.setInt(1, 1);
        removeTeamMembershipSt.setString(2, team.getCode());
        removeTeamMembershipSt.execute();
    }

    @Override
    public void setNewCode(Team team) {

    }

    public static void main(String[] args) throws SQLException {
//        TeamRepository repository = new SqliteTeamRepository();
//        Team team = repository.getTeam("895621");
//        System.out.println("Team with name " + team.getName() + " and id " + team.getId());
//        repository.joinTeam(new User("Anna", "pass"), "895621");
//        repository.leaveTeam(new User("Anna", "pass"), new Team(1, "aa", 5,
//                "895621"));

        TeamManager manager = new TeamManager();
        manager.createNewTeam("DBteam");
        List<Team> teamsOfUser1 = manager.getTeamsOfCurrentUser();
        System.out.println("Teams of user 1:");
        for (Team team : teamsOfUser1) {
            System.out.println(team.getName() + " " + team.getId().get());
        }
    }
}
