package view.project.single_project;

import controller.project.single_project.ProjectController;

import javax.swing.*;
import java.awt.*;

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

  public ProjectFrame(JFrame parentFrame, int projectId,String title) {
    super(title);
    this.parentFrame = parentFrame;
    this.controller = new ProjectController(this, projectId);
    this.setPreferredSize(DIMENSION);
    this.setMinimumSize(DIMENSION);
    this.setLayout(new BorderLayout());
    initComponents();
    this.setResizable(false);
    this.setVisible(true);
  }

  private void initComponents() {
    detailsPanel = new ProjectDetailsPanel(controller.getProjectId());
    commentPanel = new ProjectCommentPanel(controller.getProjectId());
    initSplitPane();
  }

  private void initSplitPane() {
    JSplitPane splitPane = new JSplitPane();
    splitPane.setLeftComponent(detailsPanel);
    splitPane.setRightComponent(commentPanel);
    this.add(splitPane, BorderLayout.CENTER);
  }
}
