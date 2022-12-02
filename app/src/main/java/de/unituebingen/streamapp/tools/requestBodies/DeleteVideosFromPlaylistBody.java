package de.unituebingen.streamapp.tools.requestBodies;

import java.util.ArrayList;
import com.alibaba.fastjson.annotation.JSONField;

public class DeleteVideosFromPlaylistBody {
    @JSONField(name = "videos")
    ArrayList<Integer> videoIds;

    public DeleteVideosFromPlaylistBody() {}

    public DeleteVideosFromPlaylistBody(ArrayList<Integer> videoIds) {
        this.videoIds = videoIds;
    }

    public DeleteVideosFromPlaylistBody(int[] videoIDs) {
        for (int id: videoIDs) {
            this.videoIds.add(id);
        }
    }

    @JSONField(name = "videoIds")
    public ArrayList<Integer> getVideoIds() {
        return videoIds;
    }

    @JSONField(name = "videoIds")
    public void setVideoIds(ArrayList<Integer> videoIds) {
        this.videoIds = videoIds;
    }
}
