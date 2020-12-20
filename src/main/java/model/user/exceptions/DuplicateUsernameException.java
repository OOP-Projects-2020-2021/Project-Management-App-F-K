package model.user.exceptions;

/**
 * DuplicateUsernameException is thrown when a user attempts to sign-in with an already existing username
 * in the database.
 *
 * @author Beata Keresztes
 */
public class DuplicateUsernameException extends Exception {

  public DuplicateUsernameException(String userName) {
    super(
        "This operation is illegal, because there already exists a user with the "
            + userName
            + " username.");
  }
}
