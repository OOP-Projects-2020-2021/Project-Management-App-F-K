package model.project;

import model.InexistentDatabaseEntityException;
import model.Manager;
import model.UnauthorisedOperationException;
import model.project.exceptions.*;
import model.team.Team;
import model.team.exceptions.IllegalMemberRemovalException;
import model.team.exceptions.InexistentTeamException;
import model.team.exceptions.UnregisteredMemberRoleException;
import model.user.User;
import model.user.exceptions.EmptyFieldsException;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;

/**
 * ProjectManager is responsible for executing all the commands needed for the application that are
 * related to projects.
 *
 * <p>Remark that it is implemented with the singleton pattern, so only one instance of it exists.
 *
 * @author Bori Fazakas
 */
public class ProjectManager extends Manager {
  private static ProjectManager instance = new ProjectManager();

  private ProjectManager() {}

  public static ProjectManager getInstance() {
    return instance;
  }

  public enum ProjectChangeablePropertyName {
    UPDATE_PROJECT, // event fires when project is updated
    CREATE_PROJECT, // event fires when project is created
    SET_PROJECT_STATUS, // event fires when state of the project is changed
    DELETE_PROJECT // event is fired when a project is deleted
  }

  private boolean isEmptyText(String text) {
    return text == null || text.isEmpty();
  }

  /**
   * Checks if all the required (compulsory) data was introduced to create a new project.
   *
   * @param title of the project
   * @param assignee to whom the project is assigned
   * @param deadline until which the project can be turned in
   * @return true if all some fields are left uncompleted, if all the necessary data has been
   *     introduced it returns false
   */
  private boolean isMissingProjectData(String title, String assignee, LocalDate deadline) {
    return isEmptyText(title) || isEmptyText(assignee) || isEmptyText(deadline.toString());
  }

  /**
   * Checks if the given date is outdated
   *
   * @param date the selected date
   * @return true if the date is before the current date
   */
  private boolean isOutdatedDate(LocalDate date) {
    return date.isBefore(LocalDate.now());
  }
  /**
   * Creates a new project with the specified data and saves it in the database. The supervisor of
   * the project will be automatically the current user. There should not be another project with
   * the same title/name in the team.
   *
   * @param projectName is the name of the project to be saved.
   * @param teamId is the id of the team to which the new project belongs.
   * @param assigneeName is the name of the user to whom this project is assigned.
   * @param deadline is the deadline of the project to be saved.
   * @param description is the description of the project to be saved.
   * @throws NoSignedInUserException if there is no one signed in.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentUserException if the user with assigneeName does not exist.
   * @throws InexistentTeamException if the team with teamId does not exist.
   * @throws DuplicateProjectNameException if there is already a project with the same name in the
   *     same team. This is not allowed.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws InvalidDeadlineException if the selected deadline is outdated.
   */
  public void createProject(
      String projectName, int teamId, String assigneeName, LocalDate deadline, String description)
      throws NoSignedInUserException, SQLException, InexistentUserException,
          InexistentTeamException, DuplicateProjectNameException, InexistentDatabaseEntityException,
          EmptyFieldsException, InvalidDeadlineException {
    if (isMissingProjectData(projectName, assigneeName, deadline)) {
      throw new EmptyFieldsException();
    }
    User currentUser = getMandatoryCurrentUser();
    User assignee = getMandatoryUser(assigneeName);
    Team team = getMandatoryTeam(teamId);
    // check that there is no other project with the same name
    if (projectRepository.getProject(teamId, projectName).isPresent()) {
      throw new DuplicateProjectNameException(projectName, team.getName());
    }
    // check if the new deadline of project is outdated (before the current date)
    if (isOutdatedDate(deadline)) {
      throw new InvalidDeadlineException();
    }
    // save project
    Project.SavableProject project =
        new Project.SavableProject(
            projectName, teamId, deadline, currentUser.getId(), assignee.getId());
    project.setDescription(description);
    projectRepository.saveProject(project);
    support.firePropertyChange(
        ProjectChangeablePropertyName.CREATE_PROJECT.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Updates the data of the project with id projectId with the specified data, provided that all
   * the teams and users mentions exist, and the current user is the supervisor of the project. For
   * others, these settings are not accessible.
   *
   * @param projectId is the id of the project to be updated.
   * @param newProjectTitle is the title to set.
   * @param newAssigneeName is the name of the new assignee.
   * @param newSupervisorName is the nam of the new supervisor.
   * @param newDeadline is the new deadline of the project.
   * @param newDescription is the new description of the project.
   * @throws NoSignedInUserException is there is noone signed in.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentUserException if the user with assigneeName does not exist.
   * @throws InexistentProjectException if the project to update does not exist.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws UnauthorisedOperationException if the current user is not the manager of the team.
   * @throws InexistentUserException if the user with assigneeName does not exist.
   * @throws DuplicateProjectNameException if there is already another project in the team with the
   *     desired name.
   * @throws UnregisteredMemberRoleException if the assignee or the supervisor to be set is not the
   *     member of the team.
   * @throws InvalidDeadlineException if the selected deadline is outdated.
   */
  public void updateProject(
      int projectId,
      String newProjectTitle,
      String newAssigneeName,
      String newSupervisorName,
      LocalDate newDeadline,
      String newDescription)
      throws NoSignedInUserException, SQLException, InexistentProjectException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          InexistentUserException, DuplicateProjectNameException, UnregisteredMemberRoleException,
          InvalidDeadlineException {
    User currentUser = getMandatoryCurrentUser();
    Project project = getMandatoryProject(projectId);
    guaranteeUserIsSupervisor(
        currentUser, project, "change data of project", "they are not the " + "supervisor");
    User assignee = getMandatoryUser(newAssigneeName);
    guaranteeUserIsTeamMember(assignee, project.getTeamId(), "be assignee");
    User supervisor = getMandatoryUser(newSupervisorName);
    guaranteeUserIsTeamMember(supervisor, project.getTeamId(), "be supervisor");
    // check that there is no other project with the new name
    if (!newProjectTitle.equals(project.getTitle())
        && projectRepository.getProject(project.getTeamId(), newProjectTitle).isPresent()) {
      throw new DuplicateProjectNameException(newProjectTitle);
    }
    // check that the new deadline of the project is valid
    if (isOutdatedDate(newDeadline)) {
      throw new InvalidDeadlineException();
    }
    // update project
    project.setAssigneeId(assignee.getId());
    project.setSupervisorId(supervisor.getId());
    project.setDescription(newDescription);
    project.setTitle(newProjectTitle);
    project.setDeadline(newDeadline);
    projectRepository.updateProject(project);
    support.firePropertyChange(
        ProjectChangeablePropertyName.UPDATE_PROJECT.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Deletes the project with id projectId with all of its data (comments), but only if the current
   * user is the supervisor of the project.
   *
   * @param projectId is the id of the project to delete.
   * @throws InexistentProjectException if the project to delete is not found.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if there is noone signed in.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws UnauthorisedOperationException if the current user is not the supervisor of the
   *     project.
   */
  public void deleteProject(int projectId)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException {
    User currentUser = getMandatoryCurrentUser();
    Project project = getMandatoryProject(projectId);
    guaranteeUserIsSupervisor(
        currentUser, project, "delete project", "they are not the " + "supervisor");
    commentRepository.deleteAllCommentsOfProject(projectId);
    projectRepository.deleteProject(projectId);
    support.firePropertyChange(
        ProjectChangeablePropertyName.DELETE_PROJECT.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Deletes all the projects of a given team, safely, after first deleting all of its their
   * comments.
   *
   * @param teamId is the id of the team whose projects are deleted.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws InexistentUserException should never occur.
   */
  public void deleteAllProjectsOfTeam(int teamId)
      throws SQLException, InexistentDatabaseEntityException, InexistentUserException {
    List<Project> projectsOfTeam =
        getProjectsOfTeam(
            teamId,
            null,
            null,
            EnumSet.allOf(Project.Status.class),
            EnumSet.allOf(Project.DeadlineStatus.class));
    for (Project project : projectsOfTeam) {
      commentRepository.deleteAllCommentsOfProject(project.getId());
      projectRepository.deleteProject(project.getId());
    }
  }

  /**
   * Sets a project's status from TO_DO to IN_PROGRESS.
   *
   * @param projectId is the id of the project to update.
   * @throws InexistentProjectException if the Project with projectId does not exist.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws IllegalProjectStatusChangeException if the current status of the project is not TO_DO.
   */
  public void setProjectInProgress(int projectId)
      throws InexistentProjectException, SQLException, InexistentDatabaseEntityException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    if (project.getStatus() == Project.Status.TO_DO) {
      project.setStatus(Project.Status.IN_PROGRESS);
      projectRepository.updateProject(project);
    } else {
      throw new IllegalProjectStatusChangeException(
          project.getStatus(), Project.Status.IN_PROGRESS);
    }
    support.firePropertyChange(
        ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Sets a project's status from IN_PROGRESS to TO_DO, but only if the current user is the asignee.
   *
   * @param projectId is the id of the project to update.
   * @throws NoSignedInUserException if there is noone signed in.
   * @throws UnauthorisedOperationException if the current user is not the assignee.
   * @throws InexistentProjectException if the Project with projectId does not exist.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws IllegalProjectStatusChangeException if the current status of the project is not
   *     IN_PROGRESS.
   */
  public void setProjectAsToDo(int projectId)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() == Project.Status.IN_PROGRESS) {
      if (userIsAssignee(currentUser, project)) {
        project.setStatus(Project.Status.TO_DO);
        projectRepository.updateProject(project);
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(),
            "set back the project status to to do",
            "they are not the assignee");
      }
    } else {
      throw new IllegalProjectStatusChangeException(project.getStatus(), Project.Status.TO_DO);
    }
    support.firePropertyChange(
        ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Sets the projects status tu turned-in provided that the current user is the assignee and the
   * status of the project is not already turned-in or finished.
   *
   * @param projectId is the id of the project to turn in.
   * @throws InexistentProjectException if the project with projectId does not exist.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if there is noone signed in.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws UnauthorisedOperationException if the current user is not the assignee.
   * @throws IllegalProjectStatusChangeException if the project's current status is turned-in or
   *     finished.
   */
  public void turnInProject(int projectId)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() != Project.Status.FINISHED
        && project.getStatus() != Project.Status.TURNED_IN) {
      if (userIsAssignee(currentUser, project)) {
        project.setStatus(Project.Status.TURNED_IN);
        project.setTurnInDate(LocalDate.now());
        projectRepository.updateProject(project);
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(), "turn in project", "they " + "are not the assignee");
      }
    } else {
      throw new IllegalProjectStatusChangeException(project.getStatus(), Project.Status.TURNED_IN);
    }
    support.firePropertyChange(
        ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Removes the turn-in and sets the project's status to newStatus, which can be either TO_DO or
   * IN_PROGRESS, provided that the current user is the assignee.
   *
   * @param projectId if the id of the project whose status is changed.
   * @param newStatus is the new status of the project.
   * @throws InexistentProjectException if the project with projectId does not exist.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if there is noone signed in.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws UnauthorisedOperationException if the current user is not the assignee.
   * @throws IllegalProjectStatusChangeException if the current status of the project is not
   *     TURNED_IN or the newStatus is not TO_DO or IN_PROGRESS.
   */
  public void undoTurnIn(int projectId, Project.Status newStatus)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() == Project.Status.TURNED_IN) {
      if (userIsAssignee(currentUser, project)) {
        if (newStatus == Project.Status.TO_DO || newStatus == Project.Status.IN_PROGRESS) {
          project.setStatus(newStatus);
          project.setTurnInDate(null);
          projectRepository.updateProject(project);
        } else {
          throw new IllegalProjectStatusChangeException(project.getStatus(), newStatus);
        }
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(), "undo turn in", "they " + "are not the assignee");
      }
    } else {
      throw new IllegalProjectStatusChangeException(project.getStatus(), newStatus);
    }
    support.firePropertyChange(
        ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Sets a previously turned in project's status to finished, but only if the current user is the
   * supervisor.
   *
   * @param projectId is the id of the finished project.
   * @throws InexistentProjectException if the project with projectId does not exist.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws NoSignedInUserException if there is noone signed in.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws UnauthorisedOperationException if the current user is not the supervisor.
   * @throws IllegalProjectStatusChangeException if the project with projectId is not yet turned in.
   */
  public void acceptAsFinished(int projectId)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() == Project.Status.TURNED_IN) {
      if (userIsSupervisor(currentUser, project)) {
        project.setStatus(Project.Status.FINISHED);
        projectRepository.updateProject(project);
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(), "accept as finished", "they" + " are not the supervisor");
      }
    } else {
      throw new IllegalProjectStatusChangeException(project.getStatus(), Project.Status.FINISHED);
    }
    support.firePropertyChange(
        ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Changes the status of the project with projectId to newStatus, but unly if the current user is
   * the supervisor.
   *
   * @param projectId is the id of the project to update.
   * @param newStatus is the new status of the project. Cannot be finished.
   * @throws InexistentProjectException if the project does not exist.
   * @throws SQLException if the operation could not pe performed in the database.
   * @throws NoSignedInUserException if there is noone signed in.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws UnauthorisedOperationException if the current user is not the supervisor.
   * @throws IllegalProjectStatusChangeException if the current state is not TURNED_IN, or the
   *     newState is FINISHED or TURNED_IN.
   */
  public void discardTurnIn(int projectId, Project.Status newStatus)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() == Project.Status.TURNED_IN) {
      if (userIsSupervisor(currentUser, project)) {
        if (newStatus != Project.Status.FINISHED && newStatus != Project.Status.TURNED_IN) {
          project.setStatus(newStatus);
          project.setTurnInDate(null);
          projectRepository.updateProject(project);
        } else {
          throw new IllegalProjectStatusChangeException(project.getStatus(), newStatus);
        }
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(), "discard turn in", "they" + " are not the supervisor");
      }
    } else {
      throw new IllegalProjectStatusChangeException(project.getStatus(), newStatus);
    }
    support.firePropertyChange(
        ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString(), OLD_VALUE, NEW_VALUE);
  }

  /**
   * Returns a list of all the projects in the team with teamId, assigned to a user with assigneeId,
   * if assigeeId is not null, otherwise assigned to any user, supervised by a user with id
   * supervisorId, if supervisorId is not null, otherwise supervised by any user, having any status
   * specified by queryStatus (possibly ALL), and a status with respect to the deadline specified by
   * queryProject.DeadlineStatus.
   *
   * @param allowedStatuses is the set of all statuses which are allowed for the returned projects.
   * @param assignedToCurrentUser shows whether the returned projects should be assigned to the
   *     current user (true) or assigned to anyone (false).
   * @param supervisedByCurrentUser shows whether the returned projects should be supervised by the
   *     current user (true) or supervised by anyone (false).
   * @param allowedDeadlineStatuses is the set of deadline statuses which are allowed for the
   *     returned projects.
   * @return the list of projects fulfilling all the above requirements.
   * @throws SQLException if the operations could not be performed in the database.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws NoSignedInUserException if there is noone signed in.
   */
  public List<Project> getProjects(
      boolean assignedToCurrentUser,
      boolean supervisedByCurrentUser,
      EnumSet<Project.Status> allowedStatuses,
      EnumSet<Project.DeadlineStatus> allowedDeadlineStatuses)
      throws NoSignedInUserException, InexistentDatabaseEntityException, SQLException {
    User currentUser = getMandatoryCurrentUser();
    Integer assigneeId = null;
    if (assignedToCurrentUser) {
      assigneeId = currentUser.getId();
    }
    Integer supervisorId = null;
    if (supervisedByCurrentUser) {
      supervisorId = currentUser.getId();
    }
    return projectRepository.getProjects(
        allowedStatuses, assigneeId, supervisorId, allowedDeadlineStatuses);
  }

  /**
   * Returns a list of all the projects in the team with teamId, assigned to a user with assigneeId,
   * if assigeeId is not null, otherwise assigned to any user, supervised by a user with id
   * supervisorId, if supervisorId is not null, otherwise supervised by any user, having any status
   * specified by queryStatus (possibly ALL), and a status with respect to the deadline specified by
   * queryProject.DeadlineStatus.
   *
   * @param teamId is the id of the team whose projects are returned.
   * @param allowedStatuses is the set of all statuses which are allowed for the returned projects.
   * @param assigneeName is an optional parameter. If it is null, it doesn't count. Othwerise, only
   *     those projects are returned, which are assigned to the user with name assigneeName.
   * @param supervisorName is an optional parameter. If it is null, it doesn't count. Othwerise,
   *     only those projects are returned, which are supervised by the user with id supervisorName.
   * @param allowedDeadlineStatuses is the set of deadline statuses which are allowed for the
   *     returned projects.
   * @return the list of projects fulfilling all the above requirements.
   * @throws SQLException if the operations could not be performed in the database.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws InexistentUserException if the user with assigneeName or supervisorName does not exist
   *     in the database.
   */
  public List<Project> getProjectsOfTeam(
      int teamId,
      String supervisorName,
      String assigneeName,
      EnumSet<Project.Status> allowedStatuses,
      EnumSet<Project.DeadlineStatus> allowedDeadlineStatuses)
      throws InexistentDatabaseEntityException, SQLException, InexistentUserException {
    Integer assigneeId = null;
    if (assigneeName != null) {
      User assignee = getMandatoryUser(assigneeName);
      assigneeId = assignee.getId();
    }
    Integer supervisorId = null;
    if (supervisorName != null) {
      User supervisor = getMandatoryUser(supervisorName);
      supervisorId = supervisor.getId();
    }
    return projectRepository.getProjectsOfTeam(
        teamId, allowedStatuses, assigneeId, supervisorId, allowedDeadlineStatuses);
  }

  private void guaranteeUserIsSupervisor(
      User user, Project project, String operation, String reason)
      throws InexistentDatabaseEntityException, UnauthorisedOperationException {
    if (!userIsSupervisor(user, project)) {
      throw new UnauthorisedOperationException(user.getId(), operation, reason);
    }
  }

  private void guaranteeUserIsAssignee(User user, Project project, String operation, String reason)
      throws InexistentDatabaseEntityException, UnauthorisedOperationException {
    if (!userIsAssignee(user, project)) {
      throw new UnauthorisedOperationException(user.getId(), operation, reason);
    }
  }

  private void guaranteeUserIsTeamMember(User user, int teamId, String operation)
      throws InexistentDatabaseEntityException, SQLException, UnregisteredMemberRoleException {
    if (!teamRepository.isMemberOfTeam(teamId, user.getId())) {
      throw new UnregisteredMemberRoleException(user.getUsername(), teamId, operation);
    }
  }

  private void guaranteeUserIsSupervisorOrAssignee(
      User user, Project project, String operation, String reason)
      throws UnauthorisedOperationException, InexistentDatabaseEntityException {
    if (!userIsAssignee(user, project) && !userIsSupervisor(user, project)) {
      throw new UnauthorisedOperationException(user.getId(), operation, reason);
    }
  }

  private boolean userIsSupervisor(User user, Project project)
      throws InexistentDatabaseEntityException {
    return project.getSupervisorId() == user.getId();
  }

  private boolean userIsAssignee(User user, Project project)
      throws InexistentDatabaseEntityException {
    return project.getAssigneeId() == user.getId();
  }

  /**
   * @param project is the project whose supervisor is checked.
   * @return true iff the current user is the supervisor of project.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws NoSignedInUserException if there is noone signed in.
   */
  public boolean currentUserIsSupervisor(Project project)
      throws InexistentDatabaseEntityException, NoSignedInUserException {
    return userIsSupervisor(getMandatoryCurrentUser(), project);
  }

  /**
   * @param project is the project whose assignee is checked.
   * @return true iff the current user is the assignee of project.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws NoSignedInUserException if there is noone signed in.
   */
  public boolean currentUserIsAssignee(Project project)
      throws InexistentDatabaseEntityException, NoSignedInUserException {
    return userIsAssignee(getMandatoryCurrentUser(), project);
  }

  /**
   * Gets the project with the specified id.
   *
   * @param projectId uniquely identifies the project
   * @return the project if it was found
   * @throws InexistentProjectException if the project with that id was not found
   * @throws SQLException if a database related error occurs
   */
  public Project getProjectById(int projectId) throws InexistentProjectException, SQLException {
    return getMandatoryProject(projectId);
  }

  /**
   * Guarantee that the member of the given team has no unfinished projects assigned to them or
   * supervised by them.
   *
   * @param member = the name of the member
   * @param teamId = id of the team of the given member
   * @throws SQLException in case a database error occurs
   * @throws InexistentDatabaseEntityException in case there is no team with the given id
   * @throws InexistentUserException if no member exists with given name
   * @throws IllegalMemberRemovalException if the given member has any unfinished projects left
   */
  public void guaranteeNoUnfinishedAssignedOrSupervisedProjects(String member, int teamId)
      throws SQLException, InexistentDatabaseEntityException, InexistentUserException,
          IllegalMemberRemovalException {
    List<Project> unFinishedAssignedProjects =
        getProjectsOfTeam(
            teamId,
            null,
            member,
            EnumSet.range(Project.Status.TO_DO, Project.Status.TURNED_IN),
            EnumSet.allOf(Project.DeadlineStatus.class));
    List<Project> unFinishedSupervisedProjects =
        getProjectsOfTeam(
            teamId,
            member,
            null,
            EnumSet.range(Project.Status.TO_DO, Project.Status.TURNED_IN),
            EnumSet.allOf(Project.DeadlineStatus.class));
    if (!unFinishedAssignedProjects.isEmpty() || !unFinishedSupervisedProjects.isEmpty()) {
      throw new IllegalMemberRemovalException(member);
    }
  }
}
