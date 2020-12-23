import model.project.ProjectManager;
import model.project.exceptions.InexistentProjectException;
import model.user.User;
import model.user.UserManager;
import view.project.single_project.ProjectFrame;
import view.user.SignInFrame;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
  public static void main(String[] args) {
    //JFrame signInFrame = new SignInFrame();
    UserManager u = UserManager.getInstance();
    try {
      u.signIn("bori","p");
    }catch(SQLException e) {
      e.printStackTrace();
    }
    try {
      JFrame p = new ProjectFrame(new JFrame(), ProjectManager.getInstance().getProjectById(2));
    } catch (InexistentProjectException | SQLException e) {
      e.printStackTrace();
    }
  }
}
