package main.java.model.team.repository;

import main.java.model.User;
import main.java.model.team.Team;

import java.sql.SQLException;

public interface TeamRepository {
  void createTeam(Team team);

    Team getTeam(String code) throws SQLException;

  void deleteTeam(Team team);

    void joinTeam(User user, String teamCode) throws SQLException;

    void leaveTeam(User user, Team team) throws SQLException;

}
