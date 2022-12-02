package de.unituebingen.streamapp.tools.requestBodies;

import com.alibaba.fastjson.annotation.JSONField;

public class PatchBody {

    @JSONField(name = "op")
    String operation;

    @JSONField(name = "path")
    String path;

    @JSONField(name = "value")
    String value;

    public PatchBody() {}

    public PatchBody(String op, String path, String value) {
        this.operation = op;
        this.path = path;
        this.value = value;
    }


    @JSONField(name = "op")
    public String getOperation() {
        return operation;
    }

    @JSONField(name = "op")
    public void setOperation(String operation) {
        this.operation = operation;
    }

    @JSONField(name = "path")
    public String getPath() {
        return path;
    }

    @JSONField(name = "path")
    public void setPath(String path) {
        this.path = path;
    }

    @JSONField(name = "value")
    public String getValue() {
        return value;
    }

    @JSONField(name = "value")
    public void setValue(String value) {
        this.value = value;
    }
}