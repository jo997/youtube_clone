package de.unituebingen.streamapp.tools.wrapper;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

public class SearchWrapper<T>  {
    @JSONField(name = "total")
    private int amount;

    @JSONField(name = "href")
    private String endpointUrl;

    @JSONField(name = "limit")
    private int limit;

    @JSONField(name = "offset")
    private int offset;

    @JSONField(name = "next")
    private String nextQuery;

    @JSONField(name = "previous")
    private String prevQuery;

    @JSONField(name = "items")
    ArrayList<T> items;

    public SearchWrapper() {}

    @JSONField(name = "total")
    public int getAmount() {
        return amount;
    }

    @JSONField(name = "total")
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @JSONField(name = "href")
    public String getEndpointUrl() {
        return endpointUrl;
    }

    @JSONField(name = "href")
    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    @JSONField(name = "items")
    public ArrayList<T> getItems() {
        return items;
    }

    @JSONField(name = "limit")
    public int getLimit() {
        return limit;
    }

    @JSONField(name = "limit")
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @JSONField(name = "offset")
    public int getOffset() {
        return offset;
    }

    @JSONField(name = "offset")
    public void setOffset(int offset) {
        this.offset = offset;
    }

    @JSONField(name = "next")
    public String getNextQuery() {
        return nextQuery;
    }

    @JSONField(name = "next")
    public void setNextQuery(String nextQuery) {
        this.nextQuery = nextQuery;
    }

    @JSONField(name = "previous")
    public String getPrevQuery() {
        return prevQuery;
    }

    @JSONField(name = "previous")
    public void setPrevQuery(String prevQuery) {
        this.prevQuery = prevQuery;
    }

    @JSONField(name = "items")
    public void setItems(ArrayList<T> items) {
        this.items = items;
    }


}
