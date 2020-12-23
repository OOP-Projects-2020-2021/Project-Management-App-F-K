package controller.project;

import controller.FrameController;

import javax.swing.*;

/**
 * The UserProjectsController manages the UserProjectsFrame, and it is responsible for redirecting the user to the
 * MainFrame upon closing the UserProjectsFrame.
 *
 * @author Beata Keresztes
 */
public class UserProjectsController extends FrameController {

  public UserProjectsController(JFrame frame) {
    super(frame);
  }

  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }
}
