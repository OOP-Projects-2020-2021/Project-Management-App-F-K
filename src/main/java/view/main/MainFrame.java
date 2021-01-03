package view.main;

import controller.MainMenuController;
import view.CloseableComponent;
import view.team.TeamListPanel;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the main frame of the application, which provides access to the main menu and displays
 * the icons of all the user's teams.
 *
 * @author Bori Fazakas
 */
public class MainFrame extends JFrame {

  List<CloseableComponent> closeableComponents = new ArrayList<>();

  public MainFrame() {
    TeamListPanel teamListPanel = new TeamListPanel(this);
    closeableComponents.add(teamListPanel);
    this.add(new JScrollPane(teamListPanel));
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
      for (CloseableComponent closeableComponent : closeableComponents) {
        closeableComponent.onClose();
      }
    }
  }
}
