package view.project;

import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Flow;

public class TeamMembersPanel extends JPanel{

    private JComboBox<String> membersList;

    private JLabel membersLabel;
    private JButton addMemberButton;
    private JButton removeMemberButton;

    public TeamMembersPanel(Dimension parentFrameDimension) {
        this.setPreferredSize(parentFrameDimension);
        initMembersPane();
    }
    private void initMembersPane() {
        GroupLayout membersLayout = new GroupLayout(this);
        membersLayout.setAutoCreateGaps(true);
        membersLayout.setAutoCreateContainerGaps(true);
        this.setLayout(membersLayout);
        initButtons();
        initMembersList();

        membersLayout.setHorizontalGroup(membersLayout.createSequentialGroup()
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(membersLabel)
                        .addComponent(membersList)
                        .addComponent(addMemberButton))
                        .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(removeMemberButton)));

        membersLayout.setVerticalGroup(membersLayout.createSequentialGroup()
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(membersLabel))
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(membersList))
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addMemberButton)
                        .addComponent(removeMemberButton)));

    }
    private void initButtons() {
        addMemberButton = UIFactory.createButton("Add Member");
        removeMemberButton = UIFactory.createButton("Remove Member");
    }
    private void initMembersList() {
        membersLabel = UIFactory.createLabel("Members:",null);
        String[] members = {"User1","User2","User3","User4","User5"};
        membersList = new JComboBox<>(members);

    }

}
