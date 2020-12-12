package model;

/**
 * Exception thrown if the user with @param userId is not authorised to perform @param operation
 * for @param reason.
 *
 * @author Bori Fazakas
 */
public class UnauthorisedOperationException extends Exception {
  public UnauthorisedOperationException(int userId, String operation, String reason) {
    super(
        "The user with id "
            + userId
            + " is not authorised to perform "
            + operation
            + " "
            + "because "
            + reason);
  }
}
