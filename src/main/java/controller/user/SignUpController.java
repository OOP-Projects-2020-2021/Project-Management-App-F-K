package controller.user;

import controller.FrameController;
import model.user.UserManager;
import model.user.exceptions.DuplicateUsernameException;
import model.user.exceptions.EmptyFieldsException;
import view.ErrorDialogFactory;
import view.user.SignUpFrame;

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
  private SignUpFrame signUpFrame;

  public SignUpController(SignUpFrame signUpFrame) {
    super(signUpFrame);
    this.signUpFrame = signUpFrame;
    userManager = UserManager.getInstance();
  }

  /**
   * Forward the data introduced by the user, to create a new account.
   *
   * @param username = the username set by the user
   * @param password = the password set by the user
   */
  public void signUp(String username, String password) {
    try {
      userManager.signUp(username, password);
      displayFinalizeSignUpDialog();
      goBack();
    } catch (SQLException | EmptyFieldsException | DuplicateUsernameException e) {
      // display error and let the user try again.
      ErrorDialogFactory.createErrorDialog(e, frame, null);
      signUpFrame.clearTextFields();
    }
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
}
