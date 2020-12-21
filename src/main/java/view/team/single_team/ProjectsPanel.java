package view.team.single_team;

import view.project.ProjectFilterPanel;
import view.project.ProjectTable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Optional;

/**
 * Displays the list of projects, which can be sorted based on the deadline or filtered based on the
 * project's status, turn-in time, assignee or supervisor.
 *
 * @author Beata Keresztes
 */
public class ProjectsPanel extends JPanel {

  private ProjectTable projectsTable;
  public ProjectsPanel(Optional<Integer> currentTeamId) {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    initProjectsTable();
    initProjectsHeader(currentTeamId);
  }

  private void initProjectsHeader(Optional<Integer> currentTeamId) {
    ProjectFilterPanel header = new ProjectFilterPanel(projectsTable,currentTeamId);
    add(header, BorderLayout.NORTH);
  }

  private void initProjectsTable() {
    projectsTable = new ProjectTable();
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
