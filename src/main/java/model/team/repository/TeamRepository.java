package main.java.model.team.repository;

import main.java.model.User;
import main.java.model.team.Team;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.List;

public interface TeamRepository {
    /**
     * Saves the new team in the database and returns the id of the team in the database.
     */
    int saveTeam(Team team) throws SQLException;

    @Nullable
    Team getTeam(String code) throws SQLException;

    List<Team> getTeamsOfUser(User user) throws SQLException;

    void deleteTeam(Team team);

    void joinTeam(User user, String teamCode) throws SQLException;

    void leaveTeam(User user, Team team) throws SQLException;

    void setNewCode(Team team);

}
