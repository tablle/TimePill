package com.tablle.timepill.model;

import java.io.Serializable;

/**
 * Created by tong on 2016/7/27.
 */
public class UserBean implements Serializable {
    public int id;
    public String name;
    public String iconUrl;

    @Override
    public String toString() {
        return "UserBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}
