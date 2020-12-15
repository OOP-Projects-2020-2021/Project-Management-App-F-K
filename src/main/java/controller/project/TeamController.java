package controller.project;

import controller.FrameController;

import javax.swing.*;

public class TeamController extends FrameController {

    public TeamController(JFrame frame) {
        super(frame);
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
}
