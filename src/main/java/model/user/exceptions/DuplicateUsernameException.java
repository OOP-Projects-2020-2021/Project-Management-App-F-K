package model.user.exceptions;

public class DuplicateUsernameException extends Exception {

  public DuplicateUsernameException(String userName) {
    super(
        "This operation is illegal, because there already exists a user with the "
            + userName
            + " username.");
  }
}
