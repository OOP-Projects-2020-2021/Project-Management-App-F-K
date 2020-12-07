import javax.swing.*;

/**
 * AccountSettingsController controls the AccountSettingsFrame, managing operations like gathering
 * the data for display, changing the data and updating the display.
 */
public class AccountSettingsController extends UserFrameController {

  AccountSettingsController(JFrame accountSettingsFrame) {
    super(accountSettingsFrame);
  }

  public String getUsername() {
    // TODO!! get user's account information
    // just a dummy user
    return "admin";
  }

  /**
   * Checks whether the password introduced is valid.
   *
   * @param password introduced in the input dialog by the user
   * @return true if the password entered by the user is valid
   */
  private boolean validateCurrentPassword(String password) {
    // TODO!! validate current password before changing it
    return true;
  }

  /**
   * Represents a validation step, to ensure the security of the account, not allowing other people
   * to change the password. It asks the user to introduce its current password before changing it.
   * In case the introduced password is correct it opens a new frame for Changing the password,
   * otherwise it displays an error message.
   */
  public void askForUserPassword() {
    String password = JOptionPane.showInputDialog(super.frame, "Enter your current password:");
    if (validateCurrentPassword(password)) {
      enableChangingPassword();
    } else {
      JOptionPane.showMessageDialog(
          super.frame, "Password was incorrect.", "Incorrect password", JOptionPane.ERROR_MESSAGE);
    }
  }
  /**
   * Opens a frame which allows the user to change its password. The current frame will be disabled
   * until the ChangePasswordFrame is used. When the user closes the ChangePasswordFrame, the
   * AccountSettingsFrame will be enabled again.
   */
  private void enableChangingPassword() {
    new ChangePasswordFrame(super.frame);
    super.frame.setEnabled(false);
  }

  /**
   * When this frame is closed, the user is redirected to the Main Frame.
   *
   * @param parentFrame the frame which will be enabled
   */
  public void onClose(JFrame parentFrame) {
    closeFrame();
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }
}
