package controller.project;

import controller.FrameController;
import model.project.Project;
import model.project.ProjectManager;

import javax.swing.*;

/**
 * Manages the ProjectFrame, displaying and updating the data about a given project.
 *
 * @author Beata Keresztes
 */
public class ProjectController extends FrameController {

    private ProjectManager projectManager;
    private int projectId;
    private Project project;

    public ProjectController(JFrame frame,int projectId) {
        super(frame);
        projectManager = ProjectManager.getInstance();
        this.projectId = projectId;
    }



}
