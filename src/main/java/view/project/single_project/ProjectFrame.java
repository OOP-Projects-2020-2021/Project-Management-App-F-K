package view.project.single_project;

import controller.project.single_project.ProjectController;
import model.project.Project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Displays the details about the project, and allows the user to change the status of the project.
 * The supervisor can edit the details of the project or delete the project.
 *
 * @author Beata Keresztes
 */
public class ProjectFrame extends JFrame {

  private JFrame parentFrame;
  private ProjectController controller;

  JPanel detailsPanel;
  JPanel commentPanel;

  private static final Dimension DIMENSION = new Dimension(800, 600);

  public ProjectFrame(JFrame parentFrame, Project project) {
    super(project.getTitle());
    this.parentFrame = parentFrame;
    this.controller = new ProjectController(this, project);
    this.setPreferredSize(DIMENSION);
    this.setMinimumSize(DIMENSION);
    this.setLayout(new BorderLayout());
    initComponents();
    this.setResizable(false);
    this.setVisible(true);
    this.addWindowListener(new ProjectWindowAdapter());
  }

  private void initComponents() {
    detailsPanel = new ProjectDetailsPanel(controller.getProject());
    commentPanel = new ProjectCommentPanel(controller.getProject());
    initSplitPane();
  }

  private void initSplitPane() {
    JSplitPane splitPane = new JSplitPane();
    splitPane.setLeftComponent(detailsPanel);
    splitPane.setRightComponent(commentPanel);
    this.add(splitPane, BorderLayout.CENTER);
  }

  class ProjectWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent evt) {
      controller.onClose(parentFrame);
    }
  }
}
