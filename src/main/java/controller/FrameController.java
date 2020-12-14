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
  private static final String DATABASE_FAILURE_MESSAGE = "Database failure";
  private static final String UNABLE_TO_READ_DATABASE_MESSAGE =
          "Database failure! \nUnable to reach the requested data.";

  /**Display an error message in case the data stored in the database could not be accessed. */
  public void displayDatabaseErrorDialog() {
    JOptionPane.showMessageDialog(
            frame,
            DATABASE_FAILURE_MESSAGE,
            UNABLE_TO_READ_DATABASE_MESSAGE,
            JOptionPane.ERROR_MESSAGE);
  }
}
