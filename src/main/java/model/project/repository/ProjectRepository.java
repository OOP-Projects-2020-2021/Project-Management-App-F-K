package model.project.repository;

import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.queryconstants.QueryProjectStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * ProjectRepository specifies the methods required from any class implementing database access for
 * project-related updates, insertions, deletions and queries.
 *
 * @author Bori Fazakas
 */
public interface ProjectRepository {
  // todo: javadoc
  // todo: extend with further methods

  void saveProject(Project.SavableProject project) throws SQLException;

  Optional<Project> getProject(int projectId) throws SQLException;

  Optional<Project> getProject(int teamId, String name) throws SQLException;

  void updateProject(Project project) throws SQLException, InexistentDatabaseEntityException;

  List<Project> getProjectsOfTeam(
      int teamId, QueryProjectStatus queryStatus, Integer assigneeId, Integer supervisorId)
      throws SQLException;

  List<Project> getProjects(
      QueryProjectStatus queryStatus, Integer assigneeId, Integer supervisorId) throws SQLException;
}
