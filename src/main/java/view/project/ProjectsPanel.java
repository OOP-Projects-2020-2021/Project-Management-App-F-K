package view.project;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * ProjectsPanel displays the list of projects, which can be sorted based on the deadline or
 * filtered based on the project's status, turn-in time, assignee or supervisor. The user can visit
 * a specific project if he/she double clicks on the title of the project.
 *
 * @author Beata Keresztes
 */
public class ProjectsPanel extends JPanel {

  private ProjectTable projectsTable;
  private JFrame frame;

  public ProjectsPanel(JFrame frame, int teamId) {
    this.frame = frame;
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    initProjectsHeader(teamId);
    initProjectsTable();
  }

  private void initProjectsHeader(int teamId) {
    ProjectFilterPanel header = new ProjectFilterPanel(teamId);
    add(header, BorderLayout.NORTH);
  }

  private void initProjectsTable() {
    projectsTable = new ProjectTable(frame);
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
}
