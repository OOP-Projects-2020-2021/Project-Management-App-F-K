package view.project;

import model.team.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The project frame allows the user to view the team's details, the members list and the projects.
 * Additionally the manager also has access to the teams settings and adding/removing members.
 * From the frame's dropdown menu, the user can choose to go back to the main page or leave the team.
 */
public class ProjectFrame extends JFrame{

    private JTabbedPane mainPane;
    private JPanel homeTab;
    private JPanel membersTab;
    private JPanel projectsTab;

    private JLabel teamNameLabel;
    private JLabel teamCodeLabel;
    private JLabel teamManagerLabel;
    private JTextArea teamDescriptionTextArea;

    private JFrame parentFrame;
    private static final Dimension DIMENSION = new Dimension(400, 400);

    public ProjectFrame(JFrame parentFrame) {
        super("Team"); // todo place here the team name and pass team to controller
        this.parentFrame = parentFrame;
        this.setSize(DIMENSION);
        this.setResizable(false);
        // add components here
        initHomePane();
        initMembersPane();
        initProjectsPane();
        initTabbedPane();
        this.pack();
        this.addWindowListener(new projectWindowAdapter());
        this.setVisible(true);
    }

    private void initTabbedPane() {
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
        JLabel nameLabel = new JLabel("Name:");
        JLabel codeLabel = new JLabel("Code:");
        JLabel managerLabel = new JLabel("Manager:");
        JLabel descriptionLabel = new JLabel("Description:");

        homeLayout.setHorizontalGroup(homeLayout.createSequentialGroup()
                        .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nameLabel)
                                .addComponent(codeLabel)
                                .addComponent(managerLabel)
                                .addComponent(descriptionLabel))
                        .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(teamNameLabel)
                                .addComponent(teamCodeLabel)
                                .addComponent(teamManagerLabel)
                                .addComponent(teamDescriptionTextArea)));

        homeLayout.setVerticalGroup(homeLayout.createSequentialGroup()
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(teamNameLabel))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(codeLabel)
                        .addComponent(teamCodeLabel))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(managerLabel)
                        .addComponent(teamManagerLabel))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(descriptionLabel)
                        .addComponent(teamDescriptionTextArea)));

    }
    private void initHomePaneComponents() {
        // TODO get data from controller
        teamNameLabel = new JLabel("name");
        teamCodeLabel = new JLabel("code");
        teamManagerLabel = new JLabel("manager");
        teamDescriptionTextArea = new JTextArea("description");
        teamDescriptionTextArea.setEditable(false);
    }
    private void updateHomePaneComponents(Team team) {
        // todo from controller
        teamNameLabel.setText("");
        teamCodeLabel.setText("");
        teamManagerLabel.setText("");
        teamDescriptionTextArea.setText("");
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
}
