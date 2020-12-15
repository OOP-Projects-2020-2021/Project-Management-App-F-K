package controller.user;

import controller.FrameController;
import model.user.UserManager;
import view.main.MainFrame;
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
  UserManager userManager;
  /**
   * The signInFlag is used to notify the windowAdapter whether the frame is closing because of a
   * successful sign-in or because the user exited the application.
   */
  private boolean signInFlag;
  /** Messages displayed to inform the user about the sign in's validation. */
  private static final String INVALID_SIGN_IN_TITLE = "Invalid sign in!";

  private static final String INVALID_SIGN_IN_MESSAGE =
      "Check that the username and password\nthat you introduced are correct!";

  public SignInController(JFrame signInFrame) {
    super(signInFrame);
    signInFlag = false;
    userManager = UserManager.getInstance();
  }

  public boolean getSignInFlag() {
    return signInFlag;
  }

  /**
   * Check if the username and password introduced are correct.
   *
   * @param username = the username introduced by the user
   * @param password = the password introduced by the user
   * @return boolean = true, if the username and password are correct
   */
  public boolean validateSignIn(String username, String password) {
    try {
      if (isNotEmptyText(username) && isNotEmptyText(password)) {
        return userManager.signIn(username, password);
      }
    } catch (SQLException sqlException) {
      super.displayDatabaseErrorDialog();
    }
    return false;
  }
  /** Checks if the text from the text-field is not empty. */
  private boolean isNotEmptyText(String text) {
    return !(text == null || text.isEmpty());
  }

  /** Opens the main menu on successful sign-in. */
  public void enableSigningIn() {
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
        frame, INVALID_SIGN_IN_MESSAGE,INVALID_SIGN_IN_TITLE, JOptionPane.WARNING_MESSAGE);
  }
}
