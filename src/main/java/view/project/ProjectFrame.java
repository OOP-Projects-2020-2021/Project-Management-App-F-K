package view.project;
import controller.project.ProjectController;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import view.UIFactory;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.*;
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

  private JTextArea commentTextArea;
  private JButton sendButton;
  private JButton saveButton;
  private JButton deleteButton;
  JPanel commentListPanel;
  JPanel detailsPanel;
  JPanel commentPanel;

  private static final Dimension DIMENSION = new Dimension(800, 600);

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
    initDetailsPanel();
    initCommentPanel();
    initSplitPane();
  }

  private void initDataFields() {
    titleTextField = UIFactory.createTextField("title");
    initDatePicker();
    initTextArea();
    initAssigneeComboBox();
    initSupervisorComboBox();
  }
  private void initDatePicker() {
    UtilDateModel dateModel = new UtilDateModel();
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
    DefaultComboBoxModel<String> assigneeModel = new DefaultComboBoxModel<>();
    String[] assignees = {"user1", "user2","user3"};
    for (String s : assignees) {
      assigneeModel.addElement(s);
    }
    assigneeComboBox.setModel(assigneeModel);
  }
  private void initSupervisorComboBox() {
    supervisorComboBox = new JComboBox<>();
    DefaultComboBoxModel<String> supervisorModel = new DefaultComboBoxModel<>();
    String[] supervisors = {"user1", "user2","user3"};
    for (String supervisor : supervisors) {
      supervisorModel.addElement(supervisor);
    }
    supervisorComboBox.setModel(supervisorModel);
  }

  private void initRadioButtons() {
    toDoButton = new JRadioButton(ProjectController.STATUS[0]);
    inProgressButton = new JRadioButton(ProjectController.STATUS[1]);
    turnedInButton = new JRadioButton(ProjectController.STATUS[2]);
    finishedButton = new JRadioButton(ProjectController.STATUS[3]);

    toDoButton.setActionCommand(ProjectController.STATUS[0]);
    inProgressButton.setActionCommand(ProjectController.STATUS[1]);
    turnedInButton.setActionCommand(ProjectController.STATUS[2]);
    finishedButton.setActionCommand(ProjectController.STATUS[3]);
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

  private void initDetailsPanel() {
    detailsPanel = new JPanel(new BorderLayout());
    initDataFields();
    initRadioButtonsPanel();
    initContentPanel();
    initButtonsPanel();
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
    detailsPanel.add(contentPanel, BorderLayout.CENTER);
  }

  private void initButtonsPanel() {
    saveButton = UIFactory.createButton("Save Project");
    deleteButton = UIFactory.createButton("Delete Project");
    JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    buttonsPanel.add(saveButton);
    buttonsPanel.add(deleteButton);
    addButtonListener();
    detailsPanel.add(buttonsPanel, BorderLayout.SOUTH);
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
  private JTextArea createCommentArea(String comment) {
    JTextArea commentArea = new JTextArea(comment);
    commentArea.setLineWrap(true);
    commentArea.setWrapStyleWord(true);
    return commentArea;
  }
  private void initCommentList() {
    commentListPanel = new JPanel(new GridLayout(3,1)); // todo get rows from controller
    for(int i=0;i<3;i++) {
      JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      JLabel senderName = UIFactory.createLabel("user"+i,null);
      JLabel sendingDate = UIFactory.createLabel("2020-12-22",null);
      headerPanel.add(senderName);
      headerPanel.add(new JLabel("-"));
      headerPanel.add(sendingDate);

      JTextArea commentArea = createCommentArea("comment"+i);
      JScrollPane commentScrollPane = new JScrollPane(commentArea);
      commentScrollPane.setPreferredSize(new Dimension(400,40));

      JPanel rowPanel = new JPanel(new BorderLayout());
      rowPanel.add(headerPanel,BorderLayout.NORTH);
      rowPanel.add(commentScrollPane,BorderLayout.CENTER);

      commentListPanel.add(rowPanel);
    }
  }
  private void initCommentPanel() {
    commentPanel = new JPanel();
    GroupLayout commentLayout = new GroupLayout(commentPanel);
    commentLayout.setAutoCreateContainerGaps(true);
    commentLayout.setAutoCreateGaps(true);
    commentPanel.setLayout(commentLayout);
    JLabel commentsLabel = UIFactory.createLabel("Comments:",null);
    initCommentList();
    commentTextArea = createCommentArea("Leave a comment");
    JScrollPane commentScrollPane = new JScrollPane(commentTextArea);
    commentScrollPane.setViewportView(commentTextArea);
    commentScrollPane.addKeyListener(new KeyAdapter() {
      @Override
      public void keyTyped(KeyEvent e) {
        commentTextArea.setText("");
      }
    });
    sendButton = UIFactory.createButton("Send");

    commentLayout.setHorizontalGroup(
            commentLayout.createSequentialGroup()
            .addGroup(commentLayout.createParallelGroup()
                    .addComponent(commentsLabel)
                    .addComponent(commentListPanel)
                    .addGroup(commentLayout.createSequentialGroup()
                            .addComponent(commentScrollPane)
                            .addComponent(sendButton)))
    );
    commentLayout.setVerticalGroup(
            commentLayout.createSequentialGroup()
                    .addComponent(commentsLabel)
                    .addComponent(commentListPanel)
                    .addGroup(commentLayout.createParallelGroup()
                            .addComponent(commentScrollPane)
                            .addComponent(sendButton)));
  }

  private void initSplitPane() {
    JSplitPane splitPane = new JSplitPane();
    splitPane.setLeftComponent(detailsPanel);
    splitPane.setRightComponent(commentPanel);
    this.add(splitPane,BorderLayout.CENTER);
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
