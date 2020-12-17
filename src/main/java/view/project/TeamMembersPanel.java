package view.project;

import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeamMembersPanel extends JPanel implements ActionListener {

    private JLabel addMemberLabel;
    private JButton addMemberButton;
    private JButton removeMemberButton;
    private JTextField addMemberTextField;
    private JList<String> membersList;

    public TeamMembersPanel(Dimension parentFrameDimension) {
        this.setPreferredSize(parentFrameDimension);
        this.setLayout(new BorderLayout());
        initMembersPane(true);  // todo get form controller

    }
    private void initMembersPane(boolean enableManagingMembers) {
        initComponents();
        initMembersHeader(enableManagingMembers);
        initMembersContent(enableManagingMembers);
    }

    private void initMembersHeader(boolean enableManagingMembers) {
        JPanel headerPanel = new JPanel();
        GroupLayout membersLayout = new GroupLayout(headerPanel);
        membersLayout.setAutoCreateGaps(true);
        membersLayout.setAutoCreateContainerGaps(true);
        headerPanel.setLayout(membersLayout);
        membersLayout.setHorizontalGroup(membersLayout.createSequentialGroup()
                .addGroup(membersLayout.createParallelGroup()
                        .addComponent(addMemberLabel))
                        .addComponent(addMemberTextField)
                        .addComponent(addMemberButton));
        membersLayout.setVerticalGroup(membersLayout.createSequentialGroup()
                .addGroup(membersLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addMemberLabel)
                        .addComponent(addMemberTextField)
                        .addComponent(addMemberButton)));
        this.add(headerPanel,BorderLayout.NORTH);
        headerPanel.setVisible(enableManagingMembers);
    }
    private void initMembersContent(boolean enableManagingMembers) {

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentPanelLayout);
        contentPanelLayout.setAutoCreateGaps(true);
        contentPanelLayout.setAutoCreateContainerGaps(true);

        initMembersList();
        JPanel membersListPanel = new JPanel();
        JScrollPane listScroller = new JScrollPane(membersListPanel);
        listScroller.setViewportView(membersList);
        listScroller.setVisible(true);
        membersListPanel.add(listScroller);

        contentPanelLayout.setHorizontalGroup(contentPanelLayout.createSequentialGroup()
                    .addComponent(listScroller)
                    .addComponent(removeMemberButton));
        contentPanelLayout.setVerticalGroup(contentPanelLayout.createSequentialGroup()
                    .addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(listScroller)
                            .addComponent(removeMemberButton)));
        this.add(contentPanel,BorderLayout.CENTER);

        removeMemberButton.setVisible(enableManagingMembers);
    }
    private void initComponents() {
        addMemberLabel = UIFactory.createLabel("Add member:",null);
        addMemberTextField = UIFactory.createTextField(""); //todo check if empty
        addMemberButton = UIFactory.createButton("Add");
        removeMemberButton = UIFactory.createButton("Remove");
    }
    private void initMembersList() {
        int noMembers = 20;
        String[] members = new String[noMembers];
        for(int i=0;i<noMembers;i++) {
            members[i] = "User" + i;
        }
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String member : members) {
            listModel.addElement(member);
        }
        membersList = new JList<>(listModel);
        membersList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        membersList.setLayoutOrientation(JList.VERTICAL);

    }
    public void enableManagingMembers(boolean enableManager) {
        addMemberButton.setVisible(enableManager);
        removeMemberButton.setVisible(enableManager);
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
