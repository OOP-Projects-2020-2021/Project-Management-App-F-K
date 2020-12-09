import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

/**
 * SignInFrame allows the user to sign in to the application.
 */
public class SignInFrame extends JFrame {

  private JLabel usernameLabel;
  private JLabel passwordLabel;
  private JTextField usernameTextField;
  private JPasswordField passwordField;
  private JButton signInButton;
  private JButton createAccountButton;
  private JLabel createAccountLabel;

  private SignInController signInController;

  private static final Dimension DIMENSION = new Dimension(400, 300);

  public SignInFrame() {

    super("Sign in");
    this.setSize(DIMENSION);
    this.setResizable(false);
    this.setVisible(true);
    this.setLayout(new BorderLayout());
    this.signInController = new SignInController(this);
    this.addWindowListener(new SignInWindowAdapter());
    initComponents();

  }

  public static void main(String[] args) {
    new SignInFrame();
  }


   /**
    Initializes the frame by adding its components.
    */

  private void initComponents() {

    JPanel mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() / 2));
    mainPanel.setBorder(UIFactory.createCenterAlignmentPadding(this));

    GridLayout mainPanelLayout = new GridLayout(2, 1);
    mainPanelLayout.setVgap(20);
    mainPanel.setLayout(mainPanelLayout);

    usernameLabel = UIFactory.createLabel("Username:", null);
    usernameLabel.setLabelFor(usernameTextField);

    usernameTextField = UIFactory.createTextField(null);

    passwordLabel = UIFactory.createLabel("Password:", null);
    passwordLabel.setLabelFor(passwordField);

    passwordField = UIFactory.createPasswordField();

    mainPanel.add(usernameLabel);
    mainPanel.add(usernameTextField);
    mainPanel.add(passwordLabel);
    mainPanel.add(passwordField);
    this.add(mainPanel, BorderLayout.NORTH);

    ButtonListener buttonListener = new ButtonListener();

    signInButton = UIFactory.createButton("Sign in");
    signInButton.addActionListener(buttonListener);

    JPanel signInButtonPanel = new JPanel();
    signInButtonPanel.add(signInButton);

    createAccountLabel = new JLabel();
    createAccountLabel.setText("Don't have an account yet?");
    createAccountLabel.setLabelFor(createAccountButton);

    createAccountButton = UIFactory.createButton("Create account");
    createAccountButton.addActionListener(buttonListener);

    JPanel createAccountPanel = new JPanel(new FlowLayout());
    createAccountPanel.add(createAccountLabel);
    createAccountPanel.add(createAccountButton);

    this.add(signInButtonPanel, BorderLayout.CENTER);
    this.add(createAccountPanel, BorderLayout.SOUTH);
    this.pack();

  }

  private class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      JButton source = (JButton) actionEvent.getSource();
      if (source == signInButton) {
        String username = usernameTextField.getName();
        String password = Arrays.toString(passwordField.getPassword());
        signInController.enableSigningIn(username, password);
      } else if (source == createAccountButton) {
        signInController.enableSigningUp();
      }
    }
  }

  private class SignInWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      signInController.closeFrame();
    }
  }
}

