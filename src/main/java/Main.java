import model.user.UserManager;
import view.ErrorDialogFactory;
import view.team.single_team.TeamFrame;
import view.user.SignInFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) {
    //JFrame signInFrame = new SignInFrame();
    UserManager u = UserManager.getInstance();
    try {
      u.signIn("anna","8888");
    }catch(SQLException e) {
      e.printStackTrace();
      ErrorDialogFactory.createErrorDialog(e,null,null);
    }
    JFrame teamFrame = new TeamFrame(new JFrame(),52);
  }

}
