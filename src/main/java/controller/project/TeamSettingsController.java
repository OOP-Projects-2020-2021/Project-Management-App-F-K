package controller.project;

import controller.FrameController;
import model.team.TeamManager;

import javax.swing.*;

public class TeamSettingsController extends FrameController {

  private TeamManager teamManager;

  public TeamSettingsController(JFrame frame) {
    super(frame);
    teamManager = TeamManager.getInstance();
  }

  public void saveTeamName(String name) {}

  public void onClose(JFrame parentFrame) {
    parentFrame.setEnabled(true);
    parentFrame.setVisible(true);
  }
}
