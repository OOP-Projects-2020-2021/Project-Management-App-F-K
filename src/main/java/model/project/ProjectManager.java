package model.project;

import model.InexistentDatabaseEntityException;
import model.Manager;
import model.UnauthorisedOperationException;
import model.project.queryconstants.QueryProjectDeadlineStatus;
import model.project.queryconstants.QueryProjectStatus;
import model.project.exceptions.*;
import model.team.Team;
import model.team.exceptions.InexistentTeamException;
import model.team.exceptions.UnregisteredMemberRoleException;
import model.user.User;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;

import java.sql.SQLException;
import java.time.LocalDate;
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
   * @throws NoSignedInUserException if there is noone signed in.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentUserException if the user with assigneeName does not exist.
   * @throws InexistentTeamException if the team with teamId does not exist.
   * @throws DuplicateProjectNameException if there is already a project with the same name in the
   *     same team. This is not allowed.
   * @throws InexistentDatabaseEntityException should never occur.
   */
  public void createProject(
      String projectName, int teamId, String assigneeName, LocalDate deadline, String description)
      throws NoSignedInUserException, SQLException, InexistentUserException,
          InexistentTeamException, DuplicateProjectNameException,
          InexistentDatabaseEntityException {
    User currentUser = getMandatoryCurrentUser();
    User assignee = getMandatoryUser(assigneeName);
    Team team = getMandatoryTeam(teamId);
    // check that there is no other project with the same name
    if (projectRepository.getProject(teamId, projectName).isPresent()) {
      throw new DuplicateProjectNameException(projectName, team.getName());
    }
    // save project
    Project.SavableProject project =
        new Project.SavableProject(
            projectName, teamId, deadline, currentUser.getId(), assignee.getId());
    project.setDescription(description);
    projectRepository.saveProject(project);
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
          InexistentUserException, DuplicateProjectNameException, UnregisteredMemberRoleException {
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
    // update project
    project.setAssigneeId(assignee.getId());
    project.setSupervisorId(supervisor.getId());
    project.setDescription(newDescription);
    project.setTitle(newProjectTitle);
    project.setDeadline(newDeadline);
    projectRepository.updateProject(project);
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
  public void deleteAllProjectsOfTeam(int teamId) throws SQLException, InexistentDatabaseEntityException, InexistentUserException {
    List<Project> projectsOfTeam = getProjectsOfTeam(teamId, null, null, QueryProjectStatus.ALL,
            QueryProjectDeadlineStatus.ALL);
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
    if (project.getStatus() == Project.ProjectStatus.TO_DO) {
      project.setStatus(Project.ProjectStatus.IN_PROGRESS);
      projectRepository.updateProject(project);
    } else {
      throw new IllegalProjectStatusChangeException(
          project.getStatus(), Project.ProjectStatus.IN_PROGRESS);
    }
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
    if (project.getStatus() == Project.ProjectStatus.IN_PROGRESS) {
      if (userIsAssignee(currentUser, project)) {
        project.setStatus(Project.ProjectStatus.TO_DO);
        projectRepository.updateProject(project);
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(),
            "set back the project status to to do",
            "they are not the assignee");
      }
    } else {
      throw new IllegalProjectStatusChangeException(
          project.getStatus(), Project.ProjectStatus.TO_DO);
    }
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
    if (project.getStatus() != Project.ProjectStatus.FINISHED
        && project.getStatus() != Project.ProjectStatus.TURNED_IN) {
      if (userIsAssignee(currentUser, project)) {
        project.setStatus(Project.ProjectStatus.TURNED_IN);
        projectRepository.updateProject(project);
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(), "turn in project", "they " + "are not the assignee");
      }
    } else {
      throw new IllegalProjectStatusChangeException(
          project.getStatus(), Project.ProjectStatus.TURNED_IN);
    }
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
  public void undoTurnIn(int projectId, Project.ProjectStatus newStatus)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() == Project.ProjectStatus.TURNED_IN) {
      if (userIsAssignee(currentUser, project)) {
        if (newStatus == Project.ProjectStatus.TO_DO
            || newStatus == Project.ProjectStatus.IN_PROGRESS) {
          project.setStatus(newStatus);
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
    if (project.getStatus() == Project.ProjectStatus.TURNED_IN) {
      if (userIsSupervisor(currentUser, project)) {
        project.setStatus(Project.ProjectStatus.FINISHED);
        projectRepository.updateProject(project);
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(), "accept as finished", "they" + " are not the supervisor");
      }
    } else {
      throw new IllegalProjectStatusChangeException(
          project.getStatus(), Project.ProjectStatus.FINISHED);
    }
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
  public void discardTurnIn(int projectId, Project.ProjectStatus newStatus)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() == Project.ProjectStatus.TURNED_IN) {
      if (userIsSupervisor(currentUser, project)) {
        if (newStatus != Project.ProjectStatus.FINISHED
            && newStatus != Project.ProjectStatus.TURNED_IN) {
          project.setStatus(newStatus);
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
  }

  /**
   * Returns a list of all the projects in the team with teamId, assigned to a user with assigneeId,
   * if assigeeId is not null, otherwise assigned to any user, supervised by a user with id
   * supervisorId, if supervisorId is not null, otherwise supervised by any user, having any status
   * specified by queryStatus (possibly ALL), and a status with respect to the deadline specified by
   * queryDeadlineStatus.
   *
   * @param queryStatus is an optional parameter. If it is ALL, it doesn't count. Othwerise, only
   *     those projects are returned, which have the status corresponding to queryStatus.
   * @param assignedToCurrentUser shows whether the returned projects should be assigned to the
   *     current user (true) or assigned to anyone (false).
   * @param supervisedByCurrentUser shows whether the returned projects should be supervised by the
   *     current user (true) or supervised by anyone (false).
   * @param queryDeadlineStatus is an optional parameter. If it is null, it doesn't count.
   *     Othwerise, only those projects are returned, which have the status with respect to the
   *     deadline corresponding to ueryDeadlineStatus.
   * @return the list of projects fulfilling all the above requirements.
   * @throws SQLException if the operations could not be performed in the database.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws NoSignedInUserException if there is noone signed in.
   */
  public List<Project> getProjects(
      boolean assignedToCurrentUser,
      boolean supervisedByCurrentUser,
      QueryProjectStatus queryStatus,
      QueryProjectDeadlineStatus queryDeadlineStatus)
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
        queryStatus, assigneeId, supervisorId, queryDeadlineStatus);
  }

  /**
   * Returns a list of all the projects in the team with teamId, assigned to a user with assigneeId,
   * if assigeeId is not null, otherwise assigned to any user, supervised by a user with id
   * supervisorId, if supervisorId is not null, otherwise supervised by any user, having any status
   * specified by queryStatus (possibly ALL), and a status with respect to the deadline specified by
   * queryDeadlineStatus.
   *
   * @param teamId is the id of the team whose projects are returned.
   * @param queryStatus is an optional parameter. If it is ALL, it doesn't count. Othwerise, only
   *     those projects are returned, which have the status corresponding to queryStatus.
   * @param assigneeName is an optional parameter. If it is null, it doesn't count. Othwerise, only
   *     those projects are returned, which are assigned to the user with name assigneeName.
   * @param supervisorName is an optional parameter. If it is null, it doesn't count. Othwerise,
   *     only those projects are returned, which are supervised by the user with id supervisorName.
   * @param queryDeadlineStatus is an optional parameter. If it is null, it doesn't count.
   *     Othwerise, only those projects are returned, which have the status with respect to the
   *     deadline corresponding to ueryDeadlineStatus.
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
      QueryProjectStatus queryStatus,
      QueryProjectDeadlineStatus queryDeadlineStatus)
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
        teamId, queryStatus, assigneeId, supervisorId, queryDeadlineStatus);
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
      throws UnauthorisedOperationException, InexistentDatabaseEntityException, SQLException {
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
}
