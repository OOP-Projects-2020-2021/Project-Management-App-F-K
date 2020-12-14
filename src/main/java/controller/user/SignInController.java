package controller.user;

import controller.FrameController;
import view.main.MainFrame;
import view.user.SignUpFrame;

import javax.swing.*;

/**
 * Controller for managing the SignInFrame.
 *
 * @author Beata Keresztes
 */
public class SignInController extends FrameController {

  /**
   * The signInFlag is used to notify the windowAdapter whether the frame is closing because of a
   * successful sign-in or because the user exited the application.
   */
  private boolean signInFlag;

  /** Messages displayed to inform the user about the sign in's validation. */
  private static final String WRONG_SIGN_IN_CREDENTIALS_MESSAGE = "Wrong credentials!";

  private static final String INVALID_SIGN_IN_MESSAGE =
      "Invalid sign in! \nCheck that the username and password\nthat you introduced are correct!";

  public SignInController(JFrame signInFrame) {
    super(signInFrame);
    signInFlag = false;
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
  public boolean validSignIn(String username, String password) {
    // TODO validate the user credentials
    //  set Max no of trials
    if (!isEmptyField(username) && !isEmptyField(password)) {
      //
    }
    return true;
  }

  /**
   * Check if the text-field contains data introduced by the user
   *
   * @param text = from the text-field
   * @return boolean = true if the field is empty
   */
  private boolean isEmptyField(String text) {
    return (text == null || text.isEmpty());
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
  /** Opens an error message dialog that informs the user about the invalid sign-in. */
  public void displayInvalidSignInMessageDialog() {
    JOptionPane.showMessageDialog(
        frame,
        INVALID_SIGN_IN_MESSAGE,
        WRONG_SIGN_IN_CREDENTIALS_MESSAGE,
        JOptionPane.WARNING_MESSAGE);
  }
}
