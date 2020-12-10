package controller;
import view.MainFrame;
import view.SignUpFrame;

import javax.swing.*;

/**
 * Controller for managing the SignInFrame.
 *
 * @author Beata Keresztes
 */
public class SignInController extends FrameController {

  public SignInController(JFrame signInFrame) {
    super(signInFrame);
  }

  /** Opens the main menu on successful sign-in. */
  public void enableSigningIn(String username, String password) {
    // TODO!! validate user credentials: if correct close frame, and redirect user to the main menu,
    //  otherwise clear the field(s) which contained wrong information and let the user try again
    //  set Max no of trials
    new MainFrame();
    closeFrame();
  }

  /** Opens a new Frame for the User to sign up, also it disables and hides the current frame. */
  public void enableSigningUp() {
    new SignUpFrame(super.frame);
    frame.setVisible(false);
    frame.setEnabled(false);
  }
}
