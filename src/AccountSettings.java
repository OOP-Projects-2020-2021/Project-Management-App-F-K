import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class AccountSettings extends JFrame{
    
    private JLabel usernameLabel;
    private JTextField usernameTextField;
    private JButton changePasswordButton;
    private JButton goBackButton;

    private JLabel newPasswordAgainLabel;
    private JPasswordField newPasswordAgainField;
    private JLabel newPasswordLabel;
    private JPasswordField newPasswordField;
    private JButton savePasswordButton;
    JDialog changePasswordDialog;

    private AccountSettingsController accountSettingsController;
    
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 300;
    private static final int SIDE_PADDING = FRAME_WIDTH/20;
    private static final int TOP_PADDING = FRAME_HEIGHT/10;

    private static final int DIALOG_WIDTH = 500;
    private static final int DIALOG_HEIGHT = 300;
    private static final int DIALOG_SIDE_PADDING = DIALOG_WIDTH/20;
    private static final int DIALOG_TOP_PADDING = DIALOG_HEIGHT/10;

    public AccountSettings(User user) {

        this.accountSettingsController = new AccountSettingsController(this);

        this.setTitle("Account Settings");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT));

        JPanel mainPanel = new JPanel();    // main panel

        Border mainPanelPadding = BorderFactory.createEmptyBorder(TOP_PADDING,SIDE_PADDING,TOP_PADDING,SIDE_PADDING);

        mainPanel.setBorder(mainPanelPadding);  // set borders for the main panel
        mainPanel.setPreferredSize(new Dimension(FRAME_WIDTH,FRAME_HEIGHT * 2/3));
        this.setContentPane(mainPanel); // add the main panel to the frame

        BorderLayout mainPanelLayout = new BorderLayout();
        mainPanel.setLayout(mainPanelLayout);

        JPanel userDataPanel = new JPanel(new GridLayout(1,2));
        Border userDataPanelPadding = BorderFactory.createEmptyBorder(TOP_PADDING,SIDE_PADDING*3,TOP_PADDING,SIDE_PADDING*3);
        userDataPanel.setBorder(userDataPanelPadding);

        // set font and dimension of the labels&text-fields
        Font textFont = new Font("Courier", Font.BOLD,15);
        Dimension textFieldDimension = new Dimension(30,10);

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

        userDataPanel.add(usernameLabel);
        userDataPanel.add(usernameTextField);

        mainPanel.add(userDataPanel,BorderLayout.CENTER);

        // set borders for the buttons
        Border buttonBorder = BorderFactory.createEmptyBorder(10,20,10,20);
        // create an action listener for the buttons
        ButtonListener buttonListener = new ButtonListener();

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

        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(changePasswordButton);
        buttonsPanel.add(goBackButton);
        mainPanel.add(buttonsPanel,BorderLayout.SOUTH); // add button panel to the frame

        // other dialog

        changePasswordDialog = new JDialog(this,"Change password");
        changePasswordDialog.setSize(DIALOG_WIDTH,DIALOG_HEIGHT);
        changePasswordDialog.setResizable(false);
        changePasswordDialog.setVisible(false);
        changePasswordDialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Border dialogMainPanelPadding = BorderFactory.createEmptyBorder(DIALOG_TOP_PADDING,DIALOG_SIDE_PADDING,DIALOG_TOP_PADDING,DIALOG_SIDE_PADDING);

        JPanel mainDialogPanel = new JPanel();
        mainDialogPanel.setBorder(dialogMainPanelPadding);
        mainDialogPanel.setPreferredSize(new Dimension(DIALOG_WIDTH,DIALOG_HEIGHT*3/4));
        changePasswordDialog.setContentPane(mainDialogPanel);

        JPanel dataInputPanel = new JPanel(new GridLayout(2,1,20,20));
        dataInputPanel.setPreferredSize(new Dimension(DIALOG_WIDTH,DIALOG_HEIGHT));
        dataInputPanel.setBorder(dialogMainPanelPadding);

        BorderLayout dialogPanelLayout = new BorderLayout();
        mainDialogPanel.setLayout(dialogPanelLayout);

        newPasswordLabel = new JLabel();
        newPasswordLabel.setText("Enter new password:");
        newPasswordLabel.setPreferredSize(textFieldDimension);
        newPasswordLabel.setFont(textFont);
        newPasswordLabel.setLabelFor(newPasswordField);

        newPasswordField = new JPasswordField();
        newPasswordField.setPreferredSize(textFieldDimension);
        newPasswordField.setFont(textFont);

        newPasswordAgainLabel = new JLabel();
        newPasswordAgainLabel.setText("Reenter new password:");
        newPasswordAgainLabel.setPreferredSize(textFieldDimension);
        newPasswordAgainLabel.setFont(textFont);
        newPasswordAgainLabel.setLabelFor(newPasswordAgainField);

        newPasswordAgainField = new JPasswordField();
        newPasswordAgainField.setPreferredSize(textFieldDimension);
        newPasswordAgainField.setFont(textFont);

        dataInputPanel.add(newPasswordLabel);
        dataInputPanel.add(newPasswordField);
        dataInputPanel.add(newPasswordAgainLabel);
        dataInputPanel.add(newPasswordAgainField);

        JPanel saveButtonPanel = new JPanel(new FlowLayout());

        savePasswordButton = new JButton();
        savePasswordButton.setText("Save");
        savePasswordButton.setBackground(Color.GRAY);
        savePasswordButton.setBorder(buttonBorder);
        savePasswordButton.setFocusable(false);
        savePasswordButton.addActionListener(buttonListener);

        saveButtonPanel.add(savePasswordButton);

        mainDialogPanel.add(dataInputPanel,BorderLayout.CENTER);
        mainDialogPanel.add(saveButtonPanel,BorderLayout.SOUTH);

        this.pack();                // pack components
        this.setResizable(false);   // doesn't allow window resizing
        this.setVisible(true);      // make frame visible

    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JButton source = (JButton) actionEvent.getSource();
            if(source == changePasswordButton) {
                // TODO!! validate the current password
                String currentPassword = JOptionPane.showInputDialog(changePasswordButton,"Enter your current password:");
                if(accountSettingsController.validateCurrentPassword(currentPassword)) {
                    changePasswordDialog.setVisible(true);
                }
                else {
                    JOptionPane.showMessageDialog(changePasswordButton,"Password was incorrect.","Incorrect password",JOptionPane.WARNING_MESSAGE);
                }
            }
            else if(source == goBackButton) {
                JOptionPane.showConfirmDialog(goBackButton,"Do you want to go back?","Going back",JOptionPane.YES_NO_OPTION);
               // accountSettingsController.closeFrame();
            }else if(source == savePasswordButton) {
                String newPassword1 = Arrays.toString(newPasswordField.getPassword());
                String newPassword2 = Arrays.toString(newPasswordAgainField.getPassword());
                accountSettingsController.changePassword(newPassword1,newPassword2);
                JOptionPane.showMessageDialog(goBackButton,"Password was updated successfully","Saved password",JOptionPane.INFORMATION_MESSAGE);
                changePasswordDialog.dispose();
            }
        }
    }

}
