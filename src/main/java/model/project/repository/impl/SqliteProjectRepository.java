package model.project.repository.impl;

import model.project.Project;
import model.project.repository.ProjectRepository;

import java.sql.*;
import java.util.List;

public class SqliteProjectRepository implements ProjectRepository {
    private Connection c;

    // Save a new team.
    private static final String SAVE_PROJECT_STATEMENT =
            "INSERT INTO Project (Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, " +
                    "StatusId) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private PreparedStatement saveProjectSt;
    
    // Get status id
    private static final String GET_PROJECTS_STATUS_ID =
            "SELECT StatusId from ProjectStatus WHERE StatusName = ?";
    private PreparedStatement getProjectStatusIdSt;


    public SqliteProjectRepository() {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:project_management_app.db");
            prepareStatements();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * The statements are prepared only once, when the reposiroy is constructed, because this way sql
     * parsing and creating a query plan is created only once, so query execution is faster.
     */
    private void prepareStatements() throws SQLException {
        saveProjectSt = c.prepareStatement(SAVE_PROJECT_STATEMENT);
        getProjectStatusIdSt = c.prepareStatement(GET_PROJECTS_STATUS_ID);
    }

    @Override
    public void saveProject(Project.SavableProject project) throws SQLException {
        saveProjectSt.setString(1, project.getTitle());
        saveProjectSt.setInt(2, project.getTeamId());
        if (project.getDescription().isPresent()) {
            saveProjectSt.setString(3,
                    project.getDescription().get());
        } else {
            saveProjectSt.setNull(3, Types.NVARCHAR);
        }
        saveProjectSt.setString(4, project.getDeadline().toString());
        saveProjectSt.setInt(5, project.getAssigneeId());
        saveProjectSt.setInt(6, project.getAssigneeId());
        saveProjectSt.setInt(7, getProjectStatusId(project));
        saveProjectSt.execute();
    }

    @Override
    public Project getProject(int projectId) {
        return null;
    }

    @Override
    public List<Project> getProjectsOfTeam(int teamId) {
        return null;
    }

    @Override
    public List<Project> getProjectsSupervisedByUser(int userId) {
        return null;
    }

    @Override
    public List<Project> getProjectsAssignedToUser(int userId) {
        return null;
    }

    private int getProjectStatusId(Project.SavableProject project) throws SQLException {
        getProjectStatusIdSt.setString(1, project.getStatus().toString());
        ResultSet result = getProjectStatusIdSt.executeQuery();
        result.next();
        return result.getInt("StatusId");
    }
}
