package controller.project.single_project;

import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.exceptions.DuplicateProjectNameException;
import model.project.exceptions.IllegalProjectStatusChangeException;
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
public class ProjectDetailsController implements PropertyChangeListener {

  private TeamManager teamManager;
  private UserManager userManager;
  private ProjectManager projectManager;
  private Project project;
  private ProjectDetailsPanel panel;

  public ProjectDetailsController(Project project, ProjectDetailsPanel panel) {
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    projectManager = ProjectManager.getInstance();
    projectManager.addPropertyChangeListener(this);
    this.project = project;
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
    }
  }

  private void setProject() {
    try {
      project = projectManager.getProjectById(project.getId());
    } catch (InexistentProjectException | InexistentDatabaseEntityException | SQLException e) {
      ErrorDialogFactory.createErrorDialog(e, null, "The project could not be updated.");
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

  private boolean isAssignee() {
    try {
      return userManager.getCurrentUser().get().getId() == project.getAssigneeId();
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

  public void saveProject(
      String title, String assignee, String supervisor, LocalDate deadline, String description) {
    try {
      projectManager.updateProject(
          project.getId(), title, assignee, supervisor, deadline, description);
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
        null,
        "The project was updated successfully!",
        "Changes saved",
        JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Displays a message to inform the user that the attempt to change the project's state was
   * rejected and had caused an exception.
   */
  private void displayIllegalStateErrorDialog(
      IllegalProjectStatusChangeException e, Project.Status newState) {
    ErrorDialogFactory.createErrorDialog(
        e,
        null,
        "You cannot set the project from status " + project.getStatus() + " to " + newState);
  }

  public void deleteProject() {
    // todo
  }
}
