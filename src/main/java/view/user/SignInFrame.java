package main.java.view.user;

import main.java.view.UIFactory;
import main.java.controller.user.SignInController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * SignInFrame allows the user to sign in to the application.
 *
 * @author Beata Keresztes
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
  /** Messages displayed to inform the user about the sign in's validation. */
  private static final String WRONG_SIGN_IN_CREDENTIALS_MESSAGE = "Wrong credentials!";

  private static final String INVALID_SIGN_IN_MESSAGE =
      "Invalid sign in! \nCheck that the username and password\nthat you introduced are correct!";

  public SignInFrame() {

    super("Sign in");
    this.setSize(DIMENSION);
    this.setResizable(false);
    this.setVisible(true);
    this.setLayout(new BorderLayout());
    this.signInController = new SignInController(this);
    initComponents();
    this.addWindowListener(new SignInWindowAdapter());
  }

  /** Initializes the frame by adding its components. */
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
        String username = usernameTextField.getText();
        char[] password = passwordField.getPassword();
        if (signInController.validSignIn(username, password)) {
          signInController.enableSigningIn();
        } else {
          // clear fields and let the user try again
          JOptionPane.showMessageDialog(
              signInButton,
              INVALID_SIGN_IN_MESSAGE,
              WRONG_SIGN_IN_CREDENTIALS_MESSAGE,
              JOptionPane.WARNING_MESSAGE);
          usernameTextField.setText("");
          passwordField.setText("");
        }

      } else if (source == createAccountButton) {
        signInController.enableSigningUp();
      }
    }
  }

  private class SignInWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      if (!signInController.getSignInFlag()) {
        System.exit(0);
      }
    }
  }
}
