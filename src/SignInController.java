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

  public void signIn(String username, String password) {
    // TODO!! validate user credentials: if correct close frame, and redirect user to the main menu,
    //  otherwise clear the field(s) which contained wrong information and let the user try again
    //  set Max no of trials
    new MainFrame();
    frame.setEnabled(false);
    closeFrame();
  }

  /** Opens a new Frame for the User to sign up, and closes the current frame. */
  public void enableSigningUp() {
    new SignUpFrame(super.frame);
    frame.setEnabled(false);
    closeFrame();
  }
  public void onClose() {
    frame.setEnabled(false);
    closeFrame();
  }
}
