package controller.team;

import view.team.TeamLabel;
import view.user.SignInFrame;

/**
 * Controller for a TeamLabel, which is displayed in the MainFrame.
 *
 * @author Bori Fazakas
 */
public class TeamLabelController {
    private final int teamId;

    public TeamLabelController(int teamId) {
        this.teamId = teamId;
    }

    public void onLabelClicked() {
        // new TeamFrame();
        System.out.println("Label clicked");
        new SignInFrame();
    }
}
