package com.sundroid.capri4physio.adapter;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.pojo.certificateIssued.CertificateIssuedResultPOJO;

import java.util.List;

/**
 * Created by sunil on 03-11-2017.
 */

public class StudentCertificateListAdapter extends RecyclerView.Adapter<StudentCertificateListAdapter.ViewHolder> {
    private List<CertificateIssuedResultPOJO> items;
    Activity activity;
    Fragment fragment;

    public StudentCertificateListAdapter(Activity activity, Fragment fragment, List<CertificateIssuedResultPOJO> items) {
        this.items = items;
        this.activity = activity;
        this.fragment = fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.inflate_student_certificate_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final StudentCertificateListAdapter.ViewHolder holder, final int position) {

        holder.tv_student_name.setText(items.get(position).getFirstName()+" "+items.get(position).getLastName());
        if(items.get(position).getIssued()){
            holder.check_student.setChecked(true);
        }else{
            holder.check_student.setChecked(false);
        }

        holder.check_student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                items.get(position).setIssued(b);
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
        public TextView tv_student_name;
        public CheckBox check_student;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_student_name=itemView.findViewById(R.id.tv_student_name);
            check_student=itemView.findViewById(R.id.check_student);
        }
    }
}
