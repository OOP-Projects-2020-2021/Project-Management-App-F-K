import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Shows the account information of the user and allows changing the data.
 */
public class AccountSettingsFrame extends UserFrame {

  private JLabel usernameLabel;
  private JTextField usernameTextField;
  private JButton changePasswordButton;
  private JButton goBackButton;

  private AccountSettingsController accountSettingsController;

  public AccountSettingsFrame(User user) {

    super(400,300,10);
    this.accountSettingsController = new AccountSettingsController(this,user);

    this.setTitle("Account Settings");
    this.setSize(FRAME_DIMENSION);

    // create a main panel and add it to the frame
    JPanel mainPanel = new JPanel();
    this.setContentPane(mainPanel);

    mainPanel.setBorder(CENTER_ALIGNMENT_PADDING);
    mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT * 2 / 3));
    mainPanel.setLayout(new BorderLayout());

    // create a user panel to display the user data
    JPanel userDataPanel = new JPanel(new GridLayout(1, 2));
    Border userDataPanelPadding =
        BorderFactory.createEmptyBorder(
            TOP_PADDING, SIDE_PADDING * 3, TOP_PADDING, SIDE_PADDING * 3);
    userDataPanel.setBorder(userDataPanelPadding);

    usernameLabel = createLabel("Username:");
    usernameLabel.setLabelFor(usernameTextField);

    usernameTextField = createTextField(user.getUsername());
    usernameTextField.setEditable(false);

    userDataPanel.add(usernameLabel);
    userDataPanel.add(usernameTextField);

    mainPanel.add(userDataPanel, BorderLayout.CENTER);

    ButtonListener buttonListener = new ButtonListener();

    changePasswordButton = createButton("Change password");
    changePasswordButton.addActionListener(buttonListener);

    goBackButton = createButton("Back");
    goBackButton.setFocusable(false);
    goBackButton.addActionListener(buttonListener);

    JPanel buttonsPanel = new JPanel(new FlowLayout());
    buttonsPanel.add(changePasswordButton);
    buttonsPanel.add(goBackButton);

    mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

    this.pack();
    this.setResizable(false);
    this.setVisible(true);
    // TODO!! solve this
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public static void main(String[] args) {
    new AccountSettingsFrame(new User("admin","admin"));
  }

  private class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      JButton source = (JButton) actionEvent.getSource();
      if (source == changePasswordButton) {
        // TODO!! validate the current password
        String password =
                JOptionPane.showInputDialog(changePasswordButton, "Enter your current password:");
        if (accountSettingsController.validateCurrentPassword(password)) {
            // TODO!! enable change password frame
        } else {
          JOptionPane.showMessageDialog(
                  changePasswordButton,
                  "Password was incorrect.",
                  "Incorrect password",
                  JOptionPane.WARNING_MESSAGE);
        }
      } else if(source == goBackButton) {
        // TODO!! close frame and return to main frame
      }
    }
  }
}
