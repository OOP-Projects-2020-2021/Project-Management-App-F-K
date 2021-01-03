package model.project.repository.impl;

import model.InexistentDatabaseEntityException;
import model.database.SqliteDatabaseConnectionFactory;
import model.project.Project;
import model.project.repository.ProjectRepository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

/**
 * SqliteTeamRepository is an implementation of ProjectRepository which provides database access to
 * an sqlite database holding project-related data. Implemented with singleton pattern.
 *
 * @author Bori Fazakas
 */
public class SqliteProjectRepository implements ProjectRepository {
  protected static SqliteProjectRepository instance;

  private SqliteProjectRepository() {}

  /** Implemented with the singleton pattern. */
  public static SqliteProjectRepository getInstance() {
    if (instance == null) {
      instance = new SqliteProjectRepository();
    }
    return instance;
  }

  // Save a new team.
  private static final String SAVE_PROJECT_STATEMENT =
      "INSERT INTO Project (Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, "
          + "StatusId, FinishingDate, ImportanceId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

  // Get project based on id.
  private static final String GET_PROJECT_BY_ID =
      "SELECT ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, "
          + "StatusName, FinishingDate, ImportanceName "
          + "From Project p JOIN ProjectStatus st ON p"
          + ".StatusId = st.StatusId JOIN Importance i ON p.ImportanceId = i.ImportanceId WHERE "
          + "ProjectId = ?";

  // Update project bases on id.
  private static final String UPDATE_PROJECT =
      "UPDATE Project "
          + " SET Name = ?, TeamId = ?, Description = ?, Deadline = ?, AssigneeId = ?, "
          + "SupervisorId = ?, StatusId = ?, FinishingDate = ?, ImportanceId = ?"
          + "Where ProjectId = ?";

  // Get projects based on team and title.
  private static final String GET_PROJECT_BY_TEAM_TITLE_STATEMENT =
      "SELECT ProjectId, Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, "
          + "StatusName, FinishingDate, ImportanceName "
          + "From Project p JOIN ProjectStatus st ON p"
          + ".StatusId = st.StatusId JOIN Importance i ON p.ImportanceId = i.ImportanceId WHERE "
          + "Name = ? and TeamId = ? ";

  // Delete project.
  private static final String DELETE_PROJECT_STATEMENT = "DELETE FROM Project WHERE ProjectId = ?";

  // Get projects of team, possibly with a given assignee, supervisor, status and status with
  // respect to deadline. The extra wildcards are responsible for making some attributes optional.
  private static final String GET_PROJECTS_OF_TEAM =
      "SELECT ProjectId, p.Name AS Name, p.TeamId AS TeamId, Description, Deadline, "
          + "AssigneeId, SupervisorId, StatusName, FinishingDate, ImportanceName From Project p "
          + "JOIN ProjectStatus st ON p.StatusId = st.StatusId JOIN Importance i ON p"
          + ".ImportanceId = i.ImportanceId "
          + "WHERE p.TeamId = ? AND "
          + "(p.SupervisorId = ? OR ?) AND "
          + "(p.AssigneeId = ? OR ?) AND "
          + "((st.StatusName = 'TO_DO' AND ?) OR" // TO_DO allowed
          + " (st.StatusName = 'IN_PROGRESS' AND ?) OR" // IN_PROGRESS allowed
          + " (st.StatusName = 'TURNED_IN' AND ?) OR" // TURNED_IN allowed
          + " (st.StatusName = 'FINISHED' AND ?)) AND " // FINISHED allowed
          + "(((p.Deadline >= date(\"now\") AND p.StatusId <= 3) AND ?) OR " // IN_TIME_TO_FINISH
          + " ((p.Deadline < date(\"now\") AND p.statusId <= 3) AND ?) OR" // OVERDUE
          + " ((p.StatusId = 4 AND p.FinishingDate <= p.Deadline) AND ?) OR" // FINISHED_IN_TIME
          + " ((p.StatusId = 4 AND p.FinishingDate > p.Deadline) AND ?)) "; // FINISHED_LATE

  // Get projects possibly with a given assignee, supervisor, status and status with respect to
  // deadline. The extra wildcards are responsible for making some attributes optional.
  private static final String GET_PROJECTS =
      "SELECT ProjectId, p.Name AS Name, p.TeamId AS TeamId, Description, Deadline, "
          + "AssigneeId, SupervisorId, StatusName, FinishingDate, ImportanceName From Project p "
          + "JOIN ProjectStatus st ON p.StatusId = st.StatusId JOIN Importance i ON p"
          + ".ImportanceId = i.ImportanceId "
          + "WHERE (p.SupervisorId = ? OR ?) AND "
          + "(p.AssigneeId = ? OR ?) AND"
          + "((st.StatusName = 'TO_DO' AND ?) OR" // TO_DO allowed
          + " (st.StatusName = 'IN_PROGRESS' AND ?) OR" // IN_PROGRESS allowed
          + " (st.StatusName = 'TURNED_IN' AND ?) OR" // TURNED_IN allowed
          + " (st.StatusName = 'FINISHED' AND ?)) AND " // FINISHED allowed
          + "(((p.Deadline >= date(\"now\") AND p.StatusId <= 3) AND ?) OR " // IN_TIME_TO_FINISH
          + " ((p.Deadline < date(\"now\") AND p.statusId <= 3) AND ?) OR" // OVERDUE
          + " ((p.StatusId = 4 AND p.FinishingDate <= p.Deadline) AND ?) OR" // FINISHED_IN_TIME
          + " ((p.StatusId = 4 AND p.FinishingDate > p.Deadline) AND ?))"; // FINISHED_LATE

  // Get status id
  private static final String GET_PROJECTS_STATUS_ID =
      "SELECT StatusId from ProjectStatus WHERE StatusName = ?";

  // Get importance id.
  private static final String GET_PROJECTS_IMPORTANCE_ID =
      "SELECT ImportanceId from Importance WHERE ImportanceName = ?";

  @Override
  public int saveProject(Project.SavableProject project)
      throws SQLException, InexistentDatabaseEntityException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement saveProjectSt = c.prepareStatement(SAVE_PROJECT_STATEMENT)) {
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
      if (project.getFinishingDate().isPresent()) {
        saveProjectSt.setString(8, project.getFinishingDate().get().toString());
      } else {
        saveProjectSt.setNull(8, Types.NVARCHAR);
      }
      saveProjectSt.setInt(9, getProjectImportanceId(project.getImportance()));
      saveProjectSt.executeUpdate();
      Optional<Project> savedProjectOp = getProject(project.getTeamId(), project.getTitle());
      if (savedProjectOp.isEmpty()) {
        throw new SQLException("the project could not be saved in the database");
      } else {
        return savedProjectOp.get().getId();
      }
    }
  }

  @Override
  public Optional<Project> getProject(int projectId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getProjectByIdSt = c.prepareStatement(GET_PROJECT_BY_ID)) {
      getProjectByIdSt.setInt(1, projectId);
      try (ResultSet result = getProjectByIdSt.executeQuery()) {
        if (result.next()) {
          return Optional.of(getProjectFromResult(result));
        } else {
          return Optional.empty();
        }
      }
    }
  }

  @Override
  public Optional<Project> getProject(int teamId, String name) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getProjectByTitleTeamSt =
            c.prepareStatement(GET_PROJECT_BY_TEAM_TITLE_STATEMENT)) {
      getProjectByTitleTeamSt.setString(1, name);
      getProjectByTitleTeamSt.setInt(2, teamId);
      try (ResultSet result = getProjectByTitleTeamSt.executeQuery()) {
        if (result.next()) {
          return Optional.of(getProjectFromResult(result));
        } else {
          return Optional.empty();
        }
      }
    }
  }

  @Override
  public void updateProject(Project project)
      throws SQLException, InexistentDatabaseEntityException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement updateProjectSt = c.prepareStatement(UPDATE_PROJECT)) {
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
      if (project.getFinishingDate().isPresent()) {
        updateProjectSt.setString(8, project.getFinishingDate().get().toString());
      } else {
        updateProjectSt.setNull(8, Types.NVARCHAR);
      }
      updateProjectSt.setInt(9, getProjectImportanceId(project.getImportance()));
      updateProjectSt.setInt(10, project.getId());
      updateProjectSt.executeUpdate();
    }
  }

  @Override
  public void deleteProject(int projectId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement deleteProjectSt = c.prepareStatement(DELETE_PROJECT_STATEMENT)) {
      deleteProjectSt.setInt(1, projectId);
      deleteProjectSt.executeUpdate();
    }
  }

  @Override
  public List<Project> getProjectsOfTeam(
      int teamId,
      EnumSet<Project.Status> allowedStatuses,
      Integer assigneeId,
      Integer supervisorId,
      EnumSet<Project.DeadlineStatus> allowedDeadlineStatuses,
      Project.SorterType sorterType,
      boolean descending)
      throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getProjectsOfTeamSt = c.prepareStatement(GET_PROJECTS_OF_TEAM)) {
      getProjectsOfTeamSt.setInt(1, teamId);
      // if supervisorid is null, it is don't care
      if (supervisorId != null) {
        getProjectsOfTeamSt.setInt(2, supervisorId);
        getProjectsOfTeamSt.setBoolean(3, false);
      } else {
        getProjectsOfTeamSt.setNull(2, Types.INTEGER);
        getProjectsOfTeamSt.setBoolean(3, true);
      }
      // if assigneId is null, it is don't care
      if (assigneeId != null) {
        getProjectsOfTeamSt.setInt(4, assigneeId);
        getProjectsOfTeamSt.setBoolean(5, false);
      } else {
        getProjectsOfTeamSt.setNull(4, Types.INTEGER);
        getProjectsOfTeamSt.setBoolean(5, true);
      }
      getProjectsOfTeamSt.setBoolean(6, allowedStatuses.contains(Project.Status.TO_DO));
      getProjectsOfTeamSt.setBoolean(7, allowedStatuses.contains(Project.Status.IN_PROGRESS));
      getProjectsOfTeamSt.setBoolean(8, allowedStatuses.contains(Project.Status.TURNED_IN));
      getProjectsOfTeamSt.setBoolean(9, allowedStatuses.contains(Project.Status.FINISHED));
      getProjectsOfTeamSt.setBoolean(
          10, allowedDeadlineStatuses.contains(Project.DeadlineStatus.IN_TIME_TO_FINISH));
      getProjectsOfTeamSt.setBoolean(
          11, allowedDeadlineStatuses.contains(Project.DeadlineStatus.OVERDUE));
      getProjectsOfTeamSt.setBoolean(
          12, allowedDeadlineStatuses.contains(Project.DeadlineStatus.FINISHED_IN_TIME));
      getProjectsOfTeamSt.setBoolean(
          13, allowedDeadlineStatuses.contains(Project.DeadlineStatus.FINISHED_LATE));
      try (ResultSet result = getProjectsOfTeamSt.executeQuery()) {
        ArrayList<Project> projectsOfTeam = new ArrayList<>();
        while (result.next()) {
          projectsOfTeam.add(getProjectFromResult(result));
        }
        return projectsOfTeam;
      }
    }
  }

  @Override
  public List<Project> getProjects(
      EnumSet<Project.Status> allowedStatuses,
      Integer assigneeId,
      Integer supervisorId,
      EnumSet<Project.DeadlineStatus> allowedDeadlineStatuses,
      Project.SorterType sorterType,
      boolean descending)
      throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getProjectsSt = c.prepareStatement(GET_PROJECTS)) {
      // if supervisorid is null, it is don't care
      if (supervisorId != null) {
        getProjectsSt.setInt(1, supervisorId);
        getProjectsSt.setBoolean(2, false);
      } else {
        getProjectsSt.setNull(1, Types.INTEGER);
        getProjectsSt.setBoolean(2, true);
      }
      // if assigneId is null, it is don't care
      if (assigneeId != null) {
        getProjectsSt.setInt(3, assigneeId);
        getProjectsSt.setBoolean(4, false);
      } else {
        getProjectsSt.setNull(3, Types.INTEGER);
        getProjectsSt.setBoolean(4, true);
      }
      getProjectsSt.setBoolean(5, allowedStatuses.contains(Project.Status.TO_DO));
      getProjectsSt.setBoolean(6, allowedStatuses.contains(Project.Status.IN_PROGRESS));
      getProjectsSt.setBoolean(7, allowedStatuses.contains(Project.Status.TURNED_IN));
      getProjectsSt.setBoolean(8, allowedStatuses.contains(Project.Status.FINISHED));
      getProjectsSt.setBoolean(
          9, allowedDeadlineStatuses.contains(Project.DeadlineStatus.IN_TIME_TO_FINISH));
      getProjectsSt.setBoolean(
          10, allowedDeadlineStatuses.contains(Project.DeadlineStatus.OVERDUE));
      getProjectsSt.setBoolean(
          11, allowedDeadlineStatuses.contains(Project.DeadlineStatus.FINISHED_IN_TIME));
      getProjectsSt.setBoolean(
          12, allowedDeadlineStatuses.contains(Project.DeadlineStatus.FINISHED_LATE));
      try (ResultSet result = getProjectsSt.executeQuery()) {
        ArrayList<Project> projects = new ArrayList<>();
        while (result.next()) {
          projects.add(getProjectFromResult(result));
        }
        return projects;
      }
    }
  }

  private int getProjectStatusId(Project.Status status) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getProjectStatusIdSt = c.prepareStatement(GET_PROJECTS_STATUS_ID)) {
      getProjectStatusIdSt.setString(1, status.toString());
      try (ResultSet result = getProjectStatusIdSt.executeQuery()) {
        result.next();
        return result.getInt("StatusId");
      }
    }
  }

  private int getProjectImportanceId(Project.Importance importance) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
        PreparedStatement getProjectImportanceIdSt =
            c.prepareStatement(GET_PROJECTS_IMPORTANCE_ID)) {
      getProjectImportanceIdSt.setString(1, importance.toString());
      ResultSet result = getProjectImportanceIdSt.executeQuery();
      result.next();
      return result.getInt("ImportanceId");
    }
  }

  private static Project getProjectFromResult(ResultSet result) throws SQLException {
    int id = result.getInt("ProjectId");
    String title = result.getString("Name");
    int teamId = result.getInt("TeamId");
    String description = result.getString("Description");
    LocalDate deadline = LocalDate.parse(result.getString("Deadline"));
    int supervisorId = result.getInt("SupervisorId");
    int assigneeId = result.getInt("AssigneeId");
    LocalDate finishingDate = null;
    if (result.getString("FinishingDate") != null) {
      finishingDate = LocalDate.parse(result.getString("finishingDate"));
    }
    Project.Status status = Project.Status.valueOf(result.getString("StatusName"));
    Project.Importance importance = Project.Importance.valueOf(result.getString("ImportanceName"));
    Project project =
        new Project(
            id,
            title,
            teamId,
            deadline,
            status,
            supervisorId,
            assigneeId,
            finishingDate,
            importance);
    project.setDescription(description);
    return project;
  }

  private String getGetProjectsOfTeamQuery(Project.SorterType sorterType, boolean descending) {
    return GET_PROJECTS_OF_TEAM.concat(getOrderClause(sorterType, descending));
  }

  private String getGetProjectsSt(Project.SorterType sorterType, boolean descending) {
    return GET_PROJECTS.concat(getOrderClause(sorterType, descending));
  }

  private String getOrderClause(Project.SorterType sorterType, boolean descending) {
    if (sorterType != Project.SorterType.NONE) {
      String clause = "ORDER BY ".concat(sorterType.getColumnName());
      if (descending) {
        clause = clause.concat(" DESC");
      }
      return clause;
    } else {
      return "";
    }
  }
}
