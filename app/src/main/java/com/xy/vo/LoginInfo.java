package com.xy.vo;

/**
 * Created by FQ.CHINA on 2015/9/23.
 */
public class LoginInfo {
    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASS = "user_pass";
    public static final String USER_TRUENAME = "user_truename";
    public static final String USER_MPHONE = "user_mphone";
    public static final String USER_PHOTO = "user_photo";

    private int id;
    private String username;
    private String userpass;
    private String truename;
    private String mphone;
    private String photo;
    private int ret;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpass() {
        return userpass;
    }

    public void setUserpass(String userpass) {
        this.userpass = userpass;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getMphone() {
        return mphone;
    }

    public void setMphone(String mphone) {
        this.mphone = mphone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }
}
