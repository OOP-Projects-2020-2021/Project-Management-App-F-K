package controller.project;

import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.ProjectManager;
import model.user.UserManager;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;
import view.project.ProjectListModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

/**
 * ProjectFilterController controls the ProjectFilterPanel containing the filters applied to the
 * projects that are displayed. It is responsible for updating the ProjectListModel when a user
 * selects a certain combination of filters. If the teamId is specified and it is a non-negative
 * integer, then only the projects related to that team are listed. Otherwise, if the teamId is a
 * negative number, for example -1, then all the projects corresponding to the current user are
 * listed, independent of the team.
 *
 * @author Beata Keresztes
 */
public class ProjectFilterController implements PropertyChangeListener {

  private ProjectManager projectManager;
  private UserManager userManager;
  private int teamId;
  private ProjectListModel projectListModel;

  private boolean assignedToUser;
  private boolean supervisedByUser;

  public ProjectFilterController(int teamId) {
    this.teamId = teamId;
    projectManager = ProjectManager.getInstance();
    projectManager.addPropertyChangeListener(this);
    userManager = UserManager.getInstance();
    projectListModel = ProjectListModel.getInstance();
    assignedToUser = supervisedByUser = true;
    filterProjects();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.UPDATE_PROJECT.toString())
        || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.CREATE_PROJECT.toString())
        || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString())) {
      filterProjects();
    }
  }

  public enum PrivilegeTypes {
    ASSIGNED_TO_ME,
    SUPERVISED_BY_ME
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
              Project.Status.valueOf(statusFilter),
              Project.DeadlineStatus.valueOf(turnInTimeFilter)));
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
              Project.Status.valueOf(statusFilter),
              Project.DeadlineStatus.valueOf(turnInTimeFilter)));
    } catch (SQLException | InexistentDatabaseEntityException | NoSignedInUserException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
  }

  public String[] getProjectStatusTypes() {
    return new String[] {
      String.valueOf(Project.Status.TO_DO),
      String.valueOf(Project.Status.IN_PROGRESS),
      String.valueOf(Project.Status.TURNED_IN),
      String.valueOf(Project.Status.FINISHED)
    };
  }

  public String[] getProjectTurnInTimes() {
    return new String[] {
      String.valueOf(Project.DeadlineStatus.IN_TIME_TO_FINISH),
      String.valueOf(Project.DeadlineStatus.OVERDUE),
      String.valueOf(Project.DeadlineStatus.FINISHED_IN_TIME),
      String.valueOf(Project.DeadlineStatus.FINISHED_LATE)
    };
  }
}
