package controller;
import javax.swing.*;

/**
 * AccountSettingsController controls the AccountSettingsFrame, managing operations like gathering
 * the data for display, changing the data and updating the display.
 */
public class AccountSettingsController extends FrameController {

  public AccountSettingsController(JFrame accountSettingsFrame) {
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
   * In case the introduced password is correct it enables the user to edit the Password field, otherwise not.
   *
   * @param  password = the password introduced by the user
   */
  public boolean isValidPassword(String password) {
    // todo validate password
    return true;
  }
  /**
   * Saves all the information related to the user's account, which might have been changed.
   *
   * @param username = the username of the user
   * @param password = the password of the user
   */
  public void saveAccountData(String username, String password) {
    // todo save the changes
  }
  /** On going back, it closes the current frame. */
  public void goBack() {
    closeFrame();
  }
  /**
   * When this frame is closed, the user is redirected to the Main Frame (parent frame).
   *
   * @param parentFrame the frame which will be enabled
   */
  public void onClose(JFrame parentFrame) {
    parentFrame.setEnabled(true);
  }
}
