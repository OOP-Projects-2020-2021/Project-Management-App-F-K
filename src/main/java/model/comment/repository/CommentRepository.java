package model.comment.repository;

import model.comment.Comment;

import java.sql.SQLException;
import java.util.List;

/**
 * CommentRepository specifies the methods required from any class implementing database access for
 * comment-related insertions, deletions and queries.
 *
 * @author Bori Fazakas
 */
public interface CommentRepository {
  /**
   * Saves a new comment in the database.
   *
   * @param comment is the new comment to save.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void saveComment(Comment.SavableComment comment) throws SQLException;

  /**
   * Returns all the comments attached to a given project.
   *
   * @param projectId is the id of the project whose comments are returned.
   * @return a list of all comments belonging to the given project.
   * @throws SQLException if the operation could not be performed in the database.
   */
  List<Comment> getCommentsOfProject(int








                                             projectId) throws SQLException;

  /**
   * Deletes all the comments which belong to a given project.
   *
   * @param projectId is the id of the project whose comments are returned.
   * @throws SQLException if the operation could not be performed in the database.
   */
  void deleteAllCommentsOfProject(int projectId) throws SQLException;
}
