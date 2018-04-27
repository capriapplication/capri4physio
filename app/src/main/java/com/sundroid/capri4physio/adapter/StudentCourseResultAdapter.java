package com.sundroid.capri4physio.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.activity.SelectCourseActivity;
import com.sundroid.capri4physio.activity.StudentBillActivity;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCourseResultPOJO;

import java.util.List;

/**
 * Created by sunil on 26-05-2017.
 */

public class StudentCourseResultAdapter extends RecyclerView.Adapter<StudentCourseResultAdapter.MyViewHolder> {

    private List<StudentCourseResultPOJO> horizontalList;
    private Activity activity;
    private final String TAG = getClass().getSimpleName();
    private boolean is_billing=false;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_cource_name;
        public TextView tv_seat_available;
        public TextView tv_dates;
        public TextView tv_place, tv_call;
        public LinearLayout ll_cource;

        public MyViewHolder(View view) {
            super(view);
            tv_cource_name = (TextView) view.findViewById(R.id.tv_cource_name);
            tv_seat_available = (TextView) view.findViewById(R.id.tv_seat_available);
            tv_dates = (TextView) view.findViewById(R.id.tv_dates);
            tv_place = (TextView) view.findViewById(R.id.tv_place);
            tv_call = (TextView) view.findViewById(R.id.tv_call);
            ll_cource = (LinearLayout) view.findViewById(R.id.ll_cource);
        }
    }


    public StudentCourseResultAdapter(Activity activity, List<StudentCourseResultPOJO> horizontalList, boolean is_billing) {
        this.horizontalList = horizontalList;
        this.activity = activity;
        this.is_billing=is_billing;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_cources, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_cource_name.setText(horizontalList.get(position).getCourcesResultPOJO().getName());
        holder.tv_seat_available.setText(horizontalList.get(position).getCourcesResultPOJO().getShowingSeat() + " seats available");
        holder.tv_dates.setText(horizontalList.get(position).getCourcesResultPOJO().getFromDate() + " - " + horizontalList.get(position).getCourcesResultPOJO().getToDate());
        holder.tv_place.setText(horizontalList.get(position).getCourcesResultPOJO().getPlace());
        holder.ll_cource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TagUtils.getTag(),"is billing:-"+is_billing);
                if(is_billing) {
                    Intent intent=new Intent(activity, StudentBillActivity.class);
                    intent.putExtra("studentCourseResultPOJO",horizontalList.get(position));
                    activity.startActivity(intent);
                }
                else{
                    try {
                        int seat_avail = Integer.parseInt(horizontalList.get(position).getCourcesResultPOJO().getRemSeat());
                        if (seat_avail > 0) {
                            SelectCourseActivity selectCourseActivity = (SelectCourseActivity) activity;
                            selectCourseActivity.checkAppliedCourse(horizontalList.get(position).getCourcesResultPOJO());
                        } else {
                            ToastClass.showShortToast(activity.getApplicationContext(), "No Seat Available you may apply for waiting list please contact 9063121212");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        holder.tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + horizontalList.get(position).getCourcesResultPOJO().getPhone()));
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    activity.startActivity(intent);

                }
                catch (Exception e){
                    e.printStackTrace();
                    ToastClass.showShortToast(activity.getApplicationContext(),"Failed to place call");
                }
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
