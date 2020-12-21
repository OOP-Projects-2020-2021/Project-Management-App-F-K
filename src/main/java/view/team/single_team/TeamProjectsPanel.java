package view.team.single_team;

import controller.team.single_team.TeamProjectsController;
import model.project.Project;
import view.UIFactory;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Displays the list of projects, which can be sorted based on the deadline or filtered based on the
 * project's status, importance, assignee or supervisor.
 *
 * @author Beata Keresztes
 */
public class TeamProjectsPanel extends JPanel {

  private JPanel statusFilterButtonsPanel;
  private JPanel typeFilterButtonsPanel;
  private JPanel deadlineFilterButtonsPanel;
  private DefaultTableModel projectsTableModel;
  private JTable projectsTable;
  private TeamProjectsController controller;
  private static final String[] columnNames = {"Name", "Deadline", "Status"};
  private static final String[] projectType = {"ALL","ASSIGNED TO ME","SUPERVISED BY ME"};

  public TeamProjectsPanel(JFrame frame, Dimension frameDimension, int currentTeamId) {
    controller = new TeamProjectsController(this,frame,currentTeamId);
    this.setPreferredSize(frameDimension);
    this.setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    initProjectsPane();
  }

  private void initProjectsPane() {
    initProjectsHeader();
    initProjectsTable();
  }

  private void initHeaderComponents() {
    initStatusFilter();
    initTypeFilter();
    initDeadlineFilter();
  }
  private void initStatusFilter() {
    // todo get values from table Status + "ALL"
    String[] projectStatus = {"ALL","TO DO", "IN PROGRESS", "TURNED_IN", "FINISHED"};

    statusFilterButtonsPanel = new JPanel();
    statusFilterButtonsPanel.setLayout(new BoxLayout(statusFilterButtonsPanel, BoxLayout.Y_AXIS));
    // grouping the buttons ensures that only one button can be selected at a time
    ButtonGroup statusFilterButtonGroup = new ButtonGroup();
    JRadioButton[] statusFilterButtons = new JRadioButton[projectStatus.length];

    for(int i=0;i<projectStatus.length;i++) {
      statusFilterButtons[i] = new JRadioButton(projectStatus[i]);
      statusFilterButtons[i].setActionCommand(projectStatus[i]);
      statusFilterButtons[i].addActionListener(new StatusFilterActionListener());
      statusFilterButtonGroup.add(statusFilterButtons[i]);
      statusFilterButtonsPanel.add(statusFilterButtons[i]);
    }
    // initially all projects are shown
    statusFilterButtons[0].setSelected(true);
  }

  private void initTypeFilter() {
    typeFilterButtonsPanel = new JPanel();
    typeFilterButtonsPanel.setLayout(new BoxLayout(typeFilterButtonsPanel, BoxLayout.Y_AXIS));
    // grouping the buttons ensures that only one button can be selected at a time
    ButtonGroup typeFilterButtonGroup = new ButtonGroup();
    JRadioButton[] typeFilterButtons = new JRadioButton[projectType.length];

    for(int i=0;i<projectType.length;i++){
      typeFilterButtons[i] = new JRadioButton(projectType[i]);
      typeFilterButtons[i].setActionCommand(projectType[i]);
      typeFilterButtons[i].addActionListener(new TypeFilterActionListener());
      typeFilterButtonGroup.add(typeFilterButtons[i]);
      typeFilterButtonsPanel.add(typeFilterButtons[i]);
    }
    // initially all projects are shown
    typeFilterButtons[0].setSelected(true);
  }
  private void initDeadlineFilter() {
    // todo get this from the projectManager
    String[] deadline = {"ALL","IN_TIME_TO_TURN_IN","TURNED_IN_ON_TIME","OVERDUE","TURNED_IN_LATE"};

    deadlineFilterButtonsPanel = new JPanel();
    deadlineFilterButtonsPanel.setLayout(new BoxLayout(deadlineFilterButtonsPanel, BoxLayout.Y_AXIS));
    // grouping the buttons ensures that only one button can be selected at a time
    ButtonGroup deadlineFilterButtonGroup = new ButtonGroup();
    JRadioButton[] deadlineFilterButtons = new JRadioButton[deadline.length];

    for(int i=0;i<deadline.length;i++){
      deadlineFilterButtons[i] = new JRadioButton(deadline[i]);
      deadlineFilterButtons[i].setActionCommand(deadline[i]);
      deadlineFilterButtons[i].addActionListener(new DeadlineFilterActionListener());
      deadlineFilterButtonGroup.add(deadlineFilterButtons[i]);
      deadlineFilterButtonsPanel.add(deadlineFilterButtons[i]);
    }
    // initially all projects are shown
    deadlineFilterButtons[0].setSelected(true);
  }

  private void initProjectsHeader() {
    initHeaderComponents();

    JPanel header = new JPanel();
    GroupLayout headerLayout = new GroupLayout(header);
    headerLayout.setAutoCreateGaps(true);
    headerLayout.setAutoCreateContainerGaps(true);
    header.setLayout(headerLayout);

    JLabel filterLabel = UIFactory.createLabel("Filter projects by:", null);
    JLabel statusFilterLabel = UIFactory.createLabel("Status:", null);
    JLabel deadlineFilterLabel = UIFactory.createLabel("Deadline:", null);
    JLabel typeFilterLabel = UIFactory.createLabel("Type:", null);

    headerLayout.setHorizontalGroup(
        headerLayout
            .createSequentialGroup()
                .addGroup(headerLayout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(filterLabel)
                        .addComponent(statusFilterLabel)
                        .addComponent(statusFilterButtonsPanel))
                .addGroup(headerLayout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(deadlineFilterLabel)
                        .addComponent(deadlineFilterButtonsPanel))
                .addGroup(headerLayout
                        .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(typeFilterLabel)
                        .addComponent(typeFilterButtonsPanel)));

    headerLayout.setVerticalGroup(
        headerLayout
            .createSequentialGroup()
                .addComponent(filterLabel)
                .addGroup(headerLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addGroup(headerLayout
                                .createSequentialGroup()
                                .addComponent(statusFilterLabel)
                                .addComponent(statusFilterButtonsPanel))
                        .addGroup(headerLayout
                                .createSequentialGroup()
                                .addComponent(deadlineFilterLabel)
                                .addComponent(deadlineFilterButtonsPanel))
                        .addGroup(headerLayout
                                .createSequentialGroup()
                                .addComponent(typeFilterLabel)
                                .addComponent(typeFilterButtonsPanel))));

    this.add(header, BorderLayout.NORTH);
  }

  private void clearProjectModel() {
    while(projectsTableModel.getRowCount() != 0) {
      projectsTableModel.removeRow(0);
    }
  }
  private void fillProjectModel(List<Project> projectsList) {
    if(!controller.isEmptyProjectList(projectsList)) {
      for (Project project : projectsList) {
        projectsTableModel.addRow(new String[]{project.getTitle(), String.valueOf(project.getDeadline()), String.valueOf(project.getStatus())});
      }
    }
  }
  public void updateProjectModel(List<Project> projectsList) {
    clearProjectModel();
    fillProjectModel(projectsList);
  }
  private void initProjectsTable() {
    projectsTable = new ProjectTable();

    projectsTableModel = (DefaultTableModel)projectsTable.getModel();
    projectsTableModel.setColumnIdentifiers(columnNames);
    updateProjectModel(controller.getAllProjects());
    projectsTable.addMouseListener(new ProjectsTableMouseListener());

    // the projects can be sorted by deadlines when clicking on the column's header
    TableRowSorter<TableModel> sorter = new TableRowSorter<>(projectsTableModel);
    sorter.setComparator(1, new TeamProjectsPanel.DateComparator());
    projectsTable.setRowSorter(sorter);

    JScrollPane scrollPane = new JScrollPane(projectsTable);
    // border and title of the list pane
    scrollPane.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Projects List",
            TitledBorder.CENTER,
            TitledBorder.TOP));

    this.add(scrollPane, BorderLayout.CENTER);
  }
  /** Create a table for listing the projects. */
  static class ProjectTable extends JTable {
    public ProjectTable() {
      this.setFillsViewportHeight(true);
      this.setCellSelectionEnabled(true);
      this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      this.setFont(UIFactory.NORMAL_TEXT_FONT);
      this.getTableHeader().setFont(UIFactory.HIGHLIGHT_TEXT_FONT);
      this.setRowHeight(30);
      // the columns cannot be rearranged
      this.getTableHeader().setReorderingAllowed(false);
    }
    // the data in the tables cannot be edited only viewed
    @Override
    public boolean isCellEditable(int row,int column) {
      return false;
    }

  }
  /** Compare Strings which represent dates. */
  static class DateComparator implements Comparator<String> {
    @Override
    public int compare(String s1, String s2) {
      try {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(s1);
        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(s2);
        return date1.compareTo(date2);
      } catch (ParseException e) {
        e.printStackTrace();
        return 0;
      }
    }
 }
  class StatusFilterActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      String statusFilter = actionEvent.getActionCommand();
      updateProjectModel(controller.getProjectsByStatus(statusFilter));
      }
    }

  class DeadlineFilterActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      String deadlineFilter = actionEvent.getActionCommand();
      updateProjectModel(controller.getProjectsByDeadline(deadlineFilter));
      }
    }

  class TypeFilterActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      String typeFilter = actionEvent.getActionCommand();
      if(typeFilter.equals(projectType[0])) {
        updateProjectModel(controller.getAllProjects());
      }else if(typeFilter.equals(projectType[1])) {
        updateProjectModel(controller.getAssignedProjects());
      }else if(typeFilter.equals(projectType[2])) {
        updateProjectModel(controller.getSupervisedProjects());
      }
    }
  }
  class ProjectsTableMouseListener extends MouseAdapter {
    @Override
    public void mouseClicked(java.awt.event.MouseEvent evt) {
      int row = projectsTable.getSelectedRow();
      int column = projectsTable.getSelectedColumn();
      if (column == 0 && evt.getClickCount() > 1) {
        // on double click
        String projectName = (String) projectsTableModel.getValueAt(row, column);
        // todo pass this to controller, which finds the corresponding id then passes it to the project frame
        JOptionPane.showMessageDialog(null,"you selected " + projectName,"title",JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }
}
