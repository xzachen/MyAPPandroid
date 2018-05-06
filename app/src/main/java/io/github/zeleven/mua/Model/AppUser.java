package io.github.zeleven.mua.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class AppUser implements Serializable {

    private String password;
    private int userid;
    private String username;

    public AppUser() {
    }

    public AppUser(int userid, String username,String password) {
        this.password = password;
        this.userid = userid;
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public int getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "password='" + password + '\'' +
                ", userid=" + userid +
                ", username='" + username + '\'' +
                '}';
    }

}
