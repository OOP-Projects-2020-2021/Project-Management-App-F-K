package controller.project;

import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.ProjectManager;
import model.team.TeamManager;
import model.user.User;
import model.user.exceptions.InexistentUserException;
import model.user.exceptions.NoSignedInUserException;
import org.jetbrains.annotations.Nullable;
import view.ErrorDialogFactory;
import view.project.ProjectFilterPanel;
import view.project.ProjectListModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.List;

/**
 * ProjectFilterController controls the ProjectFilterPanel containing the filters applied to the
 * projects that are displayed. It is responsible for updating the ProjectListModel when a user
 * selects a certain combination of filters. If the teamId is specified and it is a non-negative
 * integer, then only the projects related to that team are listed. Otherwise, if the teamId is a
 * negative number, for example -1, then all the projects corresponding to the current user are
 * listed, independent of the team.
 *
 * @author Beata Keresztes, Bori Fazakas
 */
public class ProjectFilterController implements PropertyChangeListener {

  private ProjectManager projectManager;
  private TeamManager teamManager;
  private Integer teamId;
  private ProjectListModel projectListModel;
  private ProjectFilterPanel panel;

  public static final String ANYONE = "Anyone";
  public static final String ASC = "ASCENDING";
  public static final String DESC = "DESCENDING";

  public enum PrivilegeTypes {
    ASSIGNED_TO_ME,
    SUPERVISED_BY_ME
  }

  public ProjectFilterController(
      @Nullable Integer teamId, ProjectFilterPanel panel, ProjectListModel projectListModel) {
    this.teamId = teamId;
    projectManager = ProjectManager.getInstance();
    projectManager.addPropertyChangeListener(this);
    teamManager = TeamManager.getInstance();
    teamManager.addPropertyChangeListener(this);
    this.projectListModel = projectListModel;
    this.panel = panel;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.UPDATE_PROJECT.toString())
        || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.CREATE_PROJECT.toString())
        || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.DELETE_PROJECT.toString())
        || evt.getPropertyName()
            .equals(ProjectManager.ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString())) {
      panel.applyFilter();
    } else if (enableProjectSelectionForTeam()) {
      if (evt.getPropertyName()
              .equals(TeamManager.ChangablePropertyName.ADDED_TEAM_MEMBER.toString())
          || evt.getPropertyName()
              .equals(TeamManager.ChangablePropertyName.REMOVED_TEAM_MEMBER.toString())) {
        panel.updateAssigneeSupervisorFilters();
      }
    }
  }

  public List<User> getTeamMembers() {
    try {
      return teamManager.getMembersOfTeam(teamId);
    } catch (SQLException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
    return null;
  }

  public boolean enableProjectSelectionForTeam() {
    return teamId != null;
  }

  public void filterProjectsOfTeam(
      List<Project.Status> selectedStatuses,
      List<Project.DeadlineStatus> selectedDeadlineStatuses,
      String assigneeName,
      String supervisorName,
      Project.SorterType sorterType,
      boolean descending) {
    assigneeName = convertAnyoneStringToNull(assigneeName);
    supervisorName = convertAnyoneStringToNull(supervisorName);
    try {
      projectListModel.setProjectList(
          projectManager.getProjectsOfTeam(
              teamId,
              supervisorName,
              assigneeName,
              EnumSet.copyOf(selectedStatuses),
              EnumSet.copyOf(selectedDeadlineStatuses),
              sorterType,
              descending));
    } catch (SQLException | InexistentDatabaseEntityException | InexistentUserException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
  }

  private String convertAnyoneStringToNull(String s) {
    if (s.equals(ANYONE)) {
      return null;
    }
    return s;
  }

  public void filterProjectsOfUser(
      List<Project.Status> selectedStatuses,
      List<Project.DeadlineStatus> selectedDeadlineStatuses,
      boolean assignedToUser,
      boolean supervisedByUser,
      Project.SorterType sorterType,
      boolean descending) {
    try {
      projectListModel.setProjectList(
          projectManager.getProjects(
              assignedToUser,
              supervisedByUser,
              EnumSet.copyOf(selectedStatuses),
              EnumSet.copyOf(selectedDeadlineStatuses),
              sorterType,
              descending));
    } catch (SQLException | InexistentDatabaseEntityException | NoSignedInUserException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
  }
}
