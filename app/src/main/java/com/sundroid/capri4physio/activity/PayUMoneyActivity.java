package com.sundroid.capri4physio.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.pojo.cources.CourcesResultPOJO;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCourseResultPOJO;
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayUMoneyActivity extends AppCompatActivity implements WebServicesCallBack {

    private static final String UPDATE_STUDENT = "update_student";
    private static final String UPDATE_COURCE = "update_course";
    private static final String CALL_STUDENT_COURSE_UPDATE = "call_student_course_update";
    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.btn_pay)
    Button btn_pay;

    String url, type;
    StudentCourseResultPOJO studentCourseResultPOJO;
    CourcesResultPOJO courcesResultPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_umoney);
        ButterKnife.bind(this);

        studentCourseResultPOJO = (StudentCourseResultPOJO) getIntent().getSerializableExtra("studentcourseresultpojo");
        courcesResultPOJO = (CourcesResultPOJO) getIntent().getSerializableExtra("courseresultpojo");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");
            type = bundle.getString("type");

            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
//            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webView.getSettings().setSupportMultipleWindows(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.setWebViewClient(new AppWebViewClients(this));
            Log.d(TagUtils.getTag(), "url:-" + url);
            webView.loadUrl(url);
        } else {
            finish();
        }

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStudentFees();
            }
        });

    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case UPDATE_STUDENT:
                parseUpdateStudent(response);
                break;
            case UPDATE_COURCE:
                parseUpdateCourseActivity(response);
                break;
            case CALL_STUDENT_COURSE_UPDATE:
                parseStudentCourseUpdate(response);
                break;
        }
    }

    public void parseStudentCourseUpdate(String response) {
        Log.d(TagUtils.getTag(), "student course update:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                ToastClass.showShortToast(getApplicationContext(), "Course Updated");
                StudentCourseResultPOJO studentCourseResultPOJO = new Gson().fromJson(jsonObject.optJSONObject("result").toString(), StudentCourseResultPOJO.class);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", studentCourseResultPOJO);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                ToastClass.showShortToast(getApplicationContext(), "Course Not Updated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseUpdateCourseActivity(String response) {
        Log.d(TagUtils.getTag(), "response:-" + response);
        try {
            if (new JSONObject(response).optString("Success").equals("true")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", studentCourseResultPOJO);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
        }
    }

    public void parseUpdateStudent(String response) {
        Log.d(TagUtils.getTag(), "update student:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("Success").equals("true")) {
                String result = jsonObject.optJSONObject("Result").toString();
                Gson gson = new Gson();
                studentCourseResultPOJO = gson.fromJson(result, StudentCourseResultPOJO.class);

                if (studentCourseResultPOJO.getStuFullFees().length() > 0) {
                    updateCourse();
                } else {
                    if (studentCourseResultPOJO.getStuRegFees().length() > 0 && studentCourseResultPOJO.getStuRemFees().length() > 0) {
                        updateCourse();
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", studentCourseResultPOJO);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }

            } else {
                ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
        }
    }

    public void updateCourse() {
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("c_name", courcesResultPOJO.getName()));
            nameValuePairs.add(new BasicNameValuePair("c_comt", courcesResultPOJO.getComt()));
            nameValuePairs.add(new BasicNameValuePair("c_from_date", courcesResultPOJO.getFromDate()));
            nameValuePairs.add(new BasicNameValuePair("c_to_date", courcesResultPOJO.getToDate()));
            nameValuePairs.add(new BasicNameValuePair("c_place", courcesResultPOJO.getPlace()));
            nameValuePairs.add(new BasicNameValuePair("c_sheet_available", courcesResultPOJO.getSeatAvailable()));
            int seat_rem = Integer.parseInt(courcesResultPOJO.getRemSeat());
            seat_rem = seat_rem - 1;
            nameValuePairs.add(new BasicNameValuePair("c_rem_seat", String.valueOf(seat_rem)));
            int showing_seat = Integer.parseInt(courcesResultPOJO.getShowingSeat());

            if (showing_seat > 1) {
                showing_seat = showing_seat - 1;
            } else {
                if (showing_seat == 1) {
                    if (seat_rem > 2) {
                        showing_seat = 3;
                    } else {
                        showing_seat = seat_rem;
                    }
                }
            }
            nameValuePairs.add(new BasicNameValuePair("c_showing_sheet", String.valueOf(showing_seat)));
            nameValuePairs.add(new BasicNameValuePair("c_reg_fees", courcesResultPOJO.getRegFees()));
            nameValuePairs.add(new BasicNameValuePair("c_rem_fees", courcesResultPOJO.getRemFees()));
            nameValuePairs.add(new BasicNameValuePair("c_fees", courcesResultPOJO.getFullFees()));
            nameValuePairs.add(new BasicNameValuePair("c_pno", courcesResultPOJO.getPhone()));
            nameValuePairs.add(new BasicNameValuePair("c_id", courcesResultPOJO.getId()));
            new WebServiceBase(nameValuePairs, PayUMoneyActivity.this, this, UPDATE_COURCE, true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
//            sendNotification(studentCourseResultPOJO.getStuId(),courcesResultPOJO.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public void sendNotification(String student_id,String course_name){
//        ArrayList<NameValuePair> arrayList=new ArrayList<>();
//        arrayList.add(new BasicNameValuePair("student_id",student_id));
//        arrayList.add(new BasicNameValuePair("course_name",course_name));
//        new WebServiceBase(arrayList,this,"send_notification").execute(ApiConfig.SEND_NOTIFICATION);
//    }

    public class AppWebViewClients extends WebViewClient {
        private ProgressDialog progressDialog;
        Activity activity;

        public AppWebViewClients(Activity activity) {
            this.activity = activity;
//            progressDialog = new ProgressDialog(activity);
//            progressDialog.setMessage("Please Wait...");
//            progressDialog.setCancelable(true);
//            progressDialog.show();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            Log.d(TagUtils.getTag(), "url:-" + url);
            view.loadUrl(url);
            if (url.contains("payumoney/success.php")) {
                ToastClass.showShortToast(getApplicationContext(), "successx ");
                Log.d(TagUtils.getTag(), "getting pgs response");
//                callCashOnDeliveryAPI(addressDataPOJO);
            } else {
                if (url.contains("Paytmgetway/fail.php")) {
//                    ToastClass.showLongToast(getApplicationContext(),"Payment Failed");
//                    startActivity(new Intent(OnlinePaymentActivity.this, HomeActivity.class));
//                    finishAffinity();
                }
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
//                view.loadUrl(url);
            if (url.equals("http://www.oldmaker.com/fijiyo/payumoney/success.php")) {
                updateStudentFees();
            }
            Log.d(TagUtils.getTag(), "url done:-" + url);
            Log.d(TagUtils.getTag(), "done loading");

        }
    }


    public void updateStudentFees() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        switch (type) {
            case "reg":
                nameValuePairs.add(new BasicNameValuePair("request_action", "update_reg_fees"));
                nameValuePairs.add(new BasicNameValuePair("stu_reg_fees", "online"));
                break;
            case "rem":
                nameValuePairs.add(new BasicNameValuePair("request_action", "update_rem_fees"));
                nameValuePairs.add(new BasicNameValuePair("stu_rem_fees", "online"));
                break;
            case "full":
                nameValuePairs.add(new BasicNameValuePair("request_action", "update_full_fees"));
                nameValuePairs.add(new BasicNameValuePair("stu_full_fees", "online"));
                break;

        }
        nameValuePairs.add(new BasicNameValuePair("c_id", studentCourseResultPOJO.getcId()));
        nameValuePairs.add(new BasicNameValuePair("stu_id", studentCourseResultPOJO.getStuId()));

        new WebServiceBase(nameValuePairs, this, this, CALL_STUDENT_COURSE_UPDATE, true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
    }


    public void updateStudent() {


//        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("sc_sid", studentCourseResultPOJO.getScSid()));
//        nameValuePairs.add(new BasicNameValuePair("sc_sname", studentCourseResultPOJO.getScSname()));
//        nameValuePairs.add(new BasicNameValuePair("sc_semail", studentCourseResultPOJO.getScSemail()));
//        nameValuePairs.add(new BasicNameValuePair("sc_sapplicationform_fill", studentCourseResultPOJO.getScSapplicationformFill()));
//        nameValuePairs.add(new BasicNameValuePair("sc_photo_upload", studentCourseResultPOJO.getScPhotoUpload()));
//        nameValuePairs.add(new BasicNameValuePair("sc_cerifiato_upload", studentCourseResultPOJO.getScCerifiatoUpload()));
//        nameValuePairs.add(new BasicNameValuePair("sc_idcard_upload", studentCourseResultPOJO.getScIdcardUpload()));
//        if(type.equals("reg")){
//            nameValuePairs.add(new BasicNameValuePair("sc_reg_fees", "online"));
//        }else {
//            nameValuePairs.add(new BasicNameValuePair("sc_reg_fees", studentCourseResultPOJO.getScRegFees()));
//        }
//        if(type.equals("rem")){
//            nameValuePairs.add(new BasicNameValuePair("sc_rem_fees", "online"));
//        }else {
//            nameValuePairs.add(new BasicNameValuePair("sc_rem_fees", studentCourseResultPOJO.getScRemFees()));
//        }
//        if(type.equals("full")){
//            nameValuePairs.add(new BasicNameValuePair("sc_fullfees", "online"));
//        }else {
//            nameValuePairs.add(new BasicNameValuePair("sc_fullfees", studentCourseResultPOJO.getScFullfees()));
//        }
//        nameValuePairs.add(new BasicNameValuePair("sc_cid", studentCourseResultPOJO.getScCid()));
//        nameValuePairs.add(new BasicNameValuePair("sc_id", studentCourseResultPOJO.getScId()));
//        nameValuePairs.add(new BasicNameValuePair("sc_date", studentCourseResultPOJO.getSc_date()));
//        nameValuePairs.add(new BasicNameValuePair("sc_time", studentCourseResultPOJO.getSc_time()));
//        nameValuePairs.add(new BasicNameValuePair("admin_application_form", studentCourseResultPOJO.getAdminApplicationForm()));
//        nameValuePairs.add(new BasicNameValuePair("admin_photo_upload", studentCourseResultPOJO.getAdminPhotoUpload()));
//        nameValuePairs.add(new BasicNameValuePair("admin_certificate_upload", studentCourseResultPOJO.getAdminCertificateUpload()));
//        nameValuePairs.add(new BasicNameValuePair("admin_icard", studentCourseResultPOJO.getAdminIcard()));
//        nameValuePairs.add(new BasicNameValuePair("admin_status", studentCourseResultPOJO.getAdminStatus()));
//        nameValuePairs.add(new BasicNameValuePair("admin_reg_fees", studentCourseResultPOJO.getAdminRegFees()));
//        nameValuePairs.add(new BasicNameValuePair("sc_admin_rem_fees", studentCourseResultPOJO.getSc_admin_rem_fees()));
//        nameValuePairs.add(new BasicNameValuePair("admin_full_fees", studentCourseResultPOJO.getAdminFullFees()));
//        new WebServiceBase(nameValuePairs, this, UPDATE_STUDENT).execute(ApiConfig.update_student_course);

    }

}
