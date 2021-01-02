package model.project.repository.impl;

import model.project.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * SqliteProjectStatementCache works as a cache for more specific queries of
 * SqliteProjectRepository, aimed to improve efficiency of database accesses.
 *
 * <p>It is known that a statement should be prepared only once, and then it will be stored in the
 * server's cache and at further calls only the missing parameters are filled in, thus icreasing
 * speed. However, these parameters can only be values, not column names, order parameters
 * (ASC/DESC), etc. Thus, the queries which have such non-value changing parameters must be prepared
 * separately for all values of the parameters. In order to avoid copying the query multiple times
 * and prepare each version of it even if it is not needed during runtime, this cache stores in a
 * hashmap the statements of a certain kind with a particular configuration of non-value parameters
 * in a hashmap (with the configuration of non-value parameters being the key, stored in an
 * OrderParameters object), and whenever a statement is requested, if it was prepared before, it
 * returns the existing instance from the hashmap, and if it hasn't been prepared yet, it prepares
 * it and saves it in the map. With this method, I can make sure that each query with each
 * configuration is prepared at most once, in a lazy way, i.e it is only prepared if it is needed.
 *
 * @author Bori Fazakas
 */
public class SqliteProjectStatementCache {
  private Connection c;

  private static class OrderParameters {
    private Project.SorterType sorterType;
    private boolean descending;

    OrderParameters(Project.SorterType sorterType, boolean descending) {
      this.sorterType = sorterType;
      this.descending = descending;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      OrderParameters that = (OrderParameters) o;
      return descending == that.descending && sorterType == that.sorterType;
    }

    @Override
    public int hashCode() {
      return Objects.hash(sorterType, descending);
    }

    public Project.SorterType getSorterType() {
      return sorterType;
    }

    public boolean isDescending() {
      return descending;
    }
  }

  SqliteProjectStatementCache(Connection c) {
    this.c = c;
    preparedGetProjectsOfTeamStataments = new HashMap<>();
    preparedGetProjectsStatements = new HashMap<>();
  }

  // Get projects of team, possibly with a given assignee, supervisor, status and status with
  // respect to deadline. The extra wildcards are responsible for making some attributes optional.
  private static final String GET_PROJECTS_OF_TEAM =
      "SELECT ProjectId, p.Name AS Name, p.TeamId AS TeamId, Description, Deadline, "
          + "AssigneeId, SupervisorId, StatusName, TurnInDate, ImportanceName From Project p "
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
          + " ((p.StatusId = 4 AND p.TurnInDate <= p.Deadline) AND ?) OR" // FINISHED_IN_TIME
          + " ((p.StatusId = 4 AND p.TurnInDate > p.Deadline) AND ?)) "; // FINISHED_LATE
  private Map<OrderParameters, PreparedStatement> preparedGetProjectsOfTeamStataments;

  // Get projects possibly with a given assignee, supervisor, status and status with respect to
  // deadline. The extra wildcards are responsible for making some attributes optional.
  private static final String GET_PROJECTS =
      "SELECT ProjectId, p.Name AS Name, p.TeamId AS TeamId, Description, Deadline, "
          + "AssigneeId, SupervisorId, StatusName, TurnInDate, ImportanceName From Project p "
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
          + " ((p.StatusId = 4 AND p.TurnInDate <= p.Deadline) AND ?) OR" // FINISHED_IN_TIME
          + " ((p.StatusId = 4 AND p.TurnInDate > p.Deadline) AND ?))"; // FINISHED_LATE
  private Map<OrderParameters, PreparedStatement> preparedGetProjectsStatements;

  /**
   * Returns a prepared statement for getting the projects of a team with the specified order
   * parameters.
   *
   * @param sorterType specifies the attribute by which the statement will sort the projects.
   * @param descending specifies the order of sorting. If true, descending, otherwise, ascending.
   * @return a prepared statement for connection c.
   * @throws SQLException if the statement could not be prepared.
   */
  public PreparedStatement getGetProjectsOfTeamSt(Project.SorterType sorterType, boolean descending)
      throws SQLException {
    OrderParameters orderParameters = new OrderParameters(sorterType, descending);
    if (preparedGetProjectsOfTeamStataments.containsKey(orderParameters)) {
      return preparedGetProjectsOfTeamStataments.get(orderParameters);
    } else {
      String query = GET_PROJECTS_OF_TEAM.concat(getOrderClause(orderParameters));
      PreparedStatement st = c.prepareStatement(query);
      preparedGetProjectsOfTeamStataments.put(orderParameters, st);
      return preparedGetProjectsOfTeamStataments.get(orderParameters);
    }
  }

  /**
   * Returns a prepared statement for getting any projects with the specified order parameters.
   *
   * @param sorterType specifies the attribute by which the statement will sort the projects.
   * @param descending specifies the order of sorting. If true, descending, otherwise, ascending.
   * @return a prepared statement for connection c.
   * @throws SQLException if the statement could not be prepared.
   */
  public PreparedStatement getGetProjectsSt(Project.SorterType sorterType, boolean descending)
      throws SQLException {
    OrderParameters orderParameters = new OrderParameters(sorterType, descending);
    if (preparedGetProjectsStatements.containsKey(orderParameters)) {
      return preparedGetProjectsStatements.get(orderParameters);
    } else {
      String query = GET_PROJECTS.concat(getOrderClause(orderParameters));
      PreparedStatement st = c.prepareStatement(query);
      preparedGetProjectsStatements.put(orderParameters, st);
      return preparedGetProjectsStatements.get(orderParameters);
    }
  }

  private String getOrderClause(OrderParameters orderParameters) {
    if (orderParameters.getSorterType() != Project.SorterType.NONE) {
      String clause = "ORDER BY ".concat(orderParameters.getSorterType().getColumnName());
      if (orderParameters.getSorterType() != Project.SorterType.NONE
          && orderParameters.isDescending()) {
        clause = clause.concat(" DESC");
      }
      return clause;
    } else {
      return "";
    }
  }
}
