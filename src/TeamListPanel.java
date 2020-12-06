import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeamListPanel extends JPanel {
    private TeamListController controller = new TeamListController();

    public TeamListPanel() {
        this.setLayout(new ModifiedFlowLayout());
        List<TeamViewModel> teams = controller.getUsersTeams();
        for (TeamViewModel team : teams) {
            this.add(new TeamLabel(team));
        }
    }
}
