package controller.team.single_team;

import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.queryconstants.QueryProjectDeadlineStatus;
import model.project.queryconstants.QueryProjectStatus;
import model.team.TeamManager;
import model.team.exceptions.*;
import model.user.User;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;
import view.team.single_team.TeamMembersPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.List;

/**
 * The TeamMembersController manages the TeamMembersPanel tab, displaying and updating the list of
 * members of a team.
 *
 * @author Beata Keresztes
 */
public class TeamMembersController extends TeamController implements PropertyChangeListener {

  private TeamMembersPanel membersPanel;

  /** Messages to confirm the removal of a member from the team. */
  private static final String CONFIRM_REMOVING_MEMBER_MESSAGE =
      "Are you sure you want to remove this member?";

  private static final String CONFIRM_REMOVING_MEMBER_TITLE = "Removing member";

  public TeamMembersController(TeamMembersPanel membersPanel, JFrame frame, int teamId) {
    super(frame, teamId);
    this.membersPanel = membersPanel;
    teamManager.addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(TeamManager.ChangablePropertyName.ADDED_TEAM_MEMBER.toString())
        || evt.getPropertyName()
            .equals(TeamManager.ChangablePropertyName.REMOVED_TEAM_MEMBER.toString())) {
      membersPanel.updateMembersList();
    } else if (evt.getPropertyName()
        .equals(TeamManager.ChangablePropertyName.CHANGED_TEAM_MANAGER.toString())) {
      setManagerAccess();
      membersPanel.enableComponents(managerAccess);
    }
  }

  /**
   * Get the members of the team. The string array contains at least one element, which is the
   * manager of the team.
   *
   * @return the members of the current team
   */
  public List<User> getTeamMembers() {
    try {
      return teamManager.getMembersOfTeam(teamId);
    } catch (SQLException sqlException) {
      ErrorDialogFactory.createErrorDialog(
          sqlException, frame, "The members could not be displayed.");
    }
    return null;
  }

  public void addMember(String name) {
    try {
      teamManager.addMemberToTeam(teamId, name);
    } catch (SQLException | InexistentTeamException databaseException) {
      ErrorDialogFactory.createErrorDialog(
          databaseException, frame, "The member \"" + name + "\" could not be added to the team.");
    } catch (UnauthorisedOperationException
        | InexistentDatabaseEntityException
        | NoSignedInUserException operationNotAllowedException) {
      ErrorDialogFactory.createErrorDialog(
          operationNotAllowedException,
          frame,
          "You are not allowed to add a new member to the team.");
    } catch (InexistentUserException inexistentUserException) {
      ErrorDialogFactory.createErrorDialog(
          inexistentUserException, frame, "The user \"" + name + "\" doesn't exist.");
    } catch (AlreadyMemberException alreadyMemberException) {
      ErrorDialogFactory.createErrorDialog(
          alreadyMemberException,
          frame,
          "The user \"" + name + "\" is already a member of this team.");
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
        if (hasUnfinishedProjects(name)) {
          throw new IllegalMemberRemovalException(name);
        }
        teamManager.removeTeamMember(teamId, name);
      } catch (InexistentTeamException
          | InexistentDatabaseEntityException
          | SQLException databaseException) {
        ErrorDialogFactory.createErrorDialog(
            databaseException, frame, "The member \"" + name + "\" could not be removed.");
      } catch (IllegalMemberRemovalException illegalMemberRemovalException) {
        ErrorDialogFactory.createErrorDialog(
            illegalMemberRemovalException,
            frame,
            "The user \"" + name + "\" cannot be removed from this team.");
      } catch (UnauthorisedOperationException | NoSignedInUserException accessDeniedException) {
        ErrorDialogFactory.createErrorDialog(
            accessDeniedException, frame, "You are not allowed to remove a member from this team.");
      } catch (UnregisteredMemberRemovalException unregisteredMemberRemovalException) {
        ErrorDialogFactory.createErrorDialog(
            unregisteredMemberRemovalException,
            frame,
            "The user \" " + name + "\" is not a member of this team.");
      } catch (InexistentUserException inexistentUserException) {
        ErrorDialogFactory.createErrorDialog(
            inexistentUserException,
            frame,
            "The user \" + name + \" could not be removed, because it doesn't exist.");
      } catch (ManagerRemovalException managerRemovalException) {
        ErrorDialogFactory.createErrorDialog(
            managerRemovalException,
            frame,
            "The user \"" + name + "\" is the current manager of the team.");
      }
    }
  }

  /**
   * Check whether the member of the team has any unfinished projects.
   *
   * @param member = name of the member after which we inquire
   * @return true if the member doesn't have any unfinished projects, otherwise false
   * @throws InexistentUserException if there is no such member
   * @throws SQLException if an error occurred when reading the projects from the database
   * @throws InexistentDatabaseEntityException if the team or project with given id doesn't exist in
   *     the database
   */
  private boolean hasUnfinishedProjects(String member)
      throws SQLException, InexistentDatabaseEntityException, InexistentUserException {
    ProjectManager projectManager = ProjectManager.getInstance();
    List<Project> projects =
        projectManager.getProjectsOfTeam(
            teamId, member, member, QueryProjectStatus.ALL, QueryProjectDeadlineStatus.ALL);
    for (Project project : projects) {
      if (project.getStatus() != Project.ProjectStatus.FINISHED) {
        return false;
      }
    }
    return true;
  }
}
