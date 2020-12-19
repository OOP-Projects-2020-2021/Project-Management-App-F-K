package controller.team.single_team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.team.TeamManager;
import model.team.exceptions.InexistentTeamException;
import model.user.UserManager;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

/**
 * This controller manages the TeamFrame.
 */
public class TeamController extends FrameController implements PropertyChangeListener {

  protected TeamManager teamManager;
  protected UserManager userManager;

  protected static int currentTeamId;
  protected static boolean managerAccess;

  public TeamController(JFrame frame, int currentTeamId) {
    super(frame);
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    TeamController.currentTeamId = currentTeamId;
    teamManager.addPropertyChangeListener(this);
    setManagerAccess();
  }

  public int getCurrentTeamId() {
    return currentTeamId;
  }

  public JFrame getFrame() {
    return frame;
  }
  /**
   * Checks if the current user is the manager of the team and grants access to them to modify data
   * about the team.
   */
  protected void setManagerAccess() {
    try {
      int currentUserId = UserManager.getInstance().getCurrentUser().get().getId();
      int currentManagerId = teamManager.getCurrentTeam(currentTeamId).getManagerId();
      managerAccess = currentUserId == currentManagerId;
    } catch (SQLException | InexistentDatabaseEntityException | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(
              e,
              frame,
              "An internal error occurred, the access to edit this team could not be granted.");
    }
  }
  public boolean getManagerAccess() {
    return managerAccess;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(TeamManager.ChangablePropertyName.CHANGED_TEAM_MANAGER.toString())) {
      setManagerAccess();
    }
  }

  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }
}
