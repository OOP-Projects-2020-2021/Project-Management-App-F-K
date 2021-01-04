package controller.user;

import controller.FrameController;
import model.user.UserManager;
import model.user.exceptions.EmptyFieldsException;
import view.ErrorDialogFactory;
import view.main.MainFrame;
import view.user.SignInFrame;
import view.user.SignUpFrame;
import javax.swing.*;
import java.sql.SQLException;

/**
 * Controller for managing the SignInFrame.
 *
 * @author Beata Keresztes
 */
public class SignInController extends FrameController {

  /** An instance of the UserManager which manages the data in the model User. */
  private UserManager userManager;
  /**
   * The signInFlag is used to notify the windowAdapter whether the frame is closing because of a
   * successful sign-in or because the user exited the application.
   */
  private boolean signInFlag;
  /** Messages displayed to inform the user about the sign in's validation. */
  private static final String INVALID_SIGN_IN_TITLE = "Invalid sign in!";

  private static final String INVALID_SIGN_IN_MESSAGE =
      "Check that the username and password\nthat you introduced are correct!";
  private SignInFrame signInFrame;

  public SignInController(SignInFrame signInFrame) {
    super(signInFrame);
    this.signInFrame = signInFrame;
    signInFlag = false;
    userManager = UserManager.getInstance();
  }

  public boolean getSignInFlag() {
    return signInFlag;
  }

  /**
   * Check if the username and password introduced are correct for the sign-in and handles both
   * cases.
   *
   * @param username = the username introduced by the user
   * @param password = the password introduced by the user
   */
  public void trySignIn(String username, String password) {
    try {
      if (userManager.signIn(username, password)) {
        signingIn();
      } else {
        // show error dialog and let the user try again
        displayInvalidSignInDialog();
        signInFrame.clearTextFields();
      }
    } catch (SQLException | EmptyFieldsException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, null);
    }
  }

  /** Opens the main menu on successful sign-in. */
  public void signingIn() {
    new MainFrame();
    signInFlag = true;
    closeFrame();
  }

  /** Opens a new Frame for the User to sign up, also it disables and hides the current frame. */
  public void enableSigningUp() {
    new SignUpFrame(super.frame);
    frame.setVisible(false);
    frame.setEnabled(false);
  }

  /** Display an error message in case of an unsuccessful sign-in attempt. */
  public void displayInvalidSignInDialog() {
    JOptionPane.showMessageDialog(
        frame, INVALID_SIGN_IN_MESSAGE, INVALID_SIGN_IN_TITLE, JOptionPane.WARNING_MESSAGE);
  }
}
