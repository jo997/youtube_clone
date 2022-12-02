package de.unituebingen.streamapp.tools.requestBodies;

import com.alibaba.fastjson.annotation.JSONField;

public class CategoryBody {
    @JSONField(name = "name")
    String name;

    public CategoryBody() {}

    public CategoryBody(String name) {
        this.name = name;
    }

    @JSONField(name = "name")
    public String getName() {
        return name;
    }

    @JSONField(name = "name")
    public void setName(String name) {
        this.name = name;
    }
}
