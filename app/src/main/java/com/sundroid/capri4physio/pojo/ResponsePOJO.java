package com.sundroid.capri4physio.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sunil on 20-03-2018.
 */

public class ResponsePOJO<T> {
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;
    @SerializedName("result")
    T result;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponsePOJO{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", result=" + result +
                '}';
    }
}
