package view.project.single_project;

import controller.project.single_project.ProjectDetailsController;
import model.project.Project;
import model.user.User;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import view.UIFactory;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Properties;
import java.util.List;

/**
 * ProjectDetailsPanel allows the user to view the details about a given project, such as the title,
 * description, deadline, assignee and supervisor. The assignee of the project can change the status
 * of the project to mark his/her contribution, but only the supervisor can declare the project to
 * be finished. Only the supervisor of the project is allowed to edit the details of a project or
 * pass the supervisor position to another member of the team.
 *
 * @author Beata Keresztes
 */
public class ProjectDetailsPanel extends JPanel {

  private JDatePickerImpl deadlineDatePicker;
  private JDatePanelImpl datePanel;
  private UtilDateModel dateModel;

  private JTextField titleTextField;
  private JTextArea descriptionTextArea;
  private JScrollPane descriptionScrollPane;
  private JLabel projectStatusLabel;

  private JComboBox<String> assigneeComboBox;
  private DefaultComboBoxModel<String> assigneeModel;
  private JComboBox<String> supervisorComboBox;
  private DefaultComboBoxModel<String> supervisorModel;

  private JPanel radioButtonsPanel;
  private JRadioButton toDoButton;
  private JRadioButton turnedInButton;
  private JRadioButton inProgressButton;
  private JRadioButton finishedButton;
  private JButton saveButton;
  private JButton deleteButton;

  private ProjectDetailsController controller;

  public ProjectDetailsPanel(Project project) {
    controller = new ProjectDetailsController(project, this);
    this.setLayout(new BorderLayout());
    initDetailsPanel();
  }

  private void initDetailsPanel() {
    initDataFields();
    initRadioButtonsPanel();
    initContentPanel();
    initButtonsPanel();
    addButtonListener();
    enableEditFields(controller.enableEditing());
    controller.selectProjectStatusButtons();
  }

  private void initDataFields() {
    titleTextField = UIFactory.createTextField(controller.getProjectTitle());
    initDatePicker();
    initDescriptionTextArea();
    initAssigneeComboBox();
    initSupervisorComboBox();
    projectStatusLabel = UIFactory.createLabel(controller.getStatus(), null);
  }

  private void initDatePicker() {
    dateModel = new UtilDateModel();
    setDeadlineDate();
    Properties properties = new Properties();
    properties.put("text.today", "Today");
    properties.put("text.month", "Month");
    properties.put("text.year", "Year");
    datePanel = new JDatePanelImpl(dateModel, properties);
    deadlineDatePicker = new JDatePickerImpl(datePanel, new DefaultFormatter());
  }

  private void setDeadlineDate() {
    dateModel.setDate(
        controller.getProjectDeadline().getYear(),
        controller.getProjectDeadline().getMonthValue(),
        controller.getProjectDeadline().getDayOfMonth());
    dateModel.setSelected(true);
  }

  private void initDescriptionTextArea() {
    descriptionTextArea = new JTextArea();
    descriptionTextArea.setText(controller.getProjectDescription());
    descriptionTextArea.setLineWrap(true);
    descriptionTextArea.setWrapStyleWord(true);
    descriptionScrollPane = new JScrollPane(descriptionTextArea);
  }

  private DefaultComboBoxModel<String> createMembersComboBoxModel() {
    DefaultComboBoxModel<String> membersModel = new DefaultComboBoxModel<>();
    List<User> members = controller.getTeamMembers();
    for (User member : members) {
      membersModel.addElement(member.getUsername());
    }
    return membersModel;
  }

  private void selectAssigneeFromComboBox() {
    if (controller.getProjectAssignee() != null) {
      assigneeModel.setSelectedItem(controller.getProjectAssignee().getUsername());
    }
  }

  private void initAssigneeComboBox() {
    assigneeComboBox = new JComboBox<>();
    assigneeModel = createMembersComboBoxModel();
    selectAssigneeFromComboBox();
    assigneeComboBox.setModel(assigneeModel);
  }

  private void selectSupervisorFromComboBox() {
    if (controller.getProjectSupervisor() != null) {
      supervisorModel.setSelectedItem(controller.getProjectSupervisor().getUsername());
    }
  }

  private void initSupervisorComboBox() {
    supervisorComboBox = new JComboBox<>();
    supervisorModel = createMembersComboBoxModel();
    selectSupervisorFromComboBox();
    supervisorComboBox.setModel(supervisorModel);
  }

  private void initRadioButtons() {
    toDoButton = new JRadioButton(Project.ProjectStatus.TO_DO.toString());
    inProgressButton = new JRadioButton(Project.ProjectStatus.IN_PROGRESS.toString());
    turnedInButton = new JRadioButton(Project.ProjectStatus.TURNED_IN.toString());
    finishedButton = new JRadioButton(Project.ProjectStatus.FINISHED.toString());

    toDoButton.setActionCommand(Project.ProjectStatus.TO_DO.toString());
    inProgressButton.setActionCommand(Project.ProjectStatus.IN_PROGRESS.toString());
    turnedInButton.setActionCommand(Project.ProjectStatus.TURNED_IN.toString());
    finishedButton.setActionCommand(Project.ProjectStatus.FINISHED.toString());
  }

  private void selectProjectStatus() {
    System.out.println(controller.getStatus());
    switch (Project.ProjectStatus.valueOf(controller.getStatus())) {
      case IN_PROGRESS:
        {
          inProgressButton.setSelected(true);
          break;
        }
      case TURNED_IN:
        {
          turnedInButton.setSelected(true);
          break;
        }
      case FINISHED:
        {
          finishedButton.setSelected(true);
          break;
        }
      case TO_DO:
        {
          toDoButton.setSelected(true);
          break;
        }
      default:
        {
          break;
        }
    }
  }

  private void createRadioButtonsGroup() {
    // add the buttons to a group so that only one can be selected at a time
    ButtonGroup statusButtonGroup = new ButtonGroup();
    statusButtonGroup.add(toDoButton);
    statusButtonGroup.add(inProgressButton);
    statusButtonGroup.add(turnedInButton);
    statusButtonGroup.add(finishedButton);
  }

  private void initRadioButtonsPanel() {
    initRadioButtons();
    createRadioButtonsGroup();
    selectProjectStatus();
    radioButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    radioButtonsPanel.add(toDoButton);
    radioButtonsPanel.add(inProgressButton);
    radioButtonsPanel.add(turnedInButton);
    radioButtonsPanel.add(finishedButton);
  }

  private void initContentPanel() {
    JPanel contentPanel = new JPanel();
    GroupLayout contentLayout = new GroupLayout(contentPanel);
    contentLayout.setAutoCreateGaps(true);
    contentLayout.setAutoCreateContainerGaps(true);
    contentPanel.setLayout(contentLayout);
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JLabel titleLabel = UIFactory.createLabel("Title:", null);
    JLabel deadlineLabel = UIFactory.createLabel("Deadline:", null);
    JLabel descriptionLabel = UIFactory.createLabel("Description:", null);
    JLabel assigneeLabel = UIFactory.createLabel("Assignee:", null);
    JLabel supervisorLabel = UIFactory.createLabel("Supervisor:", null);
    JLabel statusLabel = UIFactory.createLabel("Status:", null);

    contentLayout.setHorizontalGroup(
        contentLayout
            .createParallelGroup()
            .addGroup(
                contentLayout
                    .createSequentialGroup()
                    .addGroup(
                        contentLayout
                            .createParallelGroup()
                            .addComponent(titleLabel)
                            .addComponent(deadlineLabel)
                            .addComponent(descriptionLabel)
                            .addComponent(assigneeLabel)
                            .addComponent(supervisorLabel)
                            .addComponent(statusLabel))
                    .addGroup(
                        contentLayout
                            .createParallelGroup()
                            .addComponent(titleTextField)
                            .addComponent(datePanel)
                            .addComponent(descriptionScrollPane)
                            .addComponent(assigneeComboBox)
                            .addComponent(supervisorComboBox)
                            .addComponent(projectStatusLabel)))
            .addGroup(contentLayout.createSequentialGroup().addComponent(radioButtonsPanel)));

    contentLayout.setVerticalGroup(
        contentLayout
            .createSequentialGroup()
            .addGroup(
                contentLayout
                    .createParallelGroup()
                    .addComponent(titleLabel)
                    .addComponent(titleTextField))
            .addGroup(
                contentLayout
                    .createParallelGroup()
                    .addComponent(deadlineLabel)
                    .addComponent(datePanel))
            .addGroup(
                contentLayout
                    .createParallelGroup()
                    .addComponent(descriptionLabel)
                    .addComponent(descriptionScrollPane, 80, 80, 80))
            .addGroup(
                contentLayout
                    .createParallelGroup()
                    .addComponent(assigneeLabel)
                    .addComponent(assigneeComboBox))
            .addGroup(
                contentLayout
                    .createParallelGroup()
                    .addComponent(supervisorLabel)
                    .addComponent(supervisorComboBox))
            .addGroup(
                contentLayout
                    .createParallelGroup()
                    .addComponent(statusLabel)
                    .addComponent(projectStatusLabel))
            .addGap(20)
            .addComponent(radioButtonsPanel));
    this.add(contentPanel, BorderLayout.CENTER);
  }

  private void initButtonsPanel() {
    saveButton = UIFactory.createButton("Save Project");
    deleteButton = UIFactory.createButton("Delete Project");
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    buttonsPanel.add(saveButton);
    buttonsPanel.add(deleteButton);
    this.add(buttonsPanel, BorderLayout.SOUTH);
  }

  public void addButtonListener() {
    ButtonListener buttonListener = new ButtonListener();
    saveButton.addActionListener(buttonListener);
    deleteButton.addActionListener(buttonListener);
    toDoButton.addActionListener(buttonListener);
    turnedInButton.addActionListener(buttonListener);
    inProgressButton.addActionListener(buttonListener);
    finishedButton.addActionListener(buttonListener);
  }

  private void enableButtons(boolean enable) {
    saveButton.setVisible(enable);
    deleteButton.setVisible(enable);
  }

  private void enableEditingTextFields(boolean enable) {
    titleTextField.setEditable(enable);
    descriptionTextArea.setEditable(enable);
    deadlineDatePicker.setEnabled(enable);
    assigneeComboBox.setEnabled(enable);
    supervisorComboBox.setEnabled(enable);
  }

  public void enableProjectStatusButtons(
      boolean enableToDo,
      boolean enableInProgress,
      boolean enableTurnedIn,
      boolean enableFinished) {
    toDoButton.setVisible(enableToDo);
    inProgressButton.setVisible(enableInProgress);
    turnedInButton.setVisible(enableTurnedIn);
    finishedButton.setVisible(enableFinished);
  }

  private void enableEditFields(boolean enable) {
    enableEditingTextFields(enable);
    enableButtons(enable);
  }

  public void updatePanel() {
    titleTextField.setText(controller.getProjectTitle());
    descriptionTextArea.setText(controller.getProjectDescription());
    updateStatusLabel();
    setDeadlineDate();
    selectAssigneeFromComboBox();
    selectSupervisorFromComboBox();
    selectProjectStatus();
    enableEditFields(controller.enableEditing());
    controller.selectProjectStatusButtons();
  }

  public void updateStatusLabel() {
    projectStatusLabel.setText(controller.getStatus());
  }

  class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      if (actionEvent.getSource() == saveButton) {
        String title = titleTextField.getText();
        String assignee = Objects.requireNonNull(assigneeComboBox.getSelectedItem()).toString();
        String supervisor = Objects.requireNonNull(supervisorComboBox.getSelectedItem()).toString();
        LocalDate selectedDate =
            LocalDate.of(
                deadlineDatePicker.getModel().getYear(),
                deadlineDatePicker.getModel().getMonth(),
                deadlineDatePicker.getModel().getDay());
        String description = descriptionTextArea.getText();
        controller.saveProject(title, assignee, supervisor, selectedDate, description);
      } else if (actionEvent.getSource() == deleteButton) {
        // todo
      } else if (actionEvent.getSource() instanceof JRadioButton) {
        String selectedStatus = actionEvent.getActionCommand();
        if (!controller.setProjectStatus(selectedStatus)) {
          selectProjectStatus();
        }
      }
    }
  }
}
