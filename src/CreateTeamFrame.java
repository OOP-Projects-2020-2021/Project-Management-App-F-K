import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * CreateTeamFrame is a frame used for team creation.
 * It displays a textfield in which the user can specify the new team's name.
 * The team is created when the submit button is pushed, if the requirements for a correct team
 * creation are fulfilled.
 *
 * @author Bori Fazakas
 */
public class CreateTeamFrame extends JFrame implements ActionListener, WindowListener {
  private JLabel teamNameLabel;
  private JTextField teamNameTextField;
  private JButton submitBtn;

  private CreateTeamController controller;

  private JFrame parentFrame;

  public CreateTeamFrame(JFrame parent) {
    controller = new CreateTeamController(this);
    parentFrame = parent;

    teamNameLabel = new JLabel("Enter the team name:");
    teamNameTextField = new JTextField();
    teamNameTextField.setPreferredSize(new Dimension(140, 20));

    JPanel dataPanel = new JPanel(new GridLayout(1, 2));
    dataPanel.add(teamNameLabel);
    dataPanel.add(teamNameTextField);
    dataPanel.setPreferredSize(new Dimension(300, 50));

    submitBtn = new JButton("Submit");
    submitBtn.addActionListener(this);

    JPanel contentPannel = new JPanel();
    Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    dataPanel.setBorder(padding);
    this.setContentPane(contentPannel);

    this.setTitle("Create a new team");
    this.addWindowListener(this);
    this.setLayout(new BorderLayout(0, 0));
    this.setSize(new Dimension(380, 120));
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
