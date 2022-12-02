package de.unituebingen.streamapp.tools;

import com.alibaba.fastjson.annotation.JSONField;
import de.unituebingen.streamapp.tools.wrapper.Wrapper;

public class User {
    @JSONField(name = "id")
    int id;

    @JSONField(name = "typeid")
    int typeId;

    @JSONField(name = "type")
    String type;

    @JSONField(name = "realname")
    String realName;

    @JSONField(name = "username")
    String userName;

    @JSONField(name = "mail")
    String mail;

    @JSONField(name = "videos")
    Wrapper<Video> videos;

    @JSONField(name = "playlists")
    Wrapper<Playlist> playlists;

    @JSONField(name = "href")
    String userendpoint;

    public User(final int id,
                final String type,
                final int typeid,
                final String realName,
                final String userName,
                final String mail,
                final Wrapper<Video> videos,
                final Wrapper<Playlist> playlists,
                final String userendpoint){
        this.id =id;
        this.type = type;
        this.typeId = typeid;
        this.realName = realName;
        this.userendpoint = userName;
        this.mail = mail;
        this.videos = videos;
        this.playlists = playlists;
        this.userendpoint = userendpoint;
    }

    public User() {}

    @JSONField(name = "id")
    public int getId() {
        return id;
    }

    @JSONField(name = "id")
    public void setId(int id) {
        this.id = id;
    }

    @JSONField(name = "typeid")
    public int getTypeId() {
        return typeId;
    }

    @JSONField(name = "typeid")
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    @JSONField(name = "type")
    public String getType() {
        return type;
    }

    @JSONField(name = "type")
    public void setType(String type) {
        this.type = type;
    }

    @JSONField(name = "realname")
    public String getRealName() {
        return realName;
    }

    @JSONField(name = "realname")
    public void setRealName(String realName) {
        this.realName = realName;
    }

    @JSONField(name = "username")
    public String getUserName() {
        return userName;
    }

    @JSONField(name = "username")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    @JSONField(name = "mail")
    public String getMail() {
        return mail;
    }

    @JSONField(name = "mail")
    public void setMail(String mail) {
        this.mail = mail;
    }

    @JSONField(name = "href")
    public String getUserendpoint() {
        return userendpoint;
    }

    @JSONField(name = "href")
    public void setUserendpoint(String userendpoint) {
        this.userendpoint = userendpoint;
    }

    @JSONField(name = "videos")
    public Wrapper<Video> getVideos() {
        return videos;
    }

    @JSONField(name = "videos")
    public void setVideos(Wrapper<Video> videos) {
        this.videos = videos;
    }

    @JSONField(name = "playlists")
    public Wrapper<Playlist> getPlaylists() {
        return playlists;
    }

    @JSONField(name = "playlists")
    public void setPlaylists(Wrapper<Playlist> playlists) {
        this.playlists = playlists;
    }
}
