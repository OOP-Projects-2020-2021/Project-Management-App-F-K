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

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;

public class ProjectStatusController extends ProjectController implements PropertyChangeListener {

    ProjectManager projectManager = ProjectManager.getInstance();

    public ProjectStatusController(JFrame frame, Project project) {
        super(frame, project);
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

    public void undoTurnIn(Project.ProjectStatus newStatus) {
        try {
            projectManager.undoTurnIn(getProject().getId(), newStatus);
        } catch (SQLException | InexistentDatabaseEntityException | InexistentProjectException e) {
            Exception exception = new SQLException();
            ErrorDialogFactory.createErrorDialog(exception, frame, null);
        } catch (IllegalProjectStatusChangeException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, "You can't mark a project with status " + getProject().getStatus().toString() + " as 'In Progress'");
        } catch (NoSignedInUserException | UnauthorisedOperationException e) {
            ErrorDialogFactory.createErrorDialog(e, frame, null);
        }
    }

    public void review(Project.ProjectStatus newStatus) {
        try {
            if (newStatus == Project.ProjectStatus.FINISHED) {
                projectManager.acceptAsFinished(getProject().getId());
            } else {
                projectManager.discardTurnIn(getProject().getId(), newStatus);
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
            // todo update buttons
        }
    }
}
