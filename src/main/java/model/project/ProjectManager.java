package model.project;

import model.InexistentDatabaseEntityException;
import model.Manager;
import model.project.queryconstants.QueryProjectStatus;
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
    projectRepository.saveProject(project);
  }

  public List<Project> getProjects(
      boolean assignedToCurrentUser,
      boolean supervisedByCurrentUser,
      QueryProjectStatus queryStatus)
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
    return projectRepository.getProjects(queryStatus, assigneeId, supervisorId);
  }

  public List<Project> getProjectsOfTeam(
      int teamId, String supervisorName, String assigneeName, QueryProjectStatus queryStatus)
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
    return projectRepository.getProjectsOfTeam(teamId, queryStatus, assigneeId, supervisorId);
  }
}
