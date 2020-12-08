import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

/**
 * AccountSettingsFrame displays the account information of the user and allows changing the data.
 */
//todo
public class AccountSettingsFrame extends JFrame implements ActionListener {

  private JLabel usernameLabel;
  private JTextField usernameTextField;
  private JButton changePasswordButton;
  private JButton goBackButton;

  private AccountSettingsController accountSettingsController;

  private JFrame parentFrame;

  public AccountSettingsFrame(JFrame parentFrame) {

    super("Account Settings", 400, 300);

    this.parentFrame = parentFrame;

    this.accountSettingsController = new AccountSettingsController(this);

    JPanel mainPanel = new JPanel();
    this.setContentPane(mainPanel);

    mainPanel.setBorder(CENTER_ALIGNMENT_PADDING);
    mainPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight() * 2 / 3));
    mainPanel.setLayout(new BorderLayout());

    JPanel userDataPanel = new JPanel(new GridLayout(1, 2));
    // todo
    Border userDataPanelPadding =
        BorderFactory.createEmptyBorder(
            TOP_PADDING, SIDE_PADDING * 3, TOP_PADDING, SIDE_PADDING * 3);
    userDataPanel.setBorder(userDataPanelPadding);

    usernameLabel = UIFactory.createLabel("Username:", null);
    usernameLabel.setLabelFor(usernameTextField);

    usernameTextField = UIFactory.createTextField(accountSettingsController.getUsername(), null);
    usernameTextField.setEditable(false);

    userDataPanel.add(usernameLabel);
    userDataPanel.add(usernameTextField);

    mainPanel.add(userDataPanel, BorderLayout.CENTER);

    changePasswordButton = UIFactory.createButton("Change password", null);
    changePasswordButton.addActionListener(this);

    goBackButton = UIFactory.createButton("Back", null);
    goBackButton.setFocusable(false);
    goBackButton.addActionListener(this);

    JPanel buttonsPanel = new JPanel(new FlowLayout());
    buttonsPanel.add(changePasswordButton);
    buttonsPanel.add(goBackButton);

    mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

    this.addWindowFocusListener(new AccountSettingsWindowAdapter());

    this.pack();
  }

  @Override
  public void actionPerformed(ActionEvent actionEvent) {
    JButton source = (JButton) actionEvent.getSource();
    if (source == changePasswordButton) {
      accountSettingsController.askForUserPassword();
    } else if (source == goBackButton) {
      accountSettingsController.onClose(parentFrame);
    }
  }

  private class AccountSettingsWindowAdapter extends WindowAdapter {
    @Override
    public void windowClosing(WindowEvent e) {
      accountSettingsController.onClose(parentFrame);
    }
  }
}
