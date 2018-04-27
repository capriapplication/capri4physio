package com.sundroid.capri4physio.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.pojo.cources.CourcesResultPOJO;
import com.sundroid.capri4physio.pojo.studentapplication.ApplicationFormResultPOJO;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCourseResultPOJO;
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddApplicationFormActivity extends AppCompatActivity implements WebServicesCallBack {
    private static final String ADD_APPLICATION_FORM = "add_application_form";
    private static final String UPDATE_APPLICATION_FORM = "update_application_form";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
//    @BindView(R.id.spinner_course)
//    Spinner spinner_course;
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
    CourcesResultPOJO courcesResultPOJO;

//    String[] spinner_items = { "COMT", "Jenny McConell Concept", "CRASH COURSE"};
    ApplicationFormResultPOJO applicationFormResultPOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_application_form);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinner_items);
//        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner_course.setAdapter(aa);

        studentCourseResultPOJO= (StudentCourseResultPOJO) getIntent().getSerializableExtra("studentcourseresultpojo");
        courcesResultPOJO= (CourcesResultPOJO) getIntent().getSerializableExtra("coursepojo");
//        Log.d(TagUtils.getTag(),"course pojo:-"+courcesResultPOJO.toString());
        applicationFormResultPOJO= (ApplicationFormResultPOJO) getIntent().getSerializableExtra("applicationform");
        if(studentCourseResultPOJO!=null){
            btn_submit.setText("Update");
        }else{
            btn_submit.setText("Submit");
        }

        if(courcesResultPOJO!=null){
            et_city_date.setText(courcesResultPOJO.getPlace());
            et_amount.setText(courcesResultPOJO.getFullFees());
        }

        if(applicationFormResultPOJO==null){
            btn_submit.setText("Submit");
        }else{
            btn_submit.setText("Update");
            et_first_name.setText(applicationFormResultPOJO.getFirstName());
            et_middle_name.setText(applicationFormResultPOJO.getMiddleName());
            et_last_name.setText(applicationFormResultPOJO.getLastName());
            et_email.setText(applicationFormResultPOJO.getEmail());
            et_mobile.setText(applicationFormResultPOJO.getMobile());
            et_city_date.setText(applicationFormResultPOJO.getCity());
            et_present_addr.setText(applicationFormResultPOJO.getAddress());
            et_city.setText(applicationFormResultPOJO.getCity());
            et_state.setText(applicationFormResultPOJO.getState());
            et_zip_code.setText(applicationFormResultPOJO.getPinCode());
            et_country.setText(applicationFormResultPOJO.getCountry());
            et_qualification.setText(applicationFormResultPOJO.getQualification());
            et_clinic_addr.setText(applicationFormResultPOJO.getClinicAddress());
            et_comments.setText(applicationFormResultPOJO.getComments());

        }

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateEdits(et_first_name,et_last_name,et_email,et_mobile,
                        et_city_date,et_amount,et_present_addr,et_city,
                        et_state,et_zip_code,et_country,et_qualification)) {

                    if(applicationFormResultPOJO==null){
                        addApplicationForm(true);
                    }else{
                        addApplicationForm(false);
                    }
                }else{
                    ToastClass.showShortToast(getApplicationContext(),"Please fill all required data");
                }
            }
        });
    }

    public void addApplicationForm(boolean add){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("c_id", studentCourseResultPOJO.getcId()));
        nameValuePairs.add(new BasicNameValuePair("s_id", studentCourseResultPOJO.getStuId()));
        nameValuePairs.add(new BasicNameValuePair("first_name", courcesResultPOJO.getComt()));
        nameValuePairs.add(new BasicNameValuePair("middle_name", et_first_name.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("last_name", et_middle_name.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("email", et_last_name.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("mobile", et_email.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("applied_city", et_mobile.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("address", et_city_date.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("city", et_trans_id.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("state", et_amount.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("pin_code", et_name_of_bank.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("country", new SimpleDateFormat("dd-MM-yyyy").format(new Date())));
        nameValuePairs.add(new BasicNameValuePair("qualification", et_present_addr.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("clinic_address", et_city.getText().toString()));
        nameValuePairs.add(new BasicNameValuePair("comments", et_state.getText().toString()));

        if(add){
            nameValuePairs.add(new BasicNameValuePair("request_action", "submit_application"));
            new WebServiceBase(nameValuePairs, this,this, ADD_APPLICATION_FORM,true).execute(WebServicesUrls.STUDENT_APPLICATION_CRUD);
        }else{
            nameValuePairs.add(new BasicNameValuePair("request_action", "update_application"));
            nameValuePairs.add(new BasicNameValuePair("id", applicationFormResultPOJO.getId()));
            new WebServiceBase(nameValuePairs, this,this, UPDATE_APPLICATION_FORM,true).execute(WebServicesUrls.STUDENT_APPLICATION_CRUD);
        }

    }


    public boolean ValidateEdits(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().length() == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case ADD_APPLICATION_FORM:
                parseApplicationFormdata(response);
                break;
            case UPDATE_APPLICATION_FORM:
                parseUdateApplicationFormData(response);
                break;
        }
    }

    public void parseUdateApplicationFormData(String response){
        Log.d(TagUtils.getTag(),"update form data:-"+response);
        finish();
    }

    public void parseApplicationFormdata(String response){
        Log.d(TagUtils.getTag(),"application form response:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString("success").equals("true")){
                ToastClass.showShortToast(getApplicationContext(),"Application Form saved successfully");
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","ok");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }else{
                ToastClass.showShortToast(getApplicationContext(),"Failed to saved Application Form");
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(),"Somthing went wrong");
        }
    }
}
