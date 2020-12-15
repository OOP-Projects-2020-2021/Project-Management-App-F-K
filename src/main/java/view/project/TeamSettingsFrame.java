package view.project;

import controller.project.TeamSettingsController;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TeamSettingsFrame extends JFrame implements ActionListener {
    private JLabel nameLabel;
    private JLabel codeLabel;
    private JLabel managerLabel;
    private JLabel descriptionLabel;

    private JTextField nameTextField;
    private JTextField codeTextField;
    private JTextField managerTextField;
    private JTextArea descriptionTextArea;

    private JButton saveNameButton;
    private JButton saveManagerButton;
    private JButton saveDescriptionButton;
    private JButton changeCodeButton;
    private JButton addMemberButton;
    private JButton removeMemberButton;
    private TeamSettingsController teamSettingsController;

    private static final Dimension DIMENSION = new Dimension(500, 300);

    public TeamSettingsFrame() {
        super("Team Settings");
        this.setSize(DIMENSION);
        this.setResizable(false);
        this.setLayout(new BorderLayout());
        this.teamSettingsController = new TeamSettingsController(this);
        initComponents();
        addComponentsToPanel();
        this.addWindowListener(new TeamSettingsWindowAdapter());
        this.setVisible(true);
    }


    void initComponents() {
        //todo get data from controller
        nameLabel = UIFactory.createLabel("Name:",null);
        nameLabel.setHorizontalAlignment(JLabel.TRAILING);
        nameTextField = UIFactory.createTextField("");
        nameTextField.setColumns(10);
        nameLabel.setLabelFor(nameTextField);

        codeLabel = UIFactory.createLabel("Code:",null);
        codeLabel.setHorizontalAlignment(JLabel.TRAILING);
        codeTextField = UIFactory.createTextField("");
        codeTextField.setColumns(10);
        codeLabel.setLabelFor(codeTextField);
        codeTextField.setEditable(false);

        managerLabel = UIFactory.createLabel("Manager:",null);
        managerLabel.setHorizontalAlignment(JLabel.TRAILING);
        managerTextField = UIFactory.createTextField("");
        managerTextField.setColumns(10);
        managerLabel.setLabelFor(managerTextField);

        descriptionLabel = UIFactory.createLabel("Description:",null);
        descriptionLabel.setHorizontalAlignment(JLabel.TRAILING);
        descriptionTextArea = new JTextArea("");
        descriptionTextArea.setColumns(10);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);
        descriptionLabel.setLabelFor(descriptionTextArea);

        saveNameButton = UIFactory.createButton("Save name");
        changeCodeButton = UIFactory.createButton("Regenerate code");
        saveManagerButton = UIFactory.createButton("Save manager");
        saveDescriptionButton = UIFactory.createButton("Save description");

        addMemberButton = UIFactory.createButton("Add member");
        removeMemberButton = UIFactory.createButton("Remove member");
    }

    void addComponentsToPanel() {

        JPanel descriptionPanel = new JPanel(new BorderLayout());
        descriptionPanel.add(descriptionTextArea,BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(descriptionTextArea);
        scrollPane.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        descriptionPanel.add(scrollPane);

        JPanel dataPanel = new JPanel(new GridLayout(4,3,10,10));
        dataPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        dataPanel.add(nameLabel);
        dataPanel.add(nameTextField);
        dataPanel.add(saveNameButton);
        dataPanel.add(codeLabel);
        dataPanel.add(codeTextField);
        dataPanel.add(changeCodeButton);
        dataPanel.add(managerLabel);
        dataPanel.add(managerTextField);
        dataPanel.add(saveManagerButton);
        dataPanel.add(descriptionLabel);
        dataPanel.add(descriptionPanel);
        dataPanel.add(saveDescriptionButton);

        this.add(dataPanel,BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addMemberButton);
        buttonPanel.add(removeMemberButton);
        this.add(buttonPanel,BorderLayout.SOUTH);
    }
    private String getTeamName() {
        return nameTextField.getText();
    }
    private String getTeamManager() {
        return managerTextField.getText();
    }
    private String getDescription() {
        return descriptionTextArea.getText();
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    private class TeamSettingsWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
