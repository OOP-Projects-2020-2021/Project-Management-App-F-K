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

  /** The signInFlag is used to notify the windowAdapter whether the frame is closing because of a
   * successful sign-in or because the user exited the application. */
  private boolean signInFlag;

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
  public boolean validSignIn(String username, char[] password) {
    // TODO validate the user credentials
    // TODO transform password into String before passing it to the model
    //  set Max no of trials
    // just for testing: empty fields result in invalid sign-in
    return ((username != null && !username.isEmpty()) && (password.length != 0));
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
}
