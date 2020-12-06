import javax.swing.*;

public class MainMenu {
    private JMenuBar menuBar = new JMenuBar();

    private JMenu accountMenu = new JMenu("My account");
    private JMenu teamsMenu = new JMenu("My teams");

    // Items for accountMenu.
    private JMenuItem accountSettingsItem = new JMenuItem("Account Settings");
    private JMenuItem logoutItem = new JMenuItem("Sign out");

    // Items for teamsMenu.
    private JMenuItem createTeamItem = new JMenuItem("Create new team");
    private JMenuItem joinTeamItem = new JMenuItem("Join team");

    MainMenu() {
        menuBar.add(accountMenu);
        menuBar.add(teamsMenu);

        accountMenu.add(accountSettingsItem);
        accountMenu.add(logoutItem);

        teamsMenu.add(createTeamItem);
        teamsMenu.add(joinTeamItem);

        //todo set mnemonics
    }

}
