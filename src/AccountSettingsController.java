import javax.swing.*;

/**
 * AccountSettingsController controls the AccountSettingsFrame, managing operations like gathering
 * the data for display, changing the data and updating the display.
 */
public class AccountSettingsController extends FrameController {

  AccountSettingsController(JFrame accountSettingsFrame) {
    super(accountSettingsFrame);
  }

  public String getUsername() {
    // TODO!! get user's account information
    return "admin";
  }

  public String getPassword() {
    // TODO!! get user's account information
    return "admin";
  }

  /**
   * Represents a validation step, to ensure the security of the account, not allowing other people
   * to change the password. It asks the user to introduce its current password before changing it.
   * In case the introduced password is correct it opens a new frame for Changing the password,
   * otherwise it displays an error message.
   */
  public boolean isValidPassword(String password) {
    //todo validate password
    return true;
  }
  /**
   * Opens a frame which allows the user to change its password. The current frame will be disabled
   * until the ChangePasswordFrame is used. When the user closes the ChangePasswordFrame, the
   * AccountSettingsFrame will be enabled again.
   */

  public void saveAccountData(String username, String password) {
    //todo save the changes
  }
  /**
   * When this frame is closed, the user is redirected to the Main Frame.
   *
   * @param parentFrame the frame which will be enabled
   */
  public void onClose(JFrame parentFrame) {
    parentFrame.setEnabled(true);
    //closeFrame();
    frame.setVisible(false);
    frame.setEnabled(false);
  }
}
