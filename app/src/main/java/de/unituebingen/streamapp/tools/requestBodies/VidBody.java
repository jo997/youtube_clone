package de.unituebingen.streamapp.tools.requestBodies;

import com.alibaba.fastjson.annotation.JSONField;

public class VidBody {
    @JSONField(name = "id")
    int id;

    public VidBody() {}

    public VidBody(int id) {
        this.id = id;
    }

    @JSONField(name = "id")
    public int getId() {
        return id;
    }

    @JSONField(name = "id")
    public void setId(int id) {
        this.id = id;
    }
}
