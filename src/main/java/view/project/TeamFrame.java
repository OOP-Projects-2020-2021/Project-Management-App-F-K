package view.project;
import controller.project.TeamController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Displays the projects of the user.
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

    /** Labels for displaying details about a team.*/
    private JLabel teamNameLabel;
    private JLabel teamCodeLabel;
    private JLabel teamManagerNameLabel;
    private JTextArea teamDescriptionArea;

    // todo move it to the Project frame
    private JButton newProjectButton;

    /**The member-list is only for viewing the members of the team. It's selection doesn't trigger any action.*/
    private JList<String> memberList;

    private TeamController teamController;


    private static final Dimension DIMENSION = new Dimension(400, 400);

    public TeamFrame() {
        super("Team");
        this.setSize(DIMENSION);
        this.setResizable(false);
        initMenuComponents();
        this.setJMenuBar(teamMenuBar);
        this.teamController = new TeamController(this);
        this.setLayout(new BorderLayout());
        initmainPanel();
        this.addWindowListener(new TeamWindowAdapter());
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

    private void initmainPanel() {

        JPanel headerPanel = new JPanel(new GridLayout(3,1));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10,20,10,20));

        teamNameLabel = new JLabel(teamController.getTeamName());
        teamNameLabel.setHorizontalAlignment(JLabel.RIGHT);
        teamCodeLabel = new JLabel(teamController.getTeamCode());
        teamCodeLabel.setHorizontalAlignment(JLabel.RIGHT);
        teamManagerNameLabel = new JLabel(teamController.getManagerName());
        teamManagerNameLabel.setHorizontalAlignment(JLabel.RIGHT);

        headerPanel.add(teamNameLabel);
        headerPanel.add(teamCodeLabel);
        headerPanel.add(teamManagerNameLabel);

        JPanel mainPanel = new JPanel(new GridLayout(2,1,20,20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        JPanel descriptionPanel = new JPanel(new BorderLayout());

        teamDescriptionArea = new JTextArea(teamController.getTeamDescription());
        teamDescriptionArea.setLineWrap(true);
        teamDescriptionArea.setWrapStyleWord(true);
        teamDescriptionArea.setEditable(false);
        descriptionPanel.add(teamDescriptionArea,BorderLayout.CENTER);

        JScrollPane scrollPaneDescription = new JScrollPane(teamDescriptionArea);
        scrollPaneDescription.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
        descriptionPanel.add(scrollPaneDescription);

        JPanel membersPanel = new JPanel(new GridLayout(1,2));
        initLists();
        JPanel listPanel = new JPanel(new BorderLayout());
        JScrollPane scrollPaneList = new JScrollPane();
        scrollPaneList.setViewportView(memberList);
        memberList.setLayoutOrientation(JList.VERTICAL);
        listPanel.add(scrollPaneList);
        membersPanel.add(new JLabel("Members list:"));
        membersPanel.add(listPanel);

        mainPanel.add(descriptionPanel);
        mainPanel.add(membersPanel);

        newProjectButton = new JButton(" + New Project");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(newProjectButton);

        this.add(headerPanel,BorderLayout.NORTH);
        this.add(mainPanel,BorderLayout.CENTER);
        this.add(buttonPanel,BorderLayout.SOUTH);
    }

    private void initLists() {
        // todo get members and projects from team/project repo
        String[] Strings = { "Bird", "Cat", "Dog", "Rabbit", "Pig", "Hamster", "Parrot", "Goldfish"};
        memberList = new JList(Strings);

    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source == teamSettingsItem) {

        }else if(source == projectListItem) {

        }
    }
    private class TeamWindowAdapter extends WindowAdapter {
        @Override
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
