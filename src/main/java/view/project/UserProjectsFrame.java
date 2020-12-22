package view.project;

import controller.project.UserProjectsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
    ProjectsPanel panel = new ProjectsPanel(this, -1);
    getContentPane().add(panel, BorderLayout.CENTER);
  }

  class ProjectsWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent evt) {
      controller.onClose(parentFrame);
    }
  }
}
