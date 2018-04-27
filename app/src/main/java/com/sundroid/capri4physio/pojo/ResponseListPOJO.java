package com.sundroid.capri4physio.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sunil on 20-03-2018.
 */

public class ResponseListPOJO<T> {

    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;
    @SerializedName("result")
    List<T> resultList;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getResultList() {
        return resultList;
    }

    public void setResultList(List<T> resultList) {
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return "ResponseListPOJO{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", resultList=" + resultList.toString() +
                '}';
    }
}
