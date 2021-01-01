package model.project.exceptions;

/**
 * InvalidDeadlineException is thrown when the user attempts to set the deadline or the
 * turn in date of a project to a date before the current date, which means it is already outdated.
 *
 * @author Beata Keresztes
 */
public class InvalidDeadlineException extends Exception{
    public InvalidDeadlineException() {
        super(
                "The selected deadline is invalid, because it is before the current date.");
    }
}
