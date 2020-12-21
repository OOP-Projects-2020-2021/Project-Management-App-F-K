package controller.project;

import controller.FrameController;
import model.project.Project;
import model.project.ProjectManager;
import model.user.User;
import model.user.UserManager;

import javax.swing.*;

/**
 * Manages the ProjectFrame, displaying and updating the data about a given project.
 *
 * @author Beata Keresztes
 */
public class ProjectController extends FrameController {

  private ProjectManager projectManager;
  private UserManager userManager;
  private int projectId;
  private Project project;

  public ProjectController(JFrame frame, int projectId) {
    super(frame);
    projectManager = ProjectManager.getInstance();
    userManager = UserManager.getInstance();
    this.projectId = projectId;
  }
  public boolean isSupervisor() {
      // todo
      return true;
  }
  public String getProjectTitle() {
      return project.getTitle();
  }
  public String getProjectDescription() {
      if(project.getDescription().isPresent()) {
          return project.getDescription().get();
      }else return null;
  }
  public String getProjectDeadline() {
      return String.valueOf(project.getDeadline());
  }

}
