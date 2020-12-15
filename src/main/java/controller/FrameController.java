package controller;

import javax.swing.*;
import java.awt.event.WindowEvent;

/**
 * FrameController defines a generic contract for all the controllers of frames.
 *
 * @author Bori Fazakas
 */
public abstract class FrameController {
  protected JFrame frame;

  public FrameController(JFrame frame) {
    this.frame = frame;
  }

  protected void closeFrame() {
    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
  }

  /** Messages that inform the user of a database related failure. */
  private static final String DATABASE_ERROR_TITLE = "Database failure";

  private static final String DATABASE_ERROR_MESSAGE =
      "An error occurred during an interaction with the data source.";

  /** Display an error message in case the data stored in the database could not be accessed. */
  public void displayDatabaseErrorDialog() {
    JOptionPane.showMessageDialog(
        frame, DATABASE_ERROR_TITLE, DATABASE_ERROR_MESSAGE, JOptionPane.ERROR_MESSAGE);
  }
}
