package controller.project.single_project;

import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.exceptions.DuplicateProjectNameException;
import model.project.exceptions.InexistentProjectException;
import model.team.TeamManager;
import model.team.exceptions.UnregisteredMemberRoleException;
import model.user.User;
import model.user.UserManager;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;
import view.project.single_project.ProjectDetailsPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * ProjectDetailsController manages the ProjectDetails panel, being responsible for listing and
 * updating the currently viewed project's details.
 *
 * @author Beata Keresztes
 */
public class ProjectDetailsController extends ProjectController implements PropertyChangeListener {

  private TeamManager teamManager;
  private UserManager userManager;
  private ProjectManager projectManager;
  private ProjectDetailsPanel panel;

  /** Messages to confirm with the user the deletion of the project. */
  private static final String CONFIRM_DELETION_MESSAGE =
      "Are you sure you want to delete this project?\n"
          + "All data related to this project will be lost.";

  private static final String CONFIRM_DELETION_TITLE = "Deleting project";
  /** Messages to inform the user that the project was updated successfully. */
  private static final String SUCCESSFUL_UPDATE_TITLE = "Project saved";

  private static final String SUCCESSFUL_UPDATE_MESSAGE = "The project was updated successfully";

  public ProjectDetailsController(JFrame frame, Project project, ProjectDetailsPanel panel) {
    super(frame, project);
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    projectManager = ProjectManager.getInstance();
    projectManager.addPropertyChangeListener(this);
    this.panel = panel;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName()
        .equals(ProjectManager.ProjectChangeablePropertyName.UPDATE_PROJECT.toString())) {
      setProject();
      panel.updatePanel();
    } else if (evt.getPropertyName()
        .equals(ProjectManager.ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString())) {
      setProject();
      panel.updateStatusLabel();
    } else if (evt.getPropertyName()
        .equals(ProjectManager.ProjectChangeablePropertyName.DELETE_PROJECT.toString())) {
      closeFrame();
    }
  }

  public boolean isSupervisor() {
    try {
      return userManager.getCurrentUser().get().getId() == project.getSupervisorId();
    } catch (InexistentDatabaseEntityException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
    return false;
  }

  public boolean enableEditing() {
    return (isSupervisor() && (project.getStatus() != Project.Status.FINISHED));
  }

  public User getProjectAssignee() {
    try {
      return userManager.getUserById(project.getAssigneeId());
    } catch (SQLException sqlException) {
      ErrorDialogFactory.createErrorDialog(sqlException, null, null);
    }
    return null;
  }

  public User getProjectSupervisor() {
    try {
      return userManager.getUserById(project.getSupervisorId());
    } catch (SQLException sqlException) {
      ErrorDialogFactory.createErrorDialog(sqlException, null, null);
    }
    return null;
  }

  public String getProjectTitle() {
    return project.getTitle();
  }

  public String getProjectDescription() {
    if (project.getDescription().isPresent()) {
      return project.getDescription().get();
    }
    return null;
  }

  public LocalDate getProjectDeadline() {
    return project.getDeadline();
  }

  public String getStatus() {
    return project.getStatus().toString();
  }

  public List<User> getTeamMembers() {
    try {
      return teamManager.getMembersOfTeam(project.getTeamId());
    } catch (SQLException sqlException) {
      ErrorDialogFactory.createErrorDialog(sqlException, null, null);
      return Collections.emptyList();
    }
  }

  public Project.Importance getProjectImportance() {
    return project.getImportance();
  }

  public void saveProject(
          String title, String assignee, String supervisor, LocalDate deadline,
          String description, Project.Importance importance) {
    try {
      projectManager.updateProject(
          project.getId(), title, assignee, supervisor, deadline, description, importance);
      displaySuccessfulSaveMessage();
    } catch (InexistentDatabaseEntityException | SQLException | InexistentProjectException e) {
      ErrorDialogFactory.createErrorDialog(
          e,
          null,
          "The project \"" + project.getTitle() + "\" could not be found in the database.");
    } catch (NoSignedInUserException
        | UnauthorisedOperationException
        | InexistentUserException
        | UnregisteredMemberRoleException e) {
      ErrorDialogFactory.createErrorDialog(
          e, null, "You don't have access to edit the project \"" + project.getTitle() + "\"");
    } catch (DuplicateProjectNameException e) {
      ErrorDialogFactory.createErrorDialog(
          e, null, "The project with title\"" + project.getTitle() + "\" already exists");
    }
  }

  /** Displays a message to inform the user that the project was updated successfully. */
  private void displaySuccessfulSaveMessage() {
    JOptionPane.showMessageDialog(
        null, SUCCESSFUL_UPDATE_MESSAGE, SUCCESSFUL_UPDATE_TITLE, JOptionPane.INFORMATION_MESSAGE);
  }

  public void deleteProject() {
    try {
      int option =
          JOptionPane.showConfirmDialog(
              null,
              CONFIRM_DELETION_MESSAGE,
              CONFIRM_DELETION_TITLE,
              JOptionPane.YES_NO_OPTION,
              JOptionPane.WARNING_MESSAGE);
      if (option == JOptionPane.YES_OPTION) {
        projectManager.deleteProject(project.getId());
      }
    } catch (InexistentProjectException
        | SQLException
        | NoSignedInUserException
        | InexistentDatabaseEntityException
        | UnauthorisedOperationException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
  }
}
