package model.comment.repository.impl;

import model.comment.Comment;
import model.comment.repository.CommentRepository;
import model.database.Repository;
import model.team.repository.impl.SqliteTeamRepository;

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

    @Override
    protected void prepareStatements() throws SQLException {

    }

    @Override
    public void saveComment(Comment.SavableComment comment) {

    }

    @Override
    public List<Comment> getCommentsOfProject(int projectId) {
        return null;
    }
}
