package model.user;

public class NoSignedInUserException extends Exception {
  public NoSignedInUserException() {
    super("No user is signed in, this functionality is accessible to signed-in users only");
  }
}
