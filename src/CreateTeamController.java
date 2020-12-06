import javax.swing.*;

/**
 * Controller for CreateTeamFrame instances.
 *
 * @author Bori Fazakas
 */
public class CreateTeamController extends FrameController {

  public CreateTeamController(JFrame frame) {
    super(frame);
  }

  public void createTeam(String teamName) {
    // todo: pass data to modell, check if request is valid, handle exceptions
    System.out.println("Create team with name: " + teamName);
    closeFrame();
  }

  /**
   * When the CreateTeamFrame was created, its parent frame was disabled so the user cannot
   * perform other operations in the main frame while this one is open.
   * Thus, when the CreateTeamFrame is closed, the parentFrame must be re-enabled.
   * @param parentFrame is the frame to be re-enabled.
   */
  public void onClose(JFrame parentFrame) {
    parentFrame.setEnabled(true);
  }
}
