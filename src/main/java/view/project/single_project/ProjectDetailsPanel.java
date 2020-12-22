package view.project.single_project;

import controller.project.single_project.ProjectDetailsController;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import view.UIFactory;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;

/**
 * Allows the user to view the details about a given project, such as the title, description,
 * deadline, etc. Only the supervisor of the project can edit the details of a project or pass the
 * supervisor position.
 *
 * @author Beata Keresztes
 */
public class ProjectDetailsPanel extends JPanel {

  private JDatePickerImpl deadlineDatePicker;
  private JDatePanelImpl datePanel;

  private JTextField titleTextField;
  private JTextArea descriptionTextArea;
  private JScrollPane descriptionScrollPane;
  private JComboBox<String> assigneeComboBox;
  private JComboBox<String> supervisorComboBox;
  private JPanel radioButtonsPanel;
  private JRadioButton toDoButton;
  private JRadioButton turnedInButton;
  private JRadioButton inProgressButton;
  private JRadioButton finishedButton;
  private JButton saveButton;
  private JButton deleteButton;

  private ProjectDetailsController controller;

  public ProjectDetailsPanel(int projectId) {
    controller = new ProjectDetailsController(projectId);
    this.setLayout(new BorderLayout());
    initDetailsPanel();
    enableEditingTextFields(controller.isSupervisor());
    enableButtons(controller.isSupervisor());
  }

  private void initDetailsPanel() {
    initDataFields();
    initRadioButtonsPanel();
    initContentPanel();
    initButtonsPanel();
    addButtonListener();
  }

  private void initDataFields() {
    // todo
    titleTextField = UIFactory.createTextField("title");
    initDatePicker();
    initDescriptionTextArea();
    initAssigneeComboBox();
    initSupervisorComboBox();
  }

  private void initDatePicker() {
    // todo
    UtilDateModel dateModel = new UtilDateModel();
    // dateModel.setDate();
    Properties properties = new Properties();
    properties.put("text.today", "Today");
    properties.put("text.month", "Month");
    properties.put("text.year", "Year");
    datePanel = new JDatePanelImpl(dateModel, properties);
    deadlineDatePicker = new JDatePickerImpl(datePanel, new DefaultFormatter());
    deadlineDatePicker.addActionListener(new DateListener());
  }

  private void initDescriptionTextArea() {
    // todo
    descriptionTextArea = new JTextArea("description");
    descriptionTextArea.setLineWrap(true);
    descriptionTextArea.setWrapStyleWord(true);
    descriptionScrollPane = new JScrollPane(descriptionTextArea);
  }

  private void initAssigneeComboBox() {
    // todo
    assigneeComboBox = new JComboBox<>();
    DefaultComboBoxModel<String> assigneeModel = new DefaultComboBoxModel<>();
    String[] assignees = {"user1", "user2", "user3"};
    for (String s : assignees) {
      assigneeModel.addElement(s);
    }
    assigneeComboBox.setModel(assigneeModel);
  }

  private void initSupervisorComboBox() {
    // todo
    supervisorComboBox = new JComboBox<>();
    DefaultComboBoxModel<String> supervisorModel = new DefaultComboBoxModel<>();
    String[] supervisors = {"user1", "user2", "user3"};
    for (String supervisor : supervisors) {
      supervisorModel.addElement(supervisor);
    }
    supervisorComboBox.setModel(supervisorModel);
  }

  private void enableEditingTextFields(boolean enable) {
    titleTextField.setEditable(enable);
    descriptionTextArea.setEditable(enable);
    assigneeComboBox.setEnabled(enable);
    supervisorComboBox.setEnabled(enable);
    finishedButton.setVisible(enable);
  }

  private void initRadioButtons() {
    toDoButton = new JRadioButton(ProjectDetailsController.STATUS[0]);
    inProgressButton = new JRadioButton(ProjectDetailsController.STATUS[1]);
    turnedInButton = new JRadioButton(ProjectDetailsController.STATUS[2]);
    finishedButton = new JRadioButton(ProjectDetailsController.STATUS[3]);

    toDoButton.setActionCommand(ProjectDetailsController.STATUS[0]);
    inProgressButton.setActionCommand(ProjectDetailsController.STATUS[1]);
    turnedInButton.setActionCommand(ProjectDetailsController.STATUS[2]);
    finishedButton.setActionCommand(ProjectDetailsController.STATUS[3]);
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
                            .addComponent(supervisorLabel))
                    .addGroup(
                        contentLayout
                            .createParallelGroup()
                            .addComponent(titleTextField)
                            .addComponent(datePanel)
                            .addComponent(descriptionScrollPane)
                            .addComponent(assigneeComboBox)
                            .addComponent(supervisorComboBox)))
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

  class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      if (actionEvent.getSource() == saveButton) {
        // todo
      } else if (actionEvent.getSource() == deleteButton) {
        // todo
      } else if (actionEvent.getSource() instanceof JRadioButton) {
        String selectedStatus = actionEvent.getActionCommand();
        // todo
      }
    }
  }

  class DateListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      Date selectedDate = (Date) deadlineDatePicker.getModel().getValue();
      // todo
    }
  }
}
