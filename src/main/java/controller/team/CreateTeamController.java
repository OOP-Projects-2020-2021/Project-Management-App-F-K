package controller.team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.team.TeamManager;
import model.user.exceptions.*;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;

/**
 * Controller for CreateTeamFrame instances.
 *
 * @author Bori Fazakas
 */
public class CreateTeamController extends FrameController {
  TeamManager teamManager;

  private static final String SUCCESSFUL_TEAM_CREATION_TITLE = "Successful operation";
  private static final String SUCCESSFUL_TEAM_CREATION_MESSAGE = "The team was successfully created";

  public CreateTeamController(JFrame frame) {
    super(frame);
    teamManager = TeamManager.getInstance();
  }

  /**
   * When the user wants to create a new team, a request to the model is sent. The user is notified
   * about the outcome of the operation. If successful, a new team with the given name is created.
   *
   * @param teamName is the name of the new team.
   */
  public void createTeam(String teamName) {
    try {
      teamManager.createNewTeam(teamName);
      JOptionPane.showMessageDialog(
          frame,
          SUCCESSFUL_TEAM_CREATION_MESSAGE,
          SUCCESSFUL_TEAM_CREATION_TITLE,
          JOptionPane.PLAIN_MESSAGE);
    } catch (SQLException | InexistentDatabaseEntityException | NoSignedInUserException e) {
      e.printStackTrace();
      ErrorDialogFactory.createErrorDialog(e, frame, null);
    }
    closeFrame();
  }

  /**
   * When the CreateTeamFrame was created, its parent frame was disabled so the user cannot perform
   * other operations in the main frame while this one is open. Thus, when the CreateTeamFrame is
   * closed, the parentFrame must be re-enabled.
   *
   * @param parentFrame is the frame to be re-enabled.
   */
  public void onClose(JFrame parentFrame) {
    parentFrame.setEnabled(true);
  }
}
