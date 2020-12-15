package controller.user;

import controller.FrameController;
import model.user.InexistentUserException;
import model.user.NoSignedInUserException;
import model.user.UserManager;
import javax.swing.*;
import java.sql.SQLException;

/**
 * AccountSettingsController controls the AccountSettingsFrame, managing operations like gathering
 * the data for display, changing the data and updating the display.
 *
 * @author Beata Keresztes
 */
public class AccountSettingsController extends FrameController {

  /** Instance of a UserManager, which accesses and modifies data related to the user. */
  private UserManager userManager;
  /** Error messages shown to the user when the update of the fields is unsuccessfuul. */
  private static final String UNAVAILABLE_DATA = "Data unavailable";

  private static final String FAILED_UPDATE_MESSAGE = "Update failed!";
  private static final String CHANGES_LOST_MESSAGE =
      "An error occurred and the changes could not be saved.";
  /** Messages displayed to inform the user about the validation of the data. */
  private static final String INCORRECT_PASSWORD_MESSAGE = "Incorrect password!";

  public AccountSettingsController(JFrame accountSettingsFrame) {
    super(accountSettingsFrame);
    userManager = UserManager.getInstance();
  }

  /**
   * Represents a validation step, to ensure the security of the account, not allowing other people
   * to change the password. It asks the user to introduce its current password before changing it.
   * In case the introduced password is correct it enables the user to edit the Password field,
   * otherwise not.
   *
   * @param password = the password introduced by the user
   * @return boolean = true if the password matches, false if it doesn't match or if nothing was
   *     introduced
   */
  public boolean isValidPassword(String password) {
    if (password != null && !password.isEmpty()) {
      return userManager.validatePassword(password);
    }
    return false;
  }

  public String getUsername() {
    return userManager.getCurrentUser().get().getUsername();
  }

  public String getPassword() {
    return userManager.getCurrentUser().get().getPassword();
  }
  /**
   * Saves all the information related to the user's account, which might have been changed.
   *
   * @param username = the username of the user
   * @param password = the password of the user
   */
  public boolean saveAccountData(String username, String password) {
    // todo restore the old data if the update failed
    try {
      userManager.updateUser(username, password);
      return true;
    } catch (SQLException sqlException) {
      displayDatabaseErrorDialog();
    } catch (InexistentUserException | NoSignedInUserException e) {
      displayFailedUpdateDialog();
    }
    return false;
  }

  public void displayFailedUpdateDialog() {
    JOptionPane.showMessageDialog(
        frame, FAILED_UPDATE_MESSAGE, CHANGES_LOST_MESSAGE, JOptionPane.WARNING_MESSAGE);
  }

  public void displayIncorrectPasswordDialog() {
    JOptionPane.showMessageDialog(
        frame, INCORRECT_PASSWORD_MESSAGE, INCORRECT_PASSWORD_MESSAGE, JOptionPane.ERROR_MESSAGE);
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
