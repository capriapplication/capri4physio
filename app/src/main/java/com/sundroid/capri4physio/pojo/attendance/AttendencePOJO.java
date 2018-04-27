package com.sundroid.capri4physio.pojo.attendance;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 03-12-2017.
 */

public class AttendencePOJO {
    @SerializedName("a_id")
    String a_id;
    @SerializedName("c_id")
    String c_id;
    @SerializedName("date")
    String date;
    @SerializedName("start_time")
    String start_time;
    @SerializedName("end_time")
    String end_time;

    public String getA_id() {
        return a_id;
    }

    public void setA_id(String a_id) {
        this.a_id = a_id;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
