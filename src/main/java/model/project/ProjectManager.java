package model.project;

import model.InexistentDatabaseEntityException;
import model.Manager;
import model.UnauthorisedOperationException;
import model.project.queryconstants.QueryProjectDeadlineStatus;
import model.project.queryconstants.QueryProjectStatus;
import model.project.exceptions.*;
import model.team.Team;
import model.team.exceptions.InexistentTeamException;
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

  public void createProject(
      String projectName, int teamId, String assigneeName, LocalDate deadline, String description)
      throws Exception {
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

  public void updateProject(
      int projectId,
      String newProjectTitle,
      String newAssigneeName,
      String newSupervisorName,
      LocalDate newDeadline,
      String newDescription)
      throws NoSignedInUserException, SQLException, InexistentProjectException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          InexistentUserException, DuplicateProjectNameException {
    User currentUser = getMandatoryCurrentUser();
    Project project = getMandatoryProject(projectId);
    guaranteeUserIsSupervisor(
        currentUser, project, "change data of project", "they are not the " + "supervisor");
    User assignee = getMandatoryUser(newAssigneeName);
    guaranteeUserIsTeamMember(
        assignee,
        project.getTeamId(),
        "This user cannot be assignee " + "because they are not a member of the team");
    User supervisor = getMandatoryUser(newSupervisorName);
    guaranteeUserIsTeamMember(
        supervisor,
        project.getTeamId(),
        "This user cannot be supervisor " + "because they are not a member of the team");
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

  public void setProjectInProgress(int projectId)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() == Project.ProjectStatus.TO_DO) {
      project.setStatus(Project.ProjectStatus.IN_PROGRESS);
      projectRepository.updateProject(project);
    } else {
      throw new IllegalProjectStatusChangeException(
          project.getStatus(), Project.ProjectStatus.IN_PROGRESS);
    }
  }

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
            "set back the project " + "status to to do",
            "they are not the assignee");
      }
    } else {
      throw new IllegalProjectStatusChangeException(
          project.getStatus(), Project.ProjectStatus.TO_DO);
    }
  }

  public void turnInProject(int projectId)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() != Project.ProjectStatus.FINISHED) {
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

  public void undoTurnIn(int projectId, Project.ProjectStatus newStatus)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() == Project.ProjectStatus.TURNED_IN) {
      if (userIsAssignee(currentUser, project)) {
        project.setStatus(newStatus);
        projectRepository.updateProject(project);
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(), "undo turn in", "they " + "are not the assignee");
      }
    } else {
      throw new IllegalProjectStatusChangeException(project.getStatus(), newStatus);
    }
  }

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

  public void discardTurnIn(int projectId, Project.ProjectStatus newStatus)
      throws InexistentProjectException, SQLException, NoSignedInUserException,
          InexistentDatabaseEntityException, UnauthorisedOperationException,
          IllegalProjectStatusChangeException {
    Project project = getMandatoryProject(projectId);
    User currentUser = getMandatoryCurrentUser();
    if (project.getStatus() == Project.ProjectStatus.TURNED_IN) {
      if (userIsSupervisor(currentUser, project)) {
        project.setStatus(newStatus);
        projectRepository.updateProject(project);
      } else {
        throw new UnauthorisedOperationException(
            currentUser.getId(), "discard turn in", "they" + " are not the supervisor");
      }
    } else {
      throw new IllegalProjectStatusChangeException(project.getStatus(), newStatus);
    }
  }

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
    return projectRepository.getProjects(queryStatus, assigneeId, supervisorId, queryDeadlineStatus);
  }

  public List<Project> getProjectsOfTeam(
          int teamId, String supervisorName, String assigneeName, QueryProjectStatus queryStatus,
          QueryProjectDeadlineStatus queryDeadlineStatus)
      throws NoSignedInUserException, InexistentDatabaseEntityException, SQLException,
          InexistentUserException, InexistentTeamException {
    User currentUser = getMandatoryCurrentUser();
    Team team = getMandatoryTeam(teamId);
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
    return projectRepository.getProjectsOfTeam(teamId, queryStatus, assigneeId, supervisorId, queryDeadlineStatus);
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

  private void guaranteeUserIsTeamMember(User user, int teamId, String message)
      throws UnauthorisedOperationException, InexistentDatabaseEntityException, SQLException {
    if (!teamRepository.isMemberOfTeam(teamId, user.getId())) {
      throw new IllegalArgumentException(message);
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
