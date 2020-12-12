package main.java.model.team;

import main.java.model.team.repository.TeamRepository;
import main.java.model.team.repository.impl.SqliteTeamRepository;

import java.sql.SQLException;

public class TeamManager {
    private static TeamManager instance = new TeamManager();
    private TeamRepository teamRepository = new SqliteTeamRepository();

    public static TeamManager getInstance() {
        return instance;
    }

    public void createNewTeam(String name) {
        // todo: get managerId from UserManager
        int managerId = 2;
        try {
            teamRepository.saveTeam(new Team(name, managerId, generateTeamCode()));
        } catch (SQLException e) {
            // todo: send message to user
        }
    }

    private String generateTeamCode() throws SQLException {
        boolean found = false;
        String code = null;
        while (!found) {
            int randomNumber = (int) (Math.random() * (int) Math.pow(10, Team.CODE_LENGTH) + 1);
            code = Integer.toString(randomNumber);
            if (teamRepository.getTeam(code) == null) {
                found = true;
            }
        }
        return code;
    }
}
