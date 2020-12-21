package model.comment.repository.impl;

import model.comment.Comment;
import model.comment.repository.CommentRepository;
import model.database.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SqliteCommentRepository extends Repository implements CommentRepository {
  protected static SqliteCommentRepository instance;

  private SqliteCommentRepository() {}

  public static SqliteCommentRepository getInstance() {
    if (instance == null) {
      instance = new SqliteCommentRepository();
    }
    return instance;
  }

  // Save a new comment.
  private static final String SAVE_COMMENT_STATEMENT =
      "INSERT INTO Comment (CommentText, ProjectId, SenderId, DateTime) VALUES (?, ?, ?, ?)";
  private PreparedStatement saveCommentSt;

  @Override
  protected void prepareStatements() throws SQLException {
    saveCommentSt = c.prepareStatement(SAVE_COMMENT_STATEMENT);
  }

  @Override
  public void saveComment(Comment.SavableComment comment) throws SQLException {
    saveCommentSt.setString(1, comment.getText());
    saveCommentSt.setInt(2, comment.getProjectId());
    saveCommentSt.setInt(3, comment.getSenderId());
    saveCommentSt.setString(4, comment.getDateTime().toString());
    saveCommentSt.execute();
  }

  @Override
  public List<Comment> getCommentsOfProject(int projectId) {
    return null;
  }
}
