package controller.project.single_project;

import controller.FrameController;
import model.project.Project;

import javax.swing.*;

/**
 * Manages the ProjectFrame, which allows the user to view the details about the project, mark their
 * contribution and leave comments.
 *
 * @author Beata Keresztes
 */
public class ProjectController extends FrameController {

  private Project project;

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
}
