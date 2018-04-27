package com.sundroid.capri4physio.pojo.attendance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 11-12-2017.
 */

public class AttendanceResultPOJO {
    @SerializedName("stu_id")
    String stu_id;
    @SerializedName("stu_name")
    String stu_name;
    @SerializedName("data")
    List<AttendanceDataPOJO> attendanceDataPOJOList;

    public String getStu_id() {
        return stu_id;
    }

    public void setStu_id(String stu_id) {
        this.stu_id = stu_id;
    }

    public String getStu_name() {
        return stu_name;
    }

    public void setStu_name(String stu_name) {
        this.stu_name = stu_name;
    }

    public List<AttendanceDataPOJO> getAttendanceDataPOJOList() {
        return attendanceDataPOJOList;
    }

    public void setAttendanceDataPOJOList(List<AttendanceDataPOJO> attendanceDataPOJOList) {
        this.attendanceDataPOJOList = attendanceDataPOJOList;
    }
}
