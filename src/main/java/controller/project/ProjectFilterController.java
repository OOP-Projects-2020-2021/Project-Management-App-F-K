package controller.project;

import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.ProjectManager;
import model.team.TeamManager;
import model.user.User;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;
import view.project.ProjectFilterPanel;
import view.project.ProjectListModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.List;

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
  private TeamManager teamManager;
  private int teamId;
  private ProjectListModel projectListModel;
  private ProjectFilterPanel panel;

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.UPDATE_PROJECT.toString())
            || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.CREATE_PROJECT.toString())
            || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString())) {
      filterProjects();
    } else if (enableProjectSelectionForTeam()) {
      if (evt.getPropertyName()
              .equals(TeamManager.ChangablePropertyName.ADDED_TEAM_MEMBER.toString())
              || evt.getPropertyName()
              .equals(TeamManager.ChangablePropertyName.REMOVED_TEAM_MEMBER.toString())) {
        panel.updateAssigneeSupervisorFilters();
      }
    }
  }

  public enum PrivilegeTypes {
    ASSIGNED_TO_ME,
    SUPERVISED_BY_ME
  }

  public static final String ANYONE = "Anyone";

  private String statusFilter;
  private String turnInTimeFilter;
  private boolean assignedToUser;
  private boolean supervisedByUser;
  private String assignee;
  private String supervisor;

  public ProjectFilterController(int teamId, ProjectFilterPanel panel) {
    this.teamId = teamId;
    projectManager = ProjectManager.getInstance();
    projectManager.addPropertyChangeListener(this);
    teamManager = TeamManager.getInstance();
    projectListModel = ProjectListModel.getInstance();
    statusFilter = String.valueOf(Project.Status.ALL);
    turnInTimeFilter = String.valueOf(Project.DeadlineStatus.ALL);
    this.panel = panel;
    assignedToUser = supervisedByUser = true;
    assignee = supervisor = null;
    filterProjects();
  }

  public void createNewProject() {}

  public List<User> getTeamMembers() {
    try {
      return teamManager.getMembersOfTeam(teamId);
    } catch (SQLException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
    return null;
  }

  public boolean enableProjectSelectionForTeam() {
    return teamId > 0;
  }

  public void setStatusFilter(String status) {
    statusFilter = status;
  }

  public void setPrivilegeFilter(boolean assignedToUser, boolean supervisedByUser) {
    this.assignedToUser = assignedToUser;
    this.supervisedByUser = supervisedByUser;
  }

  public void setAssigneeFilter(String assignee) {
    if (assignee.equals(ANYONE)) {
      this.assignee = null;
    } else {
      this.assignee = assignee;
    }
  }

  public void setSupervisorFilter(String supervisor) {
    if (supervisor.equals(ANYONE)) {
      this.supervisor = null;
    } else {
      this.supervisor = supervisor;
    }
  }

  public void setTurnInTimeFilter(String turnInTime) {
    turnInTimeFilter = turnInTime;
  }

  public void filterProjects() {
    if (enableProjectSelectionForTeam()) {
      filterProjectsOfTeam();
    } else {
      filterProjectsOfUser();
    }
  }

  private void filterProjectsOfTeam() {
    try {
      projectListModel.setProjectList(
              projectManager.getProjectsOfTeam(
                      teamId,
                      supervisor,
                      assignee,
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
            String.valueOf(Project.Status.ALL),
            String.valueOf(Project.Status.TO_DO),
            String.valueOf(Project.Status.IN_PROGRESS),
            String.valueOf(Project.Status.TURNED_IN),
            String.valueOf(Project.Status.FINISHED)
    };
  }

  public String[] getProjectTurnInTimes() {
    return new String[] {
            String.valueOf(Project.DeadlineStatus.ALL),
            String.valueOf(Project.DeadlineStatus.IN_TIME),
            String.valueOf(Project.DeadlineStatus.OVERDUE)
    };
  }
}