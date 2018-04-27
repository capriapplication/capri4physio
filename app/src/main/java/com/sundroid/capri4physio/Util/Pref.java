package com.sundroid.capri4physio.Util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sundroid.capri4physio.pojo.user.AdminUserPOJO;
import com.sundroid.capri4physio.pojo.user.StudentUserPOJO;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by sunil on 26-05-2017.
 */

public class Pref {

    private static final String PrefDB = "capri4physio";
    private static final String PerPrefDB = "percapri4physio";


    public static final String FCM_REGISTRATION_TOKEN = "fcm_registration_token";


    public static void SetStringPref(Context context, String KEY, String Value) {
        try {
            SharedPreferences sp = context.getSharedPreferences(PrefDB, MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(KEY, Value);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String GetStringPref(Context context, String KEY, String defValue) {
        SharedPreferences sp = context.getSharedPreferences(PrefDB, MODE_PRIVATE);
        return sp.getString(KEY, defValue);
    }

    public static void SetBooleanPref(Context context, String KEY, boolean Value) {
        SharedPreferences sp = context.getSharedPreferences(PrefDB, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY, Value);
        editor.commit();
    }

    public static void setPermanentBoolean(Context context, String KEY, boolean Value) {
        SharedPreferences sp = context.getSharedPreferences(PerPrefDB, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY, Value);
        editor.commit();
    }

    public static boolean getPermanentBoolean(Context context, String KEY, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(PerPrefDB, MODE_PRIVATE);
        return sp.getBoolean(KEY, defValue);
    }

    public static int GetIntPref(Context context, String KEY, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(PrefDB, MODE_PRIVATE);
        return sp.getInt(KEY, defValue);
    }

    public static void SetIntPref(Context context, String KEY, int Value) {
        SharedPreferences sp = context.getSharedPreferences(PrefDB, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY, Value);
        editor.commit();
    }

    public static boolean GetBooleanPref(Context context, String KEY, boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(PrefDB, MODE_PRIVATE);
        return sp.getBoolean(KEY, defValue);
    }

    public static void clearSharedPreference(Context context) {
        SharedPreferences sp = context.getSharedPreferences(PrefDB, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void SaveDeviceToken(Context context, String Value) {
        SharedPreferences sp = context.getSharedPreferences("momytdevicetoken.txt", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("devicetoken", Value);
        editor.commit();
    }

    public static String GetDeviceToken(Context context, String defValue) {
        SharedPreferences sp = context.getSharedPreferences("momytdevicetoken.txt", MODE_PRIVATE);
        return sp.getString("devicetoken", defValue);
    }


    public static void saveStudentUser(Context context,StudentUserPOJO studentUserPOJO){
        if(studentUserPOJO!=null){
            Pref.SetStringPref(context,StringUtils.STUDENT_ID,studentUserPOJO.getId());
            Pref.SetStringPref(context,StringUtils.STUDENT_FIRST_NAME,studentUserPOJO.getFirstName());
            Pref.SetStringPref(context,StringUtils.STUDENT_LAST_NAME,studentUserPOJO.getLastName());
            Pref.SetStringPref(context,StringUtils.STUDENT_MOBILE,studentUserPOJO.getMobile());
            Pref.SetStringPref(context,StringUtils.STUDENT_EMAIL,studentUserPOJO.getEmail());
            Pref.SetStringPref(context,StringUtils.STUDENT_GENDER,studentUserPOJO.getGender());
            Pref.SetStringPref(context,StringUtils.STUDENT_PROFILE_PIC,studentUserPOJO.getProfilePic());
            Pref.SetStringPref(context,StringUtils.STUDENT_CITY,studentUserPOJO.getCity());
            Pref.SetStringPref(context,StringUtils.STUDENT_STATE,studentUserPOJO.getState());
            Pref.SetStringPref(context,StringUtils.STUDENT_ADDRESS,studentUserPOJO.getAddress());
            Pref.SetStringPref(context,StringUtils.STUDENT_COUNTRY,studentUserPOJO.getCountry());
            Pref.SetStringPref(context,StringUtils.STUDENT_PIN_CODE,studentUserPOJO.getPincode());
            Pref.SetStringPref(context,StringUtils.STUDENT_DEVICE_TYPE,studentUserPOJO.getUserPOJO().getDeviceType());
            Pref.SetStringPref(context,StringUtils.STUDENT_DEVICE_TOKEN,studentUserPOJO.getUserPOJO().getDeviceToken());
            Pref.SetStringPref(context,StringUtils.STUDENT_USER_TYPE,studentUserPOJO.getUserPOJO().getUserType());
        }
    }

//    public static StudentUserPOJO getStudentUserPOJO(Context context){
//        StudentUserPOJO studentUserPOJO=new StudentUserPOJO(
//                Pref.GetStringPref(context,StringUtils.STUDENT_ID,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_FIRST_NAME,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_LAST_NAME,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_MOBILE,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_EMAIL,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_GENDER,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_PROFILE_PIC,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_CITY,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_STATE,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_ADDRESS,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_COUNTRY,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_PIN_CODE,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_DEVICE_TYPE,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_DEVICE_TOKEN,""),
//                Pref.GetStringPref(context,StringUtils.STUDENT_USER_TYPE,"")
//        );
//        return studentUserPOJO;
//    }


    public static void saveAdminUser(Context context, AdminUserPOJO adminUserPOJO){
        if(adminUserPOJO!=null){
            Pref.SetStringPref(context,StringUtils.ADMIN_ID,adminUserPOJO.getId());
            Pref.SetStringPref(context,StringUtils.ADMIN_FIRST_NAME,adminUserPOJO.getFirstName());
            Pref.SetStringPref(context,StringUtils.ADMIN_LAST_NAME,adminUserPOJO.getLastName());
            Pref.SetStringPref(context,StringUtils.ADMIN_EMAIL,adminUserPOJO.getEmail());
            Pref.SetStringPref(context,StringUtils.ADMIN_PROFILE_PIC,adminUserPOJO.getProfilePic());
            Pref.SetStringPref(context,StringUtils.ADMIN_DEVICE_TYPE,adminUserPOJO.getDeviceType());
            Pref.SetStringPref(context,StringUtils.ADMIN_DEVICE_TOKEN,adminUserPOJO.getDeviceToken());
            Pref.SetStringPref(context,StringUtils.ADMIN_USER_TYPE,adminUserPOJO.getUserType());
        }
    }

    public static AdminUserPOJO getAdminUserPOJO(Context context){
        AdminUserPOJO adminUserPOJO=new AdminUserPOJO(
                Pref.GetStringPref(context,StringUtils.ADMIN_ID,""),
                Pref.GetStringPref(context,StringUtils.ADMIN_FIRST_NAME,""),
                Pref.GetStringPref(context,StringUtils.ADMIN_LAST_NAME,""),
                Pref.GetStringPref(context,StringUtils.ADMIN_EMAIL,""),
                Pref.GetStringPref(context,StringUtils.ADMIN_PROFILE_PIC,""),
                Pref.GetStringPref(context,StringUtils.ADMIN_DEVICE_TYPE,""),
                Pref.GetStringPref(context,StringUtils.ADMIN_DEVICE_TOKEN,""),
                Pref.GetStringPref(context,StringUtils.ADMIN_USER_TYPE,"")
        );
        return adminUserPOJO;
    }

}
