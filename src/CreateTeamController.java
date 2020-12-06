import javax.swing.*;

public class CreateTeamController extends FrameController {

  public CreateTeamController(JFrame frame) {
    super(frame);
  }

  public void createTeam(String teamName) {
    // todo: pass data to modell, check if request is valid, handle exceptions
    System.out.println("Create team with name: " + teamName);
    closeFrame();
  }
}
