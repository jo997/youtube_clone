package de.unituebingen.streamapp.tools;

import com.alibaba.fastjson.annotation.JSONField;

public class Rating {
    @JSONField(name = "id")
    private int id;

    @JSONField(name = "rating")
    private double rating;

    @JSONField(name = "videoid")
    private int videoId;

    @JSONField(name = "userid")
    private int userId;

    @JSONField(name = "username")
    private String username;

    @JSONField(name = "href")
    private String endpoint;

    public Rating() {}

    public Rating(double rating) {
        this.rating = rating;
    }

    @JSONField(name = "id")
    public int getId() {
        return id;
    }

    @JSONField(name = "id")
    public void setId(int id) {
        this.id = id;
    }

    @JSONField(name = "rating")
    public double getRating() {
        return rating;
    }

    @JSONField(name = "rating")
    public void setRating(double rating) {
        this.rating = rating;
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

    public RatingBody getRequestBody() {
        return new RatingBody(this);
    }

}

class RatingBody {
    @JSONField(name = "rating")
    double rating;

    public RatingBody() {}

    public RatingBody(Rating r){
        rating = r.getRating();
    }

    @JSONField(name = "rating")
    public double getRating() {
        return rating;
    }

    @JSONField(name = "rating")
    public void setRating(double rating) {
        this.rating = rating;
    }
}