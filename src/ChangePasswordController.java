import javax.swing.*;

public class ChangePasswordController extends FrameController {

  public ChangePasswordController(JFrame changePasswordFrame) {
    super(changePasswordFrame);
  }

  private boolean isEqualPassword(String password1, String password2) {
    return password1.equals(password2);
  }
  /**
   * Attempts to change the password introduced by the user. The password is updated only when the
   * two passwords are equal, in which case a message window will pop up and inform the user that
   * the password was changed.
   *
   * @param newPassword1 the password introduced the first time
   * @param newPassword2 the password introduced the second time
   * @return true when the password could be changed, otherwise false
   */
  public boolean isChangedPassword(String newPassword1, String newPassword2) {
    // TODO!! save the new password
    if (isEqualPassword(newPassword1, newPassword2)) {
      JOptionPane.showMessageDialog(super.frame, "The password was updated successfully.");
      return true;
    }
    return false;
  }

  public void displayErrorMessage(JLabel label) {
    label.setVisible(true);
  }

  public void clearFields(JPasswordField passwordField1, JPasswordField passwordField2) {
    passwordField1.setText("");
    passwordField2.setText("");
  }

  public void onClose(JFrame parentFrame) {
    closeFrame();
    parentFrame.setEnabled(true);
  }
}
