package com.sundroid.capri4physio.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.activity.CourseStudentListActivity;
import com.sundroid.capri4physio.pojo.attendedstudent.AttendedStudentResultPOJO;

import java.util.List;

/**
 * Created by sunil on 03-11-2017.
 */

public class StudentCheckAdapter extends RecyclerView.Adapter<StudentCheckAdapter.ViewHolder> {
    private List<AttendedStudentResultPOJO> items;
    Activity activity;
    Fragment fragment;
    boolean is_updating;
    public StudentCheckAdapter(Activity activity, Fragment fragment, List<AttendedStudentResultPOJO> items, boolean is_updating) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
        this.is_updating=is_updating;
    }

    @Override
    public StudentCheckAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_student_checks, parent, false);
        return new StudentCheckAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StudentCheckAdapter.ViewHolder holder, final int position) {
        holder.check_student.setText(items.get(position).getFirstName()+" "+items.get(position).getLastName());

        holder.check_student.setChecked(items.get(position).getAttended());

        holder.check_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(activity instanceof CourseStudentListActivity){
                    CourseStudentListActivity courseStudentListActivity= (CourseStudentListActivity) activity;
                    items.get(position).setAttended(b);
//                    if(b){
//                        courseStudentListActivity.checkedStudents.add(String.valueOf(items.get(position).getId()));
//                    }else{
//                        if(courseStudentListActivity.checkedStudents.contains(String.valueOf(items.get(position).getId()))){
//                            courseStudentListActivity.checkedStudents.remove(String.valueOf(items.get(position).getId()));
//                        }
//                    }
                }
            }
        });

//        if(is_updating){
//            if(items.get(position).getIs_present().equals("true")){
//                holder.check_student.setChecked(true);
//            }
//        }

        holder.itemView.setTag(items.get(position));
    }


    private final String TAG = getClass().getSimpleName();

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox check_student;
        public ViewHolder(View itemView) {
            super(itemView);
            check_student = (CheckBox) itemView.findViewById(R.id.check_student);
        }
    }
}
