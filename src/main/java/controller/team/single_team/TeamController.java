package controller.team.single_team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.team.TeamManager;
import model.team.exceptions.InexistentTeamException;
import model.user.UserManager;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;

public class TeamController extends FrameController {

  private TeamManager teamManager;

  protected int currentTeamId;
  protected boolean managerAccessGranted;

  public TeamController(JFrame frame,int currentTeamId) {
    super(frame);
    teamManager = TeamManager.getInstance();
    this.currentTeamId = currentTeamId;
    managerAccessGranted = grantManagerAccess();
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
      ErrorDialogFactory.createErrorDialog(e,frame,null);
    }
    return false;
  }
  public int getCurrentTeamId() {
    return currentTeamId;
  }

  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }

}
