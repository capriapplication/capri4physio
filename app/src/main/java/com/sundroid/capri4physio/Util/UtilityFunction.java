package com.sundroid.capri4physio.Util;

import android.content.Context;
import android.widget.EditText;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sunil on 01-03-2018.
 */

public class UtilityFunction {
    public static ArrayList<NameValuePair> getNameValuePairs(Context context){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("device_token",Pref.GetDeviceToken(context,"")));
        nameValuePairs.add(new BasicNameValuePair("device_type","android"));

        return nameValuePairs;
    }

    public static boolean isDataValid(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().length() == 0) {
                return false;
            }
        }
        return true;
    }

    public static String getFormattedDate(String strDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf.parse(strDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            return simpleDateFormat.format(date);
        } catch (Exception e) {

        }
        return strDate;
    }


    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = new Date();
        return sdf.format(d);
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();
        return sdf.format(d);
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        Date d = new Date();
        return sdf.format(d);
    }

    public static String getParsedDate(String date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date d = simpleDateFormat.parse(date);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getddMMYYYY() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }



}
