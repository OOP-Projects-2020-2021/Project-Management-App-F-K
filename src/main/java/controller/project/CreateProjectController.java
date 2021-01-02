package controller.project;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.exceptions.DuplicateProjectNameException;
import model.project.exceptions.InvalidDeadlineException;
import model.team.Team;
import model.team.TeamManager;
import model.team.exceptions.InexistentTeamException;
import model.user.User;
import model.user.exceptions.EmptyFieldsException;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import org.jetbrains.annotations.Nullable;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * CreateProjectController manages the CreateProjectFrame, and it is responsible for updating the
 * corresponding frame when a change in the user input occurs, to interact with the user. When the
 * user selects a specific team, the assignee list will be updated containing only the members of
 * the newly selected team. This way, the possibility of entering an invalid assignee name is
 * eliminated.
 *
 * @author Beata Keresztes
 */
public class CreateProjectController extends FrameController {

  private TeamManager teamManager;
  private ProjectManager projectManager;
  private Integer teamId;
  /** Messages to inform the user that the project was saved successfully. */
  private static final String PROJECT_SAVED_TITLE = "Project saved";

  private static final String PROJECT_SAVED_MESSAGE = "The project was successfully saved.";

  public CreateProjectController(@Nullable Integer teamId, JFrame frame) {
    super(frame);
    this.teamId = teamId;
    teamManager = TeamManager.getInstance();
    projectManager = ProjectManager.getInstance();
  }

  public void onClose(JFrame parentFrame) {
    parentFrame.setVisible(true);
    parentFrame.setEnabled(true);
  }

  public Integer getTeamId() {
    return teamId;
  }
  /**
   * Enables the user to select the team of the new project from the list of teams he/she is a
   * member of.
   *
   * @return true if the id of a team is not specified
   */
  public boolean enableTeamSelection() {
    return teamId == null;
  }

  public List<Team> getTeamsOfUser() {
    try {
      return teamManager.getTeamsOfCurrentUser();
    } catch (SQLException | NoSignedInUserException | InexistentDatabaseEntityException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
    return null;
  }

  public List<User> getTeamMembers() {
    try {
      return teamManager.getMembersOfTeam(teamId);
    } catch (SQLException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
    return null;
  }

  /**
   * Creates a project with the data introduced by the user. It displays a confirmation message if
   * all the data was successfully saved and closes the frame.
   *
   * @param title introduced by the user
   * @param deadline selected by the user from the DatePicker
   * @param assignee selected by the user from the list of members of the current team, it is an
   *     Object type, because in case it would be null, an EmptyFieldsException is thrown when
   *     trying to save the project
   * @param description added by the user
   */
  public void createProject(
      String title,
      Object assignee,
      LocalDate deadline,
      String description,
      Project.Importance importance) {
    try {
      projectManager.createProject(
          title, teamId, assignee.toString(), deadline, description, importance);
      displaySavedMessageDialog();
      closeFrame();
    } catch (NoSignedInUserException | SQLException | InexistentDatabaseEntityException | InexistentUserException | InexistentTeamException | EmptyFieldsException | InvalidDeadlineException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, null);
    } catch (DuplicateProjectNameException e) {
      ErrorDialogFactory.createErrorDialog(
          e, frame, "The project with title \"" + title + "\" already exists in this team.");
    }
  }

  private void displaySavedMessageDialog() {
    JOptionPane.showMessageDialog(
        frame, PROJECT_SAVED_MESSAGE, PROJECT_SAVED_TITLE, JOptionPane.INFORMATION_MESSAGE);
  }
}
