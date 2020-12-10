package controller;

import view.MainFrame;

import javax.swing.*;

public class SignUpController extends FrameController {

  public SignUpController(JFrame signUpFrame) {
    super(signUpFrame);
  }

  /**
   * Forward the data introduced by the user, to create a new account. Close the current frame and
   * open a new main frame instead.
   *
   * @param username the username set by the user
   * @param password the password set by the user
   */
  public void signUp(String username, String password) {
    // TODO!! save data introduced by the user
    new MainFrame();
    closeFrame();
  }

  public void goBack(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
    closeFrame();
  }
}
