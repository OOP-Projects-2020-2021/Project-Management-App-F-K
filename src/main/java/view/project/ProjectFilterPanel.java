package view.project;

import controller.project.ProjectFilterController;
import model.project.Project;
import model.user.User;
import view.CloseableComponent;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 * The ProjectsFilterPanel contains the filters that can be applied to listing the projects,
 * including the status of the project, the due time for finishing the project, and whether it is a
 * project assigned to/supervised by the current user/a member. For status and due time for
 * finishing the project multiple selection is allowed. At least one option must be selected. If the
 * filter is applyed to projects of a user, then at least one of "assigned to me"/"supervised by me"
 * options must be selected. If the filter is applied to projects of a team, then the option
 * "assigned to anyone" and "supervised by anyone" is provided.
 *
 * @author Beata Keresztes, Bori Fazakas
 */
public class ProjectFilterPanel extends JPanel implements CloseableComponent {

  private JPanel privilegeFilterButtonsPanel;
  private JCheckBox assignedToUserButton;
  private JCheckBox supervisedByUserButton;
  private DefaultComboBoxModel<String> assigneeComboBoxModel;
  private DefaultComboBoxModel<String> supervisorComboBoxModel;
  private ProjectFilterController controller;

  private JPanel statusFilterPanel;
  private JList<Project.Status> statusFilterList;
  private JPanel deadlineStatusPanel;
  private JList<Project.DeadlineStatus> deadlineStatusFilterList;
  private JPanel sorterPanel;
  private JRadioButton sortAscButton;
  private JRadioButton sortDescButton;
  private JComboBox<Project.SorterType> sortComboBox;

  private JButton listProjectsButton;

  private static final String statusName = "Project Status";
  private static final String deadlineStatusName = "Turn-in Time";

  public ProjectFilterPanel(Integer teamId, ProjectListModel projectListModel) {
    this.controller = new ProjectFilterController(teamId, this, projectListModel);
    initFilters();
    initListProjectsButton();
    createPanelLayout();
    applyFilter();
  }

  private void initListProjectsButton() {
    listProjectsButton = UIFactory.createButton("List Projects");
    listProjectsButton.addActionListener(new ButtonListener());
    listProjectsButton.setMnemonic(KeyEvent.VK_ENTER);
  }

  private void initFilters() {
    initStatusFilter();
    initPrivilegeFilter();
    initDeadlineStatusFilter();
    initSorterPanel();
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
        new JCheckBox(ProjectFilterController.PrivilegeTypes.ASSIGNED_TO_ME.toString());
    supervisedByUserButton =
        new JCheckBox(ProjectFilterController.PrivilegeTypes.SUPERVISED_BY_ME.toString());
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

  private void initSorterPanel() {
    sorterPanel = new JPanel(new GridLayout(3, 1));
    initSortComboBox();
    initSortOrderButtons();
  }

  private void initSortOrderButtons() {
    sortAscButton = new JRadioButton(ProjectFilterController.ASC);
    sortDescButton = new JRadioButton(ProjectFilterController.DESC);
    ButtonGroup sortButtonGroup = new ButtonGroup();
    sortButtonGroup.add(sortAscButton);
    sortButtonGroup.add(sortDescButton);
    sortAscButton.setSelected(true);
    sorterPanel.add(sortAscButton);
    sorterPanel.add(sortDescButton);
  }

  private void initSortComboBox() {
    sortComboBox = new JComboBox<>();
    DefaultComboBoxModel<Project.SorterType> sortModel =
        new DefaultComboBoxModel<>(Project.SorterType.values());
    sortComboBox.setModel(sortModel);
    sortComboBox.setSelectedItem(Project.SorterType.NONE);
    sorterPanel.add(sortComboBox);
  }

  private void createPanelLayout() {
    GroupLayout layout = new GroupLayout(this);
    layout.setAutoCreateGaps(true);
    layout.setAutoCreateContainerGaps(true);
    this.setLayout(layout);

    JLabel filterLabel = UIFactory.createHighlightedLabel("Filter projects by:", null);
    JLabel statusFilterLabel = UIFactory.createMediumHighlightedLabel(statusName + ": ", null);
    JLabel deadlineStatusFilterLabel =
        UIFactory.createMediumHighlightedLabel(deadlineStatusName + ": ", null);
    JLabel privilegeFilterLabel = UIFactory.createMediumHighlightedLabel("Type:", null);
    JLabel sortByLabel = UIFactory.createMediumHighlightedLabel("Sort Projects by:", null);
    JLabel sortLabel = UIFactory.createHighlightedLabel("Sort Projects", null);

    layout.setHorizontalGroup(
        layout
            .createSequentialGroup()
            .addGroup(
                layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(filterLabel)
                    .addGap(10, 10, 10)
                    .addComponent(statusFilterLabel)
                    .addComponent(statusFilterPanel))
            .addGap(10, 10, 10)
            .addGroup(
                layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(deadlineStatusFilterLabel)
                    .addComponent(deadlineStatusPanel)
                    .addComponent(listProjectsButton))
            .addGap(10, 10, 10)
            .addGroup(
                layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(privilegeFilterLabel)
                    .addComponent(privilegeFilterButtonsPanel))
            .addGap(10, 10, 10)
            .addGroup(
                layout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(sortLabel)
                    .addComponent(sortByLabel)
                    .addComponent(sorterPanel)));

    layout.setVerticalGroup(
        layout
            .createSequentialGroup()
            .addGroup(
                layout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(filterLabel)
                    .addComponent(sortLabel))
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
                            .addComponent(deadlineStatusPanel))
                    .addGroup(
                        layout
                            .createSequentialGroup()
                            .addComponent(privilegeFilterLabel)
                            .addComponent(privilegeFilterButtonsPanel))
                    .addGroup(
                        layout
                            .createSequentialGroup()
                            .addComponent(sortByLabel)
                            .addComponent(sorterPanel)))
            .addComponent(listProjectsButton));
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
      Project.SorterType sorterType = (Project.SorterType) sortComboBox.getSelectedItem();
      boolean descendingSort = sortDescButton.isSelected();
      if (controller.enableProjectSelectionForTeam()) {
        controller.filterProjectsOfTeam(
            statusFilterList.getSelectedValuesList(),
            deadlineStatusFilterList.getSelectedValuesList(),
            (String) assigneeComboBoxModel.getSelectedItem(),
            (String) supervisorComboBoxModel.getSelectedItem(),
            sorterType,
            descendingSort);
      } else {
        if (isAtLeastOnePrivilegeButtonSelected()) { // the query is valid
          controller.filterProjectsOfUser(
              statusFilterList.getSelectedValuesList(),
              deadlineStatusFilterList.getSelectedValuesList(),
              assignedToUserButton.isSelected(),
              supervisedByUserButton.isSelected(),
              sorterType,
              descendingSort);
        } else {
          showSelectAtLeastOnePrivilegeDialog();
        }
      }
    }
  }

  class ButtonListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      if (actionEvent.getSource() == listProjectsButton) {
        applyFilter();
      }
    }
  }

  private void showSelectAtLeastOnePrivilegeDialog() {
    JOptionPane.showMessageDialog(
        null,
        "Please select at least one of the role buttons (assigned" + " to me/supervised by me",
        "Error in query",
        JOptionPane.ERROR_MESSAGE);
  }

  private void showSelectAtLeastOnStatusDialog(String statusName) {
    JOptionPane.showMessageDialog(
        null,
        "Please select at least one of the option for " + statusName,
        "Error in query",
        JOptionPane.ERROR_MESSAGE);
  }

  @Override
  public void onClose() {
    controller.close();
  }
}
