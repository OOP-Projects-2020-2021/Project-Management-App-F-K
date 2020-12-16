package controller.team;

import model.InexistentDatabaseEntityException;
import model.team.Team;
import model.team.TeamManager;
import model.user.NoSignedInUserException;
import model.user.User;
import model.user.UserManager;
import view.ErrorDialogFactory;
import view.team.TeamListPanel;
import view.team.TeamViewModel;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * TeamListController controls the actions related to TeamListPanel.
 *
 * @author Bori Fazakas
 */
public class TeamListController implements PropertyChangeListener {
  TeamManager teamManager = TeamManager.getInstance();
  UserManager userManager = UserManager.getInstance();

  Frame parentFrame;
  TeamListPanel panel;

  public TeamListController(TeamListPanel panel, Frame frame) {
    this.panel = panel;
    this.parentFrame = frame;
    teamManager.addPropertyChangeListener(this);
  }

  public List<TeamViewModel> getUsersTeams() {
    List<TeamViewModel> teamsViewModels = new ArrayList<>();
    try {
      List<Team> usersTeams = teamManager.getTeamsOfCurrentUser();
      for (Team team : usersTeams) {
        User manager = userManager.getUserById(team.getManagerId());
        teamsViewModels.add(
            new TeamViewModel(
                team.getName(), team.getCode(), Objects.requireNonNull(manager).getUsername()));
      }
    } catch (SQLException | NoSignedInUserException | InexistentDatabaseEntityException e) {
      e.printStackTrace();
      ErrorDialogFactory.createErrorDialog(e, parentFrame, "Your teams cannot be displayed.");
    } catch (NullPointerException e) {
      e.printStackTrace();
      ErrorDialogFactory.createErrorDialog(
          new SQLException(), parentFrame, "Your teams cannot be displayed" + ".");
      // NullPointerException occurs when manager is not found. This can be handled as if it were
      // a database exception.
    }
    return teamsViewModels;
  }

  @Override
  public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
    if (propertyChangeEvent
        .getPropertyName()
        .equals(TeamManager.ChangablePropertyName.CURRENT_USER_TEAM_MEMBERSHIPS.toString())) {
      panel.updateTeams();
    }
  }
}
