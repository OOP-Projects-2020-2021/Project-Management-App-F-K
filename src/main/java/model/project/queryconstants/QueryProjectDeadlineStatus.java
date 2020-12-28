package model.project.queryconstants;

/**
 * QueryProjectDeadlineStatus is used for database queries and specifies the required status of the
 * project with respect to its deadline.
 *
 * @author Bori Fazakas
 */
public enum QueryProjectDeadlineStatus {
  IN_TIME_TO_FINISH, // the deadline is later than the current date (or today) and the project is
  // not finished yet
  OVERDUE, // the deadline is earlier than today and the project is not finished yet.
  // turned in.
  FINISHED_IN_TIME, //the project was finished until the deadline.
  FINISHED_LATE, //the project was finished later than the deadline.
}
