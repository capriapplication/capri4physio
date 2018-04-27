package com.sundroid.capri4physio.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.activity.CourseStudentListActivity;
import com.sundroid.capri4physio.pojo.attendance.AttendencePOJO;

import java.util.List;

/**
 * Created by sunil on 03-11-2017.
 */

public class AttendenceDateAdapter extends RecyclerView.Adapter<AttendenceDateAdapter.ViewHolder> {
    private List<AttendencePOJO> items;
    Activity activity;
    Fragment fragment;

    public AttendenceDateAdapter(Activity activity, Fragment fragment, List<AttendencePOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public AttendenceDateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_attendence_date, parent, false);
        return new AttendenceDateAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AttendenceDateAdapter.ViewHolder holder, final int position) {

        holder.tv_date.setText(items.get(position).getDate());

        holder.ll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, CourseStudentListActivity.class);
                intent.putExtra("course_id", items.get(position).getC_id());
                intent.putExtra("a_id", items.get(position).getA_id());
                intent.putExtra("date", items.get(position).getDate());
                intent.putExtra("is_updating", true);
                activity.startActivity(intent);
            }
        });

        holder.itemView.setTag(items.get(position));
    }


    private final String TAG = getClass().getSimpleName();

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_date;
        public LinearLayout ll_date;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            ll_date = (LinearLayout) itemView.findViewById(R.id.ll_date);
        }
    }
}
