package view.main;
import view.team.TeamListPanel;
import javax.swing.*;

/**
 * This is the main frame of the application, which provides access to the main menu and displays
 * the icons of all the user's teams.
 *
 * @author Bori Fazakas
 */
public class MainFrame extends JFrame {
  public MainFrame() {
    this.add(new JScrollPane(new TeamListPanel()));
    this.setTitle("Project Management App");
    this.setJMenuBar(new MainMenu(this));
    this.setSize(600, 400);
    this.setVisible(true);
  }
}
