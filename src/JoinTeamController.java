import javax.swing.*;

/**
 * Controller for CreateTeamFrame instances.
 *
 * @author Bori Fazakas
 */
public class JoinTeamController extends FrameController {

  public JoinTeamController(JFrame frame) {
    super(frame);
  }

  public void joinTeam(String teamCode) {
    // todo: pass data to modell, check if request is valid, handle exceptions
    System.out.println("Join team with code: " + teamCode);
    closeFrame();
  }

  /**
   * When the JoinTeamFrame was created, its parent frame was disabled so the user cannot
   * perform other operations in the main frame while this one is open.
   * Thus, when the JoinTeamFrame is closed, the parentFrame must be re-enabled.
   * @param parentFrame is the frame to be re-enabled.
   */
  public void onClose(JFrame parentFrame) {
    parentFrame.setEnabled(true);
  }
}
