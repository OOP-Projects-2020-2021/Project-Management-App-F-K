package controller.team.single_team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.PropertyChangeObservable;
import model.team.TeamManager;
import model.team.exceptions.InexistentTeamException;
import model.user.UserManager;
import org.jetbrains.annotations.NotNull;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLException;

public class TeamController extends FrameController implements PropertyChangeObservable {

  protected TeamManager teamManager;
  protected UserManager userManager;

  protected static int currentTeamId;
  protected static boolean managerAccess;

  public static final String MANAGER_CHANGED_PROPERTY = "Manager changed";

  protected PropertyChangeSupport support = new PropertyChangeSupport(this);
  protected final int OLD_VALUE = 1;
  protected final int NEW_VALUE = 2;

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    support.removePropertyChangeListener(pcl);
  }

  public TeamController(JFrame frame, int currentTeamId) {
    super(frame);
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    TeamController.currentTeamId = currentTeamId;
    managerAccess = grantManagerAccess();
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
  public boolean grantManagerAccess() {
    try {
      int currentUserId = UserManager.getInstance().getCurrentUser().get().getId();
      int currentManagerId = teamManager.getCurrentTeam(currentTeamId).getManagerId();
      return (currentUserId == currentManagerId);
    } catch (SQLException | InexistentDatabaseEntityException | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(
          e,
          frame,
          "An internal error occurred, the access to edit this team could not be granted.");
    }
    return false;
  }

  public boolean getManagerAccess() {
    return managerAccess;
  }

  public void setManagerAccess() {
    managerAccess = grantManagerAccess();
    support.firePropertyChange(MANAGER_CHANGED_PROPERTY,OLD_VALUE,NEW_VALUE);
  }

  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }
}
