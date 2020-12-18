package controller.team.single_team;

import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.team.Team;
import model.team.TeamManager;
import model.team.exceptions.*;
import model.user.exceptions.*;
import view.ErrorDialogFactory;
import view.team.single_team.TeamHomePanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.Objects;

/**
 * This controller manages the TeamHomePanel, and it is responsible for displaying and updating the
 * currently viewed team's data.
 */
public class TeamSettingsController extends TeamController implements PropertyChangeListener {

  private Team currentTeam;
  private TeamHomePanel homePanel;

  /** Messages to confirm leaving the team. */
  private static final String CONFIRM_LEAVING_TEAM_MESSAGE =
      "Are you sure that you want to leave this team?";

  private static final String CONFIRM_LEAVING_TEAM_TITLE = "Leaving team";

  /** Messages to inform the user that they left the team. */
  private static final String AFFIRM_LEAVING_TEAM_MESSAGE =
      "Now you are not a member of this team.";

  private static final String AFFIRM_LEAVING_TEAM_TITLE = "Left the team ";

  public TeamSettingsController(TeamHomePanel homePanel, JFrame frame, int currentTeamId) {
    super(frame, currentTeamId);
    this.homePanel = homePanel;
    teamManager.addPropertyChangeListener(this);
    try {
      currentTeam = teamManager.getCurrentTeam(currentTeamId);
    } catch (SQLException | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, "This team cannot be viewed.");
    }
  }

  public void setCurrentTeam(Team team) {
    currentTeam = team;
  }

  private void updateCurrentTeam() {
    try {
      setCurrentTeam(teamManager.getCurrentTeam(currentTeam.getId()));
    } catch (SQLException | InexistentTeamException | InexistentDatabaseEntityException e) {
      ErrorDialogFactory.createErrorDialog(
          e, frame, "An internal error occurred and the data of this team was not updated.");
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName()
        .equals(TeamManager.ChangablePropertyName.CHANGED_TEAM_DATA.toString())) {
      updateCurrentTeam();
      setManagerAccess();
      updateHomePanel();
    }
  }

  private void updateHomePanel() {
    homePanel.enableButtons(managerAccess);
    homePanel.updateHomePaneComponents();
  }

  public String getTeamName() {
    return currentTeam.getName();
  }

  public String getTeamCode() {
    return currentTeam.getCode();
  }

  public String getTeamManagerName() {
    try {
      return Objects.requireNonNull(userManager.getUserById(currentTeam.getManagerId()))
          .getUsername();
    } catch (SQLException sqlException) {
      ErrorDialogFactory.createErrorDialog(
          sqlException, frame, "An internal error occurred, the new manager could not be fetched.");
    }
    return null;
  }

  public void confirmLeavingTeam() {
    int answer =
        JOptionPane.showConfirmDialog(
            frame,
            CONFIRM_LEAVING_TEAM_MESSAGE,
            CONFIRM_LEAVING_TEAM_TITLE,
            JOptionPane.YES_NO_OPTION);
    if (answer == JOptionPane.YES_OPTION) {
      leaveTeam();
      JOptionPane.showMessageDialog(
          frame, AFFIRM_LEAVING_TEAM_MESSAGE, AFFIRM_LEAVING_TEAM_TITLE, JOptionPane.PLAIN_MESSAGE);
      closeFrame();
    }
  }

  public void leaveTeam() {
    try {
      teamManager.leaveTeam(currentTeamId);
    } catch (SQLException
        | InexistentDatabaseEntityException
        | InexistentTeamException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException,
          frame,
          "An internal error occurred, your could not be removed from the team.");
    } catch (NoSignedInUserException noSignedInUserException) {
      ErrorDialogFactory.createErrorDialog(
          noSignedInUserException,
          frame,
          "You are not allowed to this do operation, because you have not signed in.");
    } catch (UnregisteredMemberRemovalException unregisteredMemberRemovalException) {
      ErrorDialogFactory.createErrorDialog(
          unregisteredMemberRemovalException,
          frame,
          "You cannot leave the team, because you were not a member of this team.");
    } catch (ManagerRemovalException managerRemovalException) {
      ErrorDialogFactory.createErrorDialog(
          managerRemovalException,
          frame,
          "You cannot leave the team, because you are the manager.");
    }
  }

  public void saveTeamName(String name) {
    try {
      teamManager.setNewName(currentTeamId, name);
    } catch (SQLException
        | InexistentTeamException
        | InexistentDatabaseEntityException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException, frame, "An internal error occurred, the new name could not be saved.");
    } catch (UnauthorisedOperationException | NoSignedInUserException unauthorizedAccessException) {
      ErrorDialogFactory.createErrorDialog(
          unauthorizedAccessException,
          frame,
          "You are not allowed to change the name of the team.");
    }
  }

  public void regenerateTeamCode() {
    try {
      teamManager.regenerateTeamCode(currentTeamId);
    } catch (SQLException
        | InexistentDatabaseEntityException
        | InexistentTeamException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException,
          frame,
          "An internal error occurred, the new code could not be generated.");
    } catch (UnauthorisedOperationException | NoSignedInUserException unauthorisedAccessException) {
      ErrorDialogFactory.createErrorDialog(
          unauthorisedAccessException,
          frame,
          "You are not allowed to change the code of the team.");
    }
  }

  public void saveTeamManager(String newManagerName) {
    try {
      teamManager.passManagerPosition(currentTeamId, newManagerName);
    } catch (InexistentTeamException
        | InexistentDatabaseEntityException
        | SQLException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException,
          frame,
          "An internal error occurred, the new manager could not be saved.");
    } catch (InexistentUserException inexistentUserException) {
      ErrorDialogFactory.createErrorDialog(
          inexistentUserException,
          frame,
          "The user "
              + newManagerName
              + " cannot be set as the new manager,"
              + "because he/she is not a member.");
    } catch (IllegalArgumentException illegalArgumentException) {
      ErrorDialogFactory.createErrorDialog(
          illegalArgumentException,
          frame,
          "The user with username " + newManagerName + "doesn't exist.");
    } catch (NoSignedInUserException | UnauthorisedOperationException unauthorisedAccessException) {
      ErrorDialogFactory.createErrorDialog(
          unauthorisedAccessException,
          frame,
          "You are not allowed to change the manager of this team.");
    }
  }
}
