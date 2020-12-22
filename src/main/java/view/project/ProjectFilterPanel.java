package view.project;

import controller.project.ProjectFilterController;
import model.project.queryconstants.QueryProjectStatus;
import view.UIFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * Creates a panel containing the filters that can be applied on the projects, including the status
 * of the project, the due time for turning in the project, whether it is a project assigned to the
 * user or supervised by the user.
 *
 * @author Beata Keresztes
 */
public class ProjectFilterPanel extends JPanel {

  private JPanel statusFilterButtonsPanel;
  private JPanel privilegeFilterButtonsPanel;
  private JPanel turnInTimeFilterButtonsPanel;
  private ProjectFilterController controller;

  public ProjectFilterPanel(Optional<Integer> teamId) {
    this.controller = new ProjectFilterController(teamId);
    initFilters();
    createPanelLayout();
  }

  private void initFilters() {
    initStatusFilter();
    initPrivilegeFilter();
    initTurnInTimeFilter();
  }

  private void initStatusFilter() {
    String[] projectStatus = controller.getProjectStatusTypes();

    statusFilterButtonsPanel = new JPanel();
    statusFilterButtonsPanel.setLayout(new BoxLayout(statusFilterButtonsPanel, BoxLayout.Y_AXIS));
    // grouping the buttons ensures that only one button can be selected at a time
    ButtonGroup statusFilterButtonGroup = new ButtonGroup();
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
    String[] privilegeTypes = controller.getProjectPrivilegeTypes();
    privilegeFilterButtonsPanel = new JPanel();
    privilegeFilterButtonsPanel.setLayout(new BoxLayout(privilegeFilterButtonsPanel, BoxLayout.Y_AXIS));
    // grouping the buttons ensures that only one button can be selected at a time
    ButtonGroup privilegeFilterButtonGroup = new ButtonGroup();
    JRadioButton[] privilegeFilterButtons = new JRadioButton[privilegeTypes.length];

    for (int i = 0; i < privilegeTypes.length; i++) {
      privilegeFilterButtons[i] = new JRadioButton(privilegeTypes[i]);
      privilegeFilterButtons[i].setActionCommand(privilegeTypes[i]);
      privilegeFilterButtons[i].addActionListener(new PrivilegeFilterActionListener());
      privilegeFilterButtonGroup.add(privilegeFilterButtons[i]);
      privilegeFilterButtonsPanel.add(privilegeFilterButtons[i]);
    }
    // initially all projects are shown
    privilegeFilterButtons[0].setSelected(true);
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
                    .addComponent(statusFilterButtonsPanel))
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
                            .addComponent(statusFilterButtonsPanel))
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
      controller.filterProjects();
    }
  }

  class TurnInTimeFilterActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      String turnInTimeFilter = actionEvent.getActionCommand();
      controller.setTurnInTimeFilter(turnInTimeFilter);
      controller.filterProjects();
    }
  }
  class PrivilegeFilterActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      String privilegeFilter = actionEvent.getActionCommand();
      controller.setPrivilegeFilter(privilegeFilter);
      controller.filterProjects();
    }
  }
}
