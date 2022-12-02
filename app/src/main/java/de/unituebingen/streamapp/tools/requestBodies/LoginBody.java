package de.unituebingen.streamapp.tools.requestBodies;

import com.alibaba.fastjson.annotation.JSONField;

public class LoginBody {
    @JSONField(name = "username")
    String username;
    @JSONField(name = "password")
    String password;

    public LoginBody(final String username, final String password) {
        this.password = password;
        this.username = username;
    }

    public LoginBody(){

    }

    @JSONField(name = "password")
    public String getPassword() {
        return password;
    }

    @JSONField(name = "username")
    public String getUsername() {
        return username;
    }

    @JSONField(name = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JSONField(name = "password")
    public void setPassword(String password) {
        this.password = password;
    }
}