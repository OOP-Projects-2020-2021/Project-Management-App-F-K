package view;

import model.InexistentDatabaseEntityException;
import model.team.exceptions.*;
import model.user.exceptions.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * Utility class which handles frequent exception types on the front-end, by displaying a
 * corresponding dialog message to the user.
 *
 * @author Beata Keresztes, Bori Fazakas
 */
public class ErrorDialogFactory {
  /** Messages that inform the user of a database related failure. */
  private static final String DATABASE_ERROR_TITLE = "Database failure";

  private static final String DATABASE_ERROR_MESSAGE =
      "An error occurred during an interaction with the data source.";

  /**
   * Messages that inform the user that this functionality is only accessible for signed-in users.
   */
  private static final String SIGNIN_REQUIRED_ERROR_TITLE = "Authentication failure";

  private static final String SIGNIN_REQUIRED_ERROR_MESSAGE =
      "An error occurred. To access this functionality, you must sign in first";

  /** Messages that inform the user that the requested team does not exist. */
  private static final String REQUESTED_TEAM_NOT_EXISTING_TITLE = "Team not found";

  private static final String REQUESTED_TEAM_NOT_EXISTING_MESSAGE =
      "An error occurred. The team that you requested was not found.";

  /**
   * Messages that inform the user that the operation is invalid because the requested user is
   * already a member of the requested team*.
   */
  private static final String ALREADY_MEMBER_ERROR_TITLE = "Already member";

  private static final String ALREADY_MEMBER_ERROR_MESSAGE =
      "This team membership has already been registered, you can't add it again";

  /** Messages to inform the user that the username introduced is already taken. */
  private static final String DUPLICATE_USERNAME_ERROR_TITLE = "Duplicate username!";

  private static final String DUPLICATE_USERNAME_ERROR_MESSAGE =
      "This username already exists, please choose another one!";

  public static void createErrorDialog(Exception exception, Frame frame, String message) {
    if (message == null) {
      message = "";
    }
    if (exception instanceof SQLException
        || exception instanceof InexistentDatabaseEntityException) {
      displayDatabaseErrorDialog(frame, message);
    }
    if (exception instanceof NoSignedInUserException) {
      displaySigninRequiredErrorDialog(frame, message);
    }
    if (exception instanceof InexistentTeamException) {
      displayInexistentTeamErrorDialog(frame, message);
    }
    if (exception instanceof AlreadyMemberException) {
      displayAlreadyMemberErrorDialog(frame, message);
    }
    if (exception instanceof DuplicateUsernameException) {
      displayDuplicateUsernameErrorDialog(frame, message);
    }
  }

  /** Display an error message in case the data stored in the database could not be accessed. */
  private static void displayDatabaseErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        DATABASE_ERROR_MESSAGE + "\n" + message,
        DATABASE_ERROR_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Display an error message in case the functionality should have been aceesed by signed in users
   * only.
   */
  private static void displaySigninRequiredErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        SIGNIN_REQUIRED_ERROR_MESSAGE + "\n" + message,
        SIGNIN_REQUIRED_ERROR_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Display an error message in case the functionality should have been accessed by signed in users
   * only.
   */
  private static void displayInexistentTeamErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        REQUESTED_TEAM_NOT_EXISTING_MESSAGE + "\n" + message,
        REQUESTED_TEAM_NOT_EXISTING_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }

  /** Display an error message in case the user to join the team was already a member. */
  private static void displayAlreadyMemberErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        ALREADY_MEMBER_ERROR_MESSAGE + "\n" + message,
        ALREADY_MEMBER_ERROR_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Displays an error message when the user attempts to sign-up with an already existing username.
   */
  private static void displayDuplicateUsernameErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        DUPLICATE_USERNAME_ERROR_MESSAGE + "\n" + message,
        DUPLICATE_USERNAME_ERROR_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }
}
