package com.tablle.timepill.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tong on 2016/8/20.
 */
public class CommentBean {

    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    public int id;
    public int user_id;
    public int dairy_id;
    public String content;
    public String created;
    public String recipient;
    public UserBean user;

    public String getCreatedTimeString() {
        String timeCreat=null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format1.parse(created);
            timeCreat = format.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeCreat;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", dairy_id=" + dairy_id +
                ", content='" + content + '\'' +
                ", created='" + created + '\'' +
                ", recipient='" + recipient + '\'' +
                ", user=" + user +
                '}';
    }
}
