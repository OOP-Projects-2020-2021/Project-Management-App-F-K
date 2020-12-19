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
 */
public class TeamHomePanel extends JPanel implements ActionListener {

  private JTextField teamNameTextField;
  private JLabel teamCodeLabel;
  private JTextField teamManagerTextField;
  private JLabel savedLabel;

  private JButton saveButton;
  private JButton regenerateCodeButton;
  private JButton leaveTeamButton;

  private TeamSettingsController controller;

  public TeamHomePanel(JFrame frame, Dimension parentFrameDimension, int currentTeamId) {
    controller = new TeamSettingsController(this, frame, currentTeamId);
    this.setPreferredSize(parentFrameDimension);
    this.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
    initHomePane();
  }

  private void initHomePane() {
    GroupLayout homeLayout = new GroupLayout(this);
    homeLayout.setAutoCreateGaps(true);
    homeLayout.setAutoCreateContainerGaps(true);
    this.setLayout(homeLayout);

    initHomePaneComponents();

    JLabel nameLabel = UIFactory.createLabel("Name:", null);
    JLabel codeLabel = UIFactory.createLabel("Code:", null);
    JLabel managerLabel = UIFactory.createLabel("Manager:", null);

    homeLayout.setHorizontalGroup(
        homeLayout
            .createSequentialGroup()
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(nameLabel)
                    .addComponent(codeLabel)
                    .addComponent(managerLabel)
                    .addComponent(leaveTeamButton))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(teamNameTextField)
                    .addComponent(teamCodeLabel)
                    .addComponent(teamManagerTextField))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(regenerateCodeButton)
                    .addComponent(saveButton)
                    .addComponent(savedLabel)));

    homeLayout.setVerticalGroup(
        homeLayout
            .createSequentialGroup()
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(teamNameTextField))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(codeLabel)
                    .addComponent(teamCodeLabel)
                    .addComponent(regenerateCodeButton))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(managerLabel)
                    .addComponent(teamManagerTextField))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(leaveTeamButton)
                    .addComponent(saveButton))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(savedLabel)));
  }

  private void initHomePaneComponents() {
    teamNameTextField = UIFactory.createTextField(controller.getTeamName());
    teamCodeLabel = UIFactory.createLabel(controller.getTeamCode(), null);
    teamManagerTextField = UIFactory.createTextField(controller.getTeamManagerName());
    enableEditTextFields(false);

    HomeMouseAdapter homeMouseAdapter = new HomeMouseAdapter();
    teamNameTextField.addMouseListener(homeMouseAdapter);
    teamManagerTextField.addMouseListener(homeMouseAdapter);

    saveButton = UIFactory.createButton("Save");
    regenerateCodeButton = UIFactory.createButton("Regenerate code");
    enableButtons(controller.getManagerAccess());

    savedLabel = UIFactory.createLabel("*Saved.", null);
    savedLabel.setVisible(false);
    leaveTeamButton = UIFactory.createButton("Leave Team");
    addButtonListeners();
  }

  public void enableEditTextFields(boolean enableEdit) {
    teamNameTextField.setEditable(enableEdit);
    teamManagerTextField.setEditable(enableEdit);
  }

  public void enableButtons(boolean enable) {
    saveButton.setVisible(enable);
    regenerateCodeButton.setVisible(enable);
  }

  public void updateHomePaneComponents() {
    teamNameTextField.setText(controller.getTeamName());
    teamCodeLabel.setText(controller.getTeamCode());
  }

  private void addButtonListeners() {
    leaveTeamButton.addActionListener(this);
    saveButton.addActionListener(this);
    regenerateCodeButton.addActionListener(this);
  }

  public void showSavedLabel(boolean saved) {
    savedLabel.setVisible(saved);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if (source == leaveTeamButton) {
      controller.confirmLeavingTeam();
    } else {
      if (source == saveButton) {
        controller.saveTeamName(teamNameTextField.getText());
        controller.saveTeamManager(teamManagerTextField.getText());
      } else if (source == regenerateCodeButton) {
        controller.regenerateTeamCode();
      }
      showSavedLabel(true);
      enableEditTextFields(false);
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
      }
    }
  }
}
