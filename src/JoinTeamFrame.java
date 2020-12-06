import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class JoinTeamFrame extends JFrame implements ActionListener, WindowListener {
  private JLabel teamCodeLabel;
  private JTextField teamCodeTextField;
  private JButton submitBtn;

  private JoinTeamController controller;

  private JFrame parentFrame;

  public JoinTeamFrame(JFrame parent) {
    controller = new JoinTeamController(this);
    parentFrame = parent;

    teamCodeLabel = new JLabel("Enter the team code:");
    teamCodeTextField = new JTextField();
    teamCodeTextField.setPreferredSize(new Dimension(80, 20));

    JPanel dataPanel = new JPanel(new GridLayout(1, 2));
    dataPanel.add(teamCodeLabel);
    dataPanel.add(teamCodeTextField);
    dataPanel.setPreferredSize(new Dimension(240, 50));

    submitBtn = new JButton("Submit");
    submitBtn.addActionListener(this);

    JPanel contentPannel = new JPanel();
    Border padding = BorderFactory.createEmptyBorder(10, 10, 10, 10);
    dataPanel.setBorder(padding);
    this.setContentPane(contentPannel);

    this.setTitle("Join a new team");
    this.addWindowListener(this);
    this.setLayout(new BorderLayout(0, 10));
    this.setSize(new Dimension(320, 120));
    this.add(dataPanel, BorderLayout.CENTER);
    this.add(submitBtn, BorderLayout.SOUTH);
    this.setResizable(false);
    this.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == submitBtn) {
      controller.joinTeam(teamCodeTextField.getText());
    }
  }

  @Override
  public void windowOpened(WindowEvent windowEvent) {

  }

  @Override
  public void windowClosing(WindowEvent evt) {
    controller.onClose(parentFrame);
  }

  @Override
  public void windowClosed(WindowEvent windowEvent) {

  }

  @Override
  public void windowIconified(WindowEvent windowEvent) {

  }

  @Override
  public void windowDeiconified(WindowEvent windowEvent) {

  }

  @Override
  public void windowActivated(WindowEvent windowEvent) {

  }

  @Override
  public void windowDeactivated(WindowEvent windowEvent) {

  }
}
