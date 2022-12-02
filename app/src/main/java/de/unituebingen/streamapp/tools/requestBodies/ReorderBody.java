package de.unituebingen.streamapp.tools.requestBodies;

import com.alibaba.fastjson.annotation.JSONField;

public class ReorderBody {
    @JSONField(name = "range_start")
    int rangeStart;

    @JSONField(name = "range_length")
    int rangeLength;

    @JSONField(name = "insert_before")
    int insertBefore;

    public ReorderBody() {}

    public ReorderBody(final int rangeStart, final int rangeLength, final int insertBefore) {
        this.rangeStart = rangeStart;
        this.rangeLength = rangeLength;
        this.insertBefore = insertBefore;
    }

    @JSONField(name = "rangeStart")
    public int getRangeStart() {
        return rangeStart;
    }

    @JSONField(name = "rangeStart")
    public void setRangeStart(int rangeStart) {
        this.rangeStart = rangeStart;
    }

    @JSONField(name = "rangeLength")
    public int getRangeLength() {
        return rangeLength;
    }

    @JSONField(name = "rangeLength")
    public void setRangeLength(int rangeLength) {
        this.rangeLength = rangeLength;
    }

    @JSONField(name = "insertBefore")
    public int getInsertBefore() {
        return insertBefore;
    }

    @JSONField(name = "insertBefore")
    public void setInsertBefore(int insertBefore) {
        this.insertBefore = insertBefore;
    }
}
