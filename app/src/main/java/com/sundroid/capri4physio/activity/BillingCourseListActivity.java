package com.sundroid.capri4physio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.adapter.StudentCourseResultAdapter;
import com.sundroid.capri4physio.pojo.ResponseListPOJO;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCourseResultPOJO;
import com.sundroid.capri4physio.pojo.user.StudentUserPOJO;
import com.sundroid.capri4physio.webservice.ResponseListCallback;
import com.sundroid.capri4physio.webservice.ResponseListWebservice;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BillingCourseListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_course)
    RecyclerView rv_course;

    StudentUserPOJO studentUserPOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing_course_list);
        ButterKnife.bind(this);

        studentUserPOJO= (StudentUserPOJO) getIntent().getSerializableExtra("studentPOJO");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Course List");

        getAllCourses();
    }

    public void getAllCourses(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action","get_registered_course"));
        nameValuePairs.add(new BasicNameValuePair("stu_id",studentUserPOJO.getId()));
        new ResponseListWebservice<StudentCourseResultPOJO>(nameValuePairs, this, new ResponseListCallback<StudentCourseResultPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<StudentCourseResultPOJO> responseListPOJO) {
                try{
                    if(responseListPOJO.isSuccess()){
                        StudentCourseResultAdapter courceAdapter = new StudentCourseResultAdapter(BillingCourseListActivity.this, responseListPOJO.getResultList(), true);
                        LinearLayoutManager horizontalLayoutManagaer
                                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                        rv_course.setLayoutManager(horizontalLayoutManagaer);
                        rv_course.setHasFixedSize(true);
                        rv_course.setItemAnimator(new DefaultItemAnimator());
                        rv_course.setAdapter(courceAdapter);
                    }else{
                        ToastClass.showShortToast(getApplicationContext(),responseListPOJO.getMessage());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },StudentCourseResultPOJO.class,"GET_ALL_COURSE",true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
