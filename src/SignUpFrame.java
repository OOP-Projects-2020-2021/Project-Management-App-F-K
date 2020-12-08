import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;


//todo
public class SignUpFrame extends JFrame implements ActionListener {

  private JLabel usernameLabel;
  private JLabel passwordLabel;
  private JTextField usernameTextField;
  private JPasswordField passwordField;
  private JButton signUpButton;

  private SignUpController signUpController;

  private JFrame parentFrame;

  public SignUpFrame(JFrame parentFrame) {

    super("Sign up", 400, 300);

    this.setLayout(new BorderLayout());

    this.parentFrame = parentFrame;

    this.signUpController = new SignUpController(this);

    JPanel mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() / 2));

    mainPanel.setBorder(CENTER_ALIGNMENT_PADDING);

    GridLayout mainPanelLayout = new GridLayout(2, 1);
    mainPanelLayout.setVgap(40);
    mainPanel.setLayout(mainPanelLayout);

    usernameLabel = UIFactory.createLabel("Username:", null);
    usernameLabel.setLabelFor(usernameTextField);

    usernameTextField = UIFactory.createTextField(null, null);

    passwordLabel = UIFactory.createLabel("Password:", null);
    passwordLabel.setLabelFor(passwordField);

    passwordField = UIFactory.createPasswordField(null);

    mainPanel.add(usernameLabel);
    mainPanel.add(usernameTextField);
    mainPanel.add(passwordLabel);
    mainPanel.add(passwordField);

    this.add(mainPanel, BorderLayout.NORTH);

    signUpButton = UIFactory.createButton("Sign up", null);
    signUpButton.setAlignmentX(CENTER_ALIGNMENT);

    signUpButton.addActionListener(this);

    JPanel signUpButtonPanel = new JPanel();
    signUpButtonPanel.add(signUpButton);

    this.add(signUpButtonPanel, BorderLayout.CENTER);

    this.pack();

    this.addWindowFocusListener(new signUpWindowAdapter());
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == signUpButton) {
      String username = usernameTextField.getName();
      String password = Arrays.toString(passwordField.getPassword());
      signUpController.signUp(username, password, parentFrame);
    }
  }

  private class signUpWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      signUpController.onClose(parentFrame);
    }
  }
}
