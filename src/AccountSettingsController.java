import javax.swing.*;
import java.awt.event.WindowEvent;

public class AccountSettingsController {

  JFrame accountSettingsFrame;

  AccountSettingsController(JFrame accountSettingsFrame) {
    this.accountSettingsFrame = accountSettingsFrame;
  }

  public void changePassword(String password) {
    // TODO!! check if the new password has a correct format
    closeFrame();
  }

  public void closeFrame() {
    accountSettingsFrame.dispatchEvent(
        new WindowEvent(accountSettingsFrame, WindowEvent.WINDOW_CLOSING));
  }
}
