package view.project;

import controller.project.ProjectController;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Beata Keresztes
 */
public class ProjectFrame extends JFrame implements ActionListener{

    private JFrame parentFrame;
    private ProjectController controller;

    private JTextField nameTextField;
    private JTextField deadlineTextField; // todo
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

    private static final String[] status = {"TO DO","IN PROGRESS","TURNED IN","FINISHED"};
    private static final Dimension DIMENSION = new Dimension(500, 400);

    public ProjectFrame(JFrame parentFrame,int projectId) {
        super("Project");
        this.parentFrame = parentFrame;
        this.controller = new ProjectController(this,projectId);
        this.setPreferredSize(DIMENSION);
        this.setMinimumSize(DIMENSION);
        this.setLayout(new BorderLayout());
        initComponents();
        enableEditingTextFields(false); // todo controller
        this.setVisible(true);
    }
    private void initComponents() {
        initDataFields();
        initRadioButtonsPanel();
        initContentPanel();
        initButtonsPanel();
    }
    private void initDataFields() {
        nameTextField = UIFactory.createTextField("name");
        deadlineTextField = UIFactory.createTextField("deadline");
        descriptionTextArea = new JTextArea("description");
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionScrollPane = new JScrollPane(descriptionTextArea);
        descriptionScrollPane.setVisible(true);
        assigneeComboBox = new JComboBox<>();
        assigneeModel = new DefaultComboBoxModel<>();
        String[] assignees = {"bea","anna"};
        for (String s : assignees) {
            assigneeModel.addElement(s);
        }
        assigneeComboBox.setModel(assigneeModel);
        supervisorComboBox = new JComboBox<>();
        supervisorModel = new DefaultComboBoxModel<>();
        String[] supervisors = {"bea","anna"};
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

        toDoButton.addActionListener(this);
        inProgressButton.addActionListener(this);
        finishedButton.addActionListener(this);
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
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JLabel nameLabel = UIFactory.createLabel("Name:",null);
        JLabel deadlineLabel = UIFactory.createLabel("Deadline:",null);
        JLabel descriptionLabel = UIFactory.createLabel("Description:",null);
        JLabel assigneeLabel = UIFactory.createLabel("Assignee:",null);
        JLabel supervisorLabel = UIFactory.createLabel("Supervisor",null);

        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup()
                .addGroup(
                        contentLayout.createSequentialGroup()
                                .addGroup(contentLayout.createParallelGroup()
                                        .addComponent(nameLabel)
                                        .addComponent(deadlineLabel)
                                        .addComponent(descriptionLabel)
                                        .addComponent(assigneeLabel)
                                        .addComponent(supervisorLabel))
                                .addGroup(contentLayout.createParallelGroup()
                                        .addComponent(nameTextField)
                                        .addComponent(deadlineTextField)
                                        .addComponent(descriptionScrollPane)
                                        .addComponent(assigneeComboBox)
                                        .addComponent(supervisorComboBox)))
                .addGroup(contentLayout.createSequentialGroup()
                        .addComponent(buttonsPanel))

        );
        contentLayout.setVerticalGroup(
                contentLayout.createSequentialGroup()
                .addGroup(contentLayout.createParallelGroup()
                        .addComponent(nameLabel)
                        .addComponent(nameTextField))
                .addGroup(contentLayout.createParallelGroup()
                        .addComponent(deadlineLabel)
                        .addComponent(deadlineTextField))
                .addGroup(contentLayout.createParallelGroup()
                        .addComponent(descriptionLabel)
                        .addComponent(descriptionScrollPane,80,80,80))
                .addGroup(contentLayout.createParallelGroup()
                        .addComponent(assigneeLabel)
                        .addComponent(assigneeComboBox))
                .addGroup(contentLayout.createParallelGroup()
                        .addComponent(supervisorLabel)
                        .addComponent(supervisorComboBox))
                .addGap(20)
                .addComponent(buttonsPanel)
        );
        this.add(contentPanel,BorderLayout.CENTER);
    }

    private void initButtonsPanel() {
        saveButton = UIFactory.createButton("Save Project");
        deleteButton = UIFactory.createButton("Delete Project");
        saveButton.addActionListener(this);
        deleteButton.addActionListener(this);
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        buttonsPanel.add(saveButton);
        buttonsPanel.add(deleteButton);
        this.add(buttonsPanel,BorderLayout.SOUTH);
    }

    private void enableEditingTextFields(boolean enable) {
        nameTextField.setEditable(enable);
        descriptionTextArea.setEditable(enable);
        deadlineTextField.setEditable(enable);
        assigneeComboBox.setEnabled(enable);
        supervisorComboBox.setEnabled(enable);
        finishedButton.setVisible(enable);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == saveButton) {
            // todo
        } else if(actionEvent.getSource() == deleteButton) {
            // todo
        } else if(actionEvent.getSource() instanceof JRadioButton) {
            String newStatus = actionEvent.getActionCommand();
            // todo
        }
    }

}
