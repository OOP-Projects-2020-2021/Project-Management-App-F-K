package model.user.exceptions;

/**
 * InexistentUserException is thrown when a user with a name/id is requested who does not exist in
 * the database.
 *
 * @author Bori Fazakas
 */
public class InexistentUserException extends Exception {
  public InexistentUserException(int id) {
    super("This operation is illegal because the requested user does not exist");
  }

  public InexistentUserException(String userName) {
    super(
        "This operation is illegal because the requested user with name "
            + userName
            + " does not exist");
  }
}
