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

  private JPanel statusFilterPanel;
  private JList<Project.Status> statusFilterList;
  private JPanel deadlineStatusPanel;
  private JList<Project.DeadlineStatus> deadlineStatusFilterList;

  private JButton listProjectsButton;

  private final static String statusName = "Project Status";
  private final static String deadlineStatusName = "Turn-in Time";

  public ProjectFilterPanel(int teamId, ProjectListModel projectListModel) {
    this.controller = new ProjectFilterController(teamId, this, projectListModel);
    initFilters();
    initListPorjectsButton();
    createPanelLayout();
  }

  private void initListPorjectsButton() {
    listProjectsButton = UIFactory.createButton("List Projects");
    listProjectsButton.addActionListener(new ListProjectsButtonListener());
  }

  private void initFilters() {
    initStatusFilter();
    initPrivilegeFilter();
    initDeadlineStatusFilter();
  }

  private void initStatusFilter() {
    statusFilterPanel = new JPanel();
    statusFilterPanel.setLayout(new BoxLayout(statusFilterPanel, BoxLayout.Y_AXIS));
    statusFilterList = new JList<>(Project.Status.values());
    statusFilterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    statusFilterList.setSelectedIndex(0);
    statusFilterPanel.add(statusFilterList);
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
    deadlineStatusFilterList = new JList<>(Project.DeadlineStatus.values());
    deadlineStatusFilterList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    deadlineStatusFilterList.setSelectedIndex(0);
    deadlineStatusPanel.add(deadlineStatusFilterList);
  }

  private void createPanelLayout() {
    GroupLayout layout = new GroupLayout(this);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    this.setLayout(layout);

    JLabel filterLabel = UIFactory.createHighlightedLabel("Filter projects by:", null);
    JLabel statusFilterLabel = UIFactory.createMediumHighlightedLabel(statusName + ": ", null);
    JLabel deadlineStatusFilterLabel = UIFactory.createMediumHighlightedLabel(deadlineStatusName +
            ": ", null);
    JLabel privilegeFilterLabel = UIFactory.createMediumHighlightedLabel("Type:", null);

    layout.setHorizontalGroup(
            layout
                    .createSequentialGroup()
                    .addGroup(
                            layout
                                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(statusFilterLabel)
                                    .addComponent(statusFilterPanel))
                    .addGap(90, 90, 90)
                    .addGroup(
                            layout
                                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(filterLabel)
                                    .addComponent(deadlineStatusFilterLabel)
                                    .addComponent(deadlineStatusPanel)
                                    .addComponent(listProjectsButton))
                    .addGap(10, 10, 10)
                    .addGroup(
                            layout
                                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                                    .addComponent(privilegeFilterLabel)
                                    .addComponent(privilegeFilterButtonsPanel)));

    layout.setVerticalGroup(
            layout
                    .createSequentialGroup()
                    .addComponent(filterLabel)
                    .addGap(10, 10, 10)
                    .addGroup(
                            layout
                                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addGroup(
                                            layout
                                                    .createSequentialGroup()
                                                    .addComponent(statusFilterLabel)
                                                    .addComponent(statusFilterPanel))
                                    .addGroup(
                                            layout
                                                    .createSequentialGroup()
                                                    .addComponent(deadlineStatusFilterLabel)
                                                    .addComponent(deadlineStatusPanel)
                                                    .addComponent(listProjectsButton))
                                    .addGroup(
                                            layout
                                                    .createSequentialGroup()
                                                    .addComponent(privilegeFilterLabel)
                                                    .addComponent(privilegeFilterButtonsPanel))));
  }

  private boolean isAtLeastOnePrivilegeButtonSelected() {
    return assignedToUserButton.isSelected() || supervisedByUserButton.isSelected();
  }

  public void applyFilter() {
    // don't allow empty selection
    if (statusFilterList.getSelectedIndices().length == 0) {
      showSelectAtLeastOnStatusDialog(statusName);
    } else if (deadlineStatusFilterList.getSelectedIndices().length == 0) {
      showSelectAtLeastOnStatusDialog(deadlineStatusName);
    } else {
      if (controller.enableProjectSelectionForTeam()) {
        controller.filterProjectsOfTeam(
                statusFilterList.getSelectedValuesList(),
                deadlineStatusFilterList.getSelectedValuesList(),
                (String) assigneeComboBoxModel.getSelectedItem(),
                (String) supervisorComboBoxModel.getSelectedItem());
      } else {
        if (isAtLeastOnePrivilegeButtonSelected()) { //the query is valid
          controller.filterProjectsOfUser(
                  statusFilterList.getSelectedValuesList(),
                  deadlineStatusFilterList.getSelectedValuesList(),
                  assignedToUserButton.isSelected(),
                  supervisedByUserButton.isSelected());
        } else {
          showSelectAtLeastOnePrivilegeDialog();
        }
      }
    }
  }

  class ListProjectsButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      if (actionEvent.getSource() == listProjectsButton) {
        applyFilter();
      }
    }
  }

  private void showSelectAtLeastOnePrivilegeDialog() {
    JOptionPane.showMessageDialog(null, "Please select at least one of the role buttons (assigned" +
            " to me/supervisoed by me", "Error in query", JOptionPane.ERROR_MESSAGE);
  }

  private void showSelectAtLeastOnStatusDialog(String statusName) {
    JOptionPane.showMessageDialog(null, "Please select at least one of the option for " + statusName,
            "Error in query", JOptionPane.ERROR_MESSAGE);
  }
}
