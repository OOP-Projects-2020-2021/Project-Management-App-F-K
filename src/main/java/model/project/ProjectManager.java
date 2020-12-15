package model.project;

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

public class ProjectManager {
    private static ProjectManager instance = new ProjectManager();
    private ProjectRepository projectRepository = SqliteProjectRepository.getInstance();
    private UserRepository userRepository = SqliteUserRepository.getInstance();
    private TeamRepository teamRepository = SqliteTeamRepository.getInstance();

    private ProjectManager() {}

    public static ProjectManager getInstance() {
        return instance;
    }

    public void createProject(String projectName, int teamId, String assigneeName,
                             LocalDate deadline,
                       String description) throws Exception {
        User currentUser = getCurrentUser();
        User assignee = getUser(assigneeName);
        Team team = getTeam(teamId);
        // check that there is no other project with the same name
        if (projectRepository.getProject(teamId, projectName).isPresent()) {
            throw new DuplicateProjectNameException(projectName, team.getName());
        }
        // save project
        Project.SavableProject project = new Project.SavableProject(projectName, teamId,
                deadline, currentUser.getId().get(), assignee.getId().get());
        projectRepository.saveProject(project);
    }

    /** Returns the current user if it exists, and throws NoSignedInUserException otherwise. */
    private User getCurrentUser() throws NoSignedInUserException {
        if (UserManager.getInstance().getCurrentUser().isEmpty()) {
            throw new NoSignedInUserException();

        }
        return UserManager.getInstance().getCurrentUser().get();
    }

    /** Returns the user with the given id if it exists, otherwise throws InexistentUserEXception. */
    private User getUser(int userId) throws SQLException, InexistentUserException {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw new InexistentUserException(userId);
        }
        return user;
    }

    /**
     * Returns the user with the given name if it exists, otherwise throws InexistentUserEXception.
     */
    private User getUser(String userName) throws SQLException, InexistentUserException {
        User user = userRepository.getUserByUsername(userName);
        if (user == null) {
            throw new InexistentUserException(userName);
        }
        return user;
    }

    /**
     * Returns the team with the given id if it exists in the database, and throws
     * InexistentTeamException otherwise. The returned team is guaranteed to have an id.
     */
    private Team getTeam(int teamId) throws InexistentTeamException, SQLException {
        Optional<Team> team = teamRepository.getTeam(teamId);
        if (team.isEmpty()) {
            throw new InexistentTeamException(teamId);
        }
        return team.get();
    }
}
