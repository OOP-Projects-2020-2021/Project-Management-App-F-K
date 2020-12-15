package controller.user;

import controller.FrameController;
import model.user.UserManager;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Controller for managing the SignUpFrame.
 *
 * @author Beata Keresztes
 */
public class SignUpController extends FrameController {

  /** Messages displayed to inform the user about the steps of signing in. " */
  private static final String FINALIZE_SIGN_UP_TITLE = "Finalize signing up";

  private static final String FINALIZE_SIGN_UP_MESSAGE =
      "Please sign in to your account to finalize the registration.";

  private UserManager userManager;

  public SignUpController(JFrame signUpFrame) {
    super(signUpFrame);
    userManager = UserManager.getInstance();
  }

  /**
   * Forward the data introduced by the user, to create a new account. Close the current frame and
   * open the SignIn Frame. The user has to introduce its new credentials to sign in.
   *
   * @param username = the username set by the user
   * @param password = the password set by the user
   * @return boolean = true on successful sign-up
   */
  public boolean signUp(String username, String password) {
    try {
      userManager.signUp(username, password);
    } catch (SQLException sqlException) {
      super.displayDatabaseErrorDialog();
      return false;
    }
    return true;
  }

  /** It closes the current frame and returns to the parent Frame. */
  public void goBack() {
    closeFrame();
  }

  /** On close it returns to the parent frame, which is the sign in page. */
  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }

  /** Display message to inform the user about the final step of signing up. */
  public void displayFinalizeSignUpDialog() {
    JOptionPane.showMessageDialog(
        frame, FINALIZE_SIGN_UP_TITLE, FINALIZE_SIGN_UP_MESSAGE, JOptionPane.PLAIN_MESSAGE);
  }
}
