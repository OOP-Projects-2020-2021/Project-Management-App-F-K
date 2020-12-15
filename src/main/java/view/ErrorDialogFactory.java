package view;

import model.InexistentDatabaseEntityException;
import model.user.NoSignedInUserException;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

/**
 * @author Beata Keresztes, Bori Fazakas
 */

public class ErrorDialogFactory {
    /** Messages that inform the user of a database related failure. */
    private static final String DATABASE_ERROR_TITLE = "Database failure";
    private static final String DATABASE_ERROR_MESSAGE =
            "An error occurred during an interaction with the data source.";

    /**
     * Messages that inform the user that this functionality is only accessible for signed-in
     * users.
     */
    private static final String SIGNIN_REQUIRED_ERROR_TITLE = "Authentication failure";
    private static final String SIGNIN_REQUIRED_ERROR_MESSAGE =
            "An error occurred. To access this functionality, you must sign in first";

    public static void createErrorDialog(Exception exception, Frame frame, String message) {
        if (message == null) {
            message = "";
        }
        if (exception instanceof SQLException || exception instanceof InexistentDatabaseEntityException) {
            displayDatabaseErrorDialog(frame, message);
        }
        if (exception instanceof NoSignedInUserException) {
            displaySigninRequiredErrorDialog(frame, message);
        }
    }

    /** Display an error message in case the data stored in the database could not be accessed. */
    private static void displayDatabaseErrorDialog(Frame frame, String message) {
        JOptionPane.showMessageDialog(
                frame, DATABASE_ERROR_MESSAGE + "\n" + message, DATABASE_ERROR_TITLE,
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display an error message in case the functionality should have been aceesed by signed in
     * users only.
     */
    private static void displaySigninRequiredErrorDialog(Frame frame, String message) {
        JOptionPane.showMessageDialog(
                frame, SIGNIN_REQUIRED_ERROR_MESSAGE + "\n" + message,
                SIGNIN_REQUIRED_ERROR_TITLE,
                JOptionPane.ERROR_MESSAGE);
    }
}
