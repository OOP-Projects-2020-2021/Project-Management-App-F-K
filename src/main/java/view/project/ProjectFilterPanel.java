package view.project;

import controller.project.ProjectFilterController;
import view.UIFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;

/**
 * Creates a panel containing the filters that can be applied on the projects, including the status of the
 * project, the due time for turning in the project, whether it is a project assigned to the user or
 * supervised by the user.
 *
 * @author Beata Keresztes
 */
public class ProjectFilterPanel extends JPanel{

    private JPanel statusFilterButtonsPanel;
    private JPanel typeFilterButtonsPanel;
    private JPanel turnInTimeFilterButtonsPanel;
    private ProjectFilterController controller;

    private static final String[] projectType = {"ALL", "ASSIGNED TO ME", "SUPERVISED BY ME"};

    public ProjectFilterPanel(ProjectTable projectTable, Optional<Integer> teamId) {
        this.controller = new ProjectFilterController(projectTable,teamId);
        initFilters();
        createPanelLayout();
    }

    private void initFilters() {
        initStatusFilter();
        initTypeFilter();
        initturnInTimeFilter();
    }
    private void initStatusFilter() {
        // todo get values from table Status + "ALL"
        String[] projectStatus = {"ALL", "TO DO", "IN PROGRESS", "TURNED IN", "FINISHED"};

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

    private void initTypeFilter() {
        typeFilterButtonsPanel = new JPanel();
        typeFilterButtonsPanel.setLayout(new BoxLayout(typeFilterButtonsPanel, BoxLayout.Y_AXIS));
        // grouping the buttons ensures that only one button can be selected at a time
        ButtonGroup typeFilterButtonGroup = new ButtonGroup();
        JRadioButton[] typeFilterButtons = new JRadioButton[projectType.length];

        for (int i = 0; i < projectType.length; i++) {
            typeFilterButtons[i] = new JRadioButton(projectType[i]);
            typeFilterButtons[i].setActionCommand(projectType[i]);
            typeFilterButtons[i].addActionListener(new TypeFilterActionListener());
            typeFilterButtonGroup.add(typeFilterButtons[i]);
            typeFilterButtonsPanel.add(typeFilterButtons[i]);
        }
        // initially all projects are shown
        typeFilterButtons[0].setSelected(true);
    }

    private void initturnInTimeFilter() {
        // todo get this from the projectManager through the controller
        String[] turnInTime = {
                "ALL", "IN TIME TO TURN IN", "TURNED IN ON TIME", "OVERDUE", "TURNED IN LATE"
        };

        turnInTimeFilterButtonsPanel = new JPanel();
        turnInTimeFilterButtonsPanel.setLayout(
                new BoxLayout(turnInTimeFilterButtonsPanel, BoxLayout.Y_AXIS));
        // grouping the buttons ensures that only one button can be selected at a time
        ButtonGroup turnInTimeFilterButtonGroup = new ButtonGroup();
        JRadioButton[] turnInTimeFilterButtons = new JRadioButton[turnInTime.length];

        for (int i = 0; i < turnInTime.length; i++) {
            turnInTimeFilterButtons[i] = new JRadioButton(turnInTime[i]);
            turnInTimeFilterButtons[i].setActionCommand(turnInTime[i]);
            turnInTimeFilterButtons[i].addActionListener(new turnInTimeFilterActionListener());
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
        JLabel typeFilterLabel = UIFactory.createLabel("Type:", null);

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
                                        .addComponent(typeFilterLabel)
                                        .addComponent(typeFilterButtonsPanel)));

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
                                                        .addComponent(typeFilterLabel)
                                                        .addComponent(typeFilterButtonsPanel))));

    }

    class StatusFilterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String statusFilter = actionEvent.getActionCommand();
            controller.filterProjectsByStatus(statusFilter);
        }
    }

    class turnInTimeFilterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String turnInTimeFilter = actionEvent.getActionCommand();
            controller.filterProjectsByTurnInTime(turnInTimeFilter);
        }
    }

    class TypeFilterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String typeFilter = actionEvent.getActionCommand();
            if (typeFilter.equals(projectType[0])) {
                controller.unFilteredProjects();
            } else if (typeFilter.equals(projectType[1])) {
                controller.filterAssignedProjects();
            } else if (typeFilter.equals(projectType[2])) {
                controller.filterSupervisedProjects();
            }
        }
    }
}
