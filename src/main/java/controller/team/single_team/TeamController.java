package controller.team.single_team;

import controller.FrameController;
import model.team.TeamManager;
import model.user.UserManager;

import javax.swing.*;

/**
 * This controller manages the TeamFrame.
 */
public class TeamController extends FrameController{

  protected TeamManager teamManager;
  protected UserManager userManager;

  protected static int currentTeamId;

  public TeamController(JFrame frame, int currentTeamId) {
    super(frame);
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    TeamController.currentTeamId = currentTeamId;
  }

  public int getCurrentTeamId() {
    return currentTeamId;
  }

  public JFrame getFrame() {
    return frame;
  }

  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }
}
