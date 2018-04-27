package com.sundroid.capri4physio.pojo.studentattendance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 03-12-2017.
 */

public class StudentListPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<StudentPOJO> studentPOJOS;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<StudentPOJO> getStudentPOJOS() {
        return studentPOJOS;
    }

    public void setStudentPOJOS(List<StudentPOJO> studentPOJOS) {
        this.studentPOJOS = studentPOJOS;
    }
}
