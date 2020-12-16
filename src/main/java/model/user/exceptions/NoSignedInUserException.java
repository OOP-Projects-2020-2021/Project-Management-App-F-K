package model.user.exceptions;

/**
 * NoSignedInUserException is thrown when someone tres to access a functionality accessible to
 * signed-in users only.
 *
 * @author Bori Fazakas
 */
public class NoSignedInUserException extends Exception {
  public NoSignedInUserException() {
    super("No user is signed in, this functionality is accessible to signed-in users only");
  }
}
