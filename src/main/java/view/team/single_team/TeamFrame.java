package view.team.single_team;

import controller.team.single_team.TeamController;
import controller.team.single_team.TeamTabs;
import model.PropertyChangeObservable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * The TeamFrame allows the user to view the team's details, the members list and the projects.
 * Additionally the manager also has access to the teams settings and adding/removing members.
 * Upon closing this frame, the user will be redirected to the main frame.
 *
 * It manages three tabs for displaying the team's data, which listen to the changes occurred in this frame, namely
 * when one of the tabs gets selected, it will automatically be updated to display the correct and actual information.
 *
 * @author Beata Keresztes
 */
public class TeamFrame extends JFrame implements PropertyChangeObservable {

  private JPanel homeTab;
  private JPanel membersTab;
  private JPanel projectsTab;

  private JFrame parentFrame;
  private TeamController controller;
  private static final Dimension DIMENSION = new Dimension(600, 500);

  protected PropertyChangeSupport support = new PropertyChangeSupport(this);
  protected final int OLD_VALUE = 1; // dummy data
  protected final int NEW_VALUE = 2; // dummy data, but it must be different from OLD_VALUE

  public void addPropertyChangeListener(PropertyChangeListener pcl) {
    support.addPropertyChangeListener(pcl);
  }

  public void removePropertyChangeListener(PropertyChangeListener pcl) {
    support.removePropertyChangeListener(pcl);
  }

  public TeamFrame(JFrame parentFrame, int teamId) {
    super("Team");
    this.parentFrame = parentFrame;
    this.controller = new TeamController(this.parentFrame, teamId);
    this.setSize(DIMENSION);
    this.setResizable(false);
    addTabbedPanes();
    this.addWindowListener(new TeamWindowAdapter());
    this.setVisible(true);
  }

  private void initTabbedPanes() {
    homeTab = new TeamHomePanel(this, DIMENSION, controller.getCurrentTeamId());
    membersTab = new TeamMembersPanel(this,DIMENSION,controller.getCurrentTeamId());
    projectsTab = new TeamProjectsPanel(DIMENSION);
  }

  private void addTabbedPanes() {
    JTabbedPane mainPane = new JTabbedPane();
    initTabbedPanes();
    mainPane.addTab("Home", homeTab);
    mainPane.addTab("Members", membersTab);
    mainPane.add("Projects", projectsTab);

    mainPane.setMnemonicAt(0, KeyEvent.VK_H);
    mainPane.setMnemonicAt(1, KeyEvent.VK_M);
    mainPane.setMnemonicAt(2, KeyEvent.VK_P);

    mainPane.addChangeListener(e -> {
      TeamTabs viewedTab = selectTab(mainPane.getSelectedIndex());
      if(viewedTab != null) {
        support.firePropertyChange(viewedTab.toString(), OLD_VALUE, NEW_VALUE);
      }
      });
    this.add(mainPane);
  }
  private TeamTabs selectTab(int indexOfTab) {
    if(indexOfTab == 0) {
      return TeamTabs.HOME_TAB;
    }else if(indexOfTab == 1) {
      return TeamTabs.MEMBERS_TAB;
    }else if(indexOfTab == 2) {
      return TeamTabs.PROJECTS_TAB;
    }
    return null;
  }

  private class TeamWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      controller.onClose(parentFrame);
    }
  }
}
