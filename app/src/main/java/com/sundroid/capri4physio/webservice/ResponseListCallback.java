package com.sundroid.capri4physio.webservice;


import com.sundroid.capri4physio.pojo.ResponseListPOJO;

/**
 * Created by sunil on 29-12-2016.
 */

public interface ResponseListCallback<T> {
    public void onGetMsg(ResponseListPOJO<T> responseListPOJO);
}
