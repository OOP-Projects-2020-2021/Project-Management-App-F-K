package model.team;

import model.UnauthorisedOperationException;
import model.User;
import model.team.repository.TeamRepository;
import model.team.repository.TeamRepositoryFactory;

import java.sql.SQLException;
import java.util.List;

public class TeamManager {
  private static TeamManager instance = new TeamManager();
  private TeamRepository teamRepository =
      TeamRepositoryFactory.getTeamRepository(TeamRepositoryFactory.RepositoryType.SQLITE);

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

  public void passManagerPosition(int teamId, String newManagerName)
      throws SQLException, InexistentTeamException, UnauthorisedOperationException {
    Team team = teamRepository.getTeam(teamId);
    if (team == null) {
      throw new InexistentTeamException(teamId);
    }
    User currentUser = new User(1, "", ""); // todo get from UserManager
    if (currentUser.getId().isEmpty() || team.getManagerId() != currentUser.getId().get()) {
      throw new UnauthorisedOperationException(
          currentUser.getId().get(),
          " pass manager " + "position",
          "this user is not the manager of the project");
    }
    User newManager = new User(2, newManagerName, ""); // todo get user with name UserManager
    if (newManager == null) {
      // todo throw new InexistentUserException
    }
    teamRepository.setNewManagerPosition(teamId, newManager.getId().get());
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
