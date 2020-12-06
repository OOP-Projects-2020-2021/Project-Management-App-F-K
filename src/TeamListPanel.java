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
  private TeamListController controller = new TeamListController();

  public TeamListPanel() {
    /**
     * In order to make this panel scrollable through a JScrollPane, and keep the number of
     * teams flexible at tehe same time, ModifiedFlowLayout is required instead of the
     * simpleFlowLayout.
     */
    this.setLayout(new ModifiedFlowLayout());
    List<TeamViewModel> teams = controller.getUsersTeams();
    for (TeamViewModel team : teams) {
      this.add(new TeamLabel(team));
    }
  }
}
