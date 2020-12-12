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

    private static final String SET_NEW_TEAMCODE_STATEMENT = "UPDATE team SET Code = ? WHERE " +
            "TeamId = ?";
    private PreparedStatement setNewCodeSt;

    private static final String ADD_TEAM_MEMBERSHIP_QUERY = "INSERT Into MemberToTeam (MemberId, " +
            "TeamId) VALUES (?, ?)";
    private PreparedStatement addTeamMembershipSt;

    private static final String REMOVE_TEAM_MEMBERSHIP_QUERY = "DELETE FROM MemberToTeam WHERE  " +
            "MemberId = ? AND TeamId = ?";
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
        setNewCodeSt = c.prepareStatement(SET_NEW_TEAMCODE_STATEMENT);
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
    // todo bug: team not found despite existing
    public Team getTeam(String code) throws SQLException {
        getTeamWithCodeSt.setString(1, code);
        ResultSet result = getTeamWithCodeSt.executeQuery();
        if (result.next() || true) {
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
    public void joinTeam(int userId, int teamId) throws SQLException {
        addTeamMembershipSt.setInt(1, userId);
        addTeamMembershipSt.setInt(2, teamId);
        addTeamMembershipSt.execute();
    }

    @Override
    public void leaveTeam(int userId, int teamId) throws SQLException {
        removeTeamMembershipSt.setInt(1, userId);
        removeTeamMembershipSt.setInt(2, teamId);
        removeTeamMembershipSt.execute();
    }

    @Override
    public void setNewCode(int teamId, String newCode) throws SQLException {
        setNewCodeSt.setString(1, newCode);
        setNewCodeSt.setInt(2, teamId);
        setNewCodeSt.execute();
    }

    public static void main(String[] args) throws SQLException {
//        TeamRepository repository = new SqliteTeamRepository();
//        Team team = repository.getTeam("895621");
//        System.out.println("Team with name " + team.getName() + " and id " + team.getId());
//        repository.joinTeam(new User("Anna", "pass"), "895621");
//        repository.leaveTeam(new User("Anna", "pass"), new Team(1, "aa", 5,
//                "895621"));

        TeamManager manager = TeamManager.getInstance();
//        // manager.createNewTeam("DBteam");
//        List<Team> teamsOfUser1 = manager.getTeamsOfCurrentUser();
//        System.out.println("Teams of user 1:");
//        for (Team team : teamsOfUser1) {
//            System.out.println(team.getName() + " " + team.getId().get());
//        }
//        manager.regenerateTeamCode(2);
        manager.joinTeam("488524");
        manager.leaveTeam(3);
    }
}
