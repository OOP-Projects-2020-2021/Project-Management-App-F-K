package view.main;

import controller.MainMenuController;
import view.team.TeamListPanel;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
    this.addWindowListener(new MainWindowAdapter());
  }

  /** Returns a reference to the controller of the Main menu bar. */
  private MainMenuController getController() {
    return ((MainMenu) this.getJMenuBar()).getController();
  }

  /**
   * When the main frame is closed but not because the user logged out, then the application stops
   * running.
   */
  private class MainWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      if (!getController().getLogOutFlag()) {
        System.exit(0);
      }
    }
  }
}
