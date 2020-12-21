package model.comment;

import model.InexistentDatabaseEntityException;

import java.time.LocalDateTime;

/**
 * Represents a Comment belonging to a project and holds its data. Immutable.
 *
 * @author Bori Fazakas
 */
public class Comment {
  private final int id; // the id in the database.
  private final String text;
  private final int projectId; // the id of the project to which it belongs.
  private final int senderId; // the id of the user who sent it.
  private final LocalDateTime dateTime; // the time when it was sent.

  /**
   * This class is used only when the comment instance is created to be saved in the database, but
   * does not have a valid id yet.
   */
  public static class SavableComment extends Comment {

    public SavableComment(String text, int projectId, int senderId, LocalDateTime dateTime) {
      super(-1, text, projectId, senderId, dateTime);
    }

    @Override
    public int getId() throws InexistentDatabaseEntityException {
      throw new InexistentDatabaseEntityException(this);
    }
  }

  public Comment(int id, String text, int projectId, int senderId, LocalDateTime dateTime) {
    this.id = id;
    this.text = text;
    this.projectId = projectId;
    this.senderId = senderId;
    this.dateTime = dateTime;
  }

  public int getId() throws InexistentDatabaseEntityException {
    return id;
  }

  public String getText() {
    return text;
  }

  public int getProjectId() {
    return projectId;
  }

  public int getSenderId() {
    return senderId;
  }

  public LocalDateTime getDateTime() {
    return dateTime;
  }
}
