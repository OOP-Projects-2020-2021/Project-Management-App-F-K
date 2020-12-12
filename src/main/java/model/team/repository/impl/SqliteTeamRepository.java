package main.java.model.team.repository.impl;

import main.java.model.User;
import main.java.model.team.Team;
import main.java.model.team.repository.TeamRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SqliteTeamRepository implements TeamRepository {
    private Connection c;
    private Statement statement;

    SqliteTeamRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:project_management_app.db");
            statement = c.createStatement();
            System.out.println("Succesful database connection");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTeam(Team team) {

    }

    @Override
    public void getTeam(String code) {

    }

    @Override
    public void deleteTeam(Team team) {

    }

    @Override
    public void joinTeam(User user, String teamCode) {

    }

    @Override
    public void leaveTeam(User user, Team team) {

    }

    public static void main(String[] args) {
        new SqliteTeamRepository();
    }
}
