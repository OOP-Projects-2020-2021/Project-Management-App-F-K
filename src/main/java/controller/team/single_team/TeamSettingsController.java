package controller.team.single_team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.team.Team;
import model.team.TeamManager;
import model.team.exceptions.InexistentTeamException;
import model.team.exceptions.ManagerRemovalException;
import model.team.exceptions.UnregisteredMemberRemovalException;
import model.user.UserManager;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;
import java.util.Objects;

public class TeamSettingsController extends FrameController{

  private TeamManager teamManager;
  private UserManager userManager;
  private boolean managerAccessGranted;
  private Team currentTeam;

  public TeamSettingsController(JFrame frame,int currentTeamId) {
    super(frame);
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    managerAccessGranted = grantManagerAccess();
    try {
      currentTeam = teamManager.getCurrentTeam(currentTeamId);
    } catch (SQLException | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(e,frame,"This team cannot be viewed.");
    }
  }

  public boolean grantManagerAccess() {
    try {
      return UserManager.getInstance().getCurrentUser().get().getId() == currentTeam.getManagerId();
    } catch (InexistentDatabaseEntityException e) {
      ErrorDialogFactory.createErrorDialog(e,frame,null);
    }
    return false;
  }
  public boolean getManagerAccessGranted() {
    return managerAccessGranted;
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
