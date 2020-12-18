import model.user.UserManager;
import view.ErrorDialogFactory;
import view.team.single_team.TeamFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) {
    // JFrame signInFrame = new SignInFrame();
    UserManager u = UserManager.getInstance();
    try {
      u.signIn("bori", "p");
    } catch (SQLException e) {
      e.printStackTrace();
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
    JFrame teamFrame = new TeamFrame(new JFrame(), 52);
  }
}
