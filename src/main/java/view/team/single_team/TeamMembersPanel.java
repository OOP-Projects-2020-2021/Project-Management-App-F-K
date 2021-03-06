package view.team.single_team;

import controller.team.single_team.TeamMembersController;
import model.user.User;
import view.CloseableComponent;
import view.UIFactory;

import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The TeamMembersPanel displays the list of members of a team. Only the manager of the team is
 * allowed to add or remove members from the corresponding team.
 *
 * @author Beata Keresztes
 */
public class TeamMembersPanel extends JPanel implements ActionListener, CloseableComponent {

  private JLabel addMemberLabel;
  private JButton addMemberButton;
  private JButton removeMemberButton;
  private JTextField addMemberTextField;
  private JList<String> membersList;
  private DefaultListModel<String> membersListModel;

  private TeamMembersController controller;

  public TeamMembersPanel(JFrame frame, int currentTeamId) {
    this.controller = new TeamMembersController(this, frame, currentTeamId);
    this.setLayout(new BorderLayout());
    this.setBorder(BorderFactory.createEmptyBorder(50, 100, 50, 100));
    initMembersPane();
  }

  private void initMembersPane() {
    initComponents();
    initMembersHeader(controller.getManagerAccess());
    initMembersContent(controller.getManagerAccess());
  }

  private void initMembersHeader(boolean enableManagingMembers) {
    JPanel headerPanel = new JPanel();
    GroupLayout membersLayout = new GroupLayout(headerPanel);
    membersLayout.setAutoCreateGaps(true);
    membersLayout.setAutoCreateContainerGaps(true);
    headerPanel.setLayout(membersLayout);
    membersLayout.setHorizontalGroup(
        membersLayout
            .createSequentialGroup()
            .addGroup(membersLayout.createParallelGroup().addComponent(addMemberLabel))
            .addComponent(addMemberTextField)
            .addComponent(addMemberButton));
    membersLayout.setVerticalGroup(
        membersLayout
            .createSequentialGroup()
            .addGroup(
                membersLayout
                    .createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(addMemberLabel)
                    .addComponent(addMemberTextField)
                    .addComponent(addMemberButton)));
    this.add(headerPanel, BorderLayout.NORTH);
    headerPanel.setVisible(enableManagingMembers);
  }

  private void initMembersContent(boolean enableManagingMembers) {

    JPanel contentPanel = new JPanel();
    contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
    contentPanel.setLayout(contentPanelLayout);
    contentPanelLayout.setAutoCreateGaps(true);
    contentPanelLayout.setAutoCreateContainerGaps(true);

    initMembersList();
    JPanel membersListPanel = new JPanel();
    JScrollPane listScroller = new JScrollPane(membersListPanel);
    listScroller.setViewportView(membersList);
    listScroller.setVisible(true);
    listScroller.setBorder(
        BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Members List",
            TitledBorder.CENTER,
            TitledBorder.TOP));
    membersListPanel.add(listScroller);

    contentPanelLayout.setHorizontalGroup(
        contentPanelLayout
            .createSequentialGroup()
            .addComponent(listScroller)
            .addComponent(removeMemberButton));
    contentPanelLayout.setVerticalGroup(
        contentPanelLayout
            .createSequentialGroup()
            .addGroup(
                contentPanelLayout
                    .createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(listScroller)
                    .addComponent(removeMemberButton)));
    this.add(contentPanel, BorderLayout.CENTER);

    removeMemberButton.setVisible(enableManagingMembers);
  }

  private void initComponents() {
    addMemberLabel = UIFactory.createLabel("Add member:", null);
    addMemberTextField = UIFactory.createTextField("");
    addMemberButton = UIFactory.createButton("Add");
    removeMemberButton = UIFactory.createButton("Remove");
    addButtonListeners();
  }

  private void emptySearchField() {
    addMemberTextField.setText("");
  }

  private void addButtonListeners() {
    addMemberButton.addActionListener(this);
    removeMemberButton.addActionListener(this);
  }

  public void fillMembersList() {
    List<User> members = controller.getTeamMembers();
    for (User member : members) {
      membersListModel.addElement(member.getUsername());
    }
  }

  private void initMembersList() {
    membersListModel = new DefaultListModel<>();
    fillMembersList();
    membersList = new JList<>(membersListModel);
    membersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    membersList.setLayoutOrientation(JList.VERTICAL);
  }

  public void updateMembersList() {
    membersListModel.removeAllElements();
    fillMembersList();
    membersList.setModel(membersListModel);
  }

  public void enableComponents(boolean enableManagerAccess) {
    addMemberLabel.setVisible(enableManagerAccess);
    addMemberTextField.setVisible(enableManagerAccess);
    addMemberButton.setVisible(enableManagerAccess);
    removeMemberButton.setVisible(enableManagerAccess);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == addMemberButton) {
      String memberName = addMemberTextField.getText();
      controller.addMember(memberName);
      emptySearchField();
    } else if (actionEvent.getSource() == removeMemberButton) {
      String memberName = membersList.getSelectedValue();
      controller.removeMember(memberName);
    }
  }

  @Override
  public void onClose() {
    controller.close();
  }
}
