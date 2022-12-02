package de.unituebingen.streamapp.tools;

import com.alibaba.fastjson.annotation.JSONField;

public class ApiKey {
    @JSONField(name = "apikey")
    String apikey;

    public ApiKey(String apikey) {
        this.apikey = apikey;
    }

    public ApiKey() {}

    @JSONField(name ="apikey")
    public String getApikey() {
        return apikey;
    }

    @JSONField(name = "apikey")
    public void setApikey(String apikey) {
        this.apikey = apikey;
    }
}
