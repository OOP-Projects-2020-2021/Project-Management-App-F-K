package controller.team;


import view.team.single_team.TeamFrame;

import javax.swing.*;

/**
 * Controller for a TeamLabel, which is displayed in the MainFrame.
 *
 * @author Bori Fazakas
 */
public class TeamLabelController {
  private final int teamId;
  private final JFrame frame;

  public TeamLabelController(int teamId, JFrame frame) {
    this.teamId = teamId;
    this.frame = frame;
  }

  public void onLabelClicked() {
    frame.setEnabled(false);
    new TeamFrame(frame, teamId);
  }
}
