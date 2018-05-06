package io.github.zeleven.mua.Model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/18 0018.
 */

public class User implements Serializable {
    private String name;

    public User() {
    }
    public User(String name, String imageUri) {
        this.name = name;
        this.imageUri = imageUri;
    }

    private String imageUri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
