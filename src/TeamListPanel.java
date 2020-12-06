import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TeamListPanel extends JPanel {
    private TeamListController controller = new TeamListController();

    public TeamListPanel() {
        List<TeamViewModel> teams = controller.getUsersTeams();
        for (TeamViewModel team : teams) {
            this.add(new TeamLabel(team));
        }
    }

//    @Override
//    public Dimension getPreferredSize() {
//        int col, row, h, w;
//        Dimension d = null;
//        w = (int) sb.getViewport().getSize().getWidth();
//        col = w / chartwidth;
//        if (col > 0) {
//            row = (int)(Math.ceil(24.0/col));
//            h = row * chartwidth;
//            d = new Dimension(w, h);
//        }
//        else{
//            d = super.getPreferredSize();
//        }
//        return d;
//    }

}
