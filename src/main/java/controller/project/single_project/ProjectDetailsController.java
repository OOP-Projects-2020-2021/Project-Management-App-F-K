package controller.project.single_project;

import model.project.Project;
import model.project.ProjectManager;
import model.user.UserManager;

/**
 * Manages the ProjectDetails panel, being responsible for listing and updating the project's
 * details.
 *
 * @author Beata Keresztes
 */
public class ProjectDetailsController {

  private ProjectManager projectManager;
  private UserManager userManager;
  private Project project;

  public static final String[] STATUS = {"TO DO", "IN PROGRESS", "TURNED IN", "FINISHED"};

  public ProjectDetailsController(int projectId) {
    projectManager = ProjectManager.getInstance();
    userManager = UserManager.getInstance();
  }

  public boolean isSupervisor() {
    // todo
    return true;
  }

  public String getProjectTitle() {
    return project.getTitle();
  }

  public String getProjectDescription() {
    if (project.getDescription().isPresent()) {
      return project.getDescription().get();
    } else return null;
  }

  public String getProjectDeadline() {
    return String.valueOf(project.getDeadline());
  }
}
