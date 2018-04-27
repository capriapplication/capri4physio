package com.sundroid.capri4physio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.sundroid.capri4physio.ChatActivity;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.Pref;
import com.sundroid.capri4physio.Util.StringUtils;
import com.sundroid.capri4physio.pojo.user.StudentUserPOJO;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentDashboardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_book)
    RelativeLayout rl_book;
    @BindView(R.id.rl_chat)
    RelativeLayout rl_chat;
    @BindView(R.id.rl_logout)
    RelativeLayout rl_logout;
    @BindView(R.id.rl_billing)
    RelativeLayout rl_billing;

    StudentUserPOJO studentUserPOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        studentUserPOJO= (StudentUserPOJO) getIntent().getSerializableExtra("studentPOJO");


        rl_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StudentDashboardActivity.this,SelectCourseActivity.class).putExtra("studentPOJO",studentUserPOJO));
            }
        });

        rl_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentDashboardActivity.this, ChatActivity.class);
                intent.putExtra("user_id",studentUserPOJO.getUserPOJO().getUserId());
                intent.putExtra("fri_id","A");
                intent.putExtra("name","Admin");
                intent.putExtra("is_admin",true);

                startActivity(intent);
            }
        });

        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_STUDENT_LOGIN,false);
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_ADMIN_LOGIN,false);
                startActivity(new Intent(StudentDashboardActivity.this,LoginActivity.class));
                finishAffinity();
            }
        });

        rl_billing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentDashboardActivity.this, BillingCourseListActivity.class);
                intent.putExtra("studentPOJO",studentUserPOJO);
                startActivity(intent);
            }
        });
    }

}
