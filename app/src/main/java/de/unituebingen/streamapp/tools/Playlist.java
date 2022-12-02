package de.unituebingen.streamapp.tools;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

import de.unituebingen.streamapp.tools.wrapper.Wrapper;

public class Playlist {
    @JSONField(name = "id")
    private int id;

    @JSONField(name = "public")
    private int status;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "timestamp")
    private Date timestamp;

    @JSONField(name = "ownerid")
    private int ownerId;

    @JSONField(name = "ownername")
    private String ownerName;

    @JSONField(name = "videos")
    private Wrapper videos;

    @JSONField(name = "followers")
    private Wrapper followers;

    @JSONField(name = "href")
    private String playlistEndpoint;

    @JSONField(name = "subscribed")
    private boolean isSubscribed;

    public Playlist() {}

    public Playlist(String name, int isPublic) {
        this.name = name;
        this.status = isPublic;
    }

    @JSONField(name = "id")
    public int getId() {
        return id;
    }

    @JSONField(name = "id")
    public void setId(int id) {
        this.id = id;
    }

    @JSONField(name = "public")
    public int getStatus() {
        return status;
    }

    @JSONField(name = "public")
    public void setStatus(int status) {
        this.status = status;
    }

    @JSONField(name = "name")
    public String getName() {
        return name;
    }

    @JSONField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name = "timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    @JSONField(name = "timestamp")
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @JSONField(name = "ownerid")
    public int getOwnerId() {
        return ownerId;
    }

    @JSONField(name = "ownerid")
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    @JSONField(name = "ownername")
    public String getOwnerName() {
        return ownerName;
    }

    @JSONField(name = "ownername")
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @JSONField(name = "videos")
    public Wrapper getVideos() {
        return videos;
    }

    @JSONField(name = "videos")
    public void setVideos(Wrapper videos) {
        this.videos = videos;
    }

    @JSONField(name = "followers")
    public Wrapper getFollowers() {
        return followers;
    }

    @JSONField(name = "followers")
    public void setFollowers(Wrapper followers) {
        this.followers = followers;
    }

    @JSONField(name = "href")
    public String getPlaylistEndpoint() {
        return playlistEndpoint;
    }

    @JSONField(name = "href")
    public void setPlaylistEndpoint(String playlistEndpoint) {
        this.playlistEndpoint = playlistEndpoint;
    }

    @JSONField(name = "subscribed")
    public boolean isSubscribed() {
        return isSubscribed;
    }

    @JSONField(name = "subscribed")
    public void setSubscribed(boolean subscribed) {
        isSubscribed = subscribed;
    }

    public PlaylistBody getRequestBody() {
        return new PlaylistBody(this);
    }
}

class PlaylistBody {
    @JSONField(name = "name")
    private String name;

    @JSONField(name = "public")
    private int status;

    public PlaylistBody() {}

    public PlaylistBody(Playlist p) {
        this.name = p.getName();
        this.status = p.getStatus();
    }

    @JSONField(name = "name")
    public String getName() {
        return name;
    }

    @JSONField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name = "status")
    public int getStatus() {
        return status;
    }

    @JSONField(name = "status")
    public void setStatus(int status) {
        this.status = status;
    }
}