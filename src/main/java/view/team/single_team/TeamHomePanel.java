package view.team.single_team;

import controller.team.single_team.TeamSettingsController;
import model.user.User;
import view.UIFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * The TeamHomePanel displays general information about a team.
 * The general user can only view the listed data, but the manager can edit the team's attributes.
 * The user is allowed to leave the team, unless he/she is the manager, in which case the manager is only allowed to
 * leave the team, if first he/she passes the manager position to another member of the team.
 *
 * @author Beata Keresztes
 */
public class TeamHomePanel extends JPanel implements ActionListener {

  private JTextField teamNameTextField;
  private JLabel teamCodeLabel;
  private DefaultComboBoxModel<String> teamManagerModel;
  private JComboBox<String> teamManagerComboBox;

  private JButton saveTeamNameButton;
  private JButton saveTeamManagerButton;
  private JButton regenerateCodeButton;
  private JButton leaveTeamButton;

  private TeamSettingsController controller;

  public TeamHomePanel(JFrame frame, int teamId) {
    controller = new TeamSettingsController(this, frame, teamId);
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
                    .addComponent(teamManagerComboBox))
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(regenerateCodeButton)
                    .addComponent(saveTeamNameButton)
                    .addComponent(saveTeamManagerButton)));

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
                    .addComponent(teamManagerComboBox)
                    .addComponent(saveTeamManagerButton))
            .addGap(30)
            .addGroup(
                homeLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(leaveTeamButton)));
  }

  private void clearTeamManagerModel() {
    if (teamManagerModel.getSize() > 0) {
      teamManagerModel.removeAllElements();
    }
  }

  private void fillTeamManagerModel() {
    List<User> members = controller.getMembersOfTeam();
    if (members != null) {
      for (User member : members) {
        teamManagerModel.addElement(member.getUsername());
      }
      teamManagerModel.setSelectedItem(controller.getTeamManagerName());
    }
  }

  public void updateTeamManagerModel() {
    clearTeamManagerModel();
    fillTeamManagerModel();
  }

  private void initHomePaneComponents() {
    teamNameTextField = UIFactory.createTextField(controller.getTeamName());
    teamCodeLabel = UIFactory.createLabel(controller.getTeamCode(), null);

    teamManagerComboBox = new JComboBox<>();
    teamManagerModel = new DefaultComboBoxModel<>();
    fillTeamManagerModel();
    teamManagerComboBox.setModel(teamManagerModel);

    teamNameTextField.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (controller.getManagerAccess()) {
              teamNameTextField.setEditable(true);
            }
          }
        });

    saveTeamNameButton = UIFactory.createButton("Save");
    saveTeamManagerButton = UIFactory.createButton("Save");
    regenerateCodeButton = UIFactory.createButton("Regenerate code");
    leaveTeamButton = UIFactory.createButton("Leave Team");
    addButtonListeners();

    enableNameTextField(false);
    teamManagerComboBox.setEnabled(controller.getManagerAccess());
    enableButtons(controller.getManagerAccess());
  }

  public void updateHomePaneComponents() {
    teamNameTextField.setText(controller.getTeamName());
    updateTeamManagerModel();
    teamCodeLabel.setText(controller.getTeamCode());
  }

  private void addButtonListeners() {
    leaveTeamButton.addActionListener(this);
    saveTeamNameButton.addActionListener(this);
    saveTeamManagerButton.addActionListener(this);
    regenerateCodeButton.addActionListener(this);
  }

  private void enableButtons(boolean enable) {
    saveTeamNameButton.setVisible(enable);
    saveTeamManagerButton.setVisible(enable);
    regenerateCodeButton.setVisible(enable);
  }

  public void enableNameTextField(boolean enable) {
    teamNameTextField.setEditable(enable);
  }

  public void enableComponents(boolean enable) {
    enableNameTextField(enable);
    teamManagerComboBox.setEnabled(enable);
    enableButtons(enable);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    Object source = actionEvent.getSource();
    if (source == leaveTeamButton) {
      controller.leaveTeam();
    } else {
      if (source == saveTeamNameButton) {
        controller.saveTeamName(teamNameTextField.getText());
      } else if (source == saveTeamManagerButton) {
        System.out.println((String) teamManagerModel.getSelectedItem());
        controller.saveTeamManager((String) teamManagerModel.getSelectedItem());
      } else if (source == regenerateCodeButton) {
        controller.regenerateTeamCode();
      }
    }
  }
}
