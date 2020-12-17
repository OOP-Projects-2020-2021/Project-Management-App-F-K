package controller.user;

import controller.FrameController;
import model.user.UserManager;
import model.user.exceptions.DuplicateUsernameException;
import view.ErrorDialogFactory;

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

  /** Messages displayed to inform the user that some fields were left blank. */
  private static final String EMPTY_FIELDS_LEFT_TITLE = "Empty fields left";

  private static final String EMPTY_FIELDS_LEFT_MESSAGE =
      "Please fill in all the required information before continuing!";

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
      if (isNotEmptyText(username) && isNotEmptyText(password)) {
        userManager.signUp(username, password);
        return true;
      } else {
        displayEmptyFieldsSignUpDialog();
      }
    } catch (DuplicateUsernameException | SQLException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, null);
    }
    return false;
  }
  /** Checks if the text from the text-field is not empty. */
  private boolean isNotEmptyText(String text) {
    return !(text == null || text.isEmpty());
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
        frame, FINALIZE_SIGN_UP_MESSAGE, FINALIZE_SIGN_UP_TITLE, JOptionPane.PLAIN_MESSAGE);
  }
  /** Display warning message to inform the user that some fields were not filled. */
  public void displayEmptyFieldsSignUpDialog() {
    JOptionPane.showMessageDialog(
        frame, EMPTY_FIELDS_LEFT_MESSAGE, EMPTY_FIELDS_LEFT_TITLE, JOptionPane.WARNING_MESSAGE);
  }
}
