package view.team.single_team;

import controller.team.single_team.TeamController;
import controller.team.single_team.TeamSettingsController;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This panel displays general information about a team.
 * The general user can only view the listed data, but the manager can edit the team's attributes.
 */
public class TeamHomePanel extends JPanel implements ActionListener {

  private JTextField teamNameTextField;
  private JLabel teamCodeLabel;
  private JTextField teamManagerTextField;
  private JLabel savedLabel;

  private JButton editButton;
  private JButton saveTeamNameButton;
  private JButton regenerateCodeButton;
  private JButton saveTeamManagerButton;
  private JButton leaveTeamButton;

  private TeamSettingsController controller;

  public TeamHomePanel(JFrame frame,Dimension parentFrameDimension, TeamController teamController) {
    controller = new TeamSettingsController(frame,teamController);
    this.setPreferredSize(parentFrameDimension);
    this.setBorder(BorderFactory.createEmptyBorder(50,100,50,100));
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
                    .addComponent(saveTeamNameButton)
                    .addComponent(regenerateCodeButton)
                    .addComponent(saveTeamManagerButton)
                    .addComponent(editButton)
                    .addComponent(savedLabel)));

    homeLayout.setVerticalGroup(
        homeLayout
            .createSequentialGroup()
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(teamNameTextField)
                    .addComponent(saveTeamNameButton))
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
                    .addComponent(teamManagerTextField)
                    .addComponent(saveTeamManagerButton))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(leaveTeamButton)
                    .addComponent(editButton))
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

    editButton = UIFactory.createButton("Edit");
    saveTeamNameButton = UIFactory.createButton("Save");
    regenerateCodeButton = UIFactory.createButton("Generate code");
    saveTeamManagerButton = UIFactory.createButton("Save");
    enableButtons(controller.getManagerAccessGranted());

    savedLabel = UIFactory.createLabel("*Saved.", null);
    savedLabel.setVisible(false);
    leaveTeamButton = UIFactory.createButton("Leave Team");
    addButtonListeners();
  }

  private void enableEditTextFields(boolean enableEdit) {
    teamNameTextField.setEditable(enableEdit);
    teamManagerTextField.setEditable(enableEdit);
  }

  private void enableButtons(boolean enable) {
    editButton.setVisible(enable);
    saveTeamNameButton.setVisible(enable);
    saveTeamManagerButton.setVisible(enable);
    regenerateCodeButton.setVisible(enable);
  }

  private void updateHomePaneComponents() {
    teamNameTextField.setText(controller.getTeamName());
    teamManagerTextField.setText(controller.getTeamManagerName());
    teamCodeLabel.setText(controller.getTeamCode());
  }

  private void addButtonListeners() {
    leaveTeamButton.addActionListener(this);
    editButton.addActionListener(this);
    saveTeamNameButton.addActionListener(this);
    regenerateCodeButton.addActionListener(this);
    saveTeamManagerButton.addActionListener(this);
  }

  private void showSavedLabel(boolean saved) {
    savedLabel.setVisible(saved);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if(source == leaveTeamButton) {
      controller.leaveTeam();
      updateHomePaneComponents();
      enableButtons(false);
      enableEditTextFields(false);
    } else if (source == editButton) {
      enableEditTextFields(controller.getManagerAccessGranted());
      showSavedLabel(false);
    } else {
      if (source == saveTeamNameButton) {
        controller.saveTeamName(teamNameTextField.getText());
      } else if (source == regenerateCodeButton) {
        controller.regenerateTeamCode();
      } else if (source == saveTeamManagerButton) {
        controller.saveTeamManager(teamManagerTextField.getText());
      }
      showSavedLabel(true);
      updateHomePaneComponents();
      enableEditTextFields(false);
    }
  }
}
