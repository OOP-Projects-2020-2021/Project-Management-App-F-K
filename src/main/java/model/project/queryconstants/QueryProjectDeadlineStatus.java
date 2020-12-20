package model.project.queryconstants;

/**
 * QueryProjectDeadlineStatus is used for database queries and specifies the required status of the
 * project with respect to its deadline.
 *
 * @author Bori Fazakas
 */
public enum QueryProjectDeadlineStatus {
  ALL, // no specified status.
  IN_TIME, // the deadline is later than the current date, or today, or the project is already
  // turned in.
  OVERDUE // the deadline is earlier than the current date and the project is not yet turned in.
}
