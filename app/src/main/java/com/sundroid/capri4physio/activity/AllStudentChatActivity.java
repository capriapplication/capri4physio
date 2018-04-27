package com.sundroid.capri4physio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.adapter.StudentListAdapter;
import com.sundroid.capri4physio.pojo.ResponseListPOJO;
import com.sundroid.capri4physio.pojo.user.StudentUserPOJO;
import com.sundroid.capri4physio.webservice.ResponseListCallback;
import com.sundroid.capri4physio.webservice.ResponseListWebservice;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllStudentChatActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_students)
    RecyclerView rv_students;

    String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_student_chat);
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            user_id=bundle.getString("user_id");
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        attachAdapter();
        getAllStudents();
    }

    public void getAllStudents(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action","get_all_students"));
        new ResponseListWebservice<StudentUserPOJO>(nameValuePairs, this, new ResponseListCallback<StudentUserPOJO>() {
            @Override
            public void onGetMsg(ResponseListPOJO<StudentUserPOJO> responseListPOJO) {
                try{
                    if(responseListPOJO.isSuccess()){
                        studentUserPOJOS.clear();
                        studentUserPOJOS.addAll(responseListPOJO.getResultList());
                        studentListAdapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },StudentUserPOJO.class,"ALL_STUDENTS",true).execute(WebServicesUrls.CHAT_CRUD);
    }

    StudentListAdapter studentListAdapter;
    List<StudentUserPOJO> studentUserPOJOS=new ArrayList<>();
    public void attachAdapter() {
        studentListAdapter = new StudentListAdapter(this, studentUserPOJOS,user_id);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_students.setHasFixedSize(true);
        rv_students.setAdapter(studentListAdapter);
        rv_students.setLayoutManager(layoutManager);
        rv_students.setItemAnimator(new DefaultItemAnimator());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
