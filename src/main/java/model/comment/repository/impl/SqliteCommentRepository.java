package model.comment.repository.impl;

import model.comment.Comment;
import model.comment.repository.CommentRepository;
import model.database.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // Get comments of a project.
    private static final String GET_COMMENTS_OF_PROJECT_STATEMENT =
            "Select * FROM Comment WHERE projectId = ?";
    private PreparedStatement getCommentsOfProjectSt;

    @Override
    protected void prepareStatements() throws SQLException {
        saveCommentSt = c.prepareStatement(SAVE_COMMENT_STATEMENT);
        getCommentsOfProjectSt = c.prepareStatement(GET_COMMENTS_OF_PROJECT_STATEMENT);
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
    public List<Comment> getCommentsOfProject(int projectId) throws SQLException {
        getCommentsOfProjectSt.setInt(1, projectId);
        ResultSet result = getCommentsOfProjectSt.executeQuery();
        List<Comment> commentsOfProject = new ArrayList<>();
        while (result.next()) {
            commentsOfProject.add(getCommentFromResult(result));
        }
        return commentsOfProject;
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
