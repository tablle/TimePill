package com.tablle.timepill.constant;

/**
 * Created by tong on 2016/7/27.
 */
public class Url {
    //用户

    /*
    * 获取自己的信息
    */
    public static final String URL_URERS_MY = "https://open.timepill.net/api/users/my";

    /*
    * 获取指定用户信息
    */
    public static final String URL_URERS_OTHRE = "https://open.timepill.net/api/users/{id}";

    //日记
    /*
   * 获取首页当天所有日记
   */
    public static final String URL_Diary_ALL = "https://open.timepill.net/api/diaries/today";

    /*
     * 获取关注用户的日记
     */
    public static final String URL_Diary_ATTENTION = "https://open.timepill.net/api/diaries/follow";


}
