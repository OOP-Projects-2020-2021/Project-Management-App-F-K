package controller.team.single_team;

import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.team.Team;
import model.team.TeamManager;
import model.team.exceptions.*;
import model.user.User;
import model.user.exceptions.*;
import view.ErrorDialogFactory;
import view.team.single_team.TeamHomePanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * This controller manages the TeamHomePanel, and it is responsible for displaying and updating the
 * currently viewed team's data.
 *
 * @author Beata Keresztes
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
      "Now you are not a member of this team anymore.";

  private static final String AFFIRM_LEAVING_TEAM_TITLE = "Left the team ";

  public TeamSettingsController(TeamHomePanel homePanel, JFrame frame, int teamId) {
    super(frame, teamId);
    this.homePanel = homePanel;
    teamManager.addPropertyChangeListener(this);
    try {
      currentTeam = teamManager.getTeam(teamId);
    } catch (SQLException | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, "This team cannot be viewed.");
    }
  }

  private void updateCurrentTeam() {
    try {
      currentTeam = teamManager.getTeam(currentTeam.getId());
    } catch (SQLException | InexistentTeamException | InexistentDatabaseEntityException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, "The data of this team was not updated.");
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(TeamManager.ChangablePropertyName.CHANGED_TEAM_NAME.toString())
        || evt.getPropertyName()
            .equals(TeamManager.ChangablePropertyName.CHANGED_TEAM_CODE.toString())) {
      updateCurrentTeam();
      updateHomePanel();
    } else if (evt.getPropertyName()
        .equals(TeamManager.ChangablePropertyName.CHANGED_TEAM_MANAGER.toString())) {
      updateCurrentTeam();
      setManagerAccess();
      updateHomePanel();
    } else if (evt.getPropertyName()
            .equals(TeamManager.ChangablePropertyName.ADDED_TEAM_MEMBER.toString())
        || evt.getPropertyName()
            .equals(TeamManager.ChangablePropertyName.REMOVED_TEAM_MEMBER.toString())) {
      updateCurrentTeam();
      updateHomePanel();
    } else if (evt.getPropertyName()
        .equals(TeamManager.ChangablePropertyName.CURRENT_USER_TEAM_MEMBERSHIPS.toString())) {
      updateCurrentTeam();
      setManagerAccess();
      updateHomePanel();
    }
  }

  private void updateHomePanel() {
    homePanel.enableComponents(managerAccess);
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
          sqlException, frame, "The new manager could not be fetched.");
    }
    return null;
  }

  public List<User> getMembersOfTeam() {
    try {
      return teamManager.getMembersOfTeam(currentTeam.getId());
    } catch (InexistentDatabaseEntityException | SQLException e) {
      JOptionPane.showMessageDialog(frame, "Members could not be listed");
      return null;
    }
  }

  /** Displays a message dialog to ask the user to confirm that he/she wants to leave the team. */
  private int confirmLeavingTeam() {
    return JOptionPane.showConfirmDialog(
        frame, CONFIRM_LEAVING_TEAM_MESSAGE, CONFIRM_LEAVING_TEAM_TITLE, JOptionPane.YES_NO_OPTION);
  }
  /** Displays a message dialog to inform the user that he/she has left the team. */
  private void affirmLeavingTeam() {
    JOptionPane.showConfirmDialog(
        frame, AFFIRM_LEAVING_TEAM_MESSAGE, AFFIRM_LEAVING_TEAM_TITLE, JOptionPane.YES_NO_OPTION);
  }

  public void leaveTeam() {
    try {
      if (confirmLeavingTeam() == JOptionPane.YES_OPTION) {
        teamManager.leaveTeam(teamId);
        affirmLeavingTeam();
        closeFrame();
      }
    } catch (SQLException
        | InexistentDatabaseEntityException
        | InexistentTeamException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException, frame, "You could not be removed from the team.");
    } catch (NoSignedInUserException noSignedInUserException) {
      ErrorDialogFactory.createErrorDialog(noSignedInUserException, frame, null);
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
      teamManager.setNewName(teamId, name);
      homePanel.enableNameTextField(false);
    } catch (SQLException | InexistentTeamException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException, frame, "The new name could not be saved.");
    } catch (UnauthorisedOperationException
        | NoSignedInUserException
        | InexistentDatabaseEntityException unauthorizedAccessException) {
      ErrorDialogFactory.createErrorDialog(
          unauthorizedAccessException,
          frame,
          "You are not allowed to change the name of the team.");
    }
  }

  public void regenerateTeamCode() {
    try {
      teamManager.regenerateTeamCode(teamId);
    } catch (SQLException | InexistentTeamException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException, frame, "The new code could not be generated.");
    } catch (UnauthorisedOperationException
        | NoSignedInUserException
        | InexistentDatabaseEntityException unauthorisedAccessException) {
      ErrorDialogFactory.createErrorDialog(
          unauthorisedAccessException,
          frame,
          "You are not allowed to change the code of the team.");
    }
  }

  public void saveTeamManager(String newManagerName) {
    try {
      teamManager.passManagerPosition(teamId, newManagerName);
    } catch (InexistentTeamException | SQLException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException, frame, "The new manager could not be saved.");
    } catch (InexistentUserException inexistentUserException) {
      ErrorDialogFactory.createErrorDialog(
          inexistentUserException, frame, "The user \"" + newManagerName + "\" doesn't exist.");
    } catch (UnregisteredMemberRoleException e) {
      ErrorDialogFactory.createErrorDialog(
          e,
          frame,
          "The user with username \"" + newManagerName + "\" is not a member of the team.");
    } catch (NoSignedInUserException
        | UnauthorisedOperationException
        | InexistentDatabaseEntityException unauthorisedAccessException) {
      ErrorDialogFactory.createErrorDialog(
          unauthorisedAccessException,
          frame,
          "You are not allowed to change the manager of this team.");
    }
  }
}
