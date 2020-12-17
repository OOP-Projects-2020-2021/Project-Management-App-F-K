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


    private JFrame parentFrame;
    private static final Dimension DIMENSION = new Dimension(450, 400);

    public TeamFrame(JFrame parentFrame) {
        super("Team"); // todo place here the team name and pass team to controller
        this.parentFrame = parentFrame;
        this.setSize(DIMENSION);
        this.setResizable(false);
        this.setJMenuBar(teamMenuBar);
        addTabbedPanes();
        this.addWindowListener(new projectWindowAdapter());
        this.setVisible(true);
    }

    private void initTabbedPanes() {
        homeTab = new TeamHomePanel(DIMENSION);
        membersTab = new TeamMembersPanel(DIMENSION);
        projectsTab = new JPanel();
    }
    private void addTabbedPanes() {
        mainPane = new JTabbedPane();
        initTabbedPanes();
        mainPane.addTab("Home",homeTab);
        mainPane.addTab("Members",membersTab);
        mainPane.add("Projects",projectsTab);

        mainPane.setMnemonicAt(0, KeyEvent.VK_H);
        mainPane.setMnemonicAt(1,KeyEvent.VK_M);
        mainPane.setMnemonicAt(2,KeyEvent.VK_P);
        this.add(mainPane);
    }

    private void initProjectsPane() {
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
