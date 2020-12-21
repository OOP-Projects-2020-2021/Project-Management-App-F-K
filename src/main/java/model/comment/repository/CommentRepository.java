package model.comment.repository;

import model.comment.Comment;

import java.sql.SQLException;
import java.util.List;

public interface CommentRepository {
  void saveComment(Comment.SavableComment comment) throws SQLException;

  List<Comment> getCommentsOfProject(int projectId) throws SQLException;

  void deleteAllCommentsOfProject(int projectId) throws SQLException;
}
