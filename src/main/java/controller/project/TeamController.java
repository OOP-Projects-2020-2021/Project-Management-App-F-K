package controller.project;

import controller.FrameController;
import model.team.TeamManager;
import view.project.TeamSettingsFrame;

import javax.swing.*;

public class TeamController extends FrameController {

    private TeamManager teamManager;

    public TeamController(JFrame frame) {
        super(frame);
        teamManager = TeamManager.getInstance();
    }

    public String getTeamName() {
        return "name";
    }
    public String getTeamCode() {
        return "code";
    }
    public String getTeamDescription() {
        return "This is a very long long long long long long long long long long long long long long description";
    }
    public String getManagerName() {
        return "manager";
    }

    /**
     * Check if the current user is the manager of this team, in which case allow them to access the settings.
     */
    public void enableTeamSettings() {
        //todo change guranteeUserisManager to public
        new TeamSettingsFrame(frame);
    }



}
