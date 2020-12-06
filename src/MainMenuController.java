import javax.swing.*;

public class MainMenuController extends FrameController {

  public MainMenuController(JFrame frame) {
    super(frame);
  }

  public void logoutUser() {
    // todo
  }

  public void enableUserDataSettings() {
    // todo
  }

  public void enableCreatingNewTeam() {
    new CreateTeamFrame(frame);
    frame.setEnabled(false);
  }

  public void enableJoiningNewTeam() {
    new JoinTeamFrame(frame);
    frame.setEnabled(false);
  }
}
