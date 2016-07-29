package com.tablle.timepill.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tong on 2016/7/27.
 */
public class DiaryBean {

    SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    public int id;
    public int user_id;
    public int notebook_id;
    public String notebook_subject;
    public String content;
    public String created;
    public String updated;
    public String type;
    public String comment_count;
    public String photoUrl;
    public String photoThumbUrl;
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
        return "DiariesBean{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", notebook_id=" + notebook_id +
                ", notebook_subject='" + notebook_subject + '\'' +
                ", content='" + content + '\'' +
                ", created='" + created + '\'' +
                ", updated='" + updated + '\'' +
                ", type=" + type +
                ", comment_count=" + comment_count +
                ", photoUrl='" + photoUrl + '\'' +
                ", photoThumbUrl='" + photoThumbUrl + '\'' +
                ", user=" + user +
                '}';
    }
}
