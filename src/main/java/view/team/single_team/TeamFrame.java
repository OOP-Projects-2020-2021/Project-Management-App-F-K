package view.team.single_team;

import controller.team.single_team.TeamController;
import view.UIFactory;
import view.project.ProjectsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The TeamFrame allows the user to view the team's details, the members list and the projects in
 * separate tabs. Additionally the manager also has access to the teams settings and adding/removing
 * members. Upon closing this frame, the user will be redirected to the main frame.
 *
 * @author Beata Keresztes
 */
public class TeamFrame extends JFrame implements ActionListener {

  private JPanel homeTab;
  private JPanel membersTab;
  private JPanel projectsTab;

  private JFrame parentFrame;
  private TeamController controller;
  private JButton createProjectButton;
  private static final Dimension DIMENSION = new Dimension(600, 600);

  public TeamFrame(JFrame parentFrame, int teamId) {
    super("Team");
    this.parentFrame = parentFrame;
    this.controller = new TeamController(this.parentFrame, teamId);
    this.setSize(DIMENSION);
    addTabbedPanes();
    this.addWindowListener(new TeamWindowAdapter());
    this.setVisible(true);
  }

  private void initTabbedPanes() {
    homeTab = new TeamHomePanel(this, controller.getTeamId());
    membersTab = new TeamMembersPanel(this, controller.getTeamId());
    initProjectsTab();
  }

  private void initProjectsTab() {
    projectsTab = new JPanel(new BorderLayout());
    initProjectButtonPanel();
    initProjectListPanel();
  }

  private void initProjectButtonPanel() {
    JPanel projectButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    createProjectButton = UIFactory.createButton("Create Project");
    createProjectButton.addActionListener(this);
    projectButtonPanel.add(UIFactory.createLabel("Create a new project:", null));
    projectButtonPanel.add(createProjectButton);
    projectsTab.add(projectButtonPanel, BorderLayout.BEFORE_FIRST_LINE);
  }

  private void initProjectListPanel() {
    JPanel projectsListPanel = new ProjectsPanel(this, controller.getTeamId());
    projectsTab.add(projectsListPanel, BorderLayout.CENTER);
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
    this.add(mainPane);
  }

  private class TeamWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      controller.onClose(parentFrame);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == createProjectButton) {
      controller.enableProjectCreation(this);
    }
  }
}
