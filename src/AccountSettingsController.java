import javax.swing.*;

public class AccountSettingsController extends FrameController{

  User user;

  AccountSettingsController(JFrame accountSettingsFrame,User user) {
    super(accountSettingsFrame);
    this.user = user;
  }

  public boolean validateCurrentPassword(String password) {
      return (user.getPassword().equals(password));
  }
}
