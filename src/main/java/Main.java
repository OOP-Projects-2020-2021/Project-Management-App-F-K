import model.user.UserManager;
import view.team.single_team.TeamFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) {
    // JFrame signInFrame = new SignInFrame();
    UserManager u = UserManager.getInstance();
    try {
      u.signIn("bea", "bea");
    } catch (SQLException e) {
      System.out.println("not signed in");
    }
    JFrame t = new TeamFrame(new JFrame(), 52);
  }
}
