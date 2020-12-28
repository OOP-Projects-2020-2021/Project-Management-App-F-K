package controller;

import view.project.CreateProjectFrame;
import view.project.UserProjectsFrame;
import view.user.AccountSettingsFrame;
import view.user.SignInFrame;
import view.team.CreateTeamFrame;
import view.team.JoinTeamFrame;

import javax.swing.*;

/**
 * The MainMenuController controls the actions linked to the items in the MainMenu.
 *
 * @author Bori Fazakas
 */
public class MainMenuController extends FrameController {

  /**
   * The logOutFlag is used to notify the windowAdapter whether the main frame is closing because
   * the user logged out or because he exited the application.
   */
  private boolean logOutFlag;

  public MainMenuController(JFrame frame) {
    super(frame);
    logOutFlag = false;
  }

  public void logoutUser() {
    logOutFlag = true;
    new SignInFrame();
    closeFrame();
  }

  public boolean getLogOutFlag() {
    return logOutFlag;
  }

  /**
   * Provides access to the viewing and editing the account settings of the user in the respective
   * frame.
   */
  public void enableUserDataSettings() {
    new AccountSettingsFrame(frame);
    frame.setEnabled(false);
  }

  /** Provides access to the team creating functionality by opening the corresponding frame. */
  public void enableCreatingNewTeam() {
    new CreateTeamFrame(frame);
    frame.setEnabled(false);
  }

  /** Provides access to the team joining functionality by opening the corresponding frame. */
  public void enableJoiningNewTeam() {
    new JoinTeamFrame(frame);
    frame.setEnabled(false);
  }

  /** Provides access to viewing the list of projects in the respective frame. */
  public void enableViewingProjects() {
    new UserProjectsFrame(frame);
    frame.setEnabled(false);
  }

  /** Provides access to creating a new project in a separate frame. */
  public void enableCreatingProject() {
    new CreateProjectFrame(-1,frame);
    frame.setEnabled(false);
  }
}
