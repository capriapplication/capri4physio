package com.sundroid.capri4physio.pojo.chat;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 23-04-2018.
 */

public class ChatPOJO {

    @SerializedName("date")
    private String date;
    @SerializedName("msg")
    private String msg;
    @SerializedName("file")
    private String file;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("thumb")
    private String thumb;
    @SerializedName("fri_id")
    private String friId;
    @SerializedName("id")
    private String id;
    @SerializedName("time")
    private String time;
    @SerializedName("type")
    private String type;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getFriId() {
        return friId;
    }

    public void setFriId(String friId) {
        this.friId = friId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
