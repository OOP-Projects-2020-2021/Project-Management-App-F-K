import javax.swing.*;
import java.awt.event.WindowEvent;

public class AccountSettingsController {

    JFrame accountSettingsFrame;

    AccountSettingsController(JFrame accountSettingsFrame) {
        this.accountSettingsFrame = accountSettingsFrame;
    }

    private boolean isEqualPassword(String password1,String password2) {
        return password1.equals(password2);
    }

    public boolean validateCurrentPassword(String currentPassword) {
        // TODO!! check if the old password is correct
        return true;
    }
    public void changePassword(String newPassword1,String newPassword2) {
        // TODO!! check if the 2 passwords introduced are equal, then save the new password
        //closeFrame();
    }

    public void closeFrame() {
        accountSettingsFrame.dispatchEvent(new WindowEvent(accountSettingsFrame,WindowEvent.WINDOW_CLOSING));
    }
}
