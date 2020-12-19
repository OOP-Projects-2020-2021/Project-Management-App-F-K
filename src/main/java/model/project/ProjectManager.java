package model.project;

import model.InexistentDatabaseEntityException;
import model.Manager;
import model.UnauthorisedOperationException;
import model.project.queryconstants.QueryProjectStatus;
import model.project.repository.exceptions.DuplicateProjectNameException;
import model.project.repository.exceptions.InexistentProjectException;
import model.team.Team;
import model.team.exceptions.InexistentTeamException;
import model.user.User;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    projectRepository.saveProject(project);
  }

  public void updateProject(int projectId,
          String newProjectTitle, String newAssigneeName, String newSupervisorName,
          LocalDate newDeadline, String newDescription) throws NoSignedInUserException, SQLException, InexistentProjectException, InexistentDatabaseEntityException, UnauthorisedOperationException, InexistentUserException, DuplicateProjectNameException {
    User currentUser = getMandatoryCurrentUser();
    Optional<Project> projectOp = projectRepository.getProject(projectId);
    if (projectOp.isEmpty()) {
      throw new InexistentProjectException(projectId);
    }
    Project project = projectOp.get();
    guaranteeUserIsSupervisor(currentUser, project, "change data of project", "they are not the " +
            "supervisor");
    User assignee = getMandatoryUser(newAssigneeName);
    guaranteeUserIsTeamMember(assignee, project.getTeamId(), "This user cannot be assignee " +
            "because they are not a member of the team");
    User supervisor = getMandatoryUser(newSupervisorName);
    guaranteeUserIsTeamMember(supervisor, project.getTeamId(), "This user cannot be supervisor " +
            "because they are not a member of the team");
    // check that there is no other project with the new name
    if (!newProjectTitle.equals(project.getTitle()) && projectRepository.getProject(project.getTeamId(),
            newProjectTitle).isPresent()) {
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

  public List<Project> getProjects(boolean assignedToCurrentUser, boolean supervisedByCurrentUser
          , QueryProjectStatus queryStatus) throws NoSignedInUserException, InexistentDatabaseEntityException, SQLException {
    User currentUser = getMandatoryCurrentUser();
    Integer assigneeId = null;
    if (assignedToCurrentUser) {
      assigneeId = currentUser.getId();
    }
    Integer supervisorId = null;
    if (supervisedByCurrentUser) {
      supervisorId = currentUser.getId();
    }
    return projectRepository.getProjects(queryStatus, assigneeId, supervisorId);
  }

  public List<Project> getProjectsOfTeam(int teamId, String supervisorName, String assigneeName,
                                         QueryProjectStatus queryStatus) throws NoSignedInUserException,
          InexistentDatabaseEntityException, SQLException, InexistentUserException, InexistentTeamException {
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
    return projectRepository.getProjectsOfTeam(teamId, queryStatus, assigneeId, supervisorId);
  }

  private void guaranteeUserIsSupervisor(User user, Project project, String operation,
                                         String reason) throws InexistentDatabaseEntityException,
          UnauthorisedOperationException {
    if (!userIsSupervisor(user, project)) {
      throw new UnauthorisedOperationException(user.getId(), operation, reason);
    }
  }

  private void guaranteeUserIsAssignee(User user, Project project, String operation,
                                       String reason) throws InexistentDatabaseEntityException,
          UnauthorisedOperationException {
    if (!userIsAssignee(user, project)) {
      throw new UnauthorisedOperationException(user.getId(), operation, reason);
    }
  }

  private void guaranteeUserIsTeamMember(User user, int teamId, String message) throws

          UnauthorisedOperationException, InexistentDatabaseEntityException, SQLException {
    if (!teamRepository.isMemberOfTeam(teamId, user.getId())) {
      throw new IllegalArgumentException(message);
    }
  }

  private void guaranteeUserIsSupervisorOrAssignee(User user, Project project, String operation,
                                                   String reason) throws
          UnauthorisedOperationException, InexistentDatabaseEntityException, SQLException {
    if (!userIsAssignee(user, project) && !userIsSupervisor(user, project)) {
      throw new UnauthorisedOperationException(user.getId(), operation, reason);
    }
  }

  private boolean userIsSupervisor(User user, Project project) throws InexistentDatabaseEntityException {
    return project.getSupervisorId() == user.getId();
  }

  private boolean userIsAssignee(User user, Project project) throws InexistentDatabaseEntityException {
    return project.getAssigneeId() == user.getId();
  }

  private boolean validProjectStatusChange(Project project, Project.ProjectStatus newStatus,
                                           User user) throws InexistentDatabaseEntityException, UnauthorisedOperationException {
    switch (project.getStatus()) {
      case FINISHED:
        // if the project was finished already, its status can be changed by the supervisor only.
        if (!userIsSupervisor(user, project)) {
          return false;
        }
        // the MARKED_AS_DONE status is reachable only for the assignee
        return newStatus != Project.ProjectStatus.MARKED_AS_DONE;
      case TO_DO:
        switch (newStatus) {
          case FINISHED: return userIsSupervisor(user, project);
          case MARKED_AS_DONE: return userIsAssignee(user, project) ;
          case IN_PROGRESS: return true;
        }
        break;
      case IN_PROGRESS:
        switch (newStatus) {
          case FINISHED: return userIsSupervisor(user, project);
          case MARKED_AS_DONE: return userIsAssignee(user, project);
          case TO_DO: return userIsAssignee(user, project);
        }
        break;
      case MARKED_AS_DONE:
        switch (newStatus) {
          case FINISHED: return userIsSupervisor(user, project);
          case IN_PROGRESS: return userIsAssignee(user, project) || userIsSupervisor(user, project);
          case TO_DO: return userIsAssignee(user, project) || userIsSupervisor(user, project);
        }
    }
    return false;
  }
}
