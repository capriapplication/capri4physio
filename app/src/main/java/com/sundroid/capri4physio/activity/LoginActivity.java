package com.sundroid.capri4physio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.Pref;
import com.sundroid.capri4physio.Util.StringUtils;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.pojo.user.AdminUserPOJO;
import com.sundroid.capri4physio.pojo.user.StudentUserPOJO;
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements WebServicesCallBack{

    private static final String CALL_LOGIN_API = "call_login_api";
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.tv_forgot_password)
    TextView tv_forgot_password;
    @BindView(R.id.btn_login)
    Button btn_login;
    @BindView(R.id.tv_register)
    TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLoginAPI();
            }
        });

        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterationActivity.class));
            }
        });
    }
    public void callLoginAPI(){
        if(et_email.getText().toString().length()>0&&
                et_password.getText().toString().length()>0) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("request_action", "login_user"));
            nameValuePairs.add(new BasicNameValuePair("email", et_email.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("password", et_password.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("device_token", Pref.GetDeviceToken(getApplicationContext(),"")));
            nameValuePairs.add(new BasicNameValuePair("device_type", "android"));
            new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
                @Override
                public void onGetMsg(String[] msg) {
                    Log.d(TagUtils.getTag(),"respone:-"+msg[1]);
                    try{
                        JSONObject jsonObject=new JSONObject(msg[1]);
                        if(jsonObject.optBoolean("success")){
                            if(jsonObject.optJSONObject("result").optJSONObject("user").optString("user_type").equals("1")){
                                Pref.SetBooleanPref(getApplicationContext(),StringUtils.IS_STUDENT_LOGIN,true);
                                Pref.SetStringPref(getApplicationContext(),StringUtils.STUDENT_POJO,jsonObject.optJSONObject("result").toString());
                                Gson gson=new Gson();
                                StudentUserPOJO studentUserPOJO=gson.fromJson(jsonObject.optJSONObject("result").toString(),StudentUserPOJO.class);
                                startActivity(new Intent(LoginActivity.this,StudentDashboardActivity.class).putExtra("studentPOJO",studentUserPOJO));
                                finishAffinity();

                            }else if(jsonObject.optJSONObject("result").optJSONObject("user").optString("user_type").equals("0")){
                                Pref.SetBooleanPref(getApplicationContext(),StringUtils.IS_ADMIN_LOGIN,true);
                                Pref.SetStringPref(getApplicationContext(),StringUtils.ADMIN_POJO,jsonObject.optJSONObject("result").toString());
                                Gson gson=new Gson();
                                AdminUserPOJO adminUserPOJO=gson.fromJson(jsonObject.optJSONObject("result").toString(),AdminUserPOJO.class);
                                startActivity(new Intent(LoginActivity.this,AdminDashBoardActivity.class).putExtra("adminPOJO",adminUserPOJO));
                                finishAffinity();

                            }
                        }else{
                            ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, CALL_LOGIN_API, true).execute(WebServicesUrls.USER_CRUD);
        }else{
            ToastClass.showShortToast(getApplicationContext(),"Please enter proper email and password");
        }
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        Log.d(TagUtils.getTag(),apicall+" :- "+response);
        switch (apicall){
            case CALL_LOGIN_API:
                parseLoginResponse(response);
                break;
        }
    }

    public void parseLoginResponse(String response){
        try{
            Log.d(TagUtils.getTag(),"login response:-"+response);
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString("success").equals("true")){
                if(jsonObject.optJSONObject("result").optString("user_type").equals("4")){
                    AdminUserPOJO adminUserPOJO=new Gson().fromJson(jsonObject.optJSONObject("result").toString(),AdminUserPOJO.class);
                    Pref.saveAdminUser(getApplicationContext(),adminUserPOJO);
                    Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_ADMIN_LOGIN,true);
                    startActivity(new Intent(LoginActivity.this,AdminDashBoardActivity.class));
                    finishAffinity();
                }else if(jsonObject.optJSONObject("result").optString("user_type").equals("5")){
                    StudentUserPOJO studentUserPOJO=new Gson().fromJson(jsonObject.optJSONObject("result").toString(),StudentUserPOJO.class);
                    Pref.saveStudentUser(getApplicationContext(),studentUserPOJO);
                    Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_STUDENT_LOGIN,true);
                    startActivity(new Intent(LoginActivity.this,StudentDashboardActivity.class));
                    finishAffinity();
                }else{
                    ToastClass.showShortToast(getApplicationContext(),"Invalid User");
                }
            }else{
                ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
