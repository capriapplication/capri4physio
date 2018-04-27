package com.sundroid.capri4physio.pojo.attendedstudent;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 11-03-2018.
 */

public class AttendedUserPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<AttendedStudentResultPOJO> attendedUserPOJOS;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<AttendedStudentResultPOJO> getAttendedUserPOJOS() {
        return attendedUserPOJOS;
    }

    public void setAttendedUserPOJOS(List<AttendedStudentResultPOJO> attendedUserPOJOS) {
        this.attendedUserPOJOS = attendedUserPOJOS;
    }
}
