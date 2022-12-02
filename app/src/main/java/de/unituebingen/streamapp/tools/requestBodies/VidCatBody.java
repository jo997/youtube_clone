package de.unituebingen.streamapp.tools.requestBodies;

import com.alibaba.fastjson.annotation.JSONField;

public class VidCatBody {
    @JSONField(name = "vid")
    private int vid;

    public VidCatBody() {}

    public VidCatBody(int vid) {
        this.vid = vid;
    }

    @JSONField(name = "vid")
    public int getVid() {
        return vid;
    }

    @JSONField(name = "vid")
    public void setVid(int vid) {
        this.vid = vid;
    }
}
