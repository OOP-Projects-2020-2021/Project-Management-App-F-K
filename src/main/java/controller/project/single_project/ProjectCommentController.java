package controller.project.single_project;

import model.project.ProjectManager;

/**
 * Manages the Comments panel, displaying and updating the list of comments.
 *
 * @author Beata Keresztes
 */
public class ProjectCommentController {

  private ProjectManager projectManager;

  public ProjectCommentController(int ProjectId) {
    projectManager = ProjectManager.getInstance();
  }
}
