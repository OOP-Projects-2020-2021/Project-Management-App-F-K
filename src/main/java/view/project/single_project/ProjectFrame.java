package view.project.single_project;

import controller.project.single_project.ProjectController;
import model.project.Project;
import view.CloseableComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

/**
 * The ProjectFrame displays the details about the project, and allows the user to change the status
 * of the project and to leave comments in the comment section. Only the supervisor can edit the
 * details of the project or delete the project.
 *
 * @author Beata Keresztes
 */
public class ProjectFrame extends JFrame {

  private JFrame parentFrame;
  private ProjectController controller;

  ProjectDetailsPanel detailsPanel;
  ProjectCommentPanel commentPanel;

  private static final Dimension DIMENSION = new Dimension(700, 600);

  private List<CloseableComponent> closeableComponents = new ArrayList<>();

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
    detailsPanel = new ProjectDetailsPanel(this, controller.getProject());
    commentPanel = new ProjectCommentPanel(controller.getProject());
    closeableComponents.add(detailsPanel);
    closeableComponents.add(commentPanel);
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
      for (CloseableComponent closeableComponent : closeableComponents) {
        closeableComponent.onClose();
      }
    }
  }
}
