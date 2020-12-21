package model.comment.repository;


import model.comment.Comment;

import java.util.List;

public interface CommentRepository {
    void saveComment(Comment.SavableComment comment);

    List<Comment> getCommentsOfProject(int projectId);
}
