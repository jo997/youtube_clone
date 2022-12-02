package de.unituebingen.streamapp;

import android.app.Activity;
import android.content.SharedPreferences;

import de.unituebingen.streamapp.tools.User;

public class UserData {
    private final Activity activity;
    private final SharedPreferences sharedPref;

    private boolean isLoggedIn = false;
    private String authentication = null;

    private int userId;
    private String username;
    private String realname;
    private String email;
    private int typeId;
    private String type;

    public final static int USER_GUEST = 1;
    public final static int USER_NORMAL = 2;
    public final static int USER_PRIVILEGE = 3;
    public final static int USER_ADMIN = 4;

    public UserData(Activity activity){
        this.activity = activity;
        this.sharedPref = activity.getSharedPreferences(
                activity.getString(R.string.userdata),
                activity.MODE_PRIVATE);
        isLoggedIn();
        getAuthentication();
        getRealname();
        getUsername();
        getEmail();
        getType();
        getTypeId();
        getUserId();
    }

    public boolean isLoggedIn() {
        this.isLoggedIn = sharedPref.getBoolean(
                activity.getString(R.string.ud_isUserLoggedIn),
                false);
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(activity.getString(R.string.ud_isUserLoggedIn), loggedIn);
        editor.apply();
        isLoggedIn = loggedIn;
    }

    public String getAuthentication() {
        this.authentication = sharedPref.getString(
                activity.getString(R.string.ud_authentication),
                null);
        return authentication;
    }

    public void setAuthentication(String authentication) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.ud_authentication), authentication);
        editor.apply();
        this.authentication = authentication;
    }

    public int getUserId() {
        this.userId = sharedPref.getInt(
                activity.getString(R.string.ud_userid),
                USER_GUEST);
        return userId;
    }

    public void setUserId(int userId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(R.string.ud_userid), userId);
        editor.apply();
        this.userId = userId;
    }

    public String getUsername() {
        this.username = sharedPref.getString(
                activity.getString(R.string.ud_username),
                "");
        return username;
    }

    public void setUsername(String username) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.ud_username), username);
        editor.apply();
        this.username = username;
    }

    public String getRealname() {
        this.realname = sharedPref.getString(
            activity.getString(R.string.ud_realname),
            "");
        return realname;
    }

    public void setRealname(String realname) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.ud_realname), realname);
        editor.apply();
        this.realname = realname;
    }

    public String getEmail() {
        this.email = sharedPref.getString(
                activity.getString(R.string.ud_email),
                "");
        return email;
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.ud_realname), realname);
        editor.apply();
        this.email = email;
    }

    public int getTypeId() {
        this.typeId = sharedPref.getInt(
                activity.getString(R.string.ud_typeid),
                1);
        return typeId;
    }

    public void setTypeId(int typeId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(activity.getString(R.string.ud_typeid), typeId);
        editor.apply();
        this.typeId = typeId;
    }

    public String getType() {
        this.type = sharedPref.getString(
                activity.getString(R.string.ud_type),
                "Nutzer");
        return type;
    }

    public void setType(String type) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getString(R.string.ud_type), type);
        editor.apply();
        this.type = type;
    }

    public void setUserInfo(User user) {
        setUserId(user.getId());
        setUsername(user.getUserName());
        setRealname(user.getRealName());
        setEmail(user.getMail());
        setType(user.getType());
        setTypeId(user.getTypeId());
    }

    public void resetUserData() {
        setUserId(0);
        setLoggedIn(false);
        setAuthentication(null);
        setUsername("");
        setRealname("");
        setEmail("");
        setTypeId(1);
        setType("Nutzer");
    }
}
