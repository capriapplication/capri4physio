package com.sundroid.capri4physio.pojo.attendance;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 11-12-2017.
 */

public class AttendanceDataPOJO {
    @SerializedName("date")
    String date;
    @SerializedName("status")
    String status;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
