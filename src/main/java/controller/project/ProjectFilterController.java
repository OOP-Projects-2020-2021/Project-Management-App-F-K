package controller.project;

import model.InexistentDatabaseEntityException;
import model.project.ProjectManager;
import model.project.queryconstants.QueryProjectDeadlineStatus;
import model.project.queryconstants.QueryProjectStatus;
import model.user.UserManager;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;
import view.project.ProjectListModel;

import java.sql.SQLException;

/**
 * ProjectFilterController controls the ProjectFilterPanel containing the filters applied to the projects that are displayed.
 * It is responsible for updating the ProjectListModel when a user selects a certain combination of filters.
 * If the teamId is specified and it is a non-negative integer, then only the projects related to that team are listed.
 * Otherwise, if the teamId is a negative number, for example -1, then all the projects corresponding to the current user are listed,
 * independent of the team.
 *
 * @author Beata Keresztes
 */
public class ProjectFilterController {

  private ProjectManager projectManager;
  private UserManager userManager;
  private int teamId;
  private ProjectListModel projectListModel;

  public enum PrivilegeTypes {
    ASSIGNED_TO_ME,
    SUPERVISED_BY_ME
  }

  private String statusFilter;
  private String turnInTimeFilter;
  private boolean assignedToUser;
  private boolean supervisedByUser;

  public ProjectFilterController(int teamId) {
    this.teamId = teamId;
    projectManager = ProjectManager.getInstance();
    userManager = UserManager.getInstance();
    projectListModel = ProjectListModel.getInstance();
    statusFilter = String.valueOf(QueryProjectStatus.ALL);
    turnInTimeFilter = String.valueOf(QueryProjectDeadlineStatus.ALL);
    assignedToUser = supervisedByUser = true;
    filterProjects();
  }

  public void setStatusFilter(String status) {
    statusFilter = status;
  }

  public void setPrivilegeFilter(boolean assignedToUser, boolean supervisedByUser) {
    this.assignedToUser = assignedToUser;
    this.supervisedByUser = supervisedByUser;
  }

  public void setTurnInTimeFilter(String turnInTime) {
    turnInTimeFilter = turnInTime;
  }

  public void filterProjects() {
    if (teamId > 0) {
      filterProjectsOfTeam();
    } else {
      filterProjectsOfUser();
    }
  }

  private void filterProjectsOfTeam() {
    String assigneeName = null, supervisorName = null;
    if (assignedToUser) {
      assigneeName = userManager.getCurrentUser().get().getUsername();
    } else if (supervisedByUser) {
      supervisorName = userManager.getCurrentUser().get().getUsername();
    }
    try {
      projectListModel.setProjectList(
          projectManager.getProjectsOfTeam(
              teamId,
              supervisorName,
              assigneeName,
              QueryProjectStatus.valueOf(statusFilter),
              QueryProjectDeadlineStatus.valueOf(turnInTimeFilter)));
    } catch (SQLException | InexistentDatabaseEntityException | InexistentUserException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
  }

  private void filterProjectsOfUser() {
    try {
      projectListModel.setProjectList(
          projectManager.getProjects(
              assignedToUser,
              supervisedByUser,
              QueryProjectStatus.valueOf(statusFilter),
              QueryProjectDeadlineStatus.valueOf(turnInTimeFilter)));
    } catch (SQLException | InexistentDatabaseEntityException | NoSignedInUserException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
  }

  public String[] getProjectStatusTypes() {
    return new String[] {
      String.valueOf(QueryProjectStatus.ALL),
      String.valueOf(QueryProjectStatus.TO_DO),
      String.valueOf(QueryProjectStatus.IN_PROGRESS),
      String.valueOf(QueryProjectStatus.TURNED_IN),
      String.valueOf(QueryProjectStatus.FINISHED)
    };
  }

  public String[] getProjectTurnInTimes() {
    return new String[] {
      String.valueOf(QueryProjectDeadlineStatus.ALL),
      String.valueOf(QueryProjectDeadlineStatus.IN_TIME),
      String.valueOf(QueryProjectDeadlineStatus.OVERDUE)
    };
  }
}
