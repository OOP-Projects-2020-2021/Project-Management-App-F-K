package controller.project;

import controller.FrameController;

import javax.swing.*;

public class UserProjectsController extends FrameController {

  public UserProjectsController(JFrame frame) {
    super(frame);
  }

  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
    closeFrame();
  }
}
