package main.java.model.team;

import main.java.model.User;
import main.java.model.team.repository.TeamRepository;
import main.java.model.team.repository.impl.SqliteTeamRepository;

import java.sql.SQLException;
import java.util.List;

public class TeamManager {
  private static TeamManager instance = new TeamManager();
  private TeamRepository teamRepository = new SqliteTeamRepository();

  private TeamManager() {}

  public static TeamManager getInstance() {
    return instance;
  }

  public void createNewTeam(String name) throws SQLException {
    // todo: get managerId from UserManager
    int managerId = 2;
    teamRepository.saveTeam(new Team(name, managerId, generateTeamCode()));
  }

  public List<Team> getTeamsOfCurrentUser() throws SQLException {
    User currentUser = new User(1, "", ""); // todo get from UserManager
    return teamRepository.getTeamsOfUser(currentUser);
  }

  public String regenerateTeamCode(int teamId) throws SQLException {
    String newCode = generateTeamCode();
    teamRepository.setNewCode(teamId, newCode);
    return newCode;
  }

  public void joinTeam(String code) throws SQLException, InexistentTeamException {
    Team team = teamRepository.getTeam(code);
    User currentUser = new User(2, "", ""); // todo get from UserManager
    if (team == null) {
      throw new InexistentTeamException(code);
    } else {
      teamRepository.joinTeam(currentUser.getId().get(), team.getId().get());
    }
  }

  public void leaveTeam(int teamId) throws SQLException {
    User currentUser = new User(1, "", ""); // todo get from UserManager
    teamRepository.leaveTeam(currentUser.getId().get(), teamId);
  }

  private String generateTeamCode() throws SQLException {
    boolean found = false;
    String code = null;
    while (!found) {
      int randomNumber = (int) (Math.random() * (int) (Math.pow(10, Team.CODE_LENGTH) - 1) + 1);
      code = String.format("%06d", randomNumber);
      if (teamRepository.getTeam(code) == null) {
        found = true;
      }
    }
    return code;
  }
}
