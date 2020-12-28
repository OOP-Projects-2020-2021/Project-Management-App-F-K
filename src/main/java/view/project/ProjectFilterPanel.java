package view.project;

import controller.project.ProjectFilterController;
import model.user.User;
import view.UIFactory;

import javax.swing.*;
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

  private JPanel statusFilterButtonsPanel;
  private JPanel turnInTimeFilterButtonsPanel;
  private JPanel privilegeFilterButtonsPanel;
  private JRadioButton assignedToUserButton;
  private JRadioButton supervisedByUserButton;
  private DefaultComboBoxModel<String> assigneeComboBoxModel;
  private DefaultComboBoxModel<String> supervisorComboBoxModel;
  private ProjectFilterController controller;
  private JButton filterButton;


  public ProjectFilterPanel(int teamId) {
    this.controller = new ProjectFilterController(teamId,this);
    initFilters();
    createPanelLayout();
  }

  private void initFilters() {
    initStatusFilter();
    initPrivilegeFilter();
    initTurnInTimeFilter();
    initFilterButton();
  }

  private void initFilterButton() {
    filterButton = UIFactory.createButton("Filter projects");
    filterButton.addActionListener(new FilterButtonListener());
  }

  private void initStatusFilter() {
    statusFilterButtonsPanel = new JPanel();
    statusFilterButtonsPanel.setLayout(new BoxLayout(statusFilterButtonsPanel, BoxLayout.Y_AXIS));
    // grouping the buttons ensures that only one button can be selected at a time
    ButtonGroup statusFilterButtonGroup = new ButtonGroup();
    String[] projectStatus = controller.getProjectStatusTypes();
    JRadioButton[] statusFilterButtons = new JRadioButton[projectStatus.length];

    for (int i = 0; i < projectStatus.length; i++) {
      statusFilterButtons[i] = new JRadioButton(projectStatus[i]);
      statusFilterButtons[i].setActionCommand(projectStatus[i]);
      statusFilterButtons[i].addActionListener(new StatusFilterActionListener());
      statusFilterButtonGroup.add(statusFilterButtons[i]);
      statusFilterButtonsPanel.add(statusFilterButtons[i]);
    }
    // initially all projects are shown
    statusFilterButtons[0].setSelected(true);
  }

  private void initPrivilegeFilter() {
    privilegeFilterButtonsPanel = new JPanel();
    if(controller.enableProjectSelectionForTeam()) {
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
    privilegeFilterButtonsPanel.setLayout(new GridLayout(4,1));
    JLabel assigneeLabel = UIFactory.createLabel("Assignee:",null);
    assigneeLabel.setHorizontalAlignment(JLabel.LEFT);
    JComboBox<String> assigneeComboBox = new JComboBox<>();
    assigneeComboBoxModel = new DefaultComboBoxModel<>();
    assigneeComboBox.setModel(assigneeComboBoxModel);

    JLabel supervisorLabel = UIFactory.createLabel("Supervisor:",null);
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
    if(members != null) {
      for(User member:members) {
        assigneeComboBoxModel.addElement(member.getUsername());
        supervisorComboBoxModel.addElement(member.getUsername());
      }
    }
    assigneeComboBoxModel.setSelectedItem(ProjectFilterController.ANYONE);
    supervisorComboBoxModel.setSelectedItem(ProjectFilterController.ANYONE);
  }

  private void initTurnInTimeFilter() {
    String[] turnInTime = controller.getProjectTurnInTimes();
    turnInTimeFilterButtonsPanel = new JPanel();
    turnInTimeFilterButtonsPanel.setLayout(
        new BoxLayout(turnInTimeFilterButtonsPanel, BoxLayout.Y_AXIS));
    // grouping the buttons ensures that only one button can be selected at a time
    ButtonGroup turnInTimeFilterButtonGroup = new ButtonGroup();
    JRadioButton[] turnInTimeFilterButtons = new JRadioButton[turnInTime.length];

    for (int i = 0; i < turnInTime.length; i++) {
      turnInTimeFilterButtons[i] = new JRadioButton(turnInTime[i]);
      turnInTimeFilterButtons[i].setActionCommand(turnInTime[i]);
      turnInTimeFilterButtons[i].addActionListener(new TurnInTimeFilterActionListener());
      turnInTimeFilterButtonGroup.add(turnInTimeFilterButtons[i]);
      turnInTimeFilterButtonsPanel.add(turnInTimeFilterButtons[i]);
    }
    // initially all projects are shown
    turnInTimeFilterButtons[0].setSelected(true);
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

  class StatusFilterActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      String statusFilter = actionEvent.getActionCommand();
      controller.setStatusFilter(statusFilter);
    }
  }

  class TurnInTimeFilterActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      String turnInTimeFilter = actionEvent.getActionCommand();
      controller.setTurnInTimeFilter(turnInTimeFilter);
    }
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
      if(controller.enableProjectSelectionForTeam()) {
        controller.setAssigneeFilter((String) assigneeComboBoxModel.getSelectedItem());
        controller.setSupervisorFilter((String) supervisorComboBoxModel.getSelectedItem());
      }
      controller.filterProjects();
    }
  }
}
