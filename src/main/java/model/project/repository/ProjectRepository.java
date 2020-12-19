package model.project.repository;

import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.queryconstants.QueryProjectDeadlineStatus;
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

  /**
   * Saves the project in the database and assigns it an id, which is returned.
   *
   * @param project is the project to save.
   * @return the id of the newly saved project in the database.
   * @throws SQLException if the operations could not be performed in the database.
   */
  int saveProject(Project.SavableProject project) throws SQLException, InexistentDatabaseEntityException;

  /**
   * Finds the project with the given id and returns it, if it exists.
   *
   * @param projectId is the id of the project to search for.
   * @return an Optional containing the found project, if it exists in the database. Otherwise,
   * an enmpty Optional.
   * @throws SQLException if the operations could not be performed in the database.
   */
  Optional<Project> getProject(int projectId) throws SQLException;

  /**
   * Finds the project with the given teamId and title/name and returns it if it exists.
   *
   * @param teamId is the id of the team in which the project is supposed to be,
   * @param name is the name/title of the team that is searched.
   * @return an Optional containing the found project, if it exists in the database. Otherwise,
   * an enmpty Optional.
   * @throws SQLException if the operations could not be performed in the database.
   */
  Optional<Project> getProject(int teamId, String name) throws SQLException;

  /**
   * Updates the data of the project, which must already exist in the database with the same id,
   * with the data specified by the project object.
   *
   * @param project holds the id of the existing project and the possibly new data to be saved.
   * @throws SQLException if the operations could not be performed in the database.
   */
  void updateProject(Project project) throws SQLException, InexistentDatabaseEntityException;

  /**
   * Returns a list of all the projects in the team with teamId, assigned to a user with
   * assigneeId, if assigeeId is not null, otherwise assigned to any user, supervised by a user
   * with id supervisorId, if supervisorId is not null, otherwise supervised by any user, having
   * any status specified by queryStatus (possibly ALL), and a status with respect to the
   * deadline specified by queryDeadlineStatus.
   *
   * @param teamId is the id of the team whose projects are returned.
   * @param queryStatus is an optional parameter. If it is ALL, it doesn't count. Othwerise,
   *                    only those projects are returned, which have the status corresponding to
   *                    queryStatus.
   * @param assigneeId is an optional parameter. If it is null, it doesn't count. Othwerise,
   *                   only those projects are returned, which are assigned to the user
   *                   with id assigneeId.
   * @param supervisorId is an optional parameter. If it is null, it doesn't count. Othwerise,
   *                   only those projects are returned, which are supervised by the user
   *                   with id supervisorId.
   * @param queryDeadlineStatus is an optional parameter. If it is null, it doesn't count. Othwerise,
   *                   only those projects are returned, which have the status with respect to
   *                   the deadline corresponding to ueryDeadlineStatus.
   * @return the list of projects fulfilling all the above requirements.
   * @throws SQLException if the operations could not be performed in the database.
   */
  List<Project> getProjectsOfTeam(
      int teamId,
      QueryProjectStatus queryStatus,
      Integer assigneeId,
      Integer supervisorId,
      QueryProjectDeadlineStatus queryDeadlineStatus)
      throws SQLException;

  /**
   * Returns a list of all the projects assigned to a user with
   * assigneeId, if assigeeId is not null, otherwise assigned to any user, supervised by a user
   * with id supervisorId, if supervisorId is not null, otherwise supervised by any user, having
   * any status specified by queryStatus (possibly ALL), and a status with respect to the
   * deadline specified by queryDeadlineStatus.
   *
   * @param queryStatus is an optional parameter. If it is ALL, it doesn't count. Othwerise,
   *                    only those projects are returned, which have the status corresponding to
   *                    queryStatus.
   * @param assigneeId is an optional parameter. If it is null, it doesn't count. Othwerise,
   *                   only those projects are returned, which are assigned to the user
   *                   with id assigneeId.
   * @param supervisorId is an optional parameter. If it is null, it doesn't count. Othwerise,
   *                   only those projects are returned, which are supervised by the user
   *                   with id supervisorId.
   * @param queryDeadlineStatus is an optional parameter. If it is null, it doesn't count. Othwerise,
   *                   only those projects are returned, which have the status with respect to
   *                   the deadline corresponding to ueryDeadlineStatus.
   * @return the list of projects fulfilling all the above requirements.
   * @throws SQLException if the operations could not be performed in the database.
   */
  List<Project> getProjects(
      QueryProjectStatus queryStatus,
      Integer assigneeId,
      Integer supervisorId,
      QueryProjectDeadlineStatus queryDeadlineStatus)
      throws SQLException;
}
