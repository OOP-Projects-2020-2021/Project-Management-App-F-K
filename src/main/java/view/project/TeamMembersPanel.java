package view.project;

import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeamMembersPanel extends JPanel implements ActionListener {

    private JComboBox<String> membersList;

    private JLabel membersLabel;
    private JLabel removeMemberLabel;
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
                        .addComponent(removeMemberLabel)
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
                        .addComponent(removeMemberLabel)
                        .addComponent(membersList)
                        .addComponent(removeMemberButton))
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addMemberLabel)
                        .addComponent(addMemberTextField)
                        .addComponent(addMemberButton)));
    }
    private void initComponents() {
        membersLabel = UIFactory.createLabel("Members:",null);
        removeMemberLabel = UIFactory.createLabel("Remove member:",null);
        addMemberLabel = UIFactory.createLabel("Add member:",null);
        addMemberTextField = UIFactory.createTextField(""); //todo check if empty
        addMemberButton = UIFactory.createButton("Add");
        removeMemberButton = UIFactory.createButton("Remove");
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
