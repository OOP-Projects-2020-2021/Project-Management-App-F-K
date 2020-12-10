package view;

import controller.AccountSettingsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

/**
 * AccountSettingsFrame displays the account information of the user and allows changing the data.
 */

public class AccountSettingsFrame extends JFrame implements ActionListener {

  private JLabel accountInformationLabel;
  private JLabel usernameLabel;
  private JTextField usernameTextField;
  private JLabel passwordLabel;
  private JPasswordField passwordField;
  private JButton editUsernameButton;
  private JButton editPasswordButton;
  private JButton goBackButton;
  private JButton saveButton;
  private JLabel dataSavedLabel;
  private JToggleButton showPasswordButton;

  private AccountSettingsController accountSettingsController;

  private JFrame parentFrame;

  /** Represents the dimension of the frame. */
  private static final Dimension DIMENSION = new Dimension(500, 300);
  /** Messages displayed to inform the user about the validation of the data. */
  private static final String ASK_PASSWORD_MESSAGE = "Enter your current password:";
  private static final String INCORRECT_PASSWORD_MESSAGE = "Incorrect password!";
  private static final String ACCOUNT_INFORMATION_SAVED = "Saved.";
  /** Messages displayed on the toggle button to show/hide the password.
   * It works only when the password is being edited. */
  private static final String SHOW_PASSWORD = "Show";
  private static final String HIDE_PASSWORD = "Hide";

  public AccountSettingsFrame(JFrame parentFrame) {

    super("Account Settings");
    this.setSize(DIMENSION);
    this.setResizable(false);
    this.setVisible(true);
    this.parentFrame = parentFrame;
    this.accountSettingsController = new AccountSettingsController(this);
    this.addWindowListener(new AccountSettingsWindowAdapter());
    initComponents();

  }
public static void main(String[] args) {
    new MainFrame();
}

  /**
   * Initializes the frame by adding its components.
   */
  private void initComponents() {

    JPanel mainPanel = new JPanel();
    this.setContentPane(mainPanel);
    mainPanel.setBorder(UIFactory.createCenterAlignmentPadding(this));
    mainPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() * 4 / 5));
    mainPanel.setLayout(new BorderLayout());

    accountInformationLabel = UIFactory.createLabel("Account Information", null);
    accountInformationLabel.setFont(UIFactory.HIGHLIGHT_TEXT_FONT);
    accountInformationLabel.setHorizontalAlignment(JLabel.LEFT);
    accountInformationLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

    JPanel userDataPanel = new JPanel(new GridLayout(3, 3, 0, 10));

    usernameLabel = UIFactory.createLabel("Username ", null);
    usernameLabel.setLabelFor(usernameTextField);
    usernameLabel.setHorizontalAlignment(JLabel.CENTER);
    usernameTextField = UIFactory.createTextField(accountSettingsController.getUsername());
    usernameTextField.setEditable(false);
    editUsernameButton = UIFactory.createButton("Edit");
    editUsernameButton.addActionListener(this);

    passwordLabel = UIFactory.createLabel("Password ", null);
    passwordLabel.setLabelFor(passwordField);
    passwordLabel.setHorizontalAlignment(JLabel.CENTER);
    passwordField = UIFactory.createPasswordField();
    passwordField.setText(accountSettingsController.getPassword());
    passwordField.setEditable(false);
    editPasswordButton = UIFactory.createButton("Edit");
    editPasswordButton.addActionListener(this);

    showPasswordButton = new JToggleButton(SHOW_PASSWORD);
    showPasswordButton.addActionListener(this);

    dataSavedLabel = UIFactory.createLabel(ACCOUNT_INFORMATION_SAVED, null);
    dataSavedLabel.setHorizontalTextPosition(JLabel.RIGHT);
    dataSavedLabel.setVisible(false);

    userDataPanel.add(usernameLabel);
    userDataPanel.add(usernameTextField);
    userDataPanel.add(editUsernameButton);
    userDataPanel.add(passwordLabel);
    userDataPanel.add(passwordField);
    userDataPanel.add(editPasswordButton);
    userDataPanel.add(dataSavedLabel);
    userDataPanel.add(showPasswordButton);

    goBackButton = UIFactory.createButton("Back");
    goBackButton.addActionListener(this);
    saveButton = UIFactory.createButton("Save");
    saveButton.addActionListener(this);

    JPanel buttonsPanel = new JPanel(new FlowLayout());
    buttonsPanel.add(goBackButton);
    buttonsPanel.add(saveButton);

    mainPanel.add(accountInformationLabel, BorderLayout.NORTH);
    mainPanel.add(userDataPanel, BorderLayout.CENTER);
    mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

    this.pack();
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    if (actionEvent.getSource() == showPasswordButton) {
      togglePasswordVisibility();
    } else {
      JButton source = (JButton) actionEvent.getSource();
      if (source == editPasswordButton) {
        String password = JOptionPane.showInputDialog(this, ASK_PASSWORD_MESSAGE);
        dataSavedLabel.setVisible(false);
        if (password != null && !password.isEmpty()) {
          if (accountSettingsController.isValidPassword(password)) {
            passwordField.setEditable(true);
          } else {
            JOptionPane.showMessageDialog(editPasswordButton, INCORRECT_PASSWORD_MESSAGE, INCORRECT_PASSWORD_MESSAGE, JOptionPane.ERROR_MESSAGE);
          }
        }
      } else if (source == editUsernameButton) {
        usernameTextField.setEditable(true);
        dataSavedLabel.setVisible(false);
      }
      if (source == goBackButton) {
        accountSettingsController.onClose(parentFrame);
      } else if (source == saveButton) {
        String username = usernameTextField.getText();
        String password = Arrays.toString(passwordField.getPassword());
        accountSettingsController.saveAccountData(username, password);
        updateFieldsAfterSave();
      }
    }
  }

  private void togglePasswordVisibility() {
    ButtonModel model = showPasswordButton.getModel();
    if (passwordField.isEditable() && model.isSelected()) {
      passwordField.setEchoChar((char) 0);
      showPasswordButton.setText(HIDE_PASSWORD);
    } else {
      passwordField.setEchoChar('*');
      showPasswordButton.setText(SHOW_PASSWORD);
    }
  }
  private void updateFieldsAfterSave() {
    dataSavedLabel.setVisible(true);
    usernameTextField.setEditable(false);
    passwordField.setEditable(false);
    passwordField.setEchoChar('*');
    showPasswordButton.setText(SHOW_PASSWORD);
  }

  private class AccountSettingsWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      accountSettingsController.onClose(parentFrame);
    }
  }
}