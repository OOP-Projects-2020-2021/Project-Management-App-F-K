import javax.swing.*;

public class SignUpController extends FrameController {

  public SignUpController(JFrame signUpFrame) {
    super(signUpFrame);
  }

  /**
   * Forward the data introduced by the user, to create a new account. Close the current frame and
   * open the teamView frame instead.
   *
   * @param username the username set by the user
   * @param password the password set by the user
   */
  public void signUp(String username, String password, JFrame parentFrame) {
    // TODO!! save data introduced by the user
    // TODO!! open the frame with the main menu and close current frame
    onClose(parentFrame);
  }

  public void onClose(JFrame parentFrame) {
    closeFrame();
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }
}
