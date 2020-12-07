import javax.swing.*;

/**
 * Controller for managing the SignInFrame.
 *
 * @author Beata Keresztes
 */
public class SignInController extends UserFrameController {

  public SignInController(JFrame signInFrame) {
    super(signInFrame);
  }

  public void signIn(String username, String password) {
    // TODO!! validate user credentials: if correct close frame, otherwise clear the field(s)
    //  which contained wrong information and let the user try again
    //  set Max no of trials
    closeFrame();
  }

  /** Opens a new Frame for the User to sign up, and closes the current frame. */
  public void enableSigningUp() {
    new SignUpFrame();
    closeFrame();
  }
}
