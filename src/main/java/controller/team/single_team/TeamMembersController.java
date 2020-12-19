package controller.team.single_team;

import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.team.TeamManager;
import model.team.exceptions.*;
import model.user.UserManager;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;
import view.team.single_team.TeamMembersPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

/**
 * This controller manages the TeamMembersPanel tab, displaying and updating the list of members of
 * a team.
 */
public class TeamMembersController implements PropertyChangeListener {

  TeamMembersPanel membersPanel;
  private boolean managerAccess;
  private JFrame frame;
  private int currentTeamId;

  protected TeamManager teamManager;
  protected UserManager userManager;

  /** Messages to confirm the removal of a member from the team. */
  private static final String CONFIRM_REMOVING_MEMBER_MESSAGE =
      "Are you sure you want to remove this member?";

  private static final String CONFIRM_REMOVING_MEMBER_TITLE = "Removing member";

  public TeamMembersController(TeamMembersPanel membersPanel, JFrame frame, int currentTeamId) {
    this.membersPanel = membersPanel;
    this.frame = frame;
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    this.currentTeamId = currentTeamId;
    teamManager.addPropertyChangeListener(this);
    setManagerAccess();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName()
        .equals(TeamManager.ChangablePropertyName.ADDED_TEAM_MEMBER.toString()) || evt.getPropertyName()
            .equals(TeamManager.ChangablePropertyName.REMOVED_TEAM_MEMBER.toString())) {
      membersPanel.updateMembersList();
    } else if (evt.getPropertyName().equals(TeamManager.ChangablePropertyName.CHANGED_TEAM_MANAGER.toString())) {
      setManagerAccess();
      membersPanel.enableComponents(managerAccess);
    }
  }

  /**
   * Checks if the current user is the manager of the team and grants access to them to modify data
   * about the team.
   */
  private void setManagerAccess() {
    try {
      int currentUserId = UserManager.getInstance().getCurrentUser().get().getId();
      int currentManagerId = teamManager.getCurrentTeam(currentTeamId).getManagerId();
      managerAccess = currentUserId == currentManagerId;
    } catch (SQLException | InexistentDatabaseEntityException | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(
              e,
              frame,
              "An internal error occurred, the access to edit this team could not be granted.");
    }
  }
  public boolean getManagerAccess() {
    return managerAccess;
  }

  /**
   * Get the members of the team. The string array contains at least 1 element, which is the manager
   * of the team.
   *
   * @return the members of the current team
   */
  public String[] getTeamMembers() {
    try {
      return teamManager.getMembersOfTeam(currentTeamId);
    } catch (SQLException sqlException) {
      ErrorDialogFactory.createErrorDialog(
          sqlException, frame, "An internal error occurred, the members could not be displayed.");
    }
    return null;
  }

  public void addMember(String name) {
    try {
      teamManager.addMemberToTeam(currentTeamId, name);
    } catch (SQLException | InexistentTeamException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException,
          frame,
          "An internal error occurred, the member " + name + " could not be added to the team.");
    } catch (UnauthorisedOperationException | InexistentDatabaseEntityException
        | NoSignedInUserException operationNotAllowedException) {
      ErrorDialogFactory.createErrorDialog(
          operationNotAllowedException,
          frame,
          "You are not allowed to add a new member to the team.");
    } catch (InexistentUserException inexistentUserException) {
      ErrorDialogFactory.createErrorDialog(
          inexistentUserException,
          frame,
          "The user "
              + name
              + " doesn't exist.");
    } catch (AlreadyMemberException alreadyMemberException) {
      ErrorDialogFactory.createErrorDialog(
          alreadyMemberException, frame, "The user " + name + " is already a member of this team.");
    }
  }

  public void removeMember(String name) {
    int choice =
        JOptionPane.showConfirmDialog(
            frame,
            CONFIRM_REMOVING_MEMBER_MESSAGE,
            CONFIRM_REMOVING_MEMBER_TITLE,
            JOptionPane.YES_NO_OPTION);
    if (choice == JOptionPane.YES_OPTION) {
      try {
        teamManager.removeTeamMember(currentTeamId, name);
      } catch (InexistentTeamException
          | InexistentDatabaseEntityException
          | SQLException databaseException) {
        ErrorDialogFactory.createErrorDialog(
            databaseException,
            frame,
            "An internal error occurred, the member " + name + " could not be removed.");
      } catch (UnauthorisedOperationException | NoSignedInUserException accessDeniedException) {
        ErrorDialogFactory.createErrorDialog(
            accessDeniedException, frame, "You are not allowed to remove a member from this team.");
      } catch (UnregisteredMemberRemovalException unregisteredMemberRemovalException) {
        ErrorDialogFactory.createErrorDialog(
            unregisteredMemberRemovalException,
            frame,
            "The user " + name + " is not a member of this team.");
      } catch (InexistentUserException inexistentUserException) {
        ErrorDialogFactory.createErrorDialog(
            inexistentUserException,
            frame,
            "The user \" + name + \" could not be removed, because it doesn't exist.");
      } catch (ManagerRemovalException managerRemovalException) {
        ErrorDialogFactory.createErrorDialog(
            managerRemovalException, frame, "The user " + name + " is the current manager of the team.");
      }
    }
  }
}
