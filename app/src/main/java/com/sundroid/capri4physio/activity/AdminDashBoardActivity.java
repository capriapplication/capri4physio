package com.sundroid.capri4physio.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.Pref;
import com.sundroid.capri4physio.Util.StringUtils;
import com.sundroid.capri4physio.pojo.user.AdminUserPOJO;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminDashBoardActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_course)
    RelativeLayout rl_course;
    @BindView(R.id.rl_chat)
    RelativeLayout rl_chat;
    @BindView(R.id.rl_logout)
    RelativeLayout rl_logout;

    AdminUserPOJO adminUserPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        adminUserPOJO= (AdminUserPOJO) getIntent().getSerializableExtra("adminPOJO");


        rl_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashBoardActivity.this,CourseActivity.class).putExtra("adminPOJO",adminUserPOJO));
            }
        });

        rl_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(AdminDashBoardActivity.this, ChatActivity.class);
//                intent.putExtra("user_id","A");
//                intent.putExtra("fri_id","2");
//                intent.putExtra("name","Shravan");
//                intent.putExtra("is_admin",true);
//
//                startActivity(intent);
                startActivity(new Intent(AdminDashBoardActivity.this,AllStudentChatActivity.class).putExtra("user_id",adminUserPOJO.getUserPOJO().getUserId()));
            }
        });

        rl_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_STUDENT_LOGIN,false);
                Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_ADMIN_LOGIN,false);
                startActivity(new Intent(AdminDashBoardActivity.this,LoginActivity.class));
                finishAffinity();
            }
        });
    }



}
