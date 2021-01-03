package view.project;

import view.CloseableComponent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProjectsPanel displays the list of projects, which can be sorted based on the deadline or
 * filtered based on the project's status, turn-in time, assignee or supervisor. The user can visit
 * a specific project if he/she double clicks on the title of the project.
 *
 * @author Beata Keresztes
 */
public class ProjectsPanel extends JPanel implements CloseableComponent {

  private ProjectTable projectsTable;
  private JFrame frame;
  private List<CloseableComponent> closeableComponents = new ArrayList<>();

  public ProjectsPanel(JFrame frame, Integer teamId) {
    this.frame = frame;
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    ProjectListModel projectListModel = new ProjectListModel();
    initProjectsHeader(teamId, projectListModel);
    initProjectsTable(teamId, projectListModel);
  }

  private void initProjectsHeader(Integer teamId, ProjectListModel projectListModel) {
    ProjectFilterPanel header = new ProjectFilterPanel(teamId, projectListModel);
    closeableComponents.add(header);
    add(header, BorderLayout.NORTH);
  }

  private void initProjectsTable(Integer teamId, ProjectListModel projectListModel) {
    projectsTable = new ProjectTable(frame, projectListModel);
    closeableComponents.add(projectsTable);
    addScrollPane();
  }

  private void addScrollPane() {
    JScrollPane scrollPane = new JScrollPane(projectsTable);
    // border and title of the list pane
    scrollPane.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Projects List",
            TitledBorder.CENTER,
            TitledBorder.TOP));

    add(scrollPane, BorderLayout.CENTER);
  }

  @Override
  public void onClose() {
    for (CloseableComponent closeableComponent : closeableComponents) {
      closeableComponent.onClose();
    }
  }
}
