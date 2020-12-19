package model.project.queryconstants;

/**
 * QueryProjectDeadlineStatus is used for database queries and specifies the required status of the
 * project with respect to its deadline.
 */
public enum QueryProjectDeadlineStatus {
  ALL, // no specified status.
  IN_TIME, // the deadline is later than the current date, or today.
  OVERDUE // the deadline is earlier than the current date.
}
