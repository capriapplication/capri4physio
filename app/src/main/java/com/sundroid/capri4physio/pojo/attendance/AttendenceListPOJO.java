package com.sundroid.capri4physio.pojo.attendance;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 03-12-2017.
 */

public class AttendenceListPOJO {
    @SerializedName("success")
    String success;
    @SerializedName("result")
    List<AttendencePOJO> attendencePOJOList;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<AttendencePOJO> getAttendencePOJOList() {
        return attendencePOJOList;
    }

    public void setAttendencePOJOList(List<AttendencePOJO> attendencePOJOList) {
        this.attendencePOJOList = attendencePOJOList;
    }
}
