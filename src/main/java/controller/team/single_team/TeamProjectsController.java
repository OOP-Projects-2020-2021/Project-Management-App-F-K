package controller.team.single_team;

import model.project.Project;
import model.project.ProjectManager;
import view.team.single_team.TeamProjectsPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller manages the TeamProjectsPanel, and it is responsible for displaying and filtering
 * the list of projects.
 *
 * @author Beata Keresztes
 */
public class TeamProjectsController extends TeamController implements PropertyChangeListener {

  private TeamProjectsPanel projectsPanel;
  private ProjectManager projectManager;

  public TeamProjectsController(TeamProjectsPanel projectsPanel, JFrame frame, int currentTeamId) {
    super(frame, currentTeamId);
    this.projectsPanel = projectsPanel;
    projectManager = ProjectManager.getInstance();
    projectManager.addPropertyChangeListener(this);
  }

  public boolean isEmptyProjectList(List<Project> projects) {
    return projects == null || projects.isEmpty();
  }

  public List<Project> getProjectsByStatus(String status) {
    // todo
    return null;
  }

  public List<Project> getProjectsByDeadline(String deadline) {
    // todo
    return null;
  }

  public List<Project> getAssignedProjects() {
    // todo
    return null;
  }

  public List<Project> getSupervisedProjects() {
    // todo
    return null;
  }

  public List<Project> getAllProjects() {
    // todo
    List<Project> projects = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      projects.add(
          new Project(1, "Project" + i, 52, LocalDate.now(), Project.ProjectStatus.TO_DO, 2, 12));
    }
    return projects;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    // todo if a project is deleted/created/edited(change status) from ProjectManager
  }
}
