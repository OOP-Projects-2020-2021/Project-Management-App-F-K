package controller.team;

import model.InexistentDatabaseEntityException;
import model.team.Team;
import model.team.TeamManager;
import model.user.NoSignedInUserException;
import model.user.User;
import model.user.UserManager;
import view.ErrorDialogFactory;
import view.team.TeamViewModel;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * TeamListController controls the actions related to TeamListPanel.
 *
 * @author Bori Fazakas
 */
public class TeamListController {
  TeamManager teamManager = TeamManager.getInstance();
  UserManager userManager = UserManager.getInstance();

  public List<TeamViewModel> getUsersTeams() {
    List<TeamViewModel> teamsViewModels = new ArrayList<>();
    try {
      List<Team> usersTeams = teamManager.getTeamsOfCurrentUser();
      for (Team team : usersTeams) {
        User manager = userManager.getUserById(team.getManagerId());
        teamsViewModels.add(
            new TeamViewModel(team.getName(), team.getCode(), manager.getUsername()));
      }
    } catch (SQLException | NoSignedInUserException | InexistentDatabaseEntityException e) {
      e.printStackTrace();
      ErrorDialogFactory.createErrorDialog(e, null, "Your teams cannot be displayed.");
    } catch (NullPointerException e) {
      // todo
    }
    return teamsViewModels;
  }
}
