package com.sundroid.capri4physio.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.activity.AttendanceDateActivity;
import com.sundroid.capri4physio.activity.CourseActivity;
import com.sundroid.capri4physio.activity.IssueCertifcateActivity;
import com.sundroid.capri4physio.activity.ListCourseStudentsActivity;
import com.sundroid.capri4physio.activity.UpdateCourceActivity;
import com.sundroid.capri4physio.pojo.cources.CourcesResultPOJO;

import java.util.List;

/**
 * Created by sunil on 26-05-2017.
 */

public class CourceAdapter extends RecyclerView.Adapter<CourceAdapter.MyViewHolder> {

    private List<CourcesResultPOJO> horizontalList;
    private Activity activity;
    private final String TAG = getClass().getSimpleName();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_cource_name;
        public TextView tv_seat_available;
        public TextView tv_dates;
        public TextView tv_place;
        public LinearLayout ll_cource;

        public MyViewHolder(View view) {
            super(view);
            tv_cource_name = (TextView) view.findViewById(R.id.tv_cource_name);
            tv_seat_available = (TextView) view.findViewById(R.id.tv_seat_available);
            tv_dates = (TextView) view.findViewById(R.id.tv_dates);
            tv_place = (TextView) view.findViewById(R.id.tv_place);
            ll_cource = (LinearLayout) view.findViewById(R.id.ll_cource);
        }
    }


    public CourceAdapter(Activity activity, List<CourcesResultPOJO> horizontalList) {
        this.horizontalList = horizontalList;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_cources, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_cource_name.setText(horizontalList.get(position).getName());
        holder.tv_seat_available.setText(horizontalList.get(position).getShowingSeat()+ " seats available");
        holder.tv_dates.setText(horizontalList.get(position).getFromDate()+" - "+horizontalList.get(position).getToDate());
        holder.tv_place.setText(horizontalList.get(position).getPlace());
        holder.ll_cource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCourseMenu(horizontalList.get(position));
            }
        });
    }

    public void showCourseMenu(final CourcesResultPOJO courcesResultPOJO){
        final Dialog dialog1 = new Dialog(activity, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_course_menu);
        dialog1.setTitle("Select");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        Button btn_cancel= (Button) dialog1.findViewById(R.id.btn_cancel);
        Button btn_view_students= (Button) dialog1.findViewById(R.id.btn_view_students);
        Button btn_update_course= (Button) dialog1.findViewById(R.id.btn_update_course);
        Button btn_delete_course= (Button) dialog1.findViewById(R.id.btn_delete_course);
        Button btn_attendance= (Button) dialog1.findViewById(R.id.btn_attendance);
        Button btn_issue_certificate= (Button) dialog1.findViewById(R.id.btn_issue_certificate);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        btn_view_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity, ListCourseStudentsActivity.class);
                intent.putExtra("cource",courcesResultPOJO);
                activity.startActivity(intent);
                dialog1.dismiss();
            }
        });
        btn_update_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                Intent intent=new Intent(activity, UpdateCourceActivity.class);
                intent.putExtra("cource",courcesResultPOJO);
                activity.startActivity(intent);
            }
        });
        btn_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                Intent intent=new Intent(activity, AttendanceDateActivity.class);
                intent.putExtra("course_id",courcesResultPOJO.getId());
                activity.startActivity(intent);
            }
        });
        btn_issue_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                Intent intent=new Intent(activity, IssueCertifcateActivity.class);
                intent.putExtra("courcesResultPOJO",courcesResultPOJO);
                activity.startActivity(intent);
            }
        });
//
        btn_delete_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                showAlertDialog(courcesResultPOJO);
            }
        });

    }

    public void showAlertDialog(final CourcesResultPOJO courcesResultPOJO){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage("Do you want to delete the course?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if(activity instanceof CourseActivity){
                            CourseActivity courseActivity= (CourseActivity) activity;
                            courseActivity.callCourseDeleteAPI(courcesResultPOJO);
                        }
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
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
