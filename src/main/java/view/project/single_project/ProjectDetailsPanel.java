package view.project.single_project;

import controller.project.single_project.ProjectDetailsController;
import model.project.Project;
import model.user.User;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
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
  private JComboBox<Project.Importance> importanceComboBox;

  private ProjectStatusButtonsPanel statusButtonsPanel;
  private JPanel projectStatusPanel;

  private JButton saveButton;
  private JButton deleteButton;
  private JPanel buttonsPanel;

  private ProjectDetailsController controller;

  public ProjectDetailsPanel(JFrame frame, Project project) {
    controller = new ProjectDetailsController(frame, project, this);
    this.setLayout(new BorderLayout());
    statusButtonsPanel = new ProjectStatusButtonsPanel(frame, project);
    initDetailsPanel();
  }

  private void initDetailsPanel() {
    initDataFields();
    initButtonsPanel();
    addButtonListener();
    initProjectStatusPanel();
    initImportanceComboBox();
    initContentPanel();
    enableEditFields(controller.enableEditing());
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
    deadlineDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
  }

  private void initImportanceComboBox() {
    importanceComboBox = new JComboBox<>();
    importanceComboBox.setModel(new DefaultComboBoxModel<>(Project.Importance.values()));
    importanceComboBox.setSelectedItem(controller.getProjectImportance());
  }

  public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {

    private String datePattern = "yyyy-MM-dd";
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

    @Override
    public Object stringToValue(String text) throws ParseException {
      return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
      if (value != null) {
        Calendar cal = (Calendar) value;
        return dateFormatter.format(cal.getTime());
      }

      return "";
    }
  }

  private void setDeadlineDate() {
    dateModel.setDate(
        controller.getProjectDeadline().getYear(),
        controller.getProjectDeadline().getMonthValue() - 1, // deadlineDatePicker counts from 0,
        // but LocalDate from 1
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
    JLabel importanceLabel = UIFactory.createLabel("*Importance:", null);

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
                            .addComponent(importanceLabel))
                    .addGroup(
                        contentLayout
                            .createParallelGroup()
                            .addComponent(titleTextField)
                            .addComponent(deadlineDatePicker)
                            .addComponent(descriptionScrollPane)
                            .addComponent(assigneeComboBox)
                            .addComponent(supervisorComboBox)
                            .addComponent(importanceComboBox)))
            .addComponent(buttonsPanel)
            .addComponent(projectStatusPanel));

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
                    .addComponent(deadlineDatePicker))
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
                    .addComponent(importanceLabel)
                    .addComponent(importanceComboBox))
            .addGap(20)
            .addComponent(buttonsPanel)
            .addGap(30)
            .addComponent(projectStatusPanel));
    this.add(contentPanel, BorderLayout.CENTER);
  }

  private void initProjectStatusPanel() {
    JLabel statusLabel = UIFactory.createLabel("Status:", null);

    projectStatusPanel = new JPanel();
    GroupLayout projectStatusLayout = new GroupLayout(projectStatusPanel);
    projectStatusPanel.setLayout(projectStatusLayout);
    projectStatusLayout.setAutoCreateGaps(true);
    projectStatusLayout.setAutoCreateContainerGaps(true);

    projectStatusLayout.setHorizontalGroup(
        projectStatusLayout
            .createParallelGroup()
            .addGroup(
                projectStatusLayout
                    .createSequentialGroup()
                    .addGap(20)
                    .addComponent(statusLabel)
                    .addComponent(projectStatusLabel))
            .addComponent(statusButtonsPanel));

    projectStatusLayout.setVerticalGroup(
        projectStatusLayout
            .createSequentialGroup()
            .addGroup(
                projectStatusLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(statusLabel)
                    .addComponent(projectStatusLabel))
            .addComponent(statusButtonsPanel)
            .addGap(20));
  }

  private void initButtonsPanel() {
    saveButton = UIFactory.createButton("Save Project");
    deleteButton = UIFactory.createButton("Delete Project");
    buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    buttonsPanel.add(saveButton);
    buttonsPanel.add(deleteButton);
  }

  public void addButtonListener() {
    ButtonListener buttonListener = new ButtonListener();
    saveButton.addActionListener(buttonListener);
    deleteButton.addActionListener(buttonListener);
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
    importanceComboBox.setEnabled(enable);
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
    enableEditFields(controller.enableEditing());
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
                deadlineDatePicker.getModel().getMonth() + 1, // deadlineDatePicker counts from 0,
                // but LocalDate from 1
                deadlineDatePicker.getModel().getDay());
        String description = descriptionTextArea.getText();
        Project.Importance importance = (Project.Importance) importanceComboBox.getSelectedItem();
        controller.saveProject(title, assignee, supervisor, selectedDate, description, importance);
      } else if (actionEvent.getSource() == deleteButton) {
        controller.deleteProject();
      }
    }
  }
}
