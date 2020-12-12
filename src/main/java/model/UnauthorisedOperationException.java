package model;

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
