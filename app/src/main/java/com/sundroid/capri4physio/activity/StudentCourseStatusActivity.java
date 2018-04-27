package com.sundroid.capri4physio.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.FileUtils;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.Util.UtilityFunction;
import com.sundroid.capri4physio.pojo.cources.CourcesResultPOJO;
import com.sundroid.capri4physio.pojo.studentapplication.ApplicationFormResultPOJO;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCourseResultPOJO;
import com.sundroid.capri4physio.pojo.user.StudentUserPOJO;
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;
import com.sundroid.capri4physio.webservice.WebUploadService;

import org.apache.http.NameValuePair;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentCourseStatusActivity extends AppCompatActivity implements WebServicesCallBack, View.OnClickListener {
    private static final String PHOTO_UPLOAD_API = "photo_upload_api";
    private static final String APPLY_COURSE_API = "apply_course_api";
    private static final String GET_APPLICATION_FORM_DATA = "get_application_form_data";
    private static final String UPDATE_STUDENT = "update_student_course_api";
    private static final String CALL_COURSE_CERTIFICATE_STATUS = "call_course_certificate_status";
    private static final String CALL_REMOVE_APPLICATION = "call_remove_application";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.et_s_name)
    EditText et_s_name;
    @BindView(R.id.et_s_email)
    EditText et_s_email;
    @BindView(R.id.iv_application_status)
    ImageView iv_application_status;
    @BindView(R.id.iv_photo_upload)
    ImageView iv_photo_upload;
    @BindView(R.id.iv_id_card_upload)
    ImageView iv_id_card_upload;
    @BindView(R.id.iv_registration_fees)
    ImageView iv_registration_fees;
    @BindView(R.id.iv_rem_fees)
    ImageView iv_rem_fees;
    @BindView(R.id.iv_full_fees)
    ImageView iv_full_fees;

    @BindView(R.id.iv_certificatie_upload)
    ImageView iv_certificatie_upload;
    @BindView(R.id.tv_status)
    TextView tv_status;
    @BindView(R.id.tv_application_form)
    TextView tv_application_form;
    @BindView(R.id.tv_photo)
    TextView tv_photo;
    @BindView(R.id.tv_certificate)
    TextView tv_certificate;
    @BindView(R.id.tv_registration_fees)
    TextView tv_registration_fees;
    @BindView(R.id.tv_remaining_fees)
    TextView tv_remaining_fees;
    @BindView(R.id.tv_full_fees)
    TextView tv_full_fees;
    @BindView(R.id.tv_id_card)
    TextView tv_id_card;
    @BindView(R.id.cv_download_certificate)
    CardView cv_download_certificate;
    @BindView(R.id.cv_idcard)
    CardView cv_idcard;

    public final static int PHOTO_TYPE = 0;
    public final static int CERTIFICATE_TYPE = 1;
    public final static int ID_CARD_TYPE = 2;

    int IMAGE_TYPE = -1;

    StudentCourseResultPOJO studentCourseResultPOJO;
    CourcesResultPOJO courcesResultPOJO;
    StudentUserPOJO studentUserPOJO;


    private final static int APPLICATION_FORM_ACIVITY_RESULT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_status);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        studentCourseResultPOJO = (StudentCourseResultPOJO) getIntent().getSerializableExtra("studentcourseresultpojo");
        courcesResultPOJO = (CourcesResultPOJO) getIntent().getSerializableExtra("coursepojo");
        studentUserPOJO = (StudentUserPOJO) getIntent().getSerializableExtra("studentPOJO");

        Log.d(TagUtils.getTag(), "student course result pojo:-" + studentCourseResultPOJO);
        checkStatus();

        tv_application_form.setOnClickListener(this);
        tv_photo.setOnClickListener(this);
        tv_certificate.setOnClickListener(this);
        tv_registration_fees.setOnClickListener(this);
        tv_remaining_fees.setOnClickListener(this);
        tv_full_fees.setOnClickListener(this);
        tv_id_card.setOnClickListener(this);


        cv_download_certificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCertificateStatus();
            }
        });
        cv_idcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), IncomeReportPrintActivity.class);
                intent.putExtra("type", "IDCARD");
                intent.putExtra("id", studentCourseResultPOJO.getStuId());
                intent.putExtra("student_name", studentCourseResultPOJO.getFirstName()+" "+studentCourseResultPOJO.getLastName());
                intent.putExtra("comt", courcesResultPOJO.getComt());
                intent.putExtra("date", UtilityFunction.getFormattedDate(courcesResultPOJO.getFromDate()));
                intent.putExtra("course_id", studentCourseResultPOJO.getcId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getRegisterationData();
    }

    public void getRegisterationData(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action","get_registration"));
        nameValuePairs.add(new BasicNameValuePair("c_id",courcesResultPOJO.getId()));
        nameValuePairs.add(new BasicNameValuePair("stu_id",studentUserPOJO.getId()));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String[] msg) {
                try{
                    JSONObject jsonObject=new JSONObject(msg[1]);
                    if(jsonObject.optBoolean("success")){
                        StudentCourseResultPOJO studentCourseResultPOJO=new Gson().fromJson(jsonObject.optJSONObject("result").toString(),StudentCourseResultPOJO.class);
                        StudentCourseStatusActivity.this.studentCourseResultPOJO=studentCourseResultPOJO;
                        checkStatus();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },"GET_REGISTERATION_DATA",true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_application_form:
                checkApplicationFormStatus();
                break;
            case R.id.tv_photo:
                showUploadSelectDialog(PHOTO_TYPE);
                break;
            case R.id.tv_certificate:
                showUploadSelectDialog(CERTIFICATE_TYPE);
                break;
            case R.id.tv_id_card:
                showUploadSelectDialog(ID_CARD_TYPE);
                break;
            case R.id.tv_registration_fees:
                showFeesDialog("reg");
                break;
            case R.id.tv_remaining_fees:
                if (studentCourseResultPOJO.getStuRegFees().length() > 0) {
                    showFeesDialog("rem");
                } else {
                    ToastClass.showShortToast(getApplicationContext(), "Please Submit Registration Fees First");
                    showFeesDialog("reg");
                }
                break;
            case R.id.tv_full_fees:
                if (studentCourseResultPOJO.getStuRegFees().length() == 0) {
                    showFeesDialog("full");
                } else {
                    ToastClass.showShortToast(getApplicationContext(), "you have already submitted registration fees");
                }
                break;

        }
    }

    //
//    public String getFormattedDate(String strDate) {
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
//            Date date = sdf.parse(strDate);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//            return simpleDateFormat.format(date);
//        } catch (Exception e) {
//
//        }
//        return strDate;
//    }
//
    public void checkCertificateStatus() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "check_certificate_status"));
        nameValuePairs.add(new BasicNameValuePair("course_id", courcesResultPOJO.getId()));
        nameValuePairs.add(new BasicNameValuePair("stu_id", studentCourseResultPOJO.getStuId()));
        new WebServiceBase(nameValuePairs, this, new WebServicesCallBack() {
            @Override
            public void onGetMsg(String[] msg) {
                try{
                    JSONObject jsonObject=new JSONObject(msg[1]);
                    if(jsonObject.optBoolean("success")){
//                        download certificate
                        Intent intent = new Intent(getApplicationContext(), IncomeReportPrintActivity.class);
                        intent.putExtra("type", "CERTIFICATE");
                        intent.putExtra("id", studentCourseResultPOJO.getStuId());
                        intent.putExtra("student_name", studentCourseResultPOJO.getFirstName().toUpperCase().toString()+" "+studentCourseResultPOJO.getLastName().toUpperCase().toString());
                        intent.putExtra("course_name", courcesResultPOJO.getName());
                        intent.putExtra("duration", "Total of 8 days, duration 64 hours");
                        intent.putExtra("exams", "(12 hours theory and 52 hours practical)");
                        intent.putExtra("venu", "held in " + courcesResultPOJO.getPlace());
                        intent.putExtra("dates", "from " + UtilityFunction.getFormattedDate(courcesResultPOJO.getFromDate()) + " to " + UtilityFunction.getFormattedDate(courcesResultPOJO.getToDate()));
                        startActivity(intent);
                    }else{
                        ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("result"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, "CALL_COURSE_CERTIFICATE_STATUS",true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
    }
//
//
    public void checkStatus() {
        if (studentCourseResultPOJO != null) {
            et_s_name.setText(studentCourseResultPOJO.getFirstName() + " " + studentCourseResultPOJO.getLastName());
            et_s_email.setText(studentCourseResultPOJO.getEmail());
            if (studentCourseResultPOJO.getStuAppId().equals("0")) {
                iv_application_status.setImageResource(R.drawable.ic_not_filled);
            } else {
                if (studentCourseResultPOJO.getaStuAppId().equals("0")) {
                    iv_application_status.setImageResource(R.drawable.ic_filled);
                } else {
                    iv_application_status.setImageResource(R.drawable.ic_approved);
                }
            }

            if (studentCourseResultPOJO.getPhotoUpload().length() == 0) {
                iv_photo_upload.setImageResource(R.drawable.ic_not_filled);
            } else {
                if (studentCourseResultPOJO.getaPhotoUpload().equals("0")) {
                    iv_photo_upload.setImageResource(R.drawable.ic_filled);
                } else {
                    iv_photo_upload.setImageResource(R.drawable.ic_approved);
                }
            }

            if (studentCourseResultPOJO.getCertUpload().length() == 0) {
                iv_certificatie_upload.setImageResource(R.drawable.ic_not_filled);
            } else {
                if (studentCourseResultPOJO.getaCertUpload().equals("0")) {
                    iv_certificatie_upload.setImageResource(R.drawable.ic_filled);
                } else {
                    iv_certificatie_upload.setImageResource(R.drawable.ic_approved);
                }
            }

            if (studentCourseResultPOJO.getIdUpload().length() == 0) {
                iv_id_card_upload.setImageResource(R.drawable.ic_not_filled);
            } else {
                if (studentCourseResultPOJO.getaIdUpload().equals("0")) {
                    iv_id_card_upload.setImageResource(R.drawable.ic_filled);
                } else {
                    iv_id_card_upload.setImageResource(R.drawable.ic_approved);
                }
            }
//
            if (studentCourseResultPOJO.getStuRegFees().length() == 0) {
                iv_registration_fees.setImageResource(R.drawable.ic_not_filled);
            } else {
                if (studentCourseResultPOJO.getaStuRegFees().equals("0")) {
                    iv_registration_fees.setImageResource(R.drawable.ic_filled);
                } else {
                    iv_registration_fees.setImageResource(R.drawable.ic_approved);
                }
            }
//
            if (studentCourseResultPOJO.getStuRemFees().length() == 0) {
                iv_rem_fees.setImageResource(R.drawable.ic_not_filled);
            } else {
                if (studentCourseResultPOJO.getaStuRemFees().equals("0")) {
                    iv_rem_fees.setImageResource(R.drawable.ic_filled);
                    iv_full_fees.setImageResource(R.drawable.ic_filled);
                } else {
                    iv_rem_fees.setImageResource(R.drawable.ic_approved);
                    if (studentCourseResultPOJO.getaStuRegFees().equals("1") &&
                            studentCourseResultPOJO.getaStuRemFees().equals("1")) {
                        iv_full_fees.setImageResource(R.drawable.ic_approved);
                    }
                }
            }
//            Log.d(TagUtils.getTag(), "admin full fees:-" + studentCourseResultPOJO.getAdminFullFees());
            if (studentCourseResultPOJO.getStuFullFees().length() == 0) {
                iv_full_fees.setImageResource(R.drawable.ic_not_filled);
                if (studentCourseResultPOJO.getStuRemFees().length() != 0) {
                    if (studentCourseResultPOJO.getaStuRemFees().equals("0")) {
                        iv_rem_fees.setImageResource(R.drawable.ic_filled);
                        iv_full_fees.setImageResource(R.drawable.ic_filled);
                    } else {
                        iv_rem_fees.setImageResource(R.drawable.ic_approved);
                        if (studentCourseResultPOJO.getaStuRegFees().equals("1") &&
                                studentCourseResultPOJO.getaStuRemFees().equals("1")) {
                            iv_full_fees.setImageResource(R.drawable.ic_approved);
                        }
                    }
                }
            } else {
                if (studentCourseResultPOJO.getaStuFullFees().equals("0")) {
                    iv_full_fees.setImageResource(R.drawable.ic_filled);
                    iv_rem_fees.setImageResource(R.drawable.ic_filled);
                    iv_registration_fees.setImageResource(R.drawable.ic_filled);
                } else {
                    iv_full_fees.setImageResource(R.drawable.ic_approved);
                    iv_rem_fees.setImageResource(R.drawable.ic_approved);
                    iv_registration_fees.setImageResource(R.drawable.ic_approved);
                }
            }
//
//            if (studentCourseResultPOJO.getScRemFees().length() > 0 && studentCourseResultPOJO.getScRegFees().length() > 0
//                    && studentCourseResultPOJO.getAdminRegFees().equals("true") && studentCourseResultPOJO.getSc_admin_rem_fees().equals("true")) {
//                iv_full_fees.setImageResource(R.drawable.ic_approved);
//            } else {
//                if (studentCourseResultPOJO.getScRemFees().length() > 0 && studentCourseResultPOJO.getScRegFees().length() > 0) {
//                    iv_full_fees.setImageResource(R.drawable.ic_filled);
//                }
//            }
//
//            if (studentCourseResultPOJO.getAdminFullFees().equals("true")) {
//                iv_full_fees.setImageResource(R.drawable.ic_approved);
//                iv_rem_fees.setImageResource(R.drawable.ic_approved);
//                iv_registration_fees.setImageResource(R.drawable.ic_approved);
//            }
//            tv_status.setText(studentCourseResultPOJO.getAdminStatus());
        } else {
            finish();
        }
    }

    //
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_student_course, menu);//Menu Resource, Menu
//        return true;
//    }
//
    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case PHOTO_UPLOAD_API:
                parseUploadPhotoAPiResponse(response);
                break;
            case APPLY_COURSE_API:
                parseApplyCourseApi(response);
                break;
            case GET_APPLICATION_FORM_DATA:
                parseApplicationFormData(response);
                break;
            case CALL_STUDENT_COURSE_UPDATE:
                parseStudentCourseUpdate(response);
                break;
//            case UPDATE_STUDENT:
//                parseStudentCourse(response);
//                break;
//            case CALL_COURSE_CERTIFICATE_STATUS:
//                parseStudentCertificateResponse(response);
//                break;
//            case CALL_REMOVE_APPLICATION:
//                parseRemoveApplication(response);
//                break;
        }
    }

    public void parseStudentCourseUpdate(String response) {
        Log.d(TagUtils.getTag(), "student course update:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                ToastClass.showShortToast(getApplicationContext(), "Course Updated");
                studentCourseResultPOJO = new Gson().fromJson(jsonObject.optJSONObject("result").toString(), StudentCourseResultPOJO.class);
                checkStatus();
            } else {
                ToastClass.showShortToast(getApplicationContext(), "Course Not Updated");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
//
//    public void parseRemoveApplication(String response) {
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.optString("success").equals("true")) {
//                ToastClass.showShortToast(getApplicationContext(), "Your Application has been removed");
//                finish();
//            } else {
//                ToastClass.showShortToast(getApplicationContext(), "Failed to remove Application please try again.");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
//        }
//    }
//
//
//    public void parseStudentCertificateResponse(String response) {
//        Log.d(TagUtils.getTag(), "student certificate response:-" + response);
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.optString("success").equals("true")) {
//                Intent intent = new Intent(getApplicationContext(), IncomeReportPrintActivity.class);
//                intent.putExtra("type", "CERTIFICATE");
//                intent.putExtra("id", studentCourseResultPOJO.getScSid());
//                intent.putExtra("student_name", studentCourseResultPOJO.getScSname().toUpperCase().toString());
//                intent.putExtra("course_name", courcesResultPOJO.getC_name());
//                intent.putExtra("duration", "Total of 8 days, duration 64 hours");
//                intent.putExtra("exams", "(12 hours theory and 52 hours practical)");
//                intent.putExtra("venu", "held in " + courcesResultPOJO.getC_place());
//                intent.putExtra("dates", "from " + getFormattedDate(courcesResultPOJO.getC_from_date()) + " to " + getFormattedDate(courcesResultPOJO.getC_to_date()));
//                startActivity(intent);
//            } else {
//                ToastClass.showShortToast(getApplicationContext(), "Sorry you are not currently applicable for certificate");
//            }
//        } catch (Exception e) {
//            ToastClass.showShortToast(getApplicationContext(), "Something went wrong in connection");
//        }
//    }
//
//
//    public void parseStudentCourse(String response) {
//        Log.d(TagUtils.getTag(), "update student Course api:-" + response);
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.optString("Success").equals("true")) {
//                String result = jsonObject.optJSONObject("Result").toString();
//                Gson gson = new Gson();
//                StudentCourseResultPOJO studentCourseResultPOJO = gson.fromJson(result, StudentCourseResultPOJO.class);
//                this.studentCourseResultPOJO = studentCourseResultPOJO;
//                checkStatus();
//            } else {
//                ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
//        }
//    }
//
    public void parseApplicationFormData(String response) {
        Log.d(TagUtils.getTag(), "application form add data:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                ApplicationFormResultPOJO applicationFormResultPOJO = new Gson().fromJson(jsonObject.optJSONObject("result").toString(), ApplicationFormResultPOJO.class);
                Intent intent = new Intent(StudentCourseStatusActivity.this, AddApplicationFormActivity.class);
                intent.putExtra("studentcourseresultpojo", studentCourseResultPOJO);
                intent.putExtra("coursepojo", courcesResultPOJO);
                intent.putExtra("applicationform", applicationFormResultPOJO);
                startActivityForResult(intent, APPLICATION_FORM_ACIVITY_RESULT);
            } else {
                Intent intent = new Intent(StudentCourseStatusActivity.this, AddApplicationFormActivity.class);
                intent.putExtra("studentcourseresultpojo", studentCourseResultPOJO);
                startActivityForResult(intent, APPLICATION_FORM_ACIVITY_RESULT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
//    public void updateStudent() {
//        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//        nameValuePairs.add(new BasicNameValuePair("sc_sid", studentCourseResultPOJO.getScSid()));
//        nameValuePairs.add(new BasicNameValuePair("sc_sname", studentCourseResultPOJO.getScSname()));
//        nameValuePairs.add(new BasicNameValuePair("sc_semail", studentCourseResultPOJO.getScSemail()));
//        nameValuePairs.add(new BasicNameValuePair("sc_sapplicationform_fill", studentCourseResultPOJO.getScSapplicationformFill()));
//        nameValuePairs.add(new BasicNameValuePair("sc_photo_upload", studentCourseResultPOJO.getScPhotoUpload()));
//        nameValuePairs.add(new BasicNameValuePair("sc_cerifiato_upload", studentCourseResultPOJO.getScCerifiatoUpload()));
//        nameValuePairs.add(new BasicNameValuePair("sc_idcard_upload", studentCourseResultPOJO.getScIdcardUpload()));
//        nameValuePairs.add(new BasicNameValuePair("sc_reg_fees", studentCourseResultPOJO.getScRegFees()));
//        nameValuePairs.add(new BasicNameValuePair("sc_rem_fees", studentCourseResultPOJO.getScRemFees()));
//        nameValuePairs.add(new BasicNameValuePair("sc_fullfees", studentCourseResultPOJO.getScFullfees()));
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
//    }
//
    public void parseApplyCourseApi(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                studentCourseResultPOJO = new Gson().fromJson(jsonObject.optJSONObject("result").toString(), StudentCourseResultPOJO.class);
                checkStatus();
            } else {
                ToastClass.showShortToast(getApplicationContext(), "something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseUploadPhotoAPiResponse(String response) {
        Log.d(TagUtils.getTag(), "photo upload response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                checkAppliedCourse();
            } else {
                ToastClass.showShortToast(getApplicationContext(), "Faild to upload Image");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), "Something went wrong. Please Upload after sometime");
        }
    }

    //
    public void checkAppliedCourse() {
        if (studentCourseResultPOJO != null) {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("request_action", "get_registration"));
            nameValuePairs.add(new BasicNameValuePair("stu_id", studentCourseResultPOJO.getStuId()));
            nameValuePairs.add(new BasicNameValuePair("c_id", studentCourseResultPOJO.getcId()));
            new WebServiceBase(nameValuePairs, this, this, APPLY_COURSE_API, true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
        } else {
            ToastClass.showShortToast(getApplicationContext(), "something went wrong");
        }
    }
//
//    public void showAttachDialog() {
//        final Dialog dialog1 = new Dialog(StudentCourseStatusActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
//        dialog1.setCancelable(true);
//        dialog1.setContentView(R.layout.dialog_attach_document);
//        dialog1.setTitle("Select");
//        dialog1.show();
//        dialog1.setCancelable(true);
//        Window window = dialog1.getWindow();
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        Button btn_fill_application_form = (Button) dialog1.findViewById(R.id.btn_fill_application_form);
//        Button btn_upload_photo = (Button) dialog1.findViewById(R.id.btn_upload_photo);
//        Button btn_certificate_upload = (Button) dialog1.findViewById(R.id.btn_certificate_upload);
//        Button btn_id_card_upload = (Button) dialog1.findViewById(R.id.btn_id_card_upload);
//        Button btn_reg_fees = (Button) dialog1.findViewById(R.id.btn_reg_fees);
//        Button btn_rem_fees = (Button) dialog1.findViewById(R.id.btn_rem_fees);
//        Button btn_full_fees = (Button) dialog1.findViewById(R.id.btn_full_fees);
//        Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);
//
//
//        btn_fill_application_form.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkApplicationFormStatus();
//                dialog1.dismiss();
//            }
//        });
//
//        btn_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog1.dismiss();
//            }
//        });
//
//        btn_upload_photo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showUploadSelectDialog(PHOTO_TYPE);
//                dialog1.dismiss();
//            }
//        });
//        btn_certificate_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showUploadSelectDialog(CERTIFICATE_TYPE);
//                dialog1.dismiss();
//            }
//        });
//        btn_id_card_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showUploadSelectDialog(ID_CARD_TYPE);
//                dialog1.dismiss();
//            }
//        });
//
//        btn_reg_fees.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog1.dismiss();
//                showFeesDialog("reg");
//            }
//        });
//        btn_rem_fees.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog1.dismiss();
//                if (studentCourseResultPOJO.getScRegFees().length() > 0) {
//                    showFeesDialog("rem");
//                } else {
//                    ToastClass.showShortToast(getApplicationContext(), "Please Submit Registration Fees First");
//                    showFeesDialog("reg");
//                }
//            }
//        });
//        btn_full_fees.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog1.dismiss();
//                showFeesDialog("full");
//            }
//        });
//    }

    //
    public void showFeesDialog(final String type) {
        final Dialog dialog1 = new Dialog(StudentCourseStatusActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_fees);
        dialog1.setTitle("Select");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_trans_id = (Button) dialog1.findViewById(R.id.btn_trans_id);
        Button btn_pay_online = (Button) dialog1.findViewById(R.id.btn_pay_online);
        Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);
        TextView tv_fees = (TextView) dialog1.findViewById(R.id.tv_fees);
        TextView tv_disclaimer1 = (TextView) dialog1.findViewById(R.id.tv_disclaimer1);

        String disclaimer1 = "<span><p style='color:#FF0000'>Pay by Cheque/Cash/internet banking(IMPS/NEFT/RTGS) in favor of:</p><p> Capri Institute of Manual Therapy, A/c no: 033005500869 RTGS/NEFT/IFSC code: ICIC0000330. Bank: ICICI bank, Branch: Anand Vihar, Delhi 92(No 3rd partly Charges apply).</p><span><div><span><p style='color:#FF0000'>OR Pay using Credit/Debit Card at the link below through </p><p style='color:#558139'> PayUMoney</p><p>(in addition to fee, 3rd party charges Approx. Rs 800/- apply).</p></span></div>";
        tv_disclaimer1.setText(Html.fromHtml(disclaimer1));
        switch (type) {
            case "reg":
                tv_fees.setText("Registration Fees :- " + courcesResultPOJO.getRegFees());
                break;
            case "rem":
                tv_fees.setText("Remaining Fees :- " + courcesResultPOJO.getRemFees());
                break;
            case "full":
                tv_fees.setText("Full Fees :- " + courcesResultPOJO.getFullFees());
                break;
        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        btn_trans_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                showTransIDDialog(type);
            }
        });


        btn_pay_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();

                Intent intent = new Intent(StudentCourseStatusActivity.this, PayUMoneyActivity.class);
                intent.putExtra("type", type);

                String name = studentCourseResultPOJO.getFirstName() + " " + studentCourseResultPOJO.getLastName();
                String email = studentCourseResultPOJO.getEmail();
                String phone = studentCourseResultPOJO.getMobile();
                String product = "fees";
                String amount = "";
                switch (type) {
                    case "reg":
                        amount = courcesResultPOJO.getRegFees();
                        break;
                    case "rem":
                        amount = courcesResultPOJO.getRemFees();
                        break;
                    case "full":
                        amount = courcesResultPOJO.getFullFees();
                        break;
                }

                String url = "http://caprispine.in/payumoney/PayUMoney_form.php?amount=" + amount +
                        "&name=" + name + "&email=" + email + "&phone=" + phone + "&productinfo=" + product;
                intent.putExtra("url", url);
                intent.putExtra("studentcourseresultpojo", studentCourseResultPOJO);
                intent.putExtra("courseresultpojo", courcesResultPOJO);
                startActivityForResult(intent, 105);

            }
        });
    }

    //
//
    public void showTransIDDialog(final String type) {
        final Dialog dialog1 = new Dialog(StudentCourseStatusActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_transid);
        dialog1.setTitle("Select");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        final EditText et_trans_id = (EditText) dialog1.findViewById(R.id.et_trans_id);
        Button btn_submit = (Button) dialog1.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_trans_id.getText().toString().equals("")) {
                    ToastClass.showShortToast(getApplicationContext(), "Please Enter Trans ID");
                } else {
                    dialog1.dismiss();
//                    switch (type) {
//                        case "reg":
//                            studentCourseResultPOJO.setScRegFees(et_trans_id.getText().toString());
//                            break;
//                        case "rem":
//                            studentCourseResultPOJO.setScRemFees(et_trans_id.getText().toString());
//                            break;
//                        case "full":
//                            studentCourseResultPOJO.setScFullfees(et_trans_id.getText().toString());
//                            break;
//                    }
//                    updateStudent();
                    updateStudentFees(type,et_trans_id.getText().toString());
                }
            }
        });
    }
    private static final String CALL_STUDENT_COURSE_UPDATE = "call_student_course_update";
    public void updateStudentFees(String type,String trans_id) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        switch (type) {
            case "reg":
                nameValuePairs.add(new BasicNameValuePair("request_action", "update_reg_fees"));
                nameValuePairs.add(new BasicNameValuePair("stu_reg_fees", trans_id));
                break;
            case "rem":
                nameValuePairs.add(new BasicNameValuePair("request_action", "update_rem_fees"));
                nameValuePairs.add(new BasicNameValuePair("stu_rem_fees", trans_id));
                break;
            case "full":
                nameValuePairs.add(new BasicNameValuePair("request_action", "update_full_fees"));
                nameValuePairs.add(new BasicNameValuePair("stu_full_fees", trans_id));
                break;

        }
        nameValuePairs.add(new BasicNameValuePair("c_id", studentCourseResultPOJO.getcId()));
        nameValuePairs.add(new BasicNameValuePair("stu_id", studentCourseResultPOJO.getStuId()));

        new WebServiceBase(nameValuePairs, this, this, CALL_STUDENT_COURSE_UPDATE, true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
    }


    //
//
    public void checkApplicationFormStatus() {
        if (!studentCourseResultPOJO.getStuAppId().equals("0")) {
            callApplicationFormApi();
        } else {
            Intent intent = new Intent(StudentCourseStatusActivity.this, AddApplicationFormActivity.class);
            intent.putExtra("studentcourseresultpojo", studentCourseResultPOJO);
            intent.putExtra("coursepojo", courcesResultPOJO);
            startActivityForResult(intent, APPLICATION_FORM_ACIVITY_RESULT);
        }
    }

    //    private final static int APPLICATION_FORM_ACIVITY_RESULT = 101;
//
    public void callApplicationFormApi() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "get_application"));
        nameValuePairs.add(new BasicNameValuePair("s_id", studentCourseResultPOJO.getStuId()));
        nameValuePairs.add(new BasicNameValuePair("c_id", studentCourseResultPOJO.getcId()));
        new WebServiceBase(nameValuePairs, this, this, GET_APPLICATION_FORM_DATA, true).execute(WebServicesUrls.STUDENT_APPLICATION_CRUD);
    }

    //
    public void showUploadSelectDialog(final int type) {
        final Dialog dialog1 = new Dialog(StudentCourseStatusActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_upload_select);
        dialog1.setTitle("Select");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        Button btn_open_camera = (Button) dialog1.findViewById(R.id.btn_open_camera);
        Button btn_select_from_gallery = (Button) dialog1.findViewById(R.id.btn_select_from_gallery);
        Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);

        btn_open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMAGE_TYPE = type;
                startCamera();
                dialog1.dismiss();
            }
        });

        btn_select_from_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IMAGE_TYPE = type;
                selectImageFromGallery();
                dialog1.dismiss();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
    }

    //
    public void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private int PICK_IMAGE_REQUEST = 1;
    String pictureImagePath = "";
    private static final int CAMERA_REQUEST = 1888;

    public void startCamera() {
        String strMyImagePath = FileUtils.getBaseFilePath() + File.separator + "temp.png";

        pictureImagePath = strMyImagePath;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.capri4physio.fileProvider", file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        } else {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        }
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (null == data)
                    return;
                Uri selectedImageUri = data.getData();
                System.out.println(selectedImageUri.toString());
                // MEDIA GALLERY
                String selectedImagePath = getPath(
                        StudentCourseStatusActivity.this, selectedImageUri);
                Log.d("sun", "" + selectedImagePath);
                if (selectedImagePath != null && selectedImagePath != "") {
                    Log.d(TagUtils.getTag(), "selected path:-" + selectedImagePath);
                    callPhotoAPI(selectedImagePath);
                } else {
                    Toast.makeText(StudentCourseStatusActivity.this, "File Selected is corrupted", Toast.LENGTH_LONG).show();
                }
                System.out.println("Image Path =" + selectedImagePath);
            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
//            Bitmap photo = (Bitmap) data.getExtras().get("data");
//            Log.d(TAG, photo.toString());

            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath);
                bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 4, bmp.getHeight() / 4, false);
                String strMyImagePath = FileUtils.getChatDirPath();
                File file_name = new File(strMyImagePath + File.separator + System.currentTimeMillis() + ".png");
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(file_name);
                    Log.d(TagUtils.getTag(), "taking photos");
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    callPhotoAPI(file_name.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
//        if (requestCode == APPLICATION_FORM_ACIVITY_RESULT) {
//            if (resultCode == Activity.RESULT_OK) {
//                String result = data.getStringExtra("result");
//                if (result.equals("ok")) {
//                    checkAppliedCourse();
//                }
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//            }
//        }

        if (requestCode == 105) {
            if (resultCode == Activity.RESULT_OK) {
//                String result=data.getStringExtra("result");
                this.studentCourseResultPOJO = (StudentCourseResultPOJO) data.getSerializableExtra("result");
                checkStatus();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    //
    public void callPhotoAPI(String file_path) {
        if (file_path.length() > 0 || new File(file_path).exists()) {
            try {
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                FileBody bin1 = new FileBody(new File(file_path));

                if (IMAGE_TYPE == PHOTO_TYPE) {
                    reqEntity.addPart("request_action", new StringBody("update_photo"));
                    reqEntity.addPart("photo_upload", bin1);
                } else if (IMAGE_TYPE == CERTIFICATE_TYPE) {
                    reqEntity.addPart("request_action", new StringBody("update_certificate"));
                    reqEntity.addPart("cert_upload", bin1);
                } else if (IMAGE_TYPE == ID_CARD_TYPE) {
                    reqEntity.addPart("request_action", new StringBody("update_id_card"));
                    reqEntity.addPart("id_upload", bin1);
                }
                reqEntity.addPart("c_id", new StringBody(studentCourseResultPOJO.getcId()));
                reqEntity.addPart("stu_id", new StringBody(studentCourseResultPOJO.getStuId()));

                new WebUploadService(reqEntity, this, this, PHOTO_UPLOAD_API, true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ToastClass.showShortToast(getApplicationContext(), "File does not exist");
        }
        file_path = "";
    }

    //
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                finish();
//                return true;
//            case R.id.menu_file_attach:
//                showAttachDialog();
//                return true;
//            case R.id.menu_delete_course:
//                showRemoveCourseAlert();
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
//
//    public void showRemoveCourseAlert() {
//        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
//        builder1.setMessage("Do you delete this course?");
//        builder1.setCancelable(true);
//
//        builder1.setPositiveButton(
//                "Yes",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                        removeCourseApplication();
//                    }
//                });
//
//        builder1.setNegativeButton(
//                "No",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        dialog.cancel();
//                    }
//                });
//
//        AlertDialog alert11 = builder1.create();
//        alert11.show();
//    }
//
//    public void removeCourseApplication() {
//        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
//        nameValuePairs.add(new BasicNameValuePair("request_action", "REMOVE_STUDENT_APPLICATION"));
//        nameValuePairs.add(new BasicNameValuePair("a_s_id", studentCourseResultPOJO.getScSid()));
//        nameValuePairs.add(new BasicNameValuePair("a_c_id", studentCourseResultPOJO.getScCid()));
//        new WebServiceBase(nameValuePairs, this, CALL_REMOVE_APPLICATION).execute(ApiConfig.STUDENT_MANAGEMENT);
//    }
//
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        // check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }


}
