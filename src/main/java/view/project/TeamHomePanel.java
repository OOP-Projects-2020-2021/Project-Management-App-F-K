package view.project;

import model.team.Team;
import view.UIFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TeamHomePanel extends JPanel implements ActionListener {

    private JTextField teamNameTextField;
    private JLabel teamCodeLabel;
    private JTextField teamManagerTextField;
    private JButton editButton;
    private JButton saveTeamNameButton;
    private JButton generateCodeButton;
    private JButton saveTeamManagerButton;
    private JLabel savedLabel;
    private JButton leaveTeamButton;

    public TeamHomePanel(Dimension parentFrameDimension) {
        this.setPreferredSize(parentFrameDimension);
        initHomePane();
    }

    private void initHomePane() {

        GroupLayout homeLayout = new GroupLayout(this);
        homeLayout.setAutoCreateGaps(true);
        homeLayout.setAutoCreateContainerGaps(true);
        this.setLayout(homeLayout);
        initHomePaneComponents();
        JLabel nameLabel = UIFactory.createLabel("Name:",null);
        JLabel codeLabel = UIFactory.createLabel("Code:",null);
        JLabel managerLabel = UIFactory.createLabel("Manager:",null);

        homeLayout.setHorizontalGroup(homeLayout.createSequentialGroup()
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameLabel)
                        .addComponent(codeLabel)
                        .addComponent(managerLabel)
                        .addComponent(leaveTeamButton))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(teamNameTextField)
                        .addComponent(teamCodeLabel)
                        .addComponent(teamManagerTextField))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(saveTeamNameButton)
                        .addComponent(generateCodeButton)
                        .addComponent(saveTeamManagerButton)
                        .addComponent(editButton)
                        .addComponent(savedLabel)));

        homeLayout.setVerticalGroup(homeLayout.createSequentialGroup()
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(teamNameTextField)
                        .addComponent(saveTeamNameButton))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(codeLabel)
                        .addComponent(teamCodeLabel)
                        .addComponent(generateCodeButton))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(managerLabel)
                        .addComponent(teamManagerTextField)
                        .addComponent(saveTeamManagerButton))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(leaveTeamButton)
                        .addComponent(editButton))
                .addGroup(homeLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(savedLabel)));


    }
    private void initHomePaneComponents() {
        // TODO get data from controller
        teamNameTextField = UIFactory.createTextField("name");
        teamCodeLabel = UIFactory.createLabel("code",null);
        teamManagerTextField = UIFactory.createTextField("manager");
        editButton = UIFactory.createButton("Edit");
        saveTeamNameButton = UIFactory.createButton("Save");
        generateCodeButton = UIFactory.createButton("Generate code");
        saveTeamManagerButton = UIFactory.createButton("Save");
        savedLabel = UIFactory.createLabel("*Saved.",null);
        leaveTeamButton = UIFactory.createButton("Leave Team");
    }
    public void enableSaveButtons(boolean enableSave) {
        saveTeamNameButton.setVisible(enableSave);
        saveTeamManagerButton.setVisible(enableSave);
        generateCodeButton.setVisible(enableSave);
    }
    public void enableEditTextFields(boolean enableEdit) {
        teamNameTextField.setEditable(enableEdit);
        teamManagerTextField.setEditable(enableEdit);
    }
    public void showEditButton(boolean showEdit) {
        editButton.setVisible(showEdit);
    }
    private void updateHomePaneComponents(Team team) {
        // todo from controller
        teamNameTextField.setText("");
        teamCodeLabel.setText("");
        teamManagerTextField.setText("");
    }
    public void showSavedLabel(boolean showSave) {
        savedLabel.setVisible(showSave);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
