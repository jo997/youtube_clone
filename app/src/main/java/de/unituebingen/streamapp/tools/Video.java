package de.unituebingen.streamapp.tools;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;
import java.util.Date;

import de.unituebingen.streamapp.tools.wrapper.Wrapper;

public class Video {
    @JSONField(name = "id")
    private int id;

    @JSONField(name = "title")
    private String title;

    @JSONField(name = "description")
    private String description;

    @JSONField(name = "uploaderid")
    private int uploaderID;

    @JSONField(name = "uploadername")
    private String uploaderName;

    @JSONField(name = "uploaddate")
    private Date uploadDate;

    @JSONField(name = "rating")
    private double rating;

    @JSONField(name ="views")
    private int views;

    @JSONField(name = "maxVideoQuality")
    private int maxVideoQuality;

    @JSONField(name = "href")
    private String link;

    @JSONField(name = "uri")
    private ArrayList<Quality> qualities;

    @JSONField(name = "previews")
    private Wrapper<Thumbnail> previews;

    @JSONField(name = "comments")
    private Wrapper<Comment> comments;

    @JSONField(name = "ratings")
    private Wrapper<Rating> ratings;

    @JSONField(name = "id")
    public int getId() {
        return id;
    }

    @JSONField(name = "id")
    public void setId(int id) {
        this.id = id;
    }

    @JSONField(name = "title")
    public String getTitle() {
        return title;
    }

    @JSONField(name = "title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JSONField(name = "description")
    public String getDescription() {
        return description;
    }

    @JSONField(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JSONField(name = "uploaderid")
    public int getUploaderID() {
        return uploaderID;
    }

    @JSONField(name = "uploaderid")
    public void setUploaderID(int uploaderID) {
        this.uploaderID = uploaderID;
    }

    @JSONField(name = "uploadername")
    public String getUploaderName() {
        return uploaderName;
    }

    @JSONField(name = "uploadername")
    public void setUploaderName(String uploaderName) {
        this.uploaderName = uploaderName;
    }

    @JSONField(name = "uploaddate")
    public Date getUploadDate() {
        return uploadDate;
    }

    @JSONField(name = "uploaddate")
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @JSONField(name = "rating")
    public double getRating() {
        return rating;
    }

    @JSONField(name = "rating")
    public void setRating(double rating) {
        this.rating = rating;
    }

    @JSONField(name = "views")
    public int getViews() {
        return views;
    }

    @JSONField(name = "views")
    public void setViews(int views) {
        this.views = views;
    }

    @JSONField(name = "maxVideoQuality")
    public int getMaxVideoQuality() {
        return maxVideoQuality;
    }

    @JSONField(name = "maxVideoQuality")
    public void setMaxVideoQuality(int maxVideoQuality) {
        this.maxVideoQuality = maxVideoQuality;
    }

    @JSONField(name = "href")
    public String getLink() {
        return link;
    }

    @JSONField(name = "href")
    public void setLink(String link) {
        this.link = link;
    }

    @JSONField(name = "uri")
    public ArrayList<Quality> getQualities() {
        return qualities;
    }

    @JSONField(name = "uri")
    public void setQualities(ArrayList<Quality> qualities) {
        this.qualities = qualities;
    }

    @JSONField(name = "previews")
    public Wrapper<Thumbnail> getPreviews() {
        return previews;
    }

    @JSONField(name = "previews")
    public void setPreviews(Wrapper<Thumbnail> previews) {
        this.previews = previews;
    }

    @JSONField(name = "comments")
    public Wrapper<Comment> getComments() {
        return comments;
    }

    @JSONField(name = "comments")
    public void setComments(Wrapper<Comment> comments) {
        this.comments = comments;
    }

    @JSONField(name = "ratings")
    public Wrapper<Rating> getRatings() {
        return ratings;
    }

    @JSONField(name = "ratings")
    public void setRatings(Wrapper<Rating> ratings) {
        this.ratings = ratings;
    }

    static public class Thumbnail {
        @JSONField(name = "uri")
        private String uri;

        public Thumbnail() {}

        @JSONField(name = "uri")
        public String getUri() {
            return uri;
        }

        @JSONField(name = "uri")
        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    static public class Quality {
        @JSONField(name = "quality")
        private int quality;

        @JSONField(name = "uri")
        private String uri;

        public Quality() {}

        @JSONField(name = "quality")
        public int getQuality() {
            return quality;
        }

        @JSONField(name = "quality")
        public void setQuality(int quality) {
            this.quality = quality;
        }

        @JSONField(name = "uri")
        public String getUri() {
            return uri;
        }

        @JSONField(name = "uri")
        public void setUri(String uri) {
            this.uri = uri;
        }
    }
}
