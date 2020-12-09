import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * CreateTeamFrame is a frame used for team creation. It displays a textfield in which the user can
 * specify the new team's name. The team is created when the submit button is pushed, if the
 * requirements for a correct team creation are fulfilled.
 *
 * @author Bori Fazakas
 */
public class CreateTeamFrame extends JFrame implements ActionListener {
  private JLabel teamNameLabel;
  private JTextField teamNameTextField;
  private JButton submitBtn;

  private CreateTeamController controller;

  private JFrame parentFrame;

  private static final Dimension FRAME_DIMENSION = new Dimension(360, 120);
  private static final Dimension DATA_PANEL_DIMENSION = new Dimension(320, 80);

  private static final String TEAM_NAME_LABEL_TEXT = "Enter the team name:";
  private static final String SUBMIT_BUTTON_TEXT = "Submit";
  private static final String FRAME_TITLE = "Create a new team";

  public CreateTeamFrame(JFrame parent) {
    controller = new CreateTeamController(this);
    parentFrame = parent;
    this.addWindowListener(new CreateTeamWindowAdapter());

    teamNameLabel = UIFactory.createLabel(TEAM_NAME_LABEL_TEXT, null);
    teamNameTextField = UIFactory.createTextField(null);

    JPanel dataPanel = new JPanel(new GridLayout(1, 2));
    dataPanel.add(teamNameLabel);
    dataPanel.add(teamNameTextField);
    dataPanel.setSize(DATA_PANEL_DIMENSION);

    JPanel buttonPanel = new JPanel();
    submitBtn = UIFactory.createButton(SUBMIT_BUTTON_TEXT);
    submitBtn.addActionListener(this);
    buttonPanel.add(submitBtn);

    JPanel contentPanel = new JPanel();
    this.setContentPane(contentPanel);

    Border padding =
            BorderFactory.createEmptyBorder(10, 10, 10, 10);
    dataPanel.setBorder(padding);

    this.setTitle(FRAME_TITLE);
    this.setLayout(new BorderLayout());
    this.setSize(FRAME_DIMENSION);
    this.add(dataPanel, BorderLayout.CENTER);
    this.add(buttonPanel, BorderLayout.SOUTH);
    this.setResizable(false);
    this.setVisible(true);
    System.out.println(teamNameTextField.getWidth());
    System.out.println(teamNameTextField.getHeight());
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == submitBtn) {
      controller.createTeam(teamNameTextField.getText());
    }
  }

  private class CreateTeamWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent evt) {
      controller.onClose(parentFrame);
    }
  }
}
