package model.project.repository.impl;

import model.project.Project;
import model.project.repository.ProjectRepository;

import java.sql.*;
import java.util.List;

public class SqliteProjectRepository implements ProjectRepository {
  private Connection c;

  // Save a new team.
  private static final String SAVE_PROJECT_STATEMENT =
      //            "INSERT INTO Project (Name, TeamId) VALUES (?, ?)";
      "INSERT INTO Project (Name, TeamId, Description, Deadline, AssigneeId, SupervisorId, "
          + "StatusId) VALUES (?, ?, ?, ?, ?, ?, ?)";
  private PreparedStatement saveProjecSt;

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
    saveProjecSt = c.prepareStatement(SAVE_PROJECT_STATEMENT);
  }

  @Override
  public void saveProject(Project.SavableProject project) throws SQLException {
    saveProjecSt.setString(1, project.getTitle());
    saveProjecSt.setInt(2, project.getTeamId());
    if (project.getDescription().isPresent()) {
      saveProjecSt.setString(3, project.getDescription().get());
    } else {
      saveProjecSt.setNull(3, Types.NVARCHAR);
    }
    saveProjecSt.setString(4, project.getDeadline().toString());
    // todoL issue - assigneeId and supervisorId is notnull in database
    if (project.getAssigneeId().isPresent()) {
      saveProjecSt.setInt(5, project.getAssigneeId().get());
    } else {
      saveProjecSt.setNull(5, Types.INTEGER);
    }
    //        if (project.getSupervisorId().isPresent()) {
    //            saveProjecSt.setInt(6,
    //                    project.getSupervisorId().get());
    //        } else {
    //            saveProjecSt.setNull(6, Types.INTEGER);
    //        }
    saveProjecSt.setInt(5, 1);
    saveProjecSt.setInt(6, 1);
    saveProjecSt.setInt(7, 1);
    saveProjecSt.execute();
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
