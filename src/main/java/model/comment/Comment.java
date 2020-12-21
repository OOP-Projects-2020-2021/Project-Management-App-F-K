package model.comment;

import java.time.LocalDateTime;

public class Comment {
    private final int id;
    private final String text;
    private final int senderId;
    private final LocalDateTime dateTime;

    public Comment(int id, String text, int senderId, LocalDateTime dateTime) {
        this.id = id;
        this.text = text;
        this.senderId = senderId;
        this.dateTime = dateTime;
    }

    public int getId() {
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
