package controller.team.single_team;

import controller.team.single_team.TeamController;
import model.project.ProjectManager;
import view.team.single_team.TeamProjectsPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TeamProjectsController extends TeamController implements PropertyChangeListener {

    private TeamProjectsPanel projectsPanel;
    private ProjectManager projectManager;

    public TeamProjectsController(TeamProjectsPanel projectsPanel, JFrame frame, int currentTeamId) {
        super(frame, currentTeamId);
        this.projectsPanel = projectsPanel;
        projectManager = ProjectManager.getInstance();
        projectManager.addPropertyChangeListener(this);
    }

    public void updateProjectsList() {
        //todo get projects from controller
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
       // todo if a project is deleted/created/edited(change status) from ProjectManager
    }
}
