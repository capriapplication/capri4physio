package com.sundroid.capri4physio.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sundroid.capri4physio.ChatActivity;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.pojo.user.StudentUserPOJO;

import java.util.List;

/**
 * Created by sunil on 26-05-2017.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.MyViewHolder> {

    private List<StudentUserPOJO> horizontalList;
    private Activity activity;
    String user_id;
    private final String TAG = getClass().getSimpleName();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_email;
        public LinearLayout ll_student;

        public MyViewHolder(View view) {
            super(view);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_email = (TextView) view.findViewById(R.id.tv_email);
            ll_student = (LinearLayout) view.findViewById(R.id.ll_student);

        }
    }


    public StudentListAdapter(Activity activity,List<StudentUserPOJO> horizontalList,String user_id) {
        this.horizontalList = horizontalList;
        this.activity = activity;
        this.user_id=user_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_user_student, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_name.setText(horizontalList.get(position).getFirstName()+" "+horizontalList.get(position).getLastName());
        holder.tv_email.setText(horizontalList.get(position).getEmail());

        holder.ll_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(activity, ChatActivity.class);
                intent.putExtra("user_id","A");
                intent.putExtra("fri_id",horizontalList.get(position).getUserPOJO().getUserId());
                intent.putExtra("name",horizontalList.get(position).getFirstName()+" "+horizontalList.get(position).getLastName());
                intent.putExtra("is_admin",true);

                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (horizontalList != null) {
            return horizontalList.size();
        } else {
            return 0;
        }
    }
}
