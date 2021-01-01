package view.project;

import controller.project.UserProjectsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The UserProjectsFrame allows the user to view all the projects that are either assigned to them,
 * or supervised by them, independent of to which team they belong. The user can visit the project
 * if he/she double clicks on the name of the project, in which case a new frame is opened allowing
 * the user to view the project's details and change its status.
 *
 * @author Beata Keresztes
 */
public class UserProjectsFrame extends JFrame {
  private JFrame parentFrame;
  private UserProjectsController controller;
  private static final Dimension DIMENSION = new Dimension(600, 600);

  public UserProjectsFrame(JFrame parentFrame) {
    super("My projects");
    this.parentFrame = parentFrame;
    controller = new UserProjectsController(this);
    setSize(DIMENSION);
    getContentPane().setLayout(new BorderLayout());
    initPanel();
    addWindowListener(new ProjectsWindowAdapter());
    setVisible(true);
  }

  private void initPanel() {
    ProjectsPanel panel = new ProjectsPanel(this, null);
    getContentPane().add(panel, BorderLayout.CENTER);
  }

  class ProjectsWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent evt) {
      controller.onClose(parentFrame);
    }
  }
}
