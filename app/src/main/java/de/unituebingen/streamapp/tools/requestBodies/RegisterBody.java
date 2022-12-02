package de.unituebingen.streamapp.tools.requestBodies;

import com.alibaba.fastjson.annotation.JSONField;

public class RegisterBody {
    @JSONField(name = "username")
    String username;
    @JSONField(name = "password")
    String password;
    @JSONField(name = "realname")
    String realname;
    @JSONField(name = "mail")
    String mail;

    @JSONField(name = "username")
    public String getUsername() {
        return username;
    }

    @JSONField(name = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JSONField(name = "password")
    public String getPassword() {
        return password;
    }

    @JSONField(name = "password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JSONField(name = "realname")
    public String getRealname() {
        return realname;
    }

    @JSONField(name = "realname")
    public void setRealname(String realname) {
        this.realname = realname;
    }

    @JSONField(name = "mail")
    public String getMail() {
        return mail;
    }

    @JSONField(name = "mail")
    public void setMail(String mail) {
        this.mail = mail;
    }

    public RegisterBody() {}

    public RegisterBody(String username, String password, String realname, String mail) {
        this.username = username;
        this.realname = realname;
        this.password = password;
        this.mail = mail;
    }
}