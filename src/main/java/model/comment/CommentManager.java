package model.comment;

import model.InexistentDatabaseEntityException;
import model.Manager;
import model.UnauthorisedOperationException;
import model.project.Project;
import model.project.exceptions.InexistentProjectException;
import model.team.Team;
import model.team.exceptions.InexistentTeamException;
import model.user.User;
import model.user.exceptions.NoSignedInUserException;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * CommentManager is responsible for executing all the commands needed for the application that are
 * related to comments.
 *
 * <p>Remark that it is implemented with the singleton pattern, so only one instance of it exists.
 *
 * @author Bori Fazakas
 */
public class CommentManager extends Manager {
  private static CommentManager instance = new CommentManager();

  private CommentManager() {}

  public static CommentManager getInstance() {
    return instance;
  }

  public static final String ADD_COMMENT = "Add comment";

  /**
   * Adds a new comment to project with id projectId, from the current user, with the content text.
   *
   * @param text is the content of the new comment.
   * @param projectId is the id of the project to which it will belong.
   * @throws NoSignedInUserException if there is noone signed in.
   * @throws InexistentProjectException if there is no project with id projectId.
   * @throws SQLException if the operation could not be performed in the database.
   * @throws InexistentDatabaseEntityException should never occur.
   * @throws InexistentTeamException if the team wo which the project belongs is not found in the
   *     database.
   * @throws UnauthorisedOperationException if the current user is not the member of the team to
   *     which the project with projectId belongs.
   */
  public void addComment(String text, int projectId)
      throws NoSignedInUserException, InexistentProjectException, SQLException,
          InexistentDatabaseEntityException, InexistentTeamException,
          UnauthorisedOperationException {
    User currentUser = getMandatoryCurrentUser();
    Project project = getMandatoryProject(projectId);
    Team team = getMandatoryTeam(project.getTeamId());
    if (!teamRepository.isMemberOfTeam(team.getId(), currentUser.getId())) {
      throw new UnauthorisedOperationException(
          currentUser.getId(), "add comment", "they " + "are not member of the team");
    }
    Comment.SavableComment comment =
        new Comment.SavableComment(text, projectId, currentUser.getId(), LocalDateTime.now());
    commentRepository.saveComment(comment);
    support.firePropertyChange(ADD_COMMENT,OLD_VALUE,NEW_VALUE);
  }

  /**
   * Finds and returns from the database a list of all comments belonging to a a project.
   *
   * @param projectId is the id of the project whose comments are listed.
   * @return a list of all comments belonging to a a project.
   * @throws SQLException if the operation could not be performed in the database.
   */
  public List<Comment> getOrderedCommentsOfProject(int projectId) throws SQLException {
    List<Comment> comments = commentRepository.getCommentsOfProject(projectId);
    comments.sort(Comparator.comparing(Comment::getDateTime));
    return comments;
  }

}
