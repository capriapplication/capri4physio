package com.sundroid.capri4physio.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.activity.ListCourseStudentsActivity;
import com.sundroid.capri4physio.pojo.cources.CourcesResultPOJO;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCourseResultPOJO;

import java.util.List;

/**
 * Created by sunil on 26-05-2017.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder> {

    private List<StudentCourseResultPOJO> horizontalList;
    private Activity activity;
    private CourcesResultPOJO courcesResultPOJO;
    private final String TAG = getClass().getSimpleName();


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_applied_date,tv_id;
        public TextView tv_student_name;
        public ImageView iv_menu;
        public LinearLayout ll_students;

        public MyViewHolder(View view) {
            super(view);
            tv_applied_date = (TextView) view.findViewById(R.id.tv_applied_date);
            tv_id = (TextView) view.findViewById(R.id.tv_id);
            tv_student_name = (TextView) view.findViewById(R.id.tv_student_name);
            iv_menu = (ImageView) view.findViewById(R.id.iv_menu);
            ll_students = (LinearLayout) view.findViewById(R.id.ll_students);

        }
    }


    public StudentAdapter(Activity activity, CourcesResultPOJO courcesResultPOJO, List<StudentCourseResultPOJO> horizontalList) {
        this.horizontalList = horizontalList;
        this.courcesResultPOJO = courcesResultPOJO;
        this.activity = activity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_students, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv_applied_date.setText(horizontalList.get(position).getStuDate()+" "+horizontalList.get(position).getStuTime());
        holder.tv_student_name.setText(horizontalList.get(position).getFirstName()+" "+horizontalList.get(position).getLastName());
        holder.tv_id.setText(horizontalList.get(position).getStuId()+" : ");
        holder.ll_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListCourseStudentsActivity listCourseStudentsActivity= (ListCourseStudentsActivity) activity;
                listCourseStudentsActivity.ViewStudentDetails(horizontalList.get(position));
            }
        });
        holder.iv_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu menu = new PopupMenu(activity, view);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        switch (menuitem.getItemId()) {
                            case R.id.popup_certificate:
//                                Intent intent=new Intent(activity, IncomeReportPrintActivity.class);
//                                intent.putExtra("type","CERTIFICATE");
//                                intent.putExtra("student_name",horizontalList.get(position).getScSname());
//                                intent.putExtra("course_name",courcesResultPOJO.getC_name());
//                                intent.putExtra("duration","Total of 8 days, duration 64 hours");
//                                intent.putExtra("exams","(12 hours theory and 52 hours practical)");
//                                intent.putExtra("venu","held in "+courcesResultPOJO.getC_place());
//                                intent.putExtra("dates","from "+courcesResultPOJO.getC_from_date()+" to "+courcesResultPOJO.getC_to_date());
//                                activity.startActivity(intent);
                                break;
                            case R.id.popup_id_card:

//                                Intent intent1 = new Intent(activity, IncomeReportPrintActivity.class);
//                                intent1.putExtra("type", "IDCARD");
//                                intent1.putExtra("id", horizontalList.get(position).getScSid());
//                                intent1.putExtra("student_name",horizontalList.get(position).getScSname());
//                                intent1.putExtra("comt", courcesResultPOJO.getC_comt());
//                                intent1.putExtra("date", courcesResultPOJO.getC_from_date());
//                                activity.startActivity(intent1);
                                break;
                        }
                        return false;
                    }
                });
                menu.inflate(R.menu.menu_student);
                menu.show();
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
