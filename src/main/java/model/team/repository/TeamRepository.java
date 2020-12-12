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

    /**
     * Sets the new code for the specified team.
     *
     * @param teamId is the id of the team to update. requirement: The team must exist in the
     *               database.
     * @param newCode is the new code to set.
     */
    void setNewCode(int teamId, String newCode) throws SQLException;

}
