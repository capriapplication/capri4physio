package com.sundroid.capri4physio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.pojo.studentapplication.ApplicationFormResultPOJO;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCourseResultPOJO;
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentCourseApplicationActivity extends AppCompatActivity implements WebServicesCallBack {
    private static final String GET_APPLICATION_FORM_DATA = "get_application_form_data";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_course)
    EditText et_course;
    @BindView(R.id.et_first_name)
    EditText et_first_name;
    @BindView(R.id.et_middle_name)
    EditText et_middle_name;
    @BindView(R.id.et_last_name)
    EditText et_last_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.et_city_date)
    EditText et_city_date;
    @BindView(R.id.et_trans_id)
    EditText et_trans_id;
    @BindView(R.id.et_amount)
    EditText et_amount;
    @BindView(R.id.et_name_of_bank)
    EditText et_name_of_bank;
    @BindView(R.id.et_present_addr)
    EditText et_present_addr;
    @BindView(R.id.et_city)
    EditText et_city;
    @BindView(R.id.et_state)
    EditText et_state;
    @BindView(R.id.et_zip_code)
    EditText et_zip_code;
    @BindView(R.id.et_country)
    EditText et_country;
    @BindView(R.id.et_qualification)
    EditText et_qualification;
    @BindView(R.id.et_last_institute)
    EditText et_last_institute;
    @BindView(R.id.et_clinic_addr)
    EditText et_clinic_addr;
    @BindView(R.id.et_comments)
    EditText et_comments;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    StudentCourseResultPOJO studentCourseResultPOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_application);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        studentCourseResultPOJO= (StudentCourseResultPOJO) getIntent().getSerializableExtra("studentcourseresultpojo");
        callApplicationAPI();
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void callApplicationAPI(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "get_application"));
        nameValuePairs.add(new BasicNameValuePair("c_id", studentCourseResultPOJO.getcId()));
        nameValuePairs.add(new BasicNameValuePair("s_id", studentCourseResultPOJO.getStuId()));
        new WebServiceBase(nameValuePairs, this,this, GET_APPLICATION_FORM_DATA,true).execute(WebServicesUrls.STUDENT_APPLICATION_CRUD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setViews(ApplicationFormResultPOJO applicationFormResultPOJO){
//        et_course.setText(applicationFormResultPOJO.getaCourse());
        et_first_name.setText(applicationFormResultPOJO.getFirstName());
        et_middle_name.setText(applicationFormResultPOJO.getMiddleName());
        et_last_name.setText(applicationFormResultPOJO.getLastName());
        et_email.setText(applicationFormResultPOJO.getEmail());
        et_mobile.setText(applicationFormResultPOJO.getMobile());
        et_city_date.setText(applicationFormResultPOJO.getAppliedCity());
//        et_trans_id.setText(applicationFormResultPOJO.getaTranstionid());
//        et_amount.setText(applicationFormResultPOJO.getaAmount());
//        et_name_of_bank.setText(applicationFormResultPOJO.getaBankName());
        et_present_addr.setText(applicationFormResultPOJO.getAddress());
        et_city.setText(applicationFormResultPOJO.getCity());
        et_state.setText(applicationFormResultPOJO.getState());
        et_zip_code.setText(applicationFormResultPOJO.getPinCode());
        et_country.setText(applicationFormResultPOJO.getCountry());
        et_qualification.setText(applicationFormResultPOJO.getQualification());
//        et_last_institute.setText(applicationFormResultPOJO.getaCollege());
        et_clinic_addr.setText(applicationFormResultPOJO.getClinicAddress());
        et_comments.setText(applicationFormResultPOJO.getComments());

    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case GET_APPLICATION_FORM_DATA:
                parseApplicationFormData(response);
                break;
        }
    }

    public void parseApplicationFormData(String response){
        Log.d(TagUtils.getTag(),"applicationformresponse:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString("success").equals("true")){
                Gson gson=new Gson();
                ApplicationFormResultPOJO applicationFormResultPOJO=gson.fromJson(jsonObject.optJSONArray("result").optJSONObject(0).toString(),ApplicationFormResultPOJO.class);
                setViews(applicationFormResultPOJO);
            }else{
                ToastClass.showShortToast(getApplicationContext(),"No Application Form data found");
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(),"Something went wrong");
        }
    }
}
