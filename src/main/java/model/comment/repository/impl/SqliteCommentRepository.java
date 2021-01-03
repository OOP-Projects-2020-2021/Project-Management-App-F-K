package model.comment.repository.impl;

import model.comment.Comment;
import model.comment.repository.CommentRepository;
import model.database.SqliteDatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * SqliteCommentRepository is an implementation of CommentRepository which provides database access
 * to an sqlite database holding comment-related data.
 *
 * @author Bori Fazakas
 */
public class SqliteCommentRepository implements CommentRepository {
  protected static SqliteCommentRepository instance;

  private SqliteCommentRepository() {}

  /** Implemented with the singleton pattern. */
  public static SqliteCommentRepository getInstance() {
    if (instance == null) {
      instance = new SqliteCommentRepository();
    }
    return instance;
  }

  // Save a new comment.
  private static final String SAVE_COMMENT_STATEMENT =
      "INSERT INTO Comment (CommentText, ProjectId, SenderId, DateTime) VALUES (?, ?, ?, ?)";

  // Get comments of a project.
  private static final String GET_COMMENTS_OF_PROJECT_STATEMENT =
      "Select * FROM Comment WHERE projectId = ?";

  // Delete all comments of a project.
  private static final String DELETE_COMMENTS_OF_PROJECT_STATEMENT =
      "DELETE FROM Comment WHERE projectId = ?";

  @Override
  public void saveComment(Comment.SavableComment comment) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
    PreparedStatement saveCommentSt = c.prepareStatement(SAVE_COMMENT_STATEMENT)) {
      saveCommentSt.setString(1, comment.getText());
      saveCommentSt.setInt(2, comment.getProjectId());
      saveCommentSt.setInt(3, comment.getSenderId());
      saveCommentSt.setString(4, comment.getDateTime().toString());
      saveCommentSt.executeUpdate();
    }
  }

  @Override
  public List<Comment> getCommentsOfProject(int projectId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
    PreparedStatement getCommentsOfProjectSt =
            c.prepareStatement(GET_COMMENTS_OF_PROJECT_STATEMENT)) {
      getCommentsOfProjectSt.setInt(1, projectId);
      try (ResultSet result = getCommentsOfProjectSt.executeQuery()) {
        List<Comment> commentsOfProject = new ArrayList<>();
        while (result.next()) {
          commentsOfProject.add(getCommentFromResult(result));
        }
        return commentsOfProject;
      }
    }
  }

  @Override
  public void deleteAllCommentsOfProject(int projectId) throws SQLException {
    try (Connection c = SqliteDatabaseConnectionFactory.getConnection();
    PreparedStatement deleteCommentsOfProjectSt =
            c.prepareStatement(DELETE_COMMENTS_OF_PROJECT_STATEMENT)) {
      deleteCommentsOfProjectSt.setInt(1, projectId);
      deleteCommentsOfProjectSt.executeUpdate();
    }
  }

  private static Comment getCommentFromResult(ResultSet result) throws SQLException {
    int id = result.getInt("CommentId");
    String text = result.getString("CommentText");
    int projectId = result.getInt("ProjectId");
    int senderId = result.getInt("SenderId");
    LocalDateTime dateTime = LocalDateTime.parse(result.getString("DateTime"));
    return new Comment(id, text, projectId, senderId, dateTime);
  }
}
