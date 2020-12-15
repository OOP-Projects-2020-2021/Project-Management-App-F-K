package controller.team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.team.InexistentTeamException;
import model.team.TeamManager;
import model.user.NoSignedInUserException;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Controller for CreateTeamFrame instances.
 *
 * @author Bori Fazakas
 */
public class JoinTeamController extends FrameController {
  TeamManager teamManager;

  private static final String SUCCESFUL_TEAM_JOINING_TITLE = "Succesful operation";
  private static final String SUCCESSFUL_TEAM_JOINING_MESSAGE = "You succesfully joined the team";

  public JoinTeamController(JFrame frame) {
    super(frame);
    teamManager = TeamManager.getInstance();
  }

  /**
   * When the user wants to join team, a request to the model is sent.
   * The user is notified about the outcome of the operation.
   * If successful, a new team with the given name is created.
   *
   * @param teamCode is the code of the team to join.
   */
  public void joinTeam(String teamCode) {
    try {
      teamManager.joinTeam(teamCode);
      JOptionPane.showMessageDialog(
              frame, SUCCESSFUL_TEAM_JOINING_MESSAGE, SUCCESFUL_TEAM_JOINING_TITLE,
              JOptionPane.PLAIN_MESSAGE);
    } catch (InexistentTeamException | NoSignedInUserException | SQLException | InexistentDatabaseEntityException e) {
      e.printStackTrace();
      ErrorDialogFactory.createErrorDialog(e, frame, "You can't join team with code " + teamCode);
    }
    closeFrame();
  }

  /**
   * When the JoinTeamFrame was created, its parent frame was disabled so the user cannot perform
   * other operations in the main frame while this one is open. Thus, when the JoinTeamFrame is
   * closed, the parentFrame must be re-enabled.
   *
   * @param parentFrame is the frame to be re-enabled.
   */
  public void onClose(JFrame parentFrame) {
    parentFrame.setEnabled(true);
  }
}
