package controller.project.single_project;

import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.exceptions.IllegalProjectStatusChangeException;
import model.project.exceptions.InexistentProjectException;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;
import view.project.single_project.ProjectStatusButtonsPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.EnumSet;

import static model.project.Project.ProjectStatus.*;

/**
 * ProjectStatusController handles all the events related to the status change of the project,
 * including handling the clicks on any of the related buttons, and updateing the ui if the status
 * of the project has changed.
 *
 * @author Bori Fazakas
 */
public class ProjectStatusController extends ProjectController implements PropertyChangeListener {

  ProjectStatusButtonsPanel panel;

  public ProjectStatusController(JFrame frame, Project project, ProjectStatusButtonsPanel panel) {
    super(frame, project);
    projectManager.addPropertyChangeListener(this);
    this.panel = panel;
  }

  /**
   * If legal, marks the status of the project as IN_PROGRESS, and handles the exceptions if the
   * operation is illegal.
   */
  public void markProgress() {
    try {
      projectManager.setProjectInProgress(getProject().getId());
    } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
      Exception exception = new SQLException();
      ErrorDialogFactory.createErrorDialog(exception, frame, null);
    } catch (IllegalProjectStatusChangeException e) {
      ErrorDialogFactory.createErrorDialog(
          e,
          frame,
          "You can't mark a project with status "
              + getProject().getStatus().toString()
              + " as 'In Progress'");
    }
  }

  /**
   * If legal, marks the status of the project as TO_DO, and handles the exceptions if the operation
   * is illegal.
   */
  public void setBackToToDo() {
    try {
      projectManager.setProjectAsToDo(getProject().getId());
    } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
      Exception exception = new SQLException();
      ErrorDialogFactory.createErrorDialog(exception, frame, null);
    } catch (IllegalProjectStatusChangeException e) {
      ErrorDialogFactory.createErrorDialog(
          e,
          frame,
          "You can't mark a project with status "
              + getProject().getStatus().toString()
              + " as 'To Do'");
    } catch (NoSignedInUserException | UnauthorisedOperationException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, null);
    }
  }

  /**
   * If legal, marks the status of the project as TURNED_IN, and handles the exceptions if the
   * operation is illegal.
   */
  public void turnIn() {
    try {
      projectManager.turnInProject(getProject().getId());
    } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
      Exception exception = new SQLException();
      ErrorDialogFactory.createErrorDialog(exception, frame, null);
    } catch (IllegalProjectStatusChangeException e) {
      ErrorDialogFactory.createErrorDialog(
          e,
          frame,
          "You can't mark a project with status "
              + getProject().getStatus().toString()
              + " as 'Turned In'");
    } catch (NoSignedInUserException | UnauthorisedOperationException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, null);
    }
  }

  /**
   * If legal, marks the status of the previously turned-in project as TO_DO or IN_PROGRESS, based
   * on the option chosen by the user in the dialog, and handles the exceptions if the operation is
   * illegal.
   */
  public void undoTurnIn() {
    try {
      Project.ProjectStatus[] possibleAnswers = new Project.ProjectStatus[] {TO_DO, IN_PROGRESS};
      int answerIndex =
          JOptionPane.showOptionDialog(
              frame,
              "To which status do you "
                  + "want to set back the "
                  + "project after undoing the turn-in",
              "Undo turn-in",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              possibleAnswers,
              null);
      projectManager.undoTurnIn(getProject().getId(), possibleAnswers[answerIndex]);
    } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
      Exception exception = new SQLException();
      ErrorDialogFactory.createErrorDialog(exception, frame, null);
    } catch (IllegalProjectStatusChangeException
        | NoSignedInUserException
        | UnauthorisedOperationException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, null);
    }
  }

  /**
   * If legal, marks the status of the previously turned-in project as FINISHED, if accepted, or
   * TO_DO or IN_PROGRESS, based on the option chosen by the user in the dialog, if not accepted,
   * and handles the exceptions if the operation is illegal.
   */
  public void review() {
    try {
      int answer =
          JOptionPane.showConfirmDialog(
              frame,
              "Do you accept this project " + "as " + "finished?",
              "Review project",
              JOptionPane.YES_NO_OPTION);
      if (answer == JOptionPane.YES_OPTION) {
        projectManager.acceptAsFinished(getProject().getId());
      } else {
        Project.ProjectStatus[] possibleAnswers = new Project.ProjectStatus[] {TO_DO, IN_PROGRESS};
        int answerIndex =
            JOptionPane.showOptionDialog(
                frame,
                "To which status do you "
                    + "want to set back the "
                    + "project after dismissing the turn-in",
                "Dismiss turn-in",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                possibleAnswers,
                null);
        projectManager.discardTurnIn(getProject().getId(), possibleAnswers[answerIndex]);
      }
    } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
      Exception exception = new SQLException();
      ErrorDialogFactory.createErrorDialog(exception, frame, null);
    } catch (IllegalProjectStatusChangeException
        | NoSignedInUserException
        | UnauthorisedOperationException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, null);
    }
  }

  /** Updates the UI if the status of the project has changed. */
  @Override
  public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
    if (propertyChangeEvent
        .getPropertyName()
        .equals(ProjectManager.ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString())) {
      setProject();
      panel.updateButtons();
    }
  }

  /**
   * @return set of the buttons corresponding to the status change actions available to the current
   *     user based on the status of the project and the role of the user.
   */
  public EnumSet<ProjectStatusButtonsPanel.ButtonType> getNecessaryButtonTypes() {
    EnumSet<ProjectStatusButtonsPanel.ButtonType> result =
        EnumSet.noneOf(ProjectStatusButtonsPanel.ButtonType.class);
    try {
      if (projectManager.currentUserIsSupervisor(project)) {
        if (project.getStatus() == TURNED_IN) {
          result.add(ProjectStatusButtonsPanel.ButtonType.REVIEW);
        }
      }
      if (projectManager.currentUserIsAssignee(project)) {
        switch (project.getStatus()) {
          case TO_DO:
            result.add(ProjectStatusButtonsPanel.ButtonType.TURN_IN);
            break;
          case IN_PROGRESS:
            result.add(ProjectStatusButtonsPanel.ButtonType.TURN_IN);
            result.add(ProjectStatusButtonsPanel.ButtonType.SET_BACK_TO_TO_DO);
            break;
          case TURNED_IN:
            result.add(ProjectStatusButtonsPanel.ButtonType.UNDO_TURN_IN);
        }
      }
      if (project.getStatus() == TO_DO) {
        result.add(ProjectStatusButtonsPanel.ButtonType.MARK_PROGRESS);
      }
    } catch (InexistentDatabaseEntityException | NoSignedInUserException e) {
      ErrorDialogFactory.createErrorDialog(e, frame, null);
    }
    return result;
  }
}
