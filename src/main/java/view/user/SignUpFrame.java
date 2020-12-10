package main.java.view.user;

import main.java.view.UIFactory;
import main.java.controller.user.SignUpController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * SignUpFrame allows the user to create an account in the application. After the account was saved
 * the user is asked to sign in with its new credentials to finalize the registration.
 *
 * @author Beata Keresztes
 */
public class SignUpFrame extends JFrame implements ActionListener {

  private JLabel usernameLabel;
  private JLabel passwordLabel;
  private JTextField usernameTextField;
  private JPasswordField passwordField;
  private JButton signUpButton;
  private JButton goBackButton;

  private SignUpController signUpController;
  private JFrame parentFrame;

  private static final Dimension DIMENSION = new Dimension(400, 300);
  /** Messages displayed to inform the user about the steps of signing in. " */
  private static final String SIGN_IN_MESSAGE =
      "Please sign in to your account to finalize the registration.";

  private static final String FINALIZE_SIGN_UP_TITLE = "Finalize signing up";

  public SignUpFrame(JFrame parentFrame) {

    super("Sign up");
    this.setSize(DIMENSION);
    this.setResizable(false);
    this.setVisible(true);
    this.setLayout(new BorderLayout());
    this.parentFrame = parentFrame;
    this.signUpController = new SignUpController(this);
    initComponents();
    this.addWindowListener(new SignUpWindowAdapter());
  }
  /** Initializes the frame by adding its components. */
  private void initComponents() {

    JPanel mainPanel = new JPanel();
    mainPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() / 2));
    mainPanel.setBorder(UIFactory.createCenterAlignmentPadding(this));

    GridLayout mainPanelLayout = new GridLayout(2, 1, 0, 40);
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

    signUpButton = UIFactory.createButton("Sign up");
    signUpButton.addActionListener(this);
    goBackButton = UIFactory.createButton("Back");
    goBackButton.addActionListener(this);

    JPanel buttonsPanel = new JPanel();
    buttonsPanel.add(goBackButton);
    buttonsPanel.add(signUpButton);
    this.add(buttonsPanel, BorderLayout.CENTER);
    this.pack();
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == signUpButton) {
      String username = usernameTextField.getText();
      char[] password = passwordField.getPassword();
      signUpController.signUp(username, password);
      JOptionPane.showMessageDialog(
          this, SIGN_IN_MESSAGE, FINALIZE_SIGN_UP_TITLE, JOptionPane.PLAIN_MESSAGE);
      signUpController.goBack();
    } else if (actionEvent.getSource() == goBackButton) {
      signUpController.goBack();
    }
  }

  private class SignUpWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      signUpController.onClose(parentFrame);
    }
  }
}
