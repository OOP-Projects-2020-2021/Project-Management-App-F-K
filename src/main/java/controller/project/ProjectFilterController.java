package controller.project;

import model.project.Project;
import model.project.ProjectManager;
import view.project.ProjectTable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controls the panel containing the filters and the table in which the projects are displayed.
 *
 * @author Beata Keresztes
 */
public class ProjectFilterController implements PropertyChangeListener {

  private ProjectTable projectTable;
  private ProjectManager projectManager;
  Optional<Integer> teamId;

  public ProjectFilterController(ProjectTable projectTable, Optional<Integer> teamId) {
    this.projectTable = projectTable;
    // initially all projects are listed
    unFilteredProjects();
    this.teamId = teamId;
    projectManager = ProjectManager.getInstance();
    projectManager.addPropertyChangeListener(this);
  }

  public void updateTableModel(List<Project> projectsList) {
    projectTable.clearTableModel();
    if (!isEmptyProjectList(projectsList)) {
      projectTable.fillTableModel(projectsList);
    }
  }

  private boolean isEmptyProjectList(List<Project> projects) {
    return projects == null || projects.isEmpty();
  }

  private boolean isTeamIdPresent() {
    return teamId.isPresent();
  }

  public void filterProjectsByStatus(String status) {
    // todo
    // check if team id is present
    updateTableModel(getAllProjects());
  }

  public void filterProjectsByTurnInTime(String turnInTime) {
    // todo
    updateTableModel(getAllProjects());
  }

  public void filterAssignedProjects() {
    // todo
    updateTableModel(getAllProjects());
  }

  public void filterSupervisedProjects() {
    // todo
    updateTableModel(getAllProjects());
  }

  public void unFilteredProjects() {
    // todo
    updateTableModel(getAllProjects());
  }

  private List<Project> getAllProjects() {
    // todo
    // just some dummy data
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
