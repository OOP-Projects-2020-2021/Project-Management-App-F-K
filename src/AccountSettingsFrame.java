import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * AccountSettingsFrame displays the account information of the user and allows changing the data.
 */
public class AccountSettingsFrame extends UserFrame {

  private JLabel usernameLabel;
  private JTextField usernameTextField;
  private JButton changePasswordButton;
  private JButton goBackButton;

  private AccountSettingsController accountSettingsController;

  private JFrame parentFrame;

  public AccountSettingsFrame(JFrame parentFrame) {

    super(400,300,10);

    this.parentFrame = parentFrame;

    this.accountSettingsController = new AccountSettingsController(this);

    this.setTitle("Account Settings");
    this.setSize(FRAME_DIMENSION);

    JPanel mainPanel = new JPanel();
    this.setContentPane(mainPanel);

    mainPanel.setBorder(CENTER_ALIGNMENT_PADDING);
    mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT * 2 / 3));
    mainPanel.setLayout(new BorderLayout());

    JPanel userDataPanel = new JPanel(new GridLayout(1, 2));
    Border userDataPanelPadding =
        BorderFactory.createEmptyBorder(
            TOP_PADDING, SIDE_PADDING * 3, TOP_PADDING, SIDE_PADDING * 3);
    userDataPanel.setBorder(userDataPanelPadding);

    usernameLabel = createLabel("Username:");
    usernameLabel.setLabelFor(usernameTextField);

    usernameTextField = createTextField(accountSettingsController.getUsername());
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

    addWindowFocusListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent e) {
        accountSettingsController.onClose(parentFrame);
      }
    });
  }

  private class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      JButton source = (JButton) actionEvent.getSource();
      if (source == changePasswordButton) {
          accountSettingsController.askForUserPassword();
      } else if(source == goBackButton) {
        accountSettingsController.onClose(parentFrame);
      }
    }
  }
}
