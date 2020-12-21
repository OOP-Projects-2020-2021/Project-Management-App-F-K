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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommentManager extends Manager {
  private static CommentManager instance = new CommentManager();

  private CommentManager() {}

  public static CommentManager getInstance() {
    return instance;
  }

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
  }

  public List<Comment> getOrderedCommentsOfProject(int projectId) throws SQLException {
   List<Comment> comments = commentRepository.getCommentsOfProject(projectId);
   comments.sort(Comparator.comparing(Comment::getDateTime));
   return comments;
  }
}
