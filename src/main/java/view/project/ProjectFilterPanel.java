package view.project;

import controller.project.ProjectFilterController;
import model.project.Project;
import model.user.User;
import view.UIFactory;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * The ProjectsFilterPanel contains the filters that can be applied to listing the projects,
 * including the status of the project, the due time for turning in the project, and whether it is a
 * project assigned to the user or supervised by the user. The user can select only one option of
 * each, except in case of the assigned/ supervised projects, in which case at least one of these
 * options must be selected. Initially, both of them are selected, meaning that all the projects
 * will be listed.
 *
 * @author Beata Keresztes
 */
public class ProjectFilterPanel extends JPanel {

  private JPanel privilegeFilterButtonsPanel;
  private JRadioButton assignedToUserButton;
  private JRadioButton supervisedByUserButton;
  private DefaultComboBoxModel<String> assigneeComboBoxModel;
  private DefaultComboBoxModel<String> supervisorComboBoxModel;
  private ProjectFilterController controller;
  private JButton filterButton;

  private JPanel statusFilterPanel;
  private JList<Project.Status> statusFilterList;
  private JPanel deadlineStatusPanel;
  private JList<Project.DeadlineStatus> deadlineStatusFilterList;

  private JButton listProjectsButton;

  public ProjectFilterPanel(int teamId, ProjectListModel projectListModel) {
    this.controller = new ProjectFilterController(teamId, this, projectListModel);
    initFilters();
    createPanelLayout();

  }

  private void initListPorjectsButton() {
    listProjectsButton = new JButton("List Projects");
    // todo
    listProjectsButton.addActionListener();
  }

  private void initFilters() {
    initStatusFilter();
    initPrivilegeFilter();
    initDeadlineStatusFilter();
    initFilterButton();
  }

  private void initFilterButton() {
    filterButton = UIFactory.createButton("Filter projects");
    filterButton.addActionListener(new FilterButtonListener());
  }

  private void initStatusFilter() {
    statusFilterPanel = new JPanel();
    statusFilterPanel.setLayout(new BoxLayout(statusFilterPanel, BoxLayout.Y_AXIS));
    statusFilterList = new JList<>(controller.getProjectStatusTypes().toArray(new String[0]));
    statusFilterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    //todo
    statusFilterList.addListSelectionListener();
    statusFilterPanel.add(statusFilterList);
    // todo: select sth
  }

  private void initPrivilegeFilter() {
    privilegeFilterButtonsPanel = new JPanel();
    if (controller.enableProjectSelectionForTeam()) {
      initPrivilegeFilterTeam();
    } else {
      initPrivilegeFilterUser();
    }
  }

  private void initPrivilegeFilterUser() {
    privilegeFilterButtonsPanel.setLayout(
        new BoxLayout(privilegeFilterButtonsPanel, BoxLayout.Y_AXIS));
    // both the assigned and supervised projects are listed in the beginning, so both buttons can be
    // selected at a time
    assignedToUserButton =
        new JRadioButton(ProjectFilterController.PrivilegeTypes.ASSIGNED_TO_ME.toString());
    supervisedByUserButton =
        new JRadioButton(ProjectFilterController.PrivilegeTypes.SUPERVISED_BY_ME.toString());
    assignedToUserButton.addActionListener(new PrivilegeFilterButtonListener());
    supervisedByUserButton.addActionListener(new PrivilegeFilterButtonListener());
    // initially all projects are shown
    assignedToUserButton.setSelected(true);
    supervisedByUserButton.setSelected(true);
    privilegeFilterButtonsPanel.add(assignedToUserButton);
    privilegeFilterButtonsPanel.add(supervisedByUserButton);
  }

  private void initPrivilegeFilterTeam() {
    privilegeFilterButtonsPanel.setLayout(new GridLayout(4, 1));
    JLabel assigneeLabel = UIFactory.createLabel("Assignee:", null);
    assigneeLabel.setHorizontalAlignment(JLabel.LEFT);
    JComboBox<String> assigneeComboBox = new JComboBox<>();
    assigneeComboBoxModel = new DefaultComboBoxModel<>();
    assigneeComboBox.setModel(assigneeComboBoxModel);

    JLabel supervisorLabel = UIFactory.createLabel("Supervisor:", null);
    supervisorLabel.setHorizontalAlignment(JLabel.LEFT);
    JComboBox<String> supervisorComboBox = new JComboBox<>();
    supervisorComboBoxModel = new DefaultComboBoxModel<>();
    supervisorComboBox.setModel(supervisorComboBoxModel);

    updateAssigneeSupervisorFilters();
    privilegeFilterButtonsPanel.add(assigneeLabel);
    privilegeFilterButtonsPanel.add(assigneeComboBox);
    privilegeFilterButtonsPanel.add(supervisorLabel);
    privilegeFilterButtonsPanel.add(supervisorComboBox);
  }

  public void updateAssigneeSupervisorFilters() {
    assigneeComboBoxModel.removeAllElements();
    supervisorComboBoxModel.removeAllElements();
    List<User> members = controller.getTeamMembers();
    assigneeComboBoxModel.addElement(ProjectFilterController.ANYONE);
    supervisorComboBoxModel.addElement(ProjectFilterController.ANYONE);
    if (members != null) {
      for (User member : members) {
        assigneeComboBoxModel.addElement(member.getUsername());
        supervisorComboBoxModel.addElement(member.getUsername());
      }
    }
    assigneeComboBoxModel.setSelectedItem(ProjectFilterController.ANYONE);
    supervisorComboBoxModel.setSelectedItem(ProjectFilterController.ANYONE);
  }

  private void initDeadlineStatusFilter() {
    deadlineStatusPanel = new JPanel();
    deadlineStatusPanel.setLayout(new BoxLayout(deadlineStatusPanel, BoxLayout.Y_AXIS));
    deadlineStatusFilterList =
            new JList<>(controller.getProjectDeadlineStatusTypes().toArray(new String[0]));
    deadlineStatusFilterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    //todo
    statusFilterList.addListSelectionListener();
    statusFilterPanel.add(statusFilterList);
    // todo: select sth
  }

  private void createPanelLayout() {
    GroupLayout layout = new GroupLayout(this);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    this.setLayout(layout);

    JLabel filterLabel = UIFactory.createLabel("Filter projects by:", null);
    JLabel statusFilterLabel = UIFactory.createLabel("Status:", null);
    JLabel turnInTimeFilterLabel = UIFactory.createLabel("Turn-in time:", null);
    JLabel privilegeFilterLabel = UIFactory.createLabel("Type:", null);

    layout.setHorizontalGroup(
        layout
            .createSequentialGroup()
            .addGroup(
                layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(filterLabel)
                    .addComponent(statusFilterLabel)
                    .addComponent(statusFilterButtonsPanel)
                    .addComponent(filterButton))
            .addGroup(
                layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(turnInTimeFilterLabel)
                    .addComponent(turnInTimeFilterButtonsPanel))
            .addGroup(
                layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(privilegeFilterLabel)
                    .addComponent(privilegeFilterButtonsPanel)));

    layout.setVerticalGroup(
        layout
            .createSequentialGroup()
            .addComponent(filterLabel)
            .addGroup(
                layout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addGroup(
                        layout
                            .createSequentialGroup()
                            .addComponent(statusFilterLabel)
                            .addComponent(statusFilterButtonsPanel)
                            .addComponent(filterButton))
                    .addGroup(
                        layout
                            .createSequentialGroup()
                            .addComponent(turnInTimeFilterLabel)
                            .addComponent(turnInTimeFilterButtonsPanel))
                    .addGroup(
                        layout
                            .createSequentialGroup()
                            .addComponent(privilegeFilterLabel)
                            .addComponent(privilegeFilterButtonsPanel))));
  }

  List<String> getSelectedStatusFilters() {
    return statusFilterList.getSelectedValuesList();
  }



  class PrivilegeFilterButtonListener implements ActionListener {
    private boolean isAtLeastOnePrivilegeButtonSelected() {
      return assignedToUserButton.isSelected() || supervisedByUserButton.isSelected();
    }

    private void selectPrivilegeButtons() {
      if (!isAtLeastOnePrivilegeButtonSelected()) {
        // at least one filter must always be selected
        assignedToUserButton.setSelected(true);
        supervisedByUserButton.setSelected(true);
      }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      boolean assignedToUser = assignedToUserButton.isSelected();
      boolean supervisedByUser = supervisedByUserButton.isSelected();
      // if both filters are selected, then all the projects will be displayed
      controller.setPrivilegeFilter(assignedToUser, supervisedByUser);
      selectPrivilegeButtons();
    }
  }

  class FilterButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      if (controller.enableProjectSelectionForTeam()) {
        controller.setAssigneeFilter((String) assigneeComboBoxModel.getSelectedItem());
        controller.setSupervisorFilter((String) supervisorComboBoxModel.getSelectedItem());
      }
      controller.filterProjects();
    }
  }
}
