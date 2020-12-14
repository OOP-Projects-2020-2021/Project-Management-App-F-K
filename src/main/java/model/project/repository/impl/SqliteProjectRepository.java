package model.project.repository.impl;

import model.project.Project;
import model.project.repository.ProjectRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SqliteProjectRepository implements ProjectRepository {
    private Connection c;

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

    }

    @Override
    public void saveProject(Project.SavableProject project) {

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
}
