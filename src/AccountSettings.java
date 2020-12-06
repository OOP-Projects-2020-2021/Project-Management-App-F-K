import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class AccountSettings extends JFrame {

  private JLabel usernameLabel;
  private JTextField usernameTextField;
  private JLabel newPasswordLabel;
  private JPasswordField newPasswordField;
  private JButton changePasswordButton;
  private JButton goBackButton;

  private AccountSettingsController accountSettingsController;

  private static final int FRAME_WIDTH = 400;
  private static final int FRAME_HEIGHT = 300;
  private static final int DEFAULT_GAP_SIZE = 20;
  private static final int TEXT_WIDTH = 30;
  private static final int TEXT_HEIGHT = 10;

  public AccountSettings(User user) {
    // add controller
    this.accountSettingsController = new AccountSettingsController(this);
    // main Frame
    this.setTitle("Account Settings");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
    this.setLayout(new BorderLayout());
    this.setBackground(Color.GRAY);

    JPanel mainPanel = new JPanel(); // main panel
    mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT / 2));

    // set borders for the main panel
    Border centerAlignmentPadding =
        BorderFactory.createEmptyBorder(
            DEFAULT_GAP_SIZE, 2 * DEFAULT_GAP_SIZE, DEFAULT_GAP_SIZE, 2 * DEFAULT_GAP_SIZE);

    mainPanel.setBorder(centerAlignmentPadding); // add padding

    GridLayout mainPanelLayout = new GridLayout();
    mainPanelLayout.setColumns(1);
    mainPanelLayout.setRows(2);
    mainPanelLayout.setVgap(DEFAULT_GAP_SIZE * 2);
    mainPanel.setLayout(mainPanelLayout);

    // set font and dimension of the labels&text-fields
    Font textFont = new Font("Courier", Font.BOLD, 15);
    Dimension textFieldDimension = new Dimension(TEXT_WIDTH, TEXT_HEIGHT);

    usernameLabel = new JLabel();
    usernameLabel.setText("Username:");
    usernameLabel.setPreferredSize(textFieldDimension);
    usernameLabel.setLabelFor(usernameTextField);
    usernameLabel.setFont(textFont);

    usernameTextField = new JTextField();
    usernameTextField.setText(user.getUsername());
    usernameTextField.setPreferredSize(textFieldDimension);
    usernameTextField.setEditable(false);
    usernameTextField.setFont(textFont);

    // set borders for the buttons
    Border buttonBorder = BorderFactory.createEmptyBorder(10, 20, 10, 20);
    // create an action listener for the buttons
    ButtonListener buttonListener = new ButtonListener();

    newPasswordLabel = new JLabel();
    newPasswordLabel.setText("New password:");
    newPasswordLabel.setPreferredSize(textFieldDimension);
    newPasswordLabel.setFont(textFont);
    newPasswordLabel.setLabelFor(newPasswordField);

    newPasswordField = new JPasswordField();
    newPasswordField.setPreferredSize(textFieldDimension);
    newPasswordField.setFont(textFont);

    changePasswordButton = new JButton();
    changePasswordButton.setText("Change password");
    changePasswordButton.setBackground(Color.GRAY);
    changePasswordButton.setBorder(buttonBorder);
    changePasswordButton.setFocusable(false);
    changePasswordButton.addActionListener(buttonListener);

    goBackButton = new JButton();
    goBackButton.setText("Back");
    goBackButton.setBackground(Color.GRAY);
    goBackButton.setBorder(buttonBorder);
    goBackButton.setFocusable(false);
    goBackButton.addActionListener(buttonListener);

    mainPanel.add(usernameLabel);
    mainPanel.add(usernameTextField);
    mainPanel.add(newPasswordLabel);
    mainPanel.add(newPasswordField);

    this.add(mainPanel, BorderLayout.NORTH); // add the main panel to the frame

    JPanel buttonsPanel = new JPanel(new FlowLayout());
    buttonsPanel.add(changePasswordButton);
    buttonsPanel.add(goBackButton);

    this.add(buttonsPanel, BorderLayout.CENTER); // add button panel to the frame

    this.pack(); // pack components
    this.setResizable(false); // doesn't allow window resizing
    this.setVisible(true); // make frame visible
  }

  private class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
      JButton source = (JButton) actionEvent.getSource();
      if (source == changePasswordButton) {
        JOptionPane.showConfirmDialog(
            goBackButton,
            "Do you want to save the new password?",
            "Saev new password",
            JOptionPane.YES_NO_OPTION);
        accountSettingsController.changePassword(Arrays.toString(newPasswordField.getPassword()));
      } else if (source == goBackButton) {
        JOptionPane.showConfirmDialog(
            goBackButton, "Do you want to go back?", "Going back", JOptionPane.YES_NO_OPTION);
        accountSettingsController.closeFrame();
      }
    }
  }
}
