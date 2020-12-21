package model.comment;

import model.InexistentDatabaseEntityException;

import java.time.LocalDateTime;

public class Comment {
    private final int id;
    private final String text;
    private final int senderId;
    private final LocalDateTime dateTime;

    public static class SavableComment extends Comment {

        public SavableComment(String text, int senderId, LocalDateTime dateTime) {
            super(-1, text, senderId, dateTime);
        }

        @Override
        public int getId() throws InexistentDatabaseEntityException {
            throw new InexistentDatabaseEntityException(this);
        }
    }

    public Comment(int id, String text, int senderId, LocalDateTime dateTime) {
        this.id = id;
        this.text = text;
        this.senderId = senderId;
        this.dateTime = dateTime;
    }

    public int getId() throws InexistentDatabaseEntityException {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getSenderId() {
        return senderId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
}
