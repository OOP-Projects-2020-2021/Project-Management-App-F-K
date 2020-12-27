package controller.project.single_project;

import controller.FrameController;
import model.InexistentDatabaseEntityException;
import model.project.Project;
import model.project.ProjectManager;
import model.project.exceptions.IllegalProjectStatusChangeException;
import model.project.exceptions.InexistentProjectException;
import view.ErrorDialogFactory;

import javax.swing.*;
import java.sql.SQLException;

public class ProjectStatusController extends ProjectController {

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
}
