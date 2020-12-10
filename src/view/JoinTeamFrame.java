package view;

import controller.JoinTeamController;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * JoinTeamFrame is a frame used for team creation. It displays a textfield in which the user has to
 * enter the team's code. After pushing the submit button, the user will have joined the team with
 * the specified code if it exists.
 *
 * @author Bori Fazakas
 */
public class JoinTeamFrame extends JFrame implements ActionListener {
  private JLabel teamCodeLabel;
  private JTextField teamCodeTextField;
  private JButton submitBtn;

  private JoinTeamController controller;

  private JFrame parentFrame;

  private static final Dimension FRAME_DIMENSION = new Dimension(360, 120);
  private static final Dimension DATA_PANEL_DIMENSION = new Dimension(320, 80);
  private static final int BORDER_WIDTH = 10;

  private static final String TEAM_CODE_LABEL_TEXT = "Enter the team code:";
  private static final String SUBMIT_BUTTON_TEXT = "Submit";
  private static final String FRAME_TITLE = "Join a team";

  public JoinTeamFrame(JFrame parent) {
    controller = new JoinTeamController(this);
    parentFrame = parent;
    this.addWindowListener(new JoinTeamWindowAdapter());

    teamCodeLabel = UIFactory.createLabel(TEAM_CODE_LABEL_TEXT, null);
    teamCodeTextField = UIFactory.createTextField(null);
    teamCodeLabel.setLabelFor(teamCodeTextField);

    JPanel dataPanel = new JPanel(new GridLayout(1, 2));
    dataPanel.add(teamCodeLabel);
    dataPanel.add(teamCodeTextField);
    dataPanel.setPreferredSize(DATA_PANEL_DIMENSION);

    JPanel buttonPanel = new JPanel();
    submitBtn = UIFactory.createButton(SUBMIT_BUTTON_TEXT);
    submitBtn.addActionListener(this);
    buttonPanel.add(submitBtn);

    JPanel contentPannel = new JPanel();
    this.setContentPane(contentPannel);

    Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    dataPanel.setBorder(padding);

    this.setTitle(FRAME_TITLE);
    this.setLayout(new BorderLayout());
    this.setSize(FRAME_DIMENSION);
    this.add(dataPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);
    this.setResizable(false);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == submitBtn) {
      controller.joinTeam(teamCodeTextField.getText());
    }
  }

  private class JoinTeamWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent evt) {
      controller.onClose(parentFrame);
    }
  }
}
