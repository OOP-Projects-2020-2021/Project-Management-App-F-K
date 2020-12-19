package model.project.repository.impl;

import model.InexistentDatabaseEntityException;
import model.database.Repository;
import model.project.Project;
import model.project.queryconstants.QueryProjectStatus;
import model.project.repository.ProjectRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SqliteTeamRepository is an implementation of ProjectRepository which provides database access to
 * an sqlite database holding project-related data.
 *
 * @author Bori Fazakas
 */
public class SqliteProjectRepository extends Repository implements ProjectRepository {
  protected static SqliteProjectRepository instance;

  // Save a new team.
  private static final String SAVE_PROJECT_STATEMENT =
      "INSERT INTO Project (Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, "
          + "StatusId) VALUES (?, ?, ?, ?, ?, ?, ?)";
  private PreparedStatement saveProjectSt;

  // Get project based on id.
  private static final String GET_PROJECT_BY_ID =
      "SELECT ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, "
          + "StatusName "
          + "From Project p JOIN ProjectStatus st ON p"
          + ".StatusId = st.StatusId WHERE ProjectId = ?";
  private PreparedStatement getProjectByIdSt;

  // Update project bases on id.
  private static final String UPDATE_PROJECT =
      "UPDATE Project "
          + " SET Name = ?, TeamId = ?, Description = ?, Deadline = ?, AssigneeId = ?, "
          + "SupervisorId = ?, StatusId = ?"
          + "Where ProjectId = ?";
  private PreparedStatement updateProjectSt;

  // Get projects based on team and title.
  private static final String GET_PROJECT_BY_TEAM_TITLE_STATEMENT =
      "SELECT ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, "
          + "StatusName "
          + "From Project p JOIN ProjectStatus st ON p"
          + ".StatusId = st.StatusId WHERE Name = ? and TeamId = ? ";
  private PreparedStatement getProjectByTitleTeamSt;

  // Get projects of team, possibly with a given assignee, supervisor and status.
  private static final String GET_PROJECTS_OF_TEAM =
          "SELECT ProjectId, p.Name AS Name, p.TeamId AS TeamId, Description, Deadline, " +
          "AssigneeId, SupervisorId, StatusName From Project p "
                  + "JOIN ProjectStatus st ON p.StatusId = st.StatusId "
                  + "JOIN Team t ON t.TeamId = p.TeamId "
                  + "WHERE t.TeamId = ? AND "
                  + "(p.SupervisorId = ? OR ?) AND "
                  + "(p.AssigneeId = ? OR ?) AND"
                  + "(st.StatusName = ? OR ?)";
  private PreparedStatement getProjectsOfTeam;

  // Get status id
  private static final String GET_PROJECTS_STATUS_ID =
      "SELECT StatusId from ProjectStatus WHERE StatusName = ?";
  private PreparedStatement getProjectStatusIdSt;

  private SqliteProjectRepository() {}

  public static SqliteProjectRepository getInstance() {
    if (instance == null) {
      instance = new SqliteProjectRepository();
    }
    return instance;
  }

  /**
   * The statements are prepared only once, when the reposiroy is constructed, because this way sql
   * parsing and creating a query plan is created only once, so query execution is faster.
   */
  protected void prepareStatements() throws SQLException {
    saveProjectSt = c.prepareStatement(SAVE_PROJECT_STATEMENT);
    getProjectByIdSt = c.prepareStatement(GET_PROJECT_BY_ID);
    updateProjectSt = c.prepareStatement(UPDATE_PROJECT);
    getProjectByTitleTeamSt = c.prepareStatement(GET_PROJECT_BY_TEAM_TITLE_STATEMENT);
    getProjectStatusIdSt = c.prepareStatement(GET_PROJECTS_STATUS_ID);
    getProjectsOfTeam = c.prepareStatement(GET_PROJECTS_OF_TEAM);
  }

  @Override
  public void saveProject(Project.SavableProject project) throws SQLException {
    saveProjectSt.setString(1, project.getTitle());
    saveProjectSt.setInt(2, project.getTeamId());
    if (project.getDescription().isPresent()) {
      saveProjectSt.setString(3, project.getDescription().get());
    } else {
      saveProjectSt.setNull(3, Types.NVARCHAR);
    }
    saveProjectSt.setString(4, project.getDeadline().toString());
    saveProjectSt.setInt(5, project.getAssigneeId());
    saveProjectSt.setInt(6, project.getSupervisorId());
    saveProjectSt.setInt(7, getProjectStatusId(project.getStatus()));
    saveProjectSt.execute();
  }

  @Override
  public Optional<Project> getProject(int projectId) throws SQLException {
    getProjectByIdSt.setInt(1, projectId);
    ResultSet result = getProjectByIdSt.executeQuery();
    if (result.next()) {
      return Optional.of(getProjectFromResult(result));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public Optional<Project> getProject(int teamId, String name) throws SQLException {
    getProjectByTitleTeamSt.setString(1, name);
    getProjectByTitleTeamSt.setInt(2, teamId);
    ResultSet result = getProjectByTitleTeamSt.executeQuery();
    if (result.next()) {
      return Optional.of(getProjectFromResult(result));
    } else {
      return Optional.empty();
    }
  }

  @Override
  public void updateProject(Project project)
      throws SQLException, InexistentDatabaseEntityException {
    updateProjectSt.setString(1, project.getTitle());
    updateProjectSt.setInt(2, project.getTeamId());
    if (project.getDescription().isPresent()) {
      updateProjectSt.setString(3, project.getDescription().get());
    } else {
      updateProjectSt.setNull(3, Types.NVARCHAR);
    }
    updateProjectSt.setString(4, project.getDeadline().toString());
    updateProjectSt.setInt(5, project.getAssigneeId());
    updateProjectSt.setInt(6, project.getSupervisorId());
    updateProjectSt.setInt(7, getProjectStatusId(project.getStatus()));
    updateProjectSt.setInt(8, project.getId());
    updateProjectSt.execute();
  }

  @Override
  public List<Project> getProjectsOfTeam(int teamId, QueryProjectStatus queryStatus, Integer assigneeId, Integer supervisorId) throws SQLException {
    getProjectsOfTeam.setInt(1, teamId);
    // if supervisorid is null, it is don't care
    if (supervisorId != null) {
      getProjectsOfTeam.setInt(2, supervisorId);
      getProjectsOfTeam.setBoolean(3, false);
    } else {
      getProjectsOfTeam.setNull(2, Types.INTEGER);
      getProjectsOfTeam.setBoolean(3, true);
    }
    // if assigneId is null, it is don't care
    if (assigneeId != null) {
      getProjectsOfTeam.setInt(4, assigneeId);
      getProjectsOfTeam.setBoolean(5, false);
    } else {
      getProjectsOfTeam.setNull(4, Types.INTEGER);
      getProjectsOfTeam.setBoolean(5, true);
    }
    if (queryStatus == QueryProjectStatus.ALL) {
      getProjectsOfTeam.setNull(6, Types.NVARCHAR);
      getProjectsOfTeam.setBoolean(7, true);
    } else {
      getProjectsOfTeam.setString(6, queryStatus.getCorrespondingStatus().toString());
      getProjectsOfTeam.setBoolean(7, false);
    }
    ResultSet result = getProjectsOfTeam.executeQuery();
    ArrayList<Project> projectsOfTeam = new ArrayList<>();
    while (result.next()) {
      projectsOfTeam.add(getProjectFromResult(result));
    }
    return projectsOfTeam;
  }

  private int getProjectStatusId(Project.ProjectStatus status) throws SQLException {
    getProjectStatusIdSt.setString(1, status.toString());
    ResultSet result = getProjectStatusIdSt.executeQuery();
    result.next();
    return result.getInt("StatusId");
  }

  private Project getProjectFromResult(ResultSet result) throws SQLException {
    int id = result.getInt("ProjectId");
    String title = result.getString("Name");
    int teamId = result.getInt("TeamId");
    String description = result.getString("Description");
    LocalDate deadline = LocalDate.parse(result.getString("Deadline"));
    int supervisorId = result.getInt("SupervisorId");
    int assigneeId = result.getInt("AssigneeId");
    Project.ProjectStatus status = Project.ProjectStatus.valueOf(result.getString("StatusName"));
    Project project = new Project(id, title, teamId, deadline, status, supervisorId, assigneeId);
    project.setDescription(description);
    return project;
  }
}
