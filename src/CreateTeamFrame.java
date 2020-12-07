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
public class CreateTeamFrame extends JFrame implements ActionListener, WindowListener {
  private JLabel teamNameLabel;
  private JTextField teamNameTextField;
  private JButton submitBtn;

  private CreateTeamController controller;

  private JFrame parentFrame;

  private static final Dimension FRAME_DIMENSION = new Dimension(380, 120);
  private static final Dimension DATA_PANEL_DIMENSION = new Dimension(300, 50);
  private static final Dimension TEAM_NAME_FIELD_DIMENSION = new Dimension(140, 20);
  private static final int BORDER_WIDTH = 10;

  private static final String TEAM_NAME_LABEL_TEXT = "Enter the team name:";
  private static final String SUBMIT_BUTTON_TEXT = "Submit";
  private static final String FRAME_TITLE = "Create a new team";

  public CreateTeamFrame(JFrame parent) {
    controller = new CreateTeamController(this);
    parentFrame = parent;

    teamNameLabel = new JLabel(TEAM_NAME_LABEL_TEXT);
    teamNameTextField = new JTextField();
    teamNameTextField.setPreferredSize(TEAM_NAME_FIELD_DIMENSION);

    JPanel dataPanel = new JPanel(new GridLayout(1, 2));
    dataPanel.add(teamNameLabel);
    dataPanel.add(teamNameTextField);
    dataPanel.setPreferredSize(DATA_PANEL_DIMENSION);

    submitBtn = new JButton(SUBMIT_BUTTON_TEXT);
    submitBtn.addActionListener(this);

    JPanel contentPannel = new JPanel();
    Border padding =
        BorderFactory.createEmptyBorder(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
    dataPanel.setBorder(padding);
    this.setContentPane(contentPannel);

    this.setTitle(FRAME_TITLE);
    this.addWindowListener(this);
    this.setLayout(new BorderLayout());
    this.setSize(FRAME_DIMENSION);
    this.add(dataPanel, BorderLayout.CENTER);
    this.add(submitBtn, BorderLayout.SOUTH);
    this.setResizable(false);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == submitBtn) {
      controller.createTeam(teamNameTextField.getText());
    }
  }

  @Override
  public void windowOpened(WindowEvent windowEvent) {}

  @Override
  public void windowClosing(WindowEvent evt) {
    controller.onClose(parentFrame);
  }

  @Override
  public void windowClosed(WindowEvent windowEvent) {}

  @Override
  public void windowIconified(WindowEvent windowEvent) {}

  @Override
  public void windowDeiconified(WindowEvent windowEvent) {}

  @Override
  public void windowActivated(WindowEvent windowEvent) {}

  @Override
  public void windowDeactivated(WindowEvent windowEvent) {}
}
