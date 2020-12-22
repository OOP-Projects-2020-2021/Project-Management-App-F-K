package controller.project.single_project;

import controller.FrameController;
import javax.swing.*;

/**
 * Manages the ProjectFrame, which allows the user to view the details about the project,
 * mark their contribution and leave comments.
 *
 * @author Beata Keresztes
 */
public class ProjectController extends FrameController {

  private int projectId;

  public ProjectController(JFrame frame, int projectId) {
    super(frame);
    this.projectId = projectId;
  }
  public void onClose(JFrame parentFrame) {
      parentFrame.setVisible(true);
      parentFrame.setEnabled(true);
      closeFrame();
  }
  public int getProjectId() {
      return  projectId;
  }
}
