package controller;

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

  public MainMenuController(JFrame frame) {
    super(frame);
  }

  public void logoutUser() {
    new SignInFrame();
    closeFrame();
  }

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
}
