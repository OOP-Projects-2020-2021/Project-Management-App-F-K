package view.project;
import controller.project.ProjectController;
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
 * Displays the details about the project, and allows the user to change the status of the project.
 * The supervisor can edit the details of the project or delete the project.
 *
 *  @author Beata Keresztes
 */
public class ProjectFrame extends JFrame {

  private JFrame parentFrame;
  private ProjectController controller;

  private JTextField titleTextField;

  private JDatePickerImpl deadlineDatePicker;
  private JDatePanelImpl datePanel;
  private UtilDateModel dateModel;

  private JTextArea descriptionTextArea;
  private JScrollPane descriptionScrollPane;
  private JComboBox<String> assigneeComboBox;
  private DefaultComboBoxModel<String> assigneeModel;
  private JComboBox<String> supervisorComboBox;
  private DefaultComboBoxModel<String> supervisorModel;

  private ButtonGroup statusButtonGroup;
  private JPanel buttonsPanel;
  private JRadioButton toDoButton;
  private JRadioButton turnedInButton;
  private JRadioButton inProgressButton;
  private JRadioButton finishedButton;

  private JButton saveButton;
  private JButton deleteButton;
  // todo radio buttons

  private static final String[] status = {"TO DO", "IN PROGRESS", "TURNED IN", "FINISHED"};
  private static final Dimension DIMENSION = new Dimension(600, 600);

  public ProjectFrame(JFrame parentFrame, int projectId) {
    super("Project");
    this.parentFrame = parentFrame;
    this.controller = new ProjectController(this, projectId);
    this.setPreferredSize(DIMENSION);
    this.setMinimumSize(DIMENSION);
    this.setLayout(new BorderLayout());
    initComponents();
    enableEditingTextFields(controller.isSupervisor());
    this.setResizable(false);
    this.setVisible(true);
  }

  private void initComponents() {
    initDataFields();
    initRadioButtonsPanel();
    initContentPanel();
    initButtonsPanel();
  }

  private void initDataFields() {
    titleTextField = UIFactory.createTextField("title");
    initDatePicker();
    initTextArea();
    initTextArea();
    initAssigneeComboBox();
    initSupervisorComboBox();
  }
  private void initDatePicker() {
    dateModel = new UtilDateModel();
    Properties properties = new Properties();
    properties.put("text.today", "Today");
    properties.put("text.month", "Month");
    properties.put("text.year", "Year");
    datePanel = new JDatePanelImpl(dateModel,properties);
    deadlineDatePicker = new JDatePickerImpl(datePanel,new DefaultFormatter());
    deadlineDatePicker.addActionListener(new DateListener());
  }
  private void initTextArea() {
    descriptionTextArea = new JTextArea("description");
    descriptionTextArea.setLineWrap(true);
    descriptionTextArea.setWrapStyleWord(true);
    descriptionScrollPane = new JScrollPane(descriptionTextArea);
    descriptionScrollPane.setVisible(true);
  }
  private void initAssigneeComboBox() {
    assigneeComboBox = new JComboBox<>();
    assigneeModel = new DefaultComboBoxModel<>();
    String[] assignees = {"user1", "user2","user3"};
    for (String s : assignees) {
      assigneeModel.addElement(s);
    }
    assigneeComboBox.setModel(assigneeModel);
  }
  private void initSupervisorComboBox() {
    supervisorComboBox = new JComboBox<>();
    supervisorModel = new DefaultComboBoxModel<>();
    String[] supervisors = {"user1", "user2","user3"};
    for (String supervisor : supervisors) {
      supervisorModel.addElement(supervisor);
    }
    supervisorComboBox.setModel(supervisorModel);
  }

  private void initRadioButtons() {
    toDoButton = new JRadioButton(status[0]);
    inProgressButton = new JRadioButton(status[1]);
    turnedInButton = new JRadioButton(status[2]);
    finishedButton = new JRadioButton(status[3]);

    toDoButton.setActionCommand(status[0]);
    inProgressButton.setActionCommand(status[1]);
    turnedInButton.setActionCommand(status[2]);
    finishedButton.setActionCommand(status[3]);
  }

  private void createRadioButtonsGroup() {
    // add the buttons to a group so that only one can be selected at a time
    statusButtonGroup = new ButtonGroup();
    statusButtonGroup.add(toDoButton);
    statusButtonGroup.add(inProgressButton);
    statusButtonGroup.add(turnedInButton);
    statusButtonGroup.add(finishedButton);
  }

  private void initRadioButtonsPanel() {
    initRadioButtons();
    createRadioButtonsGroup();
    buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonsPanel.add(toDoButton);
    buttonsPanel.add(inProgressButton);
    buttonsPanel.add(turnedInButton);
    buttonsPanel.add(finishedButton);
  }

  private void initContentPanel() {
    JPanel contentPanel = new JPanel();
    GroupLayout contentLayout = new GroupLayout(contentPanel);
    contentLayout.setAutoCreateGaps(true);
    contentLayout.setAutoCreateContainerGaps(true);
    contentPanel.setLayout(contentLayout);
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

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
            .addGroup(contentLayout.createSequentialGroup().addComponent(buttonsPanel)));

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
            .addComponent(buttonsPanel));
    this.add(contentPanel, BorderLayout.CENTER);
  }

  private void initButtonsPanel() {
    saveButton = UIFactory.createButton("Save Project");
    deleteButton = UIFactory.createButton("Delete Project");

    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    buttonsPanel.add(saveButton);
    buttonsPanel.add(deleteButton);

    addButtonListener();

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

  private void enableEditingTextFields(boolean enable) {
    titleTextField.setEditable(enable);
    descriptionTextArea.setEditable(enable);
    assigneeComboBox.setEnabled(enable);
    supervisorComboBox.setEnabled(enable);
    finishedButton.setVisible(enable);
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
        System.out.println(selectedDate);
    }
  }
}
