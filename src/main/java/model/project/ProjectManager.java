package model.project;

import model.Manager;
import model.project.repository.ProjectRepository;
import model.project.repository.impl.SqliteProjectRepository;
import model.team.InexistentTeamException;
import model.team.Team;
import model.team.repository.TeamRepository;
import model.team.repository.impl.SqliteTeamRepository;
import model.user.InexistentUserException;
import model.user.NoSignedInUserException;
import model.user.User;
import model.user.UserManager;
import model.user.repository.UserRepository;
import model.user.repository.impl.SqliteUserRepository;

import java.sql.SQLException;
import java.time.LocalDate;
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

    public void createProject(String projectName, int teamId, String assigneeName,
                              LocalDate deadline,
                              String description) throws Exception {
        User currentUser = getMandatoryCurrentUser();
        User assignee = getMandatoryUser(assigneeName);
        Team team = getMandatoryTeam(teamId);
        // check that there is no other project with the same name
        if (projectRepository.getProject(teamId, projectName).isPresent()) {
            throw new DuplicateProjectNameException(projectName, team.getName());
        }
        // save project
        Project.SavableProject project = new Project.SavableProject(projectName, teamId,
                deadline, currentUser.getId().get(), assignee.getId().get());
        projectRepository.saveProject(project);
    }
}
