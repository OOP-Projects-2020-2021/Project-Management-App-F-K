import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class SignInFrame extends UserFrame {

  private JLabel usernameLabel;
  private JLabel passwordLabel;
  private JTextField usernameTextField;
  private JPasswordField passwordField;
  private JButton signInButton;
  private JButton createAccountButton;
  private JLabel createAccountLabel;

  private SignInController signInController;

  private static final int GAP_SIZE = 20;

  public SignInFrame() {

    super("Sign in",400, 300);

    this.setLayout(new BorderLayout());

    this.signInController = new SignInController(this);

    JPanel mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT / 2));

    mainPanel.setBorder(CENTER_ALIGNMENT_PADDING);

    GridLayout mainPanelLayout = new GridLayout(2, 1);
    mainPanelLayout.setVgap(GAP_SIZE);
    mainPanel.setLayout(mainPanelLayout);

    usernameLabel = createLabel("Username:");
    usernameLabel.setLabelFor(usernameTextField);

    usernameTextField = createTextField(null);

    passwordLabel = createLabel("Password:");
    passwordLabel.setLabelFor(passwordField);

    passwordField = createPasswordField();

    mainPanel.add(usernameLabel);
    mainPanel.add(usernameTextField);
    mainPanel.add(passwordLabel);
    mainPanel.add(passwordField);

    this.add(mainPanel, BorderLayout.NORTH);

    ButtonListener buttonListener = new ButtonListener();

    signInButton = createButton("Sign in");
    signInButton.setAlignmentX(CENTER_ALIGNMENT);
    signInButton.addActionListener(buttonListener);

    JPanel signInButtonPanel = new JPanel();
    signInButtonPanel.add(signInButton);

    createAccountLabel = new JLabel();
    createAccountLabel.setText("Don't have an account yet?");
    createAccountLabel.setLabelFor(createAccountButton);

    createAccountButton = createButton("Create account");
    createAccountButton.addActionListener(buttonListener);

    JPanel createAccountPanel = new JPanel(new FlowLayout());
    createAccountPanel.add(createAccountLabel);
    createAccountPanel.add(createAccountButton);

    this.add(signInButtonPanel, BorderLayout.CENTER);

    this.add(createAccountPanel, BorderLayout.SOUTH);

    this.pack();

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

  private class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      JButton source = (JButton) actionEvent.getSource();
      if (source == signInButton) {
        String username = usernameTextField.getName();
        String password = Arrays.toString(passwordField.getPassword());
        signInController.signIn(username, password);
      } else if (source == createAccountButton) {
        signInController.enableSigningUp();
      }
    }
  }
}
