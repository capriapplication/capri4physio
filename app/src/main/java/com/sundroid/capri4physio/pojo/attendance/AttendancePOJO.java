package com.sundroid.capri4physio.pojo.attendance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 11-12-2017.
 */

public class AttendancePOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<AttendanceResultPOJO> attendanceResultPOJOList;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<AttendanceResultPOJO> getAttendanceResultPOJOList() {
        return attendanceResultPOJOList;
    }

    public void setAttendanceResultPOJOList(List<AttendanceResultPOJO> attendanceResultPOJOList) {
        this.attendanceResultPOJOList = attendanceResultPOJOList;
    }
}
