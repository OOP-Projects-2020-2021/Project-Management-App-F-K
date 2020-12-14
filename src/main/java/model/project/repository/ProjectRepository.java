package model.project.repository;

import model.project.Project;

import java.sql.SQLException;
import java.util.List;

public interface ProjectRepository {
    void saveProject(Project.SavableProject project) throws SQLException;

    Project getProject(int projectId);

    List<Project> getProjectsOfTeam(int teamId);

    List<Project> getProjectsSupervisedByUser(int userId);

    List<Project> getProjectsAssignedToUser(int userId);
}
