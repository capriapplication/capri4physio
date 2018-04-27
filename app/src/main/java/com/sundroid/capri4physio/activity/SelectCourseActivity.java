package com.sundroid.capri4physio.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.adapter.StudentCourseAdapter;
import com.sundroid.capri4physio.pojo.cources.CourcesPOJO;
import com.sundroid.capri4physio.pojo.cources.CourcesResultPOJO;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCourseResultPOJO;
import com.sundroid.capri4physio.pojo.user.StudentUserPOJO;
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

public class SelectCourseActivity extends AppCompatActivity implements WebServicesCallBack {
    private static final String LIST_COURCES_API = "list_cources_api";
    private static final String CHECK_APPLY_COURSE_API = "check_apply_course_api";
    private static final String APPLY_FOR_COURSE = "apply_for_course";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_course)
    RecyclerView rv_course;

    StudentUserPOJO studentUserPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);

        ButterKnife.bind(this);

        studentUserPOJO = (StudentUserPOJO) getIntent().getSerializableExtra("studentPOJO");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        callCourseListAPI();
    }

    public void callCourseListAPI() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "get_all_course"));
        new WebServiceBase(nameValuePairs, this, this, LIST_COURCES_API, true).execute(WebServicesUrls.COURSE_CRUD);
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

    CourcesResultPOJO courseResultPOJO;

    public void checkAppliedCourse(CourcesResultPOJO courcesResultPOJO) {
        this.courseResultPOJO = courcesResultPOJO;
        if (courseResultPOJO != null) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("request_action", "check_registration"));
            nameValuePairs.add(new BasicNameValuePair("stu_id", studentUserPOJO.getId()));
            nameValuePairs.add(new BasicNameValuePair("c_id", courseResultPOJO.getId()));
            new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
                @Override
                public void onGetMsg(String[] msg) {
                    String response=msg[1];
                    Log.d(TagUtils.getTag(), "applied course response:-" + response);
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        if (jsonObject.optString("success").equals("true")) {
                            StudentCourseResultPOJO studentCourseResultPOJO = new Gson().fromJson(jsonObject.optJSONObject("result").toString(),StudentCourseResultPOJO.class);
                            Intent intent = new Intent(SelectCourseActivity.this, StudentCourseStatusActivity.class);
                            intent.putExtra("studentcourseresultpojo", studentCourseResultPOJO);
                            intent.putExtra("coursepojo", courseResultPOJO);
                            intent.putExtra("studentPOJO", studentUserPOJO);
                            startActivity(intent);
                        } else {
                            showSelectCourseDialog(courseResultPOJO);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, CHECK_APPLY_COURSE_API, true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
        } else {
            ToastClass.showShortToast(getApplicationContext(), "something went wrong");
        }
    }

    public void showSelectCourseDialog(final CourcesResultPOJO courcesResultPOJO) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Do you want to apply for course " + courcesResultPOJO.getName() + " ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                callCourseApplyAPI(courcesResultPOJO);
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void callCourseApplyAPI(CourcesResultPOJO courcesResultPOJO) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "register_student"));
        nameValuePairs.add(new BasicNameValuePair("c_id", courcesResultPOJO.getId()));
        nameValuePairs.add(new BasicNameValuePair("stu_id", studentUserPOJO.getId()));
        nameValuePairs.add(new BasicNameValuePair("stu_app_id", ""));
        nameValuePairs.add(new BasicNameValuePair("a_stu_app_id", ""));
        nameValuePairs.add(new BasicNameValuePair("photo_upload", ""));
        nameValuePairs.add(new BasicNameValuePair("a_photo_upload", ""));
        nameValuePairs.add(new BasicNameValuePair("cert_upload", ""));
        nameValuePairs.add(new BasicNameValuePair("a_cert_upload", ""));
        nameValuePairs.add(new BasicNameValuePair("cert_upload", ""));
        nameValuePairs.add(new BasicNameValuePair("a_cert_upload", ""));
        nameValuePairs.add(new BasicNameValuePair("stu_reg_fees", ""));
        nameValuePairs.add(new BasicNameValuePair("a_stu_reg_fees", ""));
        nameValuePairs.add(new BasicNameValuePair("stu_rem_fees", ""));
        nameValuePairs.add(new BasicNameValuePair("a_stu_rem_fees", ""));
        nameValuePairs.add(new BasicNameValuePair("stu_full_fees", ""));
        nameValuePairs.add(new BasicNameValuePair("a_stu_full_fees", ""));

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm a");



        String dates = sdf.format(date).split(" ")[0];
        String time = sdf.format(date).split(" ")[1] + " " + sdf.format(date).split(" ")[2];

        nameValuePairs.add(new BasicNameValuePair("stu_date", dates));
        nameValuePairs.add(new BasicNameValuePair("stu_time", time));


        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String[] msg) {
                String response=msg[1];
                Log.d(TagUtils.getTag(), "course apply response:-" + response);
                try {
                    Gson gson = new Gson();
                    JSONObject jsonObject=new JSONObject(response);
                    if (jsonObject.optString("success").equals("true")) {
                        StudentCourseResultPOJO studentCourseResultPOJO = new Gson().fromJson(jsonObject.optString("result"),StudentCourseResultPOJO.class);
                        Intent intent = new Intent(SelectCourseActivity.this, StudentCourseStatusActivity.class);
                        intent.putExtra("studentcourseresultpojo", studentCourseResultPOJO);
                        intent.putExtra("coursepojo", courseResultPOJO);
                        intent.putExtra("studentPOJO", studentUserPOJO);
                        startActivity(intent);

                        ToastClass.showShortToast(getApplicationContext(), "You Successfully applied for this course");
                    } else {
                        ToastClass.showShortToast(getApplicationContext(), "Failed to apply for the course");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
                }
            }
        }, APPLY_FOR_COURSE, true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case LIST_COURCES_API:
                parseCourseListApi(response);
                break;
            case CHECK_APPLY_COURSE_API:
                parseApplyCourseApi(response);
                break;
            case APPLY_FOR_COURSE:
                parseApplyForCourse(response);
                break;
        }
    }

    public void parseApplyForCourse(String response) {
        Log.d(TagUtils.getTag(), "course apply response:-" + response);
        try {
            Gson gson = new Gson();
            JSONObject jsonObject=new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                StudentCourseResultPOJO studentCourseResultPOJO = new Gson().fromJson(jsonObject.optString("Result"),StudentCourseResultPOJO.class);
                Intent intent = new Intent(SelectCourseActivity.this, StudentCourseStatusActivity.class);
                intent.putExtra("studentcourseresultpojo", studentCourseResultPOJO);
                intent.putExtra("coursepojo", courseResultPOJO);
                intent.putExtra("studentPOJO", studentUserPOJO);
                startActivity(intent);

                ToastClass.showShortToast(getApplicationContext(), "You Successfully applied for this course");
            } else {
                ToastClass.showShortToast(getApplicationContext(), "Failed to apply for the course");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
        }
    }

    public void parseApplyCourseApi(String response) {
        Log.d(TagUtils.getTag(), "applied course response:-" + response);
        try {
            JSONObject jsonObject=new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                StudentCourseResultPOJO studentCourseResultPOJO = new Gson().fromJson(jsonObject.optJSONObject("result").toString(),StudentCourseResultPOJO.class);
                Intent intent = new Intent(SelectCourseActivity.this, StudentCourseStatusActivity.class);
                intent.putExtra("studentcourseresultpojo", studentCourseResultPOJO);
                intent.putExtra("coursepojo", courseResultPOJO);
                intent.putExtra("studentPOJO", studentUserPOJO);
                startActivity(intent);
            } else {
                showSelectCourseDialog(courseResultPOJO);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseCourseListApi(String response) {
        Log.d(TagUtils.getTag(),"course List Response:-"+response);
        try {
            Gson gson = new Gson();
            CourcesPOJO courcesPOJO = gson.fromJson(response, CourcesPOJO.class);
            if (courcesPOJO.getSuccess().equals("true")) {
                StudentCourseAdapter courceAdapter = new StudentCourseAdapter(this, courcesPOJO.getCourcesPOJOList(), false);
                LinearLayoutManager horizontalLayoutManagaer
                        = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rv_course.setLayoutManager(horizontalLayoutManagaer);
                rv_course.setHasFixedSize(true);
                rv_course.setItemAnimator(new DefaultItemAnimator());
                rv_course.setAdapter(courceAdapter);
            } else {
                ToastClass.showShortToast(getApplicationContext(), "No Courses Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), "No Courses Found");
        }
    }
}
