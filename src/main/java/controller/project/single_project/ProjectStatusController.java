package controller.project.single_project;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.exceptions.IllegalProjectStatusChangeException;
import model.project.exceptions.InexistentProjectException;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;
import view.UIFactory;
import view.project.single_project.ProjectStatusButtonsPanel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

import static model.project.Project.ProjectStatus.*;

public class ProjectStatusController extends ProjectController implements PropertyChangeListener {

    ProjectStatusButtonsPanel panel;

    public ProjectStatusController(JFrame frame, Project project, ProjectStatusButtonsPanel panel) {
        super(frame, project);
        projectManager.addPropertyChangeListener(this);
        this.panel = panel;
    }

    public void markProgress() {
        try {
            projectManager.setProjectInProgress(getProject().getId());
        } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
            Exception exception = new SQLException();
            ErrorDialogFactory.createErrorDialog(exception, frame, null);
        } catch (IllegalProjectStatusChangeException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, "You can't mark a project with status " + getProject().getStatus().toString() + " as 'In Progress'");
        }
    }

    public void setBackToToDo() {
        try {
            projectManager.setProjectAsToDo(getProject().getId());
        } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
            Exception exception = new SQLException();
            ErrorDialogFactory.createErrorDialog(exception, frame, null);
        } catch (IllegalProjectStatusChangeException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, "You can't mark a project with status " + getProject().getStatus().toString() + " as 'In Progress'");
        } catch (NoSignedInUserException | UnauthorisedOperationException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, null);
        }
    }

    public void turnIn() {
        try {
            projectManager.turnInProject(getProject().getId());
        } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
            Exception exception = new SQLException();
            ErrorDialogFactory.createErrorDialog(exception, frame, null);
        } catch (IllegalProjectStatusChangeException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, "You can't mark a project with status " + getProject().getStatus().toString() + " as 'In Progress'");
        } catch (NoSignedInUserException | UnauthorisedOperationException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, null);
        }
    }

    public void undoTurnIn() {
        try {
            Project.ProjectStatus[] possibleAnswers = new Project.ProjectStatus[] {TO_DO,
                    IN_PROGRESS};
            int answerIndex = JOptionPane.showOptionDialog(frame, "To which status do you " +
                            "want to set back the " +
                    "project after undoing the turn-in", "Undo turn-in",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, possibleAnswers,
                    null);
            projectManager.undoTurnIn(getProject().getId(), possibleAnswers[answerIndex]);
        } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
            Exception exception = new SQLException();
            ErrorDialogFactory.createErrorDialog(exception, frame, null);
        } catch (IllegalProjectStatusChangeException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, "You can't mark a project with status " + getProject().getStatus().toString() + " as 'In Progress'");
        } catch (NoSignedInUserException | UnauthorisedOperationException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, null);
        }
    }

    public void review() {
        try {
            int answer = JOptionPane.showConfirmDialog(frame, "Do you accept this project " +
                            "as " +
                            "finished?",
                    "Review project", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                projectManager.acceptAsFinished(getProject().getId());
            } else {
                Project.ProjectStatus[] possibleAnswers = new Project.ProjectStatus[] {TO_DO,
                        IN_PROGRESS};
                int answerIndex = JOptionPane.showOptionDialog(frame, "To which status do you " +
                                "want to set back the " +
                                "project after dismissing the turn-in", "Dismiss turn-in",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, possibleAnswers,
                        null);
                projectManager.discardTurnIn(getProject().getId(), possibleAnswers[answerIndex]);
            }
        } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
            Exception exception = new SQLException();
            ErrorDialogFactory.createErrorDialog(exception, frame, null);
        } catch (IllegalProjectStatusChangeException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, "You can't mark a project with status " + getProject().getStatus().toString() + " as 'In Progress'");
        } catch (NoSignedInUserException | UnauthorisedOperationException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, null);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (propertyChangeEvent.getPropertyName()
                .equals(ProjectManager.ProjectChangeablePropertyName.SET_PROJECT_STATUS.toString())) {
            setProject();
            panel.updateButtons();
        }
    }

    public ProjectStatusButtonsPanel.Cell1Button getCell1Button() {
        if (project.getStatus() == TO_DO) {
            return ProjectStatusButtonsPanel.Cell1Button.MARK_PROGRESS;
        }
        try {
            if (project.getStatus() == IN_PROGRESS && projectManager.currentUserIsAssignee(project)) {
                return ProjectStatusButtonsPanel.Cell1Button.SET_BACK_TO_TO_DO;
            }
        } catch (InexistentDatabaseEntityException | NoSignedInUserException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, null);
        }
        return ProjectStatusButtonsPanel.Cell1Button.NONE;
    }

    public ProjectStatusButtonsPanel.Cell2Button getCell2Button() {
        try {
            if (projectManager.currentUserIsAssignee(project)) {
                if (project.getStatus() == TO_DO || project.getStatus() == IN_PROGRESS) {
                    return ProjectStatusButtonsPanel.Cell2Button.TURN_IN;
                } else if(project.getStatus() == TURNED_IN) {
                    return ProjectStatusButtonsPanel.Cell2Button.UNDO_TURN_IN;
                } else {
                    return ProjectStatusButtonsPanel.Cell2Button.NONE;
                }
            } else {
                return ProjectStatusButtonsPanel.Cell2Button.NONE;
            }
        } catch (InexistentDatabaseEntityException | NoSignedInUserException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, null);
            return ProjectStatusButtonsPanel.Cell2Button.NONE;
        }
    }

    public ProjectStatusButtonsPanel.Cell3Button getCell3Button() {
        try {
            if (projectManager.currentUserIsSupervisor(project)) {
                if (project.getStatus() == TURNED_IN) {
                    return ProjectStatusButtonsPanel.Cell3Button.REVIEW;
                } else {
                    return ProjectStatusButtonsPanel.Cell3Button.NONE;
                }
            } else {
                return ProjectStatusButtonsPanel.Cell3Button.NONE;
            }
        } catch (InexistentDatabaseEntityException | NoSignedInUserException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, null);
            return ProjectStatusButtonsPanel.Cell3Button.NONE;
        }
    }
}
