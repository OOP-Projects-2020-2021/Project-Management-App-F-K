package controller.team;

import view.team.TeamLabel;
import view.user.SignInFrame;

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
        // new TeamFrame();
        System.out.println("Label clicked");
        frame.setEnabled(false);
        new SignInFrame();
    }
}
