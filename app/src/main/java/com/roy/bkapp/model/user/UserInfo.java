package com.roy.bkapp.model.user;

/**
 * Created by Administrator on 2017/5/22.
 */

public class UserInfo {
    private String objectId;
    private String username;
    private String password;

    public UserInfo() {

    }

    public UserInfo(String objectId, String username, String password) {
        this.objectId = objectId;
        this.username = username;
        this.password = password;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
