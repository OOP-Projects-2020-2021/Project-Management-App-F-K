package controller.project.single_project;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.exceptions.InexistentProjectException;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;

/**
 * The ProjectController manages the ProjectFrame, which allows the user to view the details about
 * the project, mark their contribution and leave comments, and it is responsible for redirecting
 * the user to the parentFrame upon closing the ProjectFrame.
 *
 * @author Beata Keresztes, Bori Fazakas
 */
public class ProjectController extends FrameController {

  protected Project project;
  protected ProjectManager projectManager = ProjectManager.getInstance();

  public ProjectController(JFrame frame, Project project) {
    super(frame);
    this.project = project;
  }

  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }

  public Project getProject() {
    return project;
  }

  protected void setProject() {
    try {
      project = projectManager.getProjectById(project.getId());
    } catch (InexistentProjectException | InexistentDatabaseEntityException | SQLException e) {
      ErrorDialogFactory.createErrorDialog(e, null, "The project could not be updated.");
    }
  }
}
