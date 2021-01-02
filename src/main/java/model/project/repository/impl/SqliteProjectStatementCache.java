package model.project.repository.impl;

import model.project.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

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
            return descending == that.descending &&
                    sorterType == that.sorterType;
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
    private HashMap<OrderParameters, PreparedStatement> preparedGetProjectsOfTeamStataments;

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
    private HashMap<OrderParameters, PreparedStatement> preparedGetProjectsStatements;

    public PreparedStatement getGetProjectsOfTeamSt(Project.SorterType sorterType, boolean descending) throws SQLException {
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

    public PreparedStatement getGetProjectsSt(Project.SorterType sorterType, boolean descending) throws SQLException {
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
            if (orderParameters.getSorterType() != Project.SorterType.NONE && orderParameters.isDescending()) {
                clause = clause.concat(" DESC");
            }
            return clause;
        } else {
            return "";
        }
    }
}
