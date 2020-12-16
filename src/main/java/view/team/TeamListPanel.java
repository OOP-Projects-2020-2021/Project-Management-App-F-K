package view.team;

import controller.team.TeamListController;
import view.ModifiedFlowLayout;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * TeamListPanel is a panel which displays a label (icon) for each team in which the currently
 * logged in user is a member.
 *
 * @author Bori Fazakas
 */
public class TeamListPanel extends JPanel {
  private TeamListController controller;

  public TeamListPanel(Frame frame) {
    /**
     * In order to make this panel scrollable through a JScrollPane, and keep the number of teams
     * flexible at the same time, ModifiedFlowLayout is required instead of the simpleFlowLayout.
     */
    this.setLayout(new ModifiedFlowLayout());
    controller = new TeamListController(frame);
    List<TeamViewModel> teams = controller.getUsersTeams();
    for (TeamViewModel team : teams) {
      this.add(new TeamLabel(team));
    }
  }
}
