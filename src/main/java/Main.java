import javax.swing.*;

import view.project.ProjectFrame;
import view.project.TeamFrame;
import view.project.TeamSettingsFrame;
import view.user.SignInFrame;

public class Main {
  public static void main(String[] args) {
   // JFrame signInFrame = new SignInFrame();
    //JFrame home = new TeamSettingsFrame();
    JFrame home = new ProjectFrame(new JFrame());
  }
}
