package view.project;

import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeamMembersPanel extends JPanel implements ActionListener {

    private JComboBox<String> membersList;

    private JLabel membersLabel;
    private JLabel selectMembersToRemoveLabel;
    private JLabel addMemberLabel;
    private JButton addMemberButton;
    private JButton removeMemberButton;
    private JTextField addMemberTextField;

    public TeamMembersPanel(Dimension parentFrameDimension) {
        this.setPreferredSize(parentFrameDimension);
        initMembersPane();
    }
    private void initMembersPane() {
        GroupLayout membersLayout = new GroupLayout(this);
        membersLayout.setAutoCreateGaps(true);
        membersLayout.setAutoCreateContainerGaps(true);
        this.setLayout(membersLayout);
        initComponents();
        initMembersList();

        membersLayout.setHorizontalGroup(membersLayout.createSequentialGroup()
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(membersLabel)
                        .addComponent(selectMembersToRemoveLabel)
                        .addComponent(addMemberLabel))
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(membersList)
                        .addComponent(addMemberTextField))
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(removeMemberButton)
                        .addComponent(addMemberButton)));

        membersLayout.setVerticalGroup(membersLayout.createSequentialGroup()
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(membersLabel))
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(selectMembersToRemoveLabel)
                        .addComponent(membersList)
                        .addComponent(removeMemberButton))
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addMemberLabel)
                        .addComponent(addMemberTextField)
                        .addComponent(addMemberButton)));
    }
    private void initComponents() {
        membersLabel = UIFactory.createLabel("Members:",null);
        selectMembersToRemoveLabel = UIFactory.createLabel("Select member:",null);
        addMemberLabel = UIFactory.createLabel("Member's name:",null);
        addMemberTextField = UIFactory.createTextField(""); //todo check if empty
        addMemberButton = UIFactory.createButton("Add Member");
        removeMemberButton = UIFactory.createButton("Remove Member");
    }
    private void initMembersList() {
        String[] members = {"User1","User2","User3","User4","User5"};
        membersList = new JComboBox<>(members);

    }
    public void enableButtons(boolean enableButton) {
        addMemberButton.setVisible(enableButton);
        removeMemberButton.setVisible(enableButton);
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource() == addMemberButton) {
            //todo
        }else if(actionEvent.getSource() == removeMemberButton) {
            //todo
        }
    }

}
