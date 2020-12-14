package controller.user;
import controller.FrameController;
import model.user.UserManager;
import javax.swing.*;
import java.util.NoSuchElementException;

/**
 * AccountSettingsController controls the AccountSettingsFrame, managing operations like gathering
 * the data for display, changing the data and updating the display.
 *
 * @author Beata Keresztes
 */
public class AccountSettingsController extends FrameController {

  /** Instance of a UserManager, which accesses and modifies data related to the user. */
  private UserManager userManager;

  private static final String UNAVAILABLE_DATA = "Data unavailable";
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
   * @return boolean = true if the password matches, false if it doesn't match or if nothing was introduced
   */
  public boolean isValidPassword(String password) {
    if (password != null && !password.isEmpty()) {
      return userManager.validatePassword(password);
    }
    return false;
  }
  public String getUsername() {
    try {
      return userManager.getCurrentUser().get().getUsername();
    }catch(NoSuchElementException noSuchElementException) {
      return UNAVAILABLE_DATA;
    }
  }
  public String getPassword() {
    try {
      return userManager.getCurrentUser().get().getPassword();
    }catch(NoSuchElementException noSuchElementException) {
      return UNAVAILABLE_DATA;
    }
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
