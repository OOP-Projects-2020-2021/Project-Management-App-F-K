package controller.team.single_team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.team.TeamManager;
import model.team.exceptions.InexistentTeamException;
import model.user.UserManager;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;

/**
 * This controller manages the TeamFrame, and it contains two static fields: the id of the currently viewed team and a flag,
 * which specifies the privilege level of the user who views the team. This can be either a simple member, who can only view the data,
 * or the manager of the team, who can also edit the data about the team.
 * These fields and the method for checking the privilege level are inherited by the tabs of the TeamFrame, so that when any of these attempt
 * to change the values of the static fields, these will be changed across all instances.
 */
public class TeamController extends FrameController{

  protected TeamManager teamManager;
  protected UserManager userManager;

  protected static int currentTeamId;
  protected static boolean managerAccess;

  public TeamController(JFrame frame, int currentTeamId) {
    super(frame);
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    TeamController.currentTeamId = currentTeamId;
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
  public void setManagerAccess() {
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

  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }
}
