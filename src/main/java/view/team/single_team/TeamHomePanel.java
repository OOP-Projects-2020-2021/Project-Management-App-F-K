package view.team.single_team;

import controller.team.single_team.TeamSettingsController;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * This panel displays general information about a team. The general user can only view the listed
 * data, but the manager can edit the team's attributes.
 *
 * @author Beata Keresztes
 */
public class TeamHomePanel extends JPanel implements ActionListener {

  private JTextField teamNameTextField;
  private JLabel teamCodeLabel;
  private JTextField teamManagerTextField;
  private JLabel savedLabel;

  private JButton saveTeamNameButton;
  private JButton saveTeamManagerButton;
  private JButton regenerateCodeButton;
  private JButton leaveTeamButton;

  private TeamSettingsController controller;

  public TeamHomePanel(JFrame frame, Dimension frameDimension, int currentTeamId) {
    controller = new TeamSettingsController(this, frame, currentTeamId);
    this.setPreferredSize(frameDimension);
    this.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
    initHomePane();
  }

  private void initHomePane() {
    GroupLayout homeLayout = new GroupLayout(this);
    homeLayout.setAutoCreateGaps(true);
    homeLayout.setAutoCreateContainerGaps(true);
    this.setLayout(homeLayout);

    initHomePaneComponents();

    JLabel codeLabel = UIFactory.createLabel("Code:", null);
    JLabel nameLabel = UIFactory.createLabel("Name:", null);
    JLabel managerLabel = UIFactory.createLabel("Manager:", null);

    homeLayout.setHorizontalGroup(
        homeLayout
            .createSequentialGroup()
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(codeLabel)
                        .addComponent(nameLabel)
                        .addComponent(managerLabel)
                        .addComponent(leaveTeamButton))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(teamCodeLabel)
                        .addComponent(teamNameTextField)
                        .addComponent(teamManagerTextField))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(regenerateCodeButton)
                        .addComponent(saveTeamNameButton)
                        .addComponent(saveTeamManagerButton)
                        .addComponent(savedLabel)));

    homeLayout.setVerticalGroup(
        homeLayout
            .createSequentialGroup()
                .addGroup(
                        homeLayout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(codeLabel)
                                .addComponent(teamCodeLabel)
                                .addComponent(regenerateCodeButton))
                .addGap(30)
                .addGroup(
                        homeLayout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nameLabel)
                                .addComponent(teamNameTextField)
                                .addComponent(saveTeamNameButton))
                .addGroup(
                        homeLayout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(managerLabel)
                                .addComponent(teamManagerTextField)
                                .addComponent(saveTeamManagerButton))
                .addGap(30)

                .addGroup(
                        homeLayout
                                .createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(leaveTeamButton)
                                .addComponent(savedLabel)));
  }

  private void initHomePaneComponents() {
    teamNameTextField = UIFactory.createTextField(controller.getTeamName());
    teamCodeLabel = UIFactory.createLabel(controller.getTeamCode(), null);
    teamManagerTextField = UIFactory.createTextField(controller.getTeamManagerName());
    teamNameTextField.setEditable(false);
    teamManagerTextField.setEditable(false);

    HomeMouseAdapter homeMouseAdapter = new HomeMouseAdapter();
    teamNameTextField.addMouseListener(homeMouseAdapter);
    teamManagerTextField.addMouseListener(homeMouseAdapter);

    saveTeamNameButton = UIFactory.createButton("Save");
    saveTeamManagerButton = UIFactory.createButton("Save");
    regenerateCodeButton = UIFactory.createButton("Regenerate code");
    enableButtons(controller.getManagerAccess());

    savedLabel = UIFactory.createLabel("*Saved.", null);
    savedLabel.setVisible(false);
    leaveTeamButton = UIFactory.createButton("Leave Team");
    addButtonListeners();
  }

  public void enableButtons(boolean enable) {
    saveTeamNameButton.setVisible(enable);
    saveTeamManagerButton.setVisible(enable);
    regenerateCodeButton.setVisible(enable);
  }

  public void updateHomePaneComponents() {
    teamNameTextField.setText(controller.getTeamName());
    teamManagerTextField.setText(controller.getTeamManagerName());
    teamCodeLabel.setText(controller.getTeamCode());
  }

  private void addButtonListeners() {
    leaveTeamButton.addActionListener(this);
    saveTeamNameButton.addActionListener(this);
    saveTeamManagerButton.addActionListener(this);
    regenerateCodeButton.addActionListener(this);
  }

  public void updateNameFieldAfterSave() {
    teamNameTextField.setEditable(false);
    savedLabel.setVisible(true);
  }

  public void updateManagerFieldAfterSave() {
    teamManagerTextField.setEditable(false);
    savedLabel.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if (source == leaveTeamButton) {
      controller.confirmLeavingTeam();
    } else {
      if (source == saveTeamNameButton) {
        controller.saveTeamName(teamNameTextField.getText());
      } else if(source == saveTeamManagerButton) {
        controller.saveTeamManager(teamManagerTextField.getText());
      } else if (source == regenerateCodeButton) {
        controller.regenerateTeamCode();
      }
    }
  }

  private class HomeMouseAdapter extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent evt) {
      if (controller.getManagerAccess()) {
        if (evt.getSource() == teamNameTextField) {
          teamNameTextField.setEditable(true);
        } else if (evt.getSource() == teamManagerTextField) {
          teamManagerTextField.setEditable(true);
        }
        savedLabel.setVisible(false);
      }
    }
  }
}
