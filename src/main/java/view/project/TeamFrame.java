package view.project;

import model.team.Team;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The project frame allows the user to view the team's details, the members list and the projects.
 * Additionally the manager also has access to the teams settings and adding/removing members.
 * From the frame's dropdown menu, the user can choose to go back to the main page or leave the team.
 *
 * @author Beata Keresztes
 */
public class TeamFrame extends JFrame implements ActionListener {

    /** Components and items of the drop-down menu. */
    private JMenuBar teamMenuBar;
    private JMenu teamMenu;
    private JMenuItem teamSettingsItem;
    private JMenuItem projectListItem;
    private JMenuItem leaveTeamItem;
    private JMenuItem backItem;

    private JTabbedPane mainPane;
    private JPanel homeTab;
    private JPanel membersTab;
    private JPanel projectsTab;

    private JTextField teamNameTextField;
    private JLabel teamCodeLabel;
    private JTextField teamManagerTextField;
    private JButton editButton;
    private JButton saveTeamNameButton;
    private JButton generateCodeButton;
    private JButton saveTeamManagerButton;
    private JLabel savedLabel;

    private JFrame parentFrame;
    private static final Dimension DIMENSION = new Dimension(400, 400);

    public TeamFrame(JFrame parentFrame) {
        super("Team"); // todo place here the team name and pass team to controller
        this.parentFrame = parentFrame;
        this.setSize(DIMENSION);
        this.setResizable(false);
        initMenuComponents();
        this.setJMenuBar(teamMenuBar);
        // add components here
        initHomePane();
        initMembersPane();
        initProjectsPane();
        addTabbedPane();
        this.pack();
        this.addWindowListener(new projectWindowAdapter());
        this.setVisible(true);
    }

    private void initMenuComponents() {
        teamMenuBar = new JMenuBar();

        teamMenu = new JMenu("Team Menu");
        teamMenu.setPreferredSize(new Dimension(100,25));

        teamSettingsItem = new JMenuItem("Team Settings");
        projectListItem = new JMenuItem("Projects");
        leaveTeamItem = new JMenuItem("Leave team");
        backItem = new JMenuItem("Back");

        teamSettingsItem.setMnemonic(KeyEvent.VK_S);
        projectListItem.setMnemonic(KeyEvent.VK_P);
        leaveTeamItem.setMnemonic(KeyEvent.VK_L);
        backItem.setMnemonic(KeyEvent.VK_B);

        teamSettingsItem.addActionListener(this);
        projectListItem.addActionListener(this);
        leaveTeamItem.addActionListener(this);
        backItem.addActionListener(this);

        teamMenu.add(teamSettingsItem);
        teamMenu.add(projectListItem);
        teamMenu.add(leaveTeamItem);
        teamMenu.add(backItem);

        teamMenuBar.add(teamMenu);
    }
    private void addTabbedPane() {
        mainPane = new JTabbedPane();
        mainPane.addTab("Home",homeTab);
        mainPane.addTab("Members",membersTab);
        mainPane.add("Projects",projectsTab);

        mainPane.setMnemonicAt(0, KeyEvent.VK_H);
        mainPane.setMnemonicAt(1,KeyEvent.VK_M);
        mainPane.setMnemonicAt(2,KeyEvent.VK_P);
        this.add(mainPane);
    }
    private void initHomePane() {
        homeTab = new JPanel();
        homeTab.setPreferredSize(DIMENSION);
        GroupLayout homeLayout = new GroupLayout(homeTab);
        homeLayout.setAutoCreateGaps(true);
        homeLayout.setAutoCreateContainerGaps(true);
        homeTab.setLayout(homeLayout);
        initHomePaneComponents();
        JLabel nameLabel = UIFactory.createLabel("Name:",null);
        JLabel codeLabel = UIFactory.createLabel("Code:",null);
        JLabel managerLabel = UIFactory.createLabel("Manager:",null);

        homeLayout.setHorizontalGroup(homeLayout.createSequentialGroup()
                        .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nameLabel)
                                .addComponent(codeLabel)
                                .addComponent(managerLabel)
                                .addComponent(editButton))
                        .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(teamNameTextField)
                                .addComponent(teamCodeLabel)
                                .addComponent(teamManagerTextField)
                                .addComponent(savedLabel))
                        .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(saveTeamNameButton)
                                .addComponent(generateCodeButton)
                                .addComponent(saveTeamManagerButton)));

        homeLayout.setVerticalGroup(homeLayout.createSequentialGroup()
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(teamNameTextField)
                        .addComponent(saveTeamNameButton))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(codeLabel)
                        .addComponent(teamCodeLabel)
                        .addComponent(generateCodeButton))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(managerLabel)
                        .addComponent(teamManagerTextField)
                        .addComponent(saveTeamManagerButton))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(editButton)
                        .addComponent(savedLabel)));

    }
    private void initHomePaneComponents() {
        // TODO get data from controller
        teamNameTextField = UIFactory.createTextField("name");
        teamCodeLabel = UIFactory.createLabel("code",null);
        teamManagerTextField = UIFactory.createTextField("manager");
        editButton = UIFactory.createButton("Edit");
        saveTeamNameButton = UIFactory.createButton("Save");
        generateCodeButton = UIFactory.createButton("Generate code");
        saveTeamManagerButton = UIFactory.createButton("Save");
        savedLabel = UIFactory.createLabel("*Saved.",null);
    }
    public void enableSaveButtons(boolean enableSave) {
        saveTeamNameButton.setVisible(enableSave);
        saveTeamManagerButton.setVisible(enableSave);
        generateCodeButton.setVisible(enableSave);
    }
    public void enableEditTextFields(boolean enableEdit) {
        teamNameTextField.setEditable(enableEdit);
        teamManagerTextField.setEditable(enableEdit);
    }
    public void showEditButton(boolean showEdit) {
        editButton.setVisible(showEdit);
    }
    private void updateHomePaneComponents(Team team) {
        // todo from controller
        teamNameTextField.setText("");
        teamCodeLabel.setText("");
        teamManagerTextField.setText("");
    }
    public void showSavedLabel(boolean showSave) {
        savedLabel.setVisible(showSave);
    }

    private void initMembersPane() {
        membersTab = new JPanel();
        membersTab.setPreferredSize(DIMENSION);
    }
    private void initProjectsPane() {
        projectsTab = new JPanel();
        projectsTab.setPreferredSize(DIMENSION);
    }
    private class projectWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
