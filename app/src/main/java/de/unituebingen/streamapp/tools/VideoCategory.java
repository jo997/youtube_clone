package de.unituebingen.streamapp.tools;

import com.alibaba.fastjson.annotation.JSONField;
import de.unituebingen.streamapp.tools.wrapper.Wrapper;

public class VideoCategory {
    @JSONField(name = "id")
    private int id;

    @JSONField(name = "name")
    private String name;

    @JSONField(name = "videos")
    private Wrapper<Video> videos;

    public VideoCategory(final int id, final String name){
        this.id = id;
        this.name = name;
    }

    public VideoCategory() { }

    @JSONField(name = "id")
    public int getId() {
        return id;
    }

    @JSONField(name = "id")
    public void setId(int id) {
        this.id = id;
    }

    @JSONField(name = "name")
    public String getName() {
        return name;
    }

    @JSONField(name = "name")
    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name = "videos")
    public Wrapper<Video> getVideos() {
        return videos;
    }

    @JSONField(name = "videos")
    public void setVideos(Wrapper<Video> videos) {
        this.videos = videos;
    }
}
