package controller.team;

import controller.CloseablePropertyChangeListener;
import model.InexistentDatabaseEntityException;
import model.PropertyChangeObservable;
import model.team.Team;
import model.team.TeamManager;
import model.user.exceptions.*;
import model.user.User;
import model.user.UserManager;
import view.ErrorDialogFactory;
import view.team.TeamListPanel;
import view.team.TeamViewModel;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * TeamListController controls the actions related to TeamListPanel. It implements
 * PropertyChangeListener and updates the TeamListPanel whenever the user becomes a member of a new
 * team/looses their membership of a team.
 *
 * @author Bori Fazakas
 */
public class TeamListController implements CloseablePropertyChangeListener {
  TeamManager teamManager = TeamManager.getInstance();
  UserManager userManager = UserManager.getInstance();

  Frame parentFrame;
  TeamListPanel panel;

  private List<PropertyChangeObservable> propertyChangeObservables =
      List.of(teamManager, userManager);

  public TeamListController(TeamListPanel panel, Frame frame) {
    this.panel = panel;
    this.parentFrame = frame;
    this.setObservables();
  }

  public List<TeamViewModel> getUsersTeams() {
    List<TeamViewModel> teamsViewModels = new ArrayList<>();
    try {
      List<Team> usersTeams = teamManager.getTeamsOfCurrentUser();
      for (Team team : usersTeams) {
        User manager = userManager.getUserById(team.getManagerId());
        teamsViewModels.add(
            new TeamViewModel(
                team.getId(), team.getName(), Objects.requireNonNull(manager).getUsername()));
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
            .equals(TeamManager.ChangablePropertyName.CURRENT_USER_TEAM_MEMBERSHIPS.toString())
        || propertyChangeEvent
            .getPropertyName()
            .equals(TeamManager.ChangablePropertyName.CREATE_TEAM.toString())
        || propertyChangeEvent
            .getPropertyName()
            .equals(TeamManager.ChangablePropertyName.DELETE_TEAM.toString())
        || propertyChangeEvent
            .getPropertyName()
            .equals(TeamManager.ChangablePropertyName.CHANGED_TEAM_MANAGER.toString())
        || propertyChangeEvent
            .getPropertyName()
            .equals(TeamManager.ChangablePropertyName.CHANGED_TEAM_NAME.toString())
        || propertyChangeEvent.getPropertyName().equals(UserManager.UPDATE_ACCOUNT_PROPERTY)) {
      panel.updateTeams();
    }
  }

  @Override
  public List<PropertyChangeObservable> getPropertyChangeObservables() {
    return Collections.unmodifiableList(propertyChangeObservables);
  }
}
