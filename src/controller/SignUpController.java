package controller;

import javax.swing.*;

public class SignUpController extends FrameController {

  public SignUpController(JFrame signUpFrame) {
    super(signUpFrame);
  }

  /**
   * Forward the data introduced by the user, to create a new account. Close the current frame and
   * open the SignIn Frame. The user has to introduce its new credentials to sign in.
   *
   * @param username = the username set by the user
   * @param password = the password set by the user
   */
  public void signUp(String username, String password) {
    // TODO!! save data introduced by the user
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
}
