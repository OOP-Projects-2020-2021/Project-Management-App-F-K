package model.user;

public class InvalidSignInException extends Exception {

  @Override
  public String toString() {
    return "Invalid username and password.";
  }
}
