package controller.team.single_team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.team.Team;
import model.team.TeamManager;
import model.team.exceptions.*;
import model.user.exceptions.*;
import model.user.UserManager;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Objects;

/**
 * This controller manages the TeamHomePanel, and it is responsible for displaying and updating
 * the currently viewed team's data.
 */
public class TeamSettingsController extends FrameController{

  private TeamManager teamManager;
  private UserManager userManager;
  private Team currentTeam;
  private TeamController teamController;

  public TeamSettingsController(JFrame frame,TeamController teamController) {
    super(frame);
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    this.teamController = teamController;
    try {
      currentTeam = teamManager.getCurrentTeam(teamController.getCurrentTeamId());
    } catch (SQLException | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(e,frame,"This team cannot be viewed.");
    }
  }

  public boolean getManagerAccessGranted() {
    return teamController.getManagerAccessGranted();
  }
  public void updateManagerAccess() {
    teamController.updateManagerAccess();
  }

  public String getTeamName() {
    return currentTeam.getName();
  }
  public String getTeamCode() {
    return currentTeam.getCode();
  }
  public String getTeamManagerName() {
    try {
      return Objects.requireNonNull(userManager.getUserById(currentTeam.getManagerId())).getUsername();
    } catch (SQLException sqlException) {
      sqlException.printStackTrace();
    }
    return null;
  }

  public void leaveTeam() {
    try {
      teamManager.leaveTeam(currentTeam.getId());
      updateCurrentTeam();
      updateManagerAccess();
    } catch (SQLException | InexistentDatabaseEntityException | InexistentTeamException | NoSignedInUserException | UnregisteredMemberRemovalException | ManagerRemovalException e) {
      ErrorDialogFactory.createErrorDialog(e,frame,null);
    }
  }
  public void saveTeamName(String name) {
    try {
      teamManager.setNewName(currentTeam.getId(),name);
      updateCurrentTeam();
    }catch (SQLException | InexistentTeamException | UnauthorisedOperationException | NoSignedInUserException | InexistentDatabaseEntityException e) {
      ErrorDialogFactory.createErrorDialog(e,frame,null);
    }
  }
  public void regenerateTeamCode() {
    try {
      teamManager.regenerateTeamCode(currentTeam.getId());
      updateCurrentTeam();
    }catch(SQLException | UnauthorisedOperationException | InexistentDatabaseEntityException | NoSignedInUserException | InexistentTeamException  e) {
      ErrorDialogFactory.createErrorDialog(e,frame,null);
    }
  }
  public void saveTeamManager(String managerName) {
    try {
      teamManager.passManagerPosition(currentTeam.getId(),managerName);
      updateCurrentTeam();
      updateManagerAccess();
    } catch (InexistentUserException | InexistentTeamException | NoSignedInUserException | InexistentDatabaseEntityException | SQLException | UnauthorisedOperationException e) {
      ErrorDialogFactory.createErrorDialog(e,frame,null);
    }
  }
  private void updateCurrentTeam() {
    try {
      currentTeam = teamManager.getCurrentTeam(currentTeam.getId());
    } catch (SQLException | InexistentTeamException | InexistentDatabaseEntityException e) {
      e.printStackTrace();
      ErrorDialogFactory.createErrorDialog(e,frame,"The data of this team was not updated.");
    }
  }

}
