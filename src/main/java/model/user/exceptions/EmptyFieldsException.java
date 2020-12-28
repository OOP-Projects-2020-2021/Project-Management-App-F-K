package model.user.exceptions;

/**
 * EmptyFieldsException is thrown when a user didn't fill in all the required fields when attempting
 * to perform an operation with user interaction.
 *
 * @author Beata Keresztes
 */
public class EmptyFieldsException extends Exception {
  public EmptyFieldsException() {
    super(
        "The request is rejected, because there are empty fields left.\n"
            + "Please make sure all fields are completed before continuing.");
  }
}
