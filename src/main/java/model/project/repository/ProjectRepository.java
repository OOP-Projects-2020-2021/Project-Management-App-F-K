package model.project.repository;

import model.project.Project;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository {
  void saveProject(Project.SavableProject project) throws SQLException;

  Optional<Project> getProject(int projectId);

  Optional<Project> getProject(int teamId, String name) throws SQLException;

  List<Project> getProjectsOfTeam(int teamId);

  List<Project> getProjectsSupervisedByUser(int userId);

  List<Project> getProjectsAssignedToUser(int userId);
}
