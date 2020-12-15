package model.project;

import model.Manager;
import model.team.Team;
import model.user.User;

import java.time.LocalDate;

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
            projectName, teamId, deadline, currentUser.getId().get(), assignee.getId().get());
    projectRepository.saveProject(project);
  }
}
