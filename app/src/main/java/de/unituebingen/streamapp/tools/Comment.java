package de.unituebingen.streamapp.tools;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Comment {
    @JSONField(name = "id")
    private int id;

    @JSONField(name = "comment")
    private String comment;

    @JSONField(name = "timestamp")
    private Date timestamp;

    @JSONField(name = "videoid")
    private int videoId;

    @JSONField(name = "userid")
    private int userId;

    @JSONField(name = "username")
    private String username;

    @JSONField(name = "href")
    private String endpoint;

    public Comment() {}

    public Comment(String comment) {
        this.comment = comment;
    }

    @JSONField(name = "id")
    public int getId() {
        return id;
    }

    @JSONField(name = "id")
    public void setId(int id) {
        this.id = id;
    }

    @JSONField(name = "comment")
    public String getComment() {
        return comment;
    }

    @JSONField(name = "comment")
    public void setComment(String comment) {
        this.comment = comment;
    }

    @JSONField(name = "timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    @JSONField(name = "timestamp")
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @JSONField(name = "videoid")
    public int getVideoId() {
        return videoId;
    }

    @JSONField(name = "videoid")
    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    @JSONField(name = "userid")
    public int getUserId() {
        return userId;
    }

    @JSONField(name = "userid")
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @JSONField(name = "username")
    public String getUsername() {
        return username;
    }

    @JSONField(name = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JSONField(name = "href")
    public String getEndpoint() {
        return endpoint;
    }

    @JSONField(name = "href")
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public CommentBody getRequestBody() {
        return new CommentBody(this);
    }
}

class CommentBody {
    @JSONField(name = "comment")
    String text;

    public CommentBody() {}

    public CommentBody(Comment c) {
        text = c.getComment();
    }

    @JSONField(name = "comment")
    public String getText() {
        return text;
    }

    @JSONField(name = "comment")
    public void setText(String text) {
        this.text = text;
    }
}