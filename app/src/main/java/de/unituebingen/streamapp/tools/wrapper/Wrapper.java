package de.unituebingen.streamapp.tools.wrapper;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

import de.unituebingen.streamapp.tools.User;

public class Wrapper<T>  {
    @JSONField(name = "total")
    private int amount;

    @JSONField(name = "href")
    private String endpointUrl;

    @JSONField(name = "items")
    ArrayList<T> items;

    public Wrapper() {}

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

    @JSONField(name = "items")
    public void setItems(ArrayList<T> items) {
        this.items = items;
    }





}