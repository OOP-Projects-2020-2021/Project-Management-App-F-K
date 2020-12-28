package controller.team.single_team;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.queryconstants.QueryProjectDeadlineStatus;
import model.project.queryconstants.QueryProjectStatus;
import model.team.TeamManager;
import model.team.exceptions.InexistentTeamException;
import model.user.UserManager;
import model.user.exceptions.InexistentUserException;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * The TeamController manages the TeamFrame. It has two static fields, the teamId, which doesn't
 * change while the frame is open, and a flag, which grants access to the manager to modify the
 * team's data. The managerAccess flag gets updated every time the manager of the currently viewed
 * team is changed.
 *
 * @author Beata Keresztes
 */
public class TeamController extends FrameController {

  protected TeamManager teamManager;
  protected UserManager userManager;

  protected static int teamId;
  protected static boolean managerAccess;

  public TeamController(JFrame frame, int teamId) {
    super(frame);
    teamManager = TeamManager.getInstance();
    userManager = UserManager.getInstance();
    TeamController.teamId = teamId;
    setManagerAccess();
  }

  public int getTeamId() {
    return teamId;
  }

  public JFrame getFrame() {
    return frame;
  }

  /**
   * Checks if the current user is the manager of the team and grants access to them to modify the
   * data about the team.
   */
  protected void setManagerAccess() {
    try {
      int currentUserId = UserManager.getInstance().getCurrentUser().get().getId();
      int currentManagerId = teamManager.getTeam(teamId).getManagerId();
      managerAccess = currentUserId == currentManagerId;
    } catch (SQLException | InexistentDatabaseEntityException | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(
          e, frame, "The access to edit this team could not be granted.");
    }
  }

  public boolean getManagerAccess() {
    return managerAccess;
  }

  /**
   * Check whether a member of the team has any unfinished projects.
   *
   * @param member = name of the member after which we inquire
   * @return true if the member has any unfinished projects, otherwise false
   * @throws InexistentUserException if there is no such member
   * @throws SQLException if an error occurred when reading the projects from the database
   * @throws InexistentDatabaseEntityException if the team or project with given id doesn't exist in
   *     the database
   */
  protected boolean hasUnfinishedProjects(String member)
          throws SQLException, InexistentDatabaseEntityException, InexistentUserException {
    ProjectManager projectManager = ProjectManager.getInstance();
    List<Project> projects =
            projectManager.getProjectsOfTeam(
                    teamId, member, member, QueryProjectStatus.ALL, QueryProjectDeadlineStatus.ALL);
    for (Project project : projects) {
      if (project.getStatus() != Project.ProjectStatus.FINISHED) {
        return true;
      }
    }
    return false;
  }
  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }
}
