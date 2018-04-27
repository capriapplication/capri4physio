package com.sundroid.capri4physio.activity;

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
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewCourseActivity extends AppCompatActivity implements WebServicesCallBack, DatePickerDialog.OnDateSetListener {
    private static final String ADD_NEW_COURSE = "add_new_course";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_course_name)
    EditText et_course_name;
    @BindView(R.id.et_comt)
    EditText et_comt;
    @BindView(R.id.et_place)
    EditText et_place;
    @BindView(R.id.et_from_date)
    EditText et_from_date;
    @BindView(R.id.btn_from_select)
    Button btn_from_select;
    @BindView(R.id.et_to_date)
    EditText et_to_date;
    @BindView(R.id.btn_to_select)
    Button btn_to_select;
    @BindView(R.id.et_seat_available)
    EditText et_seat_available;
    @BindView(R.id.et_showing_seat)
    EditText et_showing_seat;
    @BindView(R.id.et_fees)
    EditText et_fees;
    @BindView(R.id.et_reg_fees)
    EditText et_reg_fees;
    @BindView(R.id.et_rem_fees)
    EditText et_rem_fees;

    @BindView(R.id.et_phone)
    EditText et_phone;
    @BindView(R.id.btn_add_course)
    Button btn_add_course;
    boolean is_from_date = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_course);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        btn_add_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callAddNewCourseAPI();
            }
        });
        btn_from_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_from_date = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddNewCourseActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        btn_to_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_from_date = false;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AddNewCourseActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    public void callAddNewCourseAPI(){
        if (ValidateEdittexts(et_course_name, et_comt, et_from_date, et_to_date, et_seat_available,
                et_showing_seat, et_fees, et_phone,et_rem_fees,et_reg_fees)) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("request_action", "create_course"));
            nameValuePairs.add(new BasicNameValuePair("name", et_course_name.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("comt", et_comt.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("from_date", et_from_date.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("to_date", et_to_date.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("place", et_place.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("seat_available", et_seat_available.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("rem_seat", et_seat_available.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("showing_seat", et_showing_seat.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("reg_fees", et_reg_fees.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("rem_fees", et_rem_fees.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("full_fees", et_fees.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("phone", et_phone.getText().toString()));
            new WebServiceBase(nameValuePairs, this, this,ADD_NEW_COURSE,true).execute(WebServicesUrls.COURSE_CRUD);
        } else {
            ToastClass.showShortToast(getApplicationContext(), "Please Enter Valid Data");
        }
    }


    public boolean ValidateEdittexts(EditText... editTexts) {
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
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case ADD_NEW_COURSE:
                parseNewCourseData(response);
                break;
        }
    }

    public void parseNewCourseData(String response) {
        Log.d(TagUtils.getTag(), "new course data:-" + response);
        try{
            if(new JSONObject(response).optString("success").equals("true")){
                ToastClass.showShortToast(getApplicationContext(),"New Course Added Successfully");
                finish();
            }else{
                ToastClass.showShortToast(getApplicationContext(),"Failed To add New Course");
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(),"Something went wrong");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String day="";
        String month="";

        if((monthOfYear+1)<10){
            month="0" + (monthOfYear + 1);
        }else{
            month=String.valueOf((monthOfYear + 1));
        }
        if(dayOfMonth<10){
            day="0" + dayOfMonth;
        }else{
            day=String.valueOf(dayOfMonth);
        }
        String date=year+"/"+month+"/"+day;
        if (is_from_date) {
            et_from_date.setText(date);
        } else {
            et_to_date.setText(date);
        }
    }
}
