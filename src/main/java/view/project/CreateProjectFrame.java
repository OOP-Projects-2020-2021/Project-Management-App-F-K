package view.project;

import controller.project.CreateProjectController;
import model.project.Project;
import model.user.User;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import view.UIFactory;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

/**
 * The CreateProjectFrame allows the user to create a new project, introducing all the required
 * data, such as the title of the project, the deadline of turning in the specific project and the
 * assignee to whom the project is intended. The creator of the project will automatically become
 * the supervisor.
 *
 * @author Beata Keresztes
 */
public class CreateProjectFrame extends JFrame {

  private JButton saveButton;

  private JDatePickerImpl deadlineDatePicker;
  private JDatePanelImpl datePanel;
  private UtilDateModel dateModel;

  private JTextField titleTextField;
  private JTextArea descriptionTextArea;
  private JScrollPane descriptionScrollPane;

  private JComboBox<String> assigneeComboBox;
  private DefaultComboBoxModel<String> assigneeModel;

  private JComboBox<Project.Importance> importanceComboBox;

  private JFrame parentFrame;
  private CreateProjectController controller;

  private static final Dimension DIMENSION = new Dimension(600, 600);

  public CreateProjectFrame(Integer teamId, JFrame parentFrame) {
    super("New Project");
    this.parentFrame = parentFrame;
    controller = new CreateProjectController(teamId, this);
    this.setMinimumSize(DIMENSION);
    this.setLayout(new BorderLayout());
    initComponents();
    this.setResizable(false);
    this.addWindowListener(new ProjectWindowAdapter());
    this.setVisible(true);
  }

  private void initComponents() {
    initDataFields();
    initContentPanel();
    initButtonsPanel();
  }

  private void initDataFields() {
    titleTextField = UIFactory.createTextField(null);
    initDatePicker();
    initDescriptionTextArea();
    initImportanceComboBox();
    initAssigneeComboBox();
  }

  private void initDatePicker() {
    dateModel = new UtilDateModel();
    Properties properties = new Properties();
    properties.put("text.today", "Today");
    properties.put("text.month", "Month");
    properties.put("text.year", "Year");
    datePanel = new JDatePanelImpl(dateModel, properties);
    deadlineDatePicker = new JDatePickerImpl(datePanel, new DefaultFormatter());
  }

  private void initDescriptionTextArea() {
    descriptionTextArea = new JTextArea();
    descriptionTextArea.setText(null);
    descriptionTextArea.setLineWrap(true);
    descriptionTextArea.setWrapStyleWord(true);
    descriptionScrollPane = new JScrollPane(descriptionTextArea);
  }

  private void initAssigneeComboBox() {
    assigneeComboBox = new JComboBox<>();
    assigneeModel = new DefaultComboBoxModel<>();
    initAssigneeModel();
    assigneeComboBox.setModel(assigneeModel);
  }

  private void initAssigneeModel() {
    List<User> members = controller.getTeamMembers();
    if (members != null) {
      for (User member : members) {
        assigneeModel.addElement(member.getUsername());
      }
    }
  }

  private void initImportanceComboBox() {
    importanceComboBox = new JComboBox<>();
    importanceComboBox.setModel(new DefaultComboBoxModel<>(Project.Importance.values()));
  }

  private void initContentPanel() {
    JPanel contentPanel = new JPanel();
    GroupLayout contentLayout = new GroupLayout(contentPanel);
    contentLayout.setAutoCreateGaps(true);
    contentLayout.setAutoCreateContainerGaps(true);
    contentPanel.setLayout(contentLayout);
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

    JLabel titleLabel = UIFactory.createLabel("*Title:", null);
    JLabel teamLabel = UIFactory.createLabel("*Team:", null);
    teamLabel.setVisible(controller.enableTeamSelection());
    JLabel deadlineLabel = UIFactory.createLabel("*Deadline:", null);
    JLabel descriptionLabel = UIFactory.createLabel("Description:", null);
    JLabel assigneeLabel = UIFactory.createLabel("*Assignee:", null);
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
                            .addComponent(teamLabel)
                            .addComponent(deadlineLabel)
                            .addComponent(descriptionLabel)
                            .addComponent(assigneeLabel)
                            .addComponent(importanceLabel))
                    .addGroup(
                        contentLayout
                            .createParallelGroup()
                            .addComponent(titleTextField)
                            .addComponent(datePanel)
                            .addComponent(descriptionScrollPane)
                            .addComponent(assigneeComboBox)
                            .addComponent(importanceComboBox))));
    contentLayout.setVerticalGroup(
        contentLayout
            .createSequentialGroup()
            .addGroup(
                contentLayout
                    .createParallelGroup()
                    .addComponent(titleLabel)
                    .addComponent(titleTextField))
            .addGroup(contentLayout.createParallelGroup().addComponent(teamLabel))
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
                    .addComponent(importanceLabel)
                    .addComponent(importanceComboBox)));

    this.add(contentPanel, BorderLayout.CENTER);
  }

  private void initButtonsPanel() {
    saveButton = UIFactory.createButton("Save Project");
    getRootPane().setDefaultButton(saveButton);
    saveButton.addActionListener(
        e -> {
          String title = titleTextField.getText();
          Object assignee = assigneeComboBox.getSelectedItem();
          LocalDate deadline =
              LocalDate.of(
                  deadlineDatePicker.getModel().getYear(),
                  deadlineDatePicker.getModel().getMonth() + 1,
                  deadlineDatePicker.getModel().getDay());
          String description = descriptionTextArea.getText();
          Project.Importance importance = (Project.Importance) importanceComboBox.getSelectedItem();
          controller.createProject(title, assignee, deadline, description, importance);
        });

    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    buttonsPanel.add(saveButton);
    this.add(buttonsPanel, BorderLayout.SOUTH);
  }

  class ProjectWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      controller.onClose(parentFrame);
    }
  }
}
