package com.sundroid.capri4physio.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.adapter.StudentCertificateListAdapter;
import com.sundroid.capri4physio.pojo.certificateIssued.CertificateIssuedPOJO;
import com.sundroid.capri4physio.pojo.certificateIssued.CertificateIssuedResultPOJO;
import com.sundroid.capri4physio.pojo.cources.CourcesResultPOJO;
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IssueCertifcateActivity extends AppCompatActivity implements WebServicesCallBack {
    private static final String CALL_GET_STUDENT_API = "call_get_students_api";
    private static final String CALL_ISSUE_CERTIFICATE = "call_issue_certificate";
    @BindView(R.id.btn_update)
    Button btn_update;
    @BindView(R.id.rv_students)
    RecyclerView rv_students;
    CourcesResultPOJO courcesResultPOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_certifcate);
        ButterKnife.bind(this);

        courcesResultPOJO= (CourcesResultPOJO) getIntent().getSerializableExtra("courcesResultPOJO");
        attachAdapter();

        callStudentsGetAPI();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String generateJson=generateJson();
                Log.d(TagUtils.getTag(),"generated json:-"+generateJson);
                updateStudents(generateJson);
            }
        });
    }

    public void updateStudents(String student_json){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action","update_student_certificate"));
        nameValuePairs.add(new BasicNameValuePair("students",student_json));
        nameValuePairs.add(new BasicNameValuePair("c_id",courcesResultPOJO.getId()));
        new WebServiceBase(nameValuePairs,this,this,CALL_ISSUE_CERTIFICATE,true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
    }

    public String generateJson(){
        try {
            JSONObject jsonObject=new JSONObject();
            JSONArray jsonArray=new JSONArray();
            for (CertificateIssuedResultPOJO studentCertificateResultPOJO:studentCertificateResultPOJOList){
                if(studentCertificateResultPOJO.getIssued()) {
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id", studentCertificateResultPOJO.getStuId());
                    jsonArray.put(jsonObject1);
                }
            }
            jsonObject.put("students",jsonArray);
            return jsonObject.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    public void callStudentsGetAPI(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action","get_certificate_issued_students"));
        nameValuePairs.add(new BasicNameValuePair("c_id",courcesResultPOJO.getId()));
        new WebServiceBase(nameValuePairs,this,this,CALL_GET_STUDENT_API,true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
    }


    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case CALL_GET_STUDENT_API:
                parseStudentGetResponse(response);
                break;
            case CALL_ISSUE_CERTIFICATE:
                parseCertificateIssued(response);
                break;
        }
    }

    public void parseCertificateIssued(String response){
        Log.d(TagUtils.getTag(),"certificate issued response:-"+response);
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString("success").equals("true")){
                ToastClass.showShortToast(getApplicationContext(),"Certificate Issued Successfully");
                finish();
            }else{
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(),"Something went wrong in Connection");
        }
    }

    StudentCertificateListAdapter studentCertificateListAdapter;
    List<CertificateIssuedResultPOJO> studentCertificateResultPOJOList=new ArrayList<>();
    public void attachAdapter() {

        studentCertificateListAdapter=new StudentCertificateListAdapter(this,null,studentCertificateResultPOJOList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);

        rv_students.setHasFixedSize(true);
        rv_students.setAdapter(studentCertificateListAdapter);
        rv_students.setLayoutManager(linearLayoutManager);
        rv_students.setNestedScrollingEnabled(false);
        rv_students.setItemAnimator(new DefaultItemAnimator());
    }

    public void parseStudentGetResponse(String response){
        Log.d(TagUtils.getTag(),"student response:-"+response);
        try{
            Gson gson=new Gson();
            CertificateIssuedPOJO studentCertificatePOJO=gson.fromJson(response,CertificateIssuedPOJO.class);
            if(studentCertificatePOJO.getSuccess().equals("true")){
                studentCertificateResultPOJOList.addAll(studentCertificatePOJO.getCertificateIssuedResultPOJOS());
                studentCertificateListAdapter.notifyDataSetChanged();
            }else{
                ToastClass.showShortToast(getApplicationContext(),"No Students Found");
            }
        }catch (Exception e){
            ToastClass.showShortToast(getApplicationContext(),"Something wrong in the connection");
            e.printStackTrace();
        }
    }
}
