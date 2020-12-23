package view;

import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.project.exceptions.IllegalProjectStatusChangeException;
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
   * already a member of the requested team.
   */
  private static final String ALREADY_MEMBER_ERROR_TITLE = "Already member";

  private static final String ALREADY_MEMBER_ERROR_MESSAGE =
      "This team membership has already been registered, you can't add it again";

  /**
   * Messages that inform the user that the operation is invalid because the manager can't be
   * removed from the members' list.
   */
  private static final String MANAGER_REMOVAL_ERROR_TITLE = "Removing manager";

  private static final String MANAGER_REMOVAL_ERROR_MESSAGE =
      "The manager cannot be removed from the team.";

  /** Messages to inform the user that the username introduced is already taken. */
  private static final String DUPLICATE_USERNAME_ERROR_TITLE = "Duplicate username!";

  private static final String DUPLICATE_USERNAME_ERROR_MESSAGE =
      "This username already exists, please choose another one!";

  /** Messages to inform the user that the user doesn not exist in the database. */
  private static final String INEXISTENT_USER_ERROR_TITLE = "Inexistent user!";

  private static final String INEXISTENT_USER_ERROR_MESSAGE =
      "No user was found with this username.\nCheck that you introduced the name correctly.";

  /** Messages to inform the user that arguments passed were incorrect. */
  private static final String ILLEGAL_ARGUMENT_ERROR_TITLE = "Illegal argument!";

  private static final String ILLEGAL_ARGUMENT_ERROR_MESSAGE =
      "Check that the arguments passed are correct.";

  /** Messages to inform the user that non-members cannot leave the team. */
  private static final String UNREGISTERED_MEMBER_REMOVAL_ERROR_TITLE =
      "Removing unregistered members!";

  private static final String UNREGISTERED_MEMBER_REMOVAL_ARGUMENT_ERROR_MESSAGE =
      "The user you attempt to remove is not a member of this team.";

  /**
   * Messages to inform the user that a role cannot be assigne to someone not member of the team.
   */
  private static final String UNREGISTERED_MEMBER_ROLE_TITLE =
      "Assigning role to unregistered member!";

  private static final String UNREGISTERED_MEMBER_ROLE_MESSAGE =
      "The user to whom you attempt to assign a role is not a member of this team.";

  /**
   * Messages to inform the user that the state of the project cannot be changed to the specified
   * value.
   */
  private static final String ILLEGAL_STATUS_CHANGE_TITLE = "Invalid status change!";

  private static final String ILLEGAL_STATUS_CHANGE_MESSAGE =
      "The project cannot be updated to the new status.";

  /** Messages to inform the user that he/she doesn't have access to that operation. */
  private static final String UNAUTHORIZED_OPERATION_TITLE = "Unauthorized operation";

  private static final String UNAUTHORIZED_OPERATION_MESSAGE =
      "You do not have access to perform this operation.";

  /** Messages displayed to inform the user that some fields were left blank. */
  private static final String EMPTY_FIELDS_LEFT_TITLE = "Empty fields left";

  private static final String EMPTY_FIELDS_LEFT_MESSAGE =
          "Please fill in all the required information before continuing!";

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
    if (exception instanceof InexistentUserException) {
      displayInexistentUserErrorDialog(frame, message);
    }
    if (exception instanceof ManagerRemovalException) {
      displayManagerRemovalErrorDialog(frame, message);
    }
    if (exception instanceof IllegalArgumentException) {
      displayIllegalArgumentErrorDialog(frame, message);
    }
    if (exception instanceof UnregisteredMemberRemovalException) {
      displayUnregisteredMemberRemovalErrorDialog(frame, message);
    }
    if (exception instanceof UnregisteredMemberRoleException) {
      displayUnregisteredMemberRoleErrorDialog(frame, message);
    }
    if (exception instanceof IllegalProjectStatusChangeException) {
      displayIllegalStateChangeErrorDialog(frame, message);
    }
    if (exception instanceof UnauthorisedOperationException) {
      displayUnauthorizedOperationErrorDialog(frame, message);
    }
    if (exception instanceof EmptyFieldsException) {
      displayEmptyFieldsErrorDialog(frame, message);
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

  /** Displays an error message when user attempts to remove the manager from the team. */
  private static void displayManagerRemovalErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        MANAGER_REMOVAL_ERROR_MESSAGE + "\n" + message,
        MANAGER_REMOVAL_ERROR_TITLE,
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
  /** Displays an error message when the manager tries to add an inexistent user to the team. */
  private static void displayInexistentUserErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        INEXISTENT_USER_ERROR_MESSAGE + "\n" + message,
        INEXISTENT_USER_ERROR_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Displays an error message when the manager tries to pass its position to a user which is not a
   * member of the team.
   */
  private static void displayIllegalArgumentErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        ILLEGAL_ARGUMENT_ERROR_MESSAGE + "\n" + message,
        ILLEGAL_ARGUMENT_ERROR_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Displays an error message when the manager tries to pass its position to a user which is not a
   * member of the team.
   */
  private static void displayUnregisteredMemberRemovalErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        UNREGISTERED_MEMBER_REMOVAL_ARGUMENT_ERROR_MESSAGE + "\n" + message,
        UNREGISTERED_MEMBER_REMOVAL_ERROR_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Displays an error message when the manager tries to pass its position to a user which is not a
   * member of the team.
   */
  private static void displayUnregisteredMemberRoleErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        UNREGISTERED_MEMBER_ROLE_MESSAGE + "\n" + message,
        UNREGISTERED_MEMBER_ROLE_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }
  /**
   * Displays an error message when the user tries to set the project to a state which is not
   * allowed.
   */
  private static void displayIllegalStateChangeErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        ILLEGAL_STATUS_CHANGE_MESSAGE + "\n" + message,
        ILLEGAL_STATUS_CHANGE_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }
  /** Displays an error message to inform the user that he/she has no access to that operation. */
  private static void displayUnauthorizedOperationErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
        frame,
        UNAUTHORIZED_OPERATION_MESSAGE + "\n" + message,
        UNAUTHORIZED_OPERATION_TITLE,
        JOptionPane.ERROR_MESSAGE);
  }
  /** Displays an error message to inform the user that some data fields have not been completed. */
  private static void displayEmptyFieldsErrorDialog(Frame frame, String message) {
    JOptionPane.showMessageDialog(
            frame,
            EMPTY_FIELDS_LEFT_MESSAGE + "\n" + message,
            EMPTY_FIELDS_LEFT_TITLE,
            JOptionPane.ERROR_MESSAGE);
  }
}
