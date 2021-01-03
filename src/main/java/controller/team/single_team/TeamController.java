package controller.team.single_team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.team.TeamManager;
import model.team.exceptions.InexistentTeamException;
import model.user.UserManager;
import view.ErrorDialogFactory;
import view.project.CreateProjectFrame;

import javax.swing.*;
import java.sql.SQLException;

/**
 * The TeamController manages the TeamFrame. It has two static fields, the teamId, which doesn't
 * change while the frame is open, and a flag, which grants access to the manager to modify the
 * team's data. The managerAccess flag gets updated every time the manager of the currently viewed
 * team is changed.
 *
 * @author Beata Keresztes
 */
public class TeamController extends FrameController {

  protected TeamManager teamManager;
  protected UserManager userManager;

  protected static int teamId;
  protected static boolean managerAccess;

  public TeamController(JFrame frame, int teamId) {
    super(frame);
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    TeamController.teamId = teamId;
    setManagerAccess();
  }

  public int getTeamId() {
    return teamId;
  }

  public JFrame getFrame() {
    return frame;
  }

  /**
   * Checks if the current user is the manager of the team and grants access to them to modify the
   * data about the team.
   */
  protected void setManagerAccess() {
    try {
      int currentUserId = UserManager.getInstance().getCurrentUser().get().getId();
      int currentManagerId = teamManager.getTeam(teamId).getManagerId();
      managerAccess = currentUserId == currentManagerId;
    } catch (SQLException | InexistentDatabaseEntityException | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(
          e, frame, null);
    }
  }

  public boolean getManagerAccess() {
    return managerAccess;
  }

  public void onClose(JFrame parentFrame) {
    parentFrame.setEnabled(true);
  }

  public void enableProjectCreation(JFrame frame) {
    new CreateProjectFrame(teamId, frame);
    frame.setEnabled(false);
  }
}
