package controller.project;

import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.queryconstants.QueryProjectDeadlineStatus;
import model.project.queryconstants.QueryProjectStatus;
import model.user.User;
import model.user.UserManager;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import org.jetbrains.annotations.Nullable;
import view.ErrorDialogFactory;
import view.project.ProjectListModel;

import java.sql.SQLException;

/**
 * Controls the panel containing the filters and the table in which the projects are displayed.
 * If the teamId is specified and it is a non-negative integer, then the only the projects related to that team are listed.
 * Otherwise, if the teamId is a negative number, -1, then all the projects corresponding to the current user are listed, independent of the team.
 *
 * @author Beata Keresztes
 */
public class ProjectFilterController {

  private ProjectManager projectManager;
  private UserManager userManager;
  private int teamId;
  private ProjectListModel projectListModel;
  public enum PrivilegeTypes {ALL, ASSIGNED_TO_ME, SUPERVISED_BY_ME}
  private String statusFilter;
  private String privilegeFilter;
  private String turnInTimeFilter;

  public ProjectFilterController(int teamId) {
    this.teamId = teamId;
    projectManager = ProjectManager.getInstance();
    userManager = UserManager.getInstance();
    projectListModel = ProjectListModel.getInstance();
    statusFilter = String.valueOf(QueryProjectStatus.ALL);
    privilegeFilter = String.valueOf(PrivilegeTypes.ALL);
    turnInTimeFilter = String.valueOf(QueryProjectDeadlineStatus.ALL);
    filterProjects();
  }

  public void setStatusFilter(String status) {
    statusFilter = status;
  }
  public void setPrivilegeFilter(String privilege) {
    privilegeFilter = privilege;
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
    String assigneeName = null,supervisorName = null;
    if(privilegeFilter.equals(String.valueOf(PrivilegeTypes.ASSIGNED_TO_ME))) {
      assigneeName = userManager.getCurrentUser().get().getUsername();
    } else if(privilegeFilter.equals(String.valueOf(PrivilegeTypes.SUPERVISED_BY_ME))) {
      supervisorName = userManager.getCurrentUser().get().getUsername();
    }
    try {
      projectListModel.setProjectList(projectManager.getProjectsOfTeam(teamId,supervisorName,assigneeName,QueryProjectStatus.valueOf(statusFilter),QueryProjectDeadlineStatus.valueOf(turnInTimeFilter)));
    }catch(SQLException | InexistentDatabaseEntityException | InexistentUserException e) {
      ErrorDialogFactory.createErrorDialog(e,null,null);
    }
  }
  private void filterProjectsOfUser() {
    boolean assignedToUser = privilegeFilter.equals(String.valueOf(PrivilegeTypes.ASSIGNED_TO_ME));
    boolean supervisedByUser = privilegeFilter.equals(String.valueOf(PrivilegeTypes.SUPERVISED_BY_ME));
    try {
      projectListModel.setProjectList(projectManager.getProjects(assignedToUser,supervisedByUser,QueryProjectStatus.valueOf(statusFilter),QueryProjectDeadlineStatus.valueOf(turnInTimeFilter)));
    }catch(SQLException | InexistentDatabaseEntityException | NoSignedInUserException e) {
      ErrorDialogFactory.createErrorDialog(e,null,null);
    }
  }

  public String[] getProjectStatusTypes() {
    return new String[] {String.valueOf(QueryProjectStatus.ALL),
            String.valueOf(QueryProjectStatus.TO_DO),
            String.valueOf(QueryProjectStatus.IN_PROGRESS),
            String.valueOf(QueryProjectStatus.TURNED_IN),
            String.valueOf(QueryProjectStatus.FINISHED)};
  }
  public String[] getProjectTurnInTimes() {
    return new String[] {String.valueOf(QueryProjectDeadlineStatus.ALL),
            String.valueOf(QueryProjectDeadlineStatus.IN_TIME),
            String.valueOf(QueryProjectDeadlineStatus.OVERDUE)};
  }
  public String[] getProjectPrivilegeTypes() {
    return new String[] {String.valueOf(PrivilegeTypes.ALL),
            String.valueOf(PrivilegeTypes.ASSIGNED_TO_ME),
            String.valueOf(PrivilegeTypes.SUPERVISED_BY_ME)
    };
  }
}
