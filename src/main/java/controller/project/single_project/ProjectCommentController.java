package controller.project.single_project;

import model.InexistentDatabaseEntityException;
import model.UnauthorisedOperationException;
import model.comment.Comment;
import model.comment.CommentManager;
import model.project.Project;
import model.project.exceptions.InexistentProjectException;
import model.team.exceptions.InexistentTeamException;
import model.user.UserManager;
import model.user.exceptions.NoSignedInUserException;
import view.ErrorDialogFactory;
import view.project.single_project.ProjectCommentPanel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 * The ProjectCommentController manages the ProjectCommentPanel, displaying and updating the list of
 * comments.
 *
 * @author Beata Keresztes
 */
public class ProjectCommentController implements PropertyChangeListener {

  private CommentManager commentManager;
  private UserManager userManager;
  private ProjectCommentPanel panel;
  private int projectId;

  public ProjectCommentController(ProjectCommentPanel panel, Project project) {
    commentManager = CommentManager.getInstance();
    commentManager.addPropertyChangeListener(this);
    userManager = UserManager.getInstance();
    this.panel = panel;
    try {
      this.projectId = project.getId();
    } catch (InexistentDatabaseEntityException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals(CommentManager.ADD_COMMENT)) {
      panel.updateCommentPanel();
    }
  }

  public List<Comment> getComments() {
    try {
      return commentManager.getOrderedCommentsOfProject(projectId);
    } catch (SQLException sqlException) {
      ErrorDialogFactory.createErrorDialog(sqlException, null, null);
    }
    return null;
  }

  public String getSenderName(Comment comment) {
    try {
      return Objects.requireNonNull(userManager.getUserById(comment.getSenderId())).getUsername();
    } catch (SQLException sqlException) {
      ErrorDialogFactory.createErrorDialog(sqlException, null, null);
    }
    return null;
  }

  public void addComment(String text) {
    try {
      commentManager.addComment(text, projectId);
    } catch (NoSignedInUserException
        | InexistentProjectException
        | SQLException
        | InexistentDatabaseEntityException
        | InexistentTeamException e) {
      ErrorDialogFactory.createErrorDialog(e, null, null);
    } catch (UnauthorisedOperationException e) {
      ErrorDialogFactory.createErrorDialog(
          e, null, "You cannot add a comment, because you are not a member of the team.");
    }
  }
}
