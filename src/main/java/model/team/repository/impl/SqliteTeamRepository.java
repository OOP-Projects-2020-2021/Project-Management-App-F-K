package main.java.model.team.repository.impl;

import main.java.model.User;
import main.java.model.team.Team;
import main.java.model.team.repository.TeamRepository;

import java.sql.*;

public class SqliteTeamRepository implements TeamRepository {
    private Connection c;

    SqliteTeamRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:project_management_app.db");
            System.out.println("Succesful database connection");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTeam(Team team) {

    }

    @Override
    public Team getTeam(String code) throws SQLException {
        String query = "Select * from Team WHERE Code = ?";
        PreparedStatement statement = c.prepareStatement(query);
        statement.setString(1, code);
        ResultSet result = statement.executeQuery();
        result.next();
        int id = result.getInt("TeamId");
        String teamName = result.getString("TeamName");
        int managerId = result.getInt("ManagerId");
        String teamCode = result.getString("Code");
        return new Team(id, teamName, new User("", ""), teamCode);
    }

    @Override
    public void deleteTeam(Team team) {

    }

    @Override
    public void joinTeam(User user, String teamCode) throws SQLException {
        String query = "INSERT Into MemberToTeam (MemberId, TeamId) VALUES (?, (SELECT TeamId " +
                "FROM Team WHERE Code = ?))";
        PreparedStatement statement = c.prepareStatement(query);
        // todo
        statement.setInt(1, 4);
        statement.setString(2, teamCode);
        statement.execute();
    }

    @Override
    public void leaveTeam(User user, Team team) throws SQLException {
        String query = "DELETE FROM MemberToTeam WHERE MemberId = ? AND TeamId = (SELECT TeamId " +
                "FROM Team WHERE Code = ?)";
        PreparedStatement statement = c.prepareStatement(query);
        // todo
        statement.setInt(1, 3);
        statement.setString(2, team.getCode());
        statement.execute();
    }

    public static void main(String[] args) throws SQLException {
        TeamRepository repository = new SqliteTeamRepository();
        Team team = repository.getTeam("895621");
        System.out.println("Team with name " + team.getName() + " and id " + team.getId());
        repository.joinTeam(new User("Anna", "pass"), "895621");
    }
}
