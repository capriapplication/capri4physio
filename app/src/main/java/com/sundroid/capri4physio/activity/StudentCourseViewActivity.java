package com.sundroid.capri4physio.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

public class StudentCourseViewActivity extends AppCompatActivity implements View.OnClickListener, WebServicesCallBack {
    private static final String UPDATE_STUDENT = "update_student";
    private static final String CALL_UPDATE_STUDENT = "call_update_student";
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
    @BindView(R.id.iv_full_fees)
    ImageView iv_full_fees;
    @BindView(R.id.iv_certificatie_upload)
    ImageView iv_certificatie_upload;

    @BindView(R.id.iv_confirm_app)
    ImageView iv_confirm_app;
    @BindView(R.id.iv_confirm_photo)
    ImageView iv_confirm_photo;
    @BindView(R.id.iv_confirm_cert)
    ImageView iv_confirm_cert;
    @BindView(R.id.iv_confirm_id)
    ImageView iv_confirm_id;
    @BindView(R.id.iv_confirm_reg)
    ImageView iv_confirm_reg;
    @BindView(R.id.iv_confirm_full)
    ImageView iv_confirm_full;
    @BindView(R.id.iv_view_app)
    ImageView iv_view_app;
    @BindView(R.id.iv_view_photo)
    ImageView iv_view_photo;
    @BindView(R.id.iv_view_cert)
    ImageView iv_view_cert;
    @BindView(R.id.iv_view_id)
    ImageView iv_view_id;
    @BindView(R.id.iv_view_reg)
    ImageView iv_view_reg;
    @BindView(R.id.iv_view_full_fees)
    ImageView iv_view_full_fees;
    @BindView(R.id.iv_rem_fees)
    ImageView iv_rem_fees;
    @BindView(R.id.iv_rem_confirm)
    ImageView iv_rem_confirm;
    @BindView(R.id.iv_rem_view)
    ImageView iv_rem_view;

    @BindView(R.id.et_remark)
    EditText et_remark;

    StudentCourseResultPOJO studentCourseResultPOJO;
    CourcesResultPOJO courcesResultPOJO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_course_view);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        studentCourseResultPOJO = (StudentCourseResultPOJO) getIntent().getSerializableExtra("studentcourse");
        courcesResultPOJO = (CourcesResultPOJO) getIntent().getSerializableExtra("coursepojo");

        if (studentCourseResultPOJO != null && courcesResultPOJO != null) {
            checkStatus();
        } else {
            finish();
        }

        iv_confirm_app.setOnClickListener(this);
        iv_confirm_photo.setOnClickListener(this);
        iv_confirm_cert.setOnClickListener(this);
        iv_confirm_id.setOnClickListener(this);
        iv_confirm_reg.setOnClickListener(this);
        iv_confirm_full.setOnClickListener(this);
        iv_rem_confirm.setOnClickListener(this);


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_remark.getText().toString().length() > 0) {
//                    studentCourseResultPOJO.setAdminStatus(et_remark.getText().toString());
                    updateStudent(false);
                } else {
                    ToastClass.showShortToast(getApplicationContext(), "Please write some remark");
                }
            }
        });
    }

    public void checkStatus() {
        if (studentCourseResultPOJO != null) {
            et_s_name.setText(studentCourseResultPOJO.getFirstName() + " " + studentCourseResultPOJO.getLastName());
            et_s_email.setText(studentCourseResultPOJO.getEmail());
            if (studentCourseResultPOJO.getStuAppId().equals("0")) {
                iv_application_status.setImageResource(R.drawable.ic_not_filled);
                iv_confirm_app.setVisibility(View.GONE);
                iv_view_app.setVisibility(View.GONE);
            } else {
                if (studentCourseResultPOJO.getaStuAppId().equals("0")) {
                    iv_application_status.setImageResource(R.drawable.ic_filled);
                    iv_confirm_app.setVisibility(View.VISIBLE);
                    iv_view_app.setVisibility(View.VISIBLE);
                } else {
                    iv_application_status.setImageResource(R.drawable.ic_approved);
                    iv_confirm_app.setVisibility(View.GONE);
                    iv_view_app.setVisibility(View.VISIBLE);
                }

                iv_view_app.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StudentCourseViewActivity.this, StudentCourseApplicationActivity.class);
                        intent.putExtra("studentcourseresultpojo", studentCourseResultPOJO);
                        startActivity(intent);

//                        Log.d(TagUtils.getTag(),"application course id:-"+studentCourseResultPOJO.getScCid());
//                        Log.d(TagUtils.getTag(),"application student id:-"+studentCourseResultPOJO.getScSid());

                    }
                });
            }

            if (studentCourseResultPOJO.getPhotoUpload().length() == 0) {
                iv_photo_upload.setImageResource(R.drawable.ic_not_filled);
                iv_confirm_photo.setVisibility(View.GONE);
                iv_view_photo.setVisibility(View.GONE);
            } else {
                if (studentCourseResultPOJO.getaPhotoUpload().equals("0")) {
                    iv_photo_upload.setImageResource(R.drawable.ic_filled);
                    iv_confirm_photo.setVisibility(View.VISIBLE);
                    iv_view_photo.setVisibility(View.VISIBLE);
                } else {
                    iv_photo_upload.setImageResource(R.drawable.ic_approved);
                    iv_confirm_photo.setVisibility(View.GONE);
                    iv_view_photo.setVisibility(View.VISIBLE);
                }
                iv_view_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StudentCourseViewActivity.this, ImageViewActivity.class);
                        intent.putExtra("url", studentCourseResultPOJO.getPhotoUpload());
                        startActivity(intent);
                    }
                });
            }

            if (studentCourseResultPOJO.getCertUpload().length() == 0) {
                iv_certificatie_upload.setImageResource(R.drawable.ic_not_filled);
                iv_confirm_cert.setVisibility(View.GONE);
                iv_view_cert.setVisibility(View.GONE);
            } else {
                if (studentCourseResultPOJO.getaCertUpload().equals("0")) {
                    iv_certificatie_upload.setImageResource(R.drawable.ic_filled);
                    iv_confirm_cert.setVisibility(View.VISIBLE);
                    iv_view_cert.setVisibility(View.VISIBLE);
                } else {
                    iv_certificatie_upload.setImageResource(R.drawable.ic_approved);
                    iv_confirm_cert.setVisibility(View.GONE);
                    iv_view_cert.setVisibility(View.VISIBLE);
                }
                iv_view_cert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StudentCourseViewActivity.this, ImageViewActivity.class);
                        intent.putExtra("url", studentCourseResultPOJO.getCertUpload());
                        startActivity(intent);
                    }
                });
            }

            if (studentCourseResultPOJO.getIdUpload().length() == 0) {
                iv_id_card_upload.setImageResource(R.drawable.ic_not_filled);
                iv_confirm_id.setVisibility(View.GONE);
                iv_view_id.setVisibility(View.GONE);
            } else {
                if (studentCourseResultPOJO.getaIdUpload().equals("0")) {
                    iv_id_card_upload.setImageResource(R.drawable.ic_filled);
                    iv_confirm_id.setVisibility(View.VISIBLE);
                    iv_view_id.setVisibility(View.VISIBLE);
                } else {
                    iv_id_card_upload.setImageResource(R.drawable.ic_approved);
                    iv_confirm_id.setVisibility(View.GONE);
                    iv_view_id.setVisibility(View.VISIBLE);
                }
                iv_view_id.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StudentCourseViewActivity.this, ImageViewActivity.class);
                        intent.putExtra("url", studentCourseResultPOJO.getIdUpload());
                        startActivity(intent);
                    }
                });
            }
            checkRegistrationFeesStatus();
            checkRemainingFeesStatus();
            checkFullFeesStatus();

//            if (studentCourseResultPOJO.getaStuRegFees().equals("false")) {
//
//                if (studentCourseResultPOJO.getStuRegFees().length() == 0) {
//                    iv_registration_fees.setImageResource(R.drawable.ic_not_filled);
//                    iv_confirm_reg.setVisibility(View.GONE);
//                } else {
//                    iv_registration_fees.setImageResource(R.drawable.ic_filled);
//                    iv_confirm_reg.setVisibility(View.VISIBLE);
//                }
//            } else {
//                iv_registration_fees.setImageResource(R.drawable.ic_approved);
//                iv_confirm_reg.setVisibility(View.GONE);
//            }
//
//
//            if (studentCourseResultPOJO.getStuRemFees().length() == 0) {
//                iv_rem_fees.setImageResource(R.drawable.ic_not_filled);
//                iv_rem_confirm.setVisibility(View.GONE);
//            } else {
//                if (studentCourseResultPOJO.getaStuRemFees().equals("false")) {
//                    iv_rem_fees.setImageResource(R.drawable.ic_filled);
//                    iv_rem_confirm.setVisibility(View.VISIBLE);
//                } else {
//                    iv_rem_fees.setImageResource(R.drawable.ic_approved);
//                    iv_rem_confirm.setVisibility(View.GONE);
//                }
//
//            }
//
//            if (studentCourseResultPOJO.getStuFullFees().length() == 0) {
//                iv_full_fees.setImageResource(R.drawable.ic_not_filled);
//                iv_confirm_full.setVisibility(View.GONE);
//            } else {
//                if (studentCourseResultPOJO.getaStuFullFees().equals("false")) {
//                    iv_full_fees.setImageResource(R.drawable.ic_filled);
//                    iv_confirm_full.setVisibility(View.VISIBLE);
//                } else {
//                    iv_full_fees.setImageResource(R.drawable.ic_approved);
//                    iv_confirm_full.setVisibility(View.GONE);
//                }
//
//            }
//
//
//            if (studentCourseResultPOJO.getStuRegFees().length() > 0
//                    && studentCourseResultPOJO.getStuRemFees().length() > 0
//                    && studentCourseResultPOJO.getaStuRemFees().equals("true")
//                    && studentCourseResultPOJO.getaStuRegFees().equals("true")
//                    ) {
//                iv_registration_fees.setImageResource(R.drawable.ic_approved);
//                iv_rem_fees.setImageResource(R.drawable.ic_approved);
//                iv_full_fees.setImageResource(R.drawable.ic_approved);
//
//                iv_confirm_reg.setVisibility(View.GONE);
//                iv_confirm_full.setVisibility(View.GONE);
//                iv_rem_confirm.setVisibility(View.GONE);
//            } else {
//                if (studentCourseResultPOJO.getStuRegFees().length() > 0
//                        && studentCourseResultPOJO.getStuRemFees().length() > 0
//                        && studentCourseResultPOJO.getaStuRemFees().equals("false")
//                        && studentCourseResultPOJO.getaStuRegFees().equals("false")
//                        ) {
//                    iv_registration_fees.setImageResource(R.drawable.ic_filled);
//                    iv_rem_fees.setImageResource(R.drawable.ic_filled);
//                    iv_full_fees.setImageResource(R.drawable.ic_filled);
//
//                    iv_confirm_reg.setVisibility(View.VISIBLE);
//                    iv_confirm_full.setVisibility(View.VISIBLE);
//                    iv_rem_confirm.setVisibility(View.VISIBLE);
//                } else {
//
//                }
//            }
//
//
//            if (studentCourseResultPOJO.getStuFullFees().length() > 0
//                    && studentCourseResultPOJO.getaStuFullFees().equals("true")) {
//                iv_registration_fees.setImageResource(R.drawable.ic_approved);
//                iv_rem_fees.setImageResource(R.drawable.ic_approved);
//                iv_full_fees.setImageResource(R.drawable.ic_approved);
//
//                iv_confirm_reg.setVisibility(View.GONE);
//                iv_confirm_full.setVisibility(View.GONE);
//                iv_rem_confirm.setVisibility(View.GONE);
//            } else {
//                if (studentCourseResultPOJO.getStuFullFees().length() > 0
//                        && studentCourseResultPOJO.getaStuFullFees().equals("false")) {
//                    iv_registration_fees.setImageResource(R.drawable.ic_filled);
//                    iv_rem_fees.setImageResource(R.drawable.ic_filled);
//                    iv_full_fees.setImageResource(R.drawable.ic_filled);
//
//                    iv_confirm_reg.setVisibility(View.VISIBLE);
//                    iv_confirm_full.setVisibility(View.VISIBLE);
//                    iv_rem_confirm.setVisibility(View.VISIBLE);
//                } else {
//
//                }
//            }


            iv_view_reg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (studentCourseResultPOJO.getaStuFullFees().length() > 0) {
                        if (studentCourseResultPOJO.getStuFullFees().equals("online")) {
                            showOnlineDialog("Full fees has been paid by Pay U Money");
                        } else {
                            showOnlineDialog("Full fees has been paid and transaction id is " + studentCourseResultPOJO.getStuFullFees());
                        }
                    } else {
                        if (studentCourseResultPOJO.getStuRegFees().length() > 0) {
                            if (studentCourseResultPOJO.getStuRegFees().equals("online")) {
                                showOnlineDialog("Registration Fees has bees paid by Pay U Money");
                            } else {
                                showOnlineDialog("Registration Fees has bees paid and transaction id is " + studentCourseResultPOJO.getStuRegFees());
                            }
                        } else {
                            ToastClass.showShortToast(getApplicationContext(), "Registration fees has not been submitted");
                        }
                    }
                }
            });


            iv_view_full_fees.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (studentCourseResultPOJO.getStuFullFees().length() > 0) {
                        if (studentCourseResultPOJO.getStuFullFees().equals("online")) {
                            showOnlineDialog("Full Fees has bees paid by Pay U Money");
                        } else {
                            showOnlineDialog("Full Fees has bees paid and transaction id is " + studentCourseResultPOJO.getStuFullFees());
                        }
                    } else {
                        if (studentCourseResultPOJO.getStuRegFees().length() > 0
                                && studentCourseResultPOJO.getStuRemFees().length() > 0) {
                            String reg_fees_status = "";
                            String rem_fees_status = "";
                            if (studentCourseResultPOJO.getStuRegFees().equals("online")) {
                                reg_fees_status = "Registration fees has been paid by Pay U Money";
                            } else {
                                reg_fees_status = "Registration fees has been pain and transaction id is " + studentCourseResultPOJO.getStuRegFees();
                            }

                            if (studentCourseResultPOJO.getStuRemFees().equals("online")) {
                                rem_fees_status = "Remaining fees has been paid by Pay U Money";
                            } else {
                                rem_fees_status = "Remaining fees has been pain and transaction id is " + studentCourseResultPOJO.getStuRemFees();
                            }

                            String fees = reg_fees_status + " and " + rem_fees_status;
                            showOnlineDialog(fees);
                        } else {
                            ToastClass.showShortToast(getApplicationContext(), "Full fees has not been submitted");
                        }
                    }
                }
            });


            iv_rem_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (studentCourseResultPOJO.getStuFullFees().length() > 0) {
                        if (studentCourseResultPOJO.getStuFullFees().equals("online")) {
                            showOnlineDialog("Full fees has been paid by Pay U Money");
                        } else {
                            showOnlineDialog("Full fees has been pain and transaction id is " + studentCourseResultPOJO.getStuFullFees());
                        }
                    } else {
                        if (studentCourseResultPOJO.getStuRemFees().length() > 0) {
                            if (studentCourseResultPOJO.getStuRemFees().equals("online")) {
                                showOnlineDialog("Remaining Fees has bees paid by Pay U Money");
                            } else {
                                showOnlineDialog("Remaining Fees has bees paid and transaction id is " + studentCourseResultPOJO.getStuRemFees());
                            }
                        } else {
                            ToastClass.showShortToast(getApplicationContext(), "Remaining fees has not been submitted");
                        }
                    }
                }
            });


        } else {
            finish();
        }
    }

    public void checkRegistrationFeesStatus() {
        if (studentCourseResultPOJO != null) {
            if (studentCourseResultPOJO.getaStuFullFees().equals("1")) {
                iv_registration_fees.setImageResource(R.drawable.ic_approved);
                iv_confirm_reg.setVisibility(View.GONE);
            } else {
                if (studentCourseResultPOJO.getaStuRegFees().equals("1")) {
                    iv_registration_fees.setImageResource(R.drawable.ic_approved);
                    iv_confirm_reg.setVisibility(View.GONE);
                } else {
                    if (studentCourseResultPOJO.getaStuRegFees().equals("0")
                            && studentCourseResultPOJO.getStuRegFees().length() > 0) {
                        iv_registration_fees.setImageResource(R.drawable.ic_filled);
                        iv_confirm_reg.setVisibility(View.VISIBLE);
                    } else {
                        iv_registration_fees.setImageResource(R.drawable.ic_not_filled);
                        iv_confirm_reg.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    public void checkRemainingFeesStatus() {
        if (studentCourseResultPOJO != null) {
            if (studentCourseResultPOJO.getaStuFullFees().equals("1")) {
                iv_rem_fees.setImageResource(R.drawable.ic_approved);
                iv_rem_confirm.setVisibility(View.GONE);
            } else {
                if (studentCourseResultPOJO.getaStuRemFees().equals("1")) {
                    iv_rem_fees.setImageResource(R.drawable.ic_approved);
                    iv_rem_confirm.setVisibility(View.GONE);
                } else {
                    if (studentCourseResultPOJO.getaStuRemFees().equals("0")
                            && studentCourseResultPOJO.getStuRemFees().length() > 0) {
                        iv_rem_fees.setImageResource(R.drawable.ic_filled);
                        iv_rem_confirm.setVisibility(View.VISIBLE);
                    } else {
                        iv_rem_fees.setImageResource(R.drawable.ic_not_filled);
                        iv_rem_confirm.setVisibility(View.GONE);
                    }
                }
            }
        }
    }


    public void checkFullFeesStatus() {
        if (studentCourseResultPOJO != null) {
            if (studentCourseResultPOJO.getaStuFullFees().equals("1")) {
                iv_full_fees.setImageResource(R.drawable.ic_approved);
                iv_confirm_full.setVisibility(View.GONE);
            } else {
                if (studentCourseResultPOJO.getaStuRegFees().equals("1")
                        && studentCourseResultPOJO.getaStuRemFees().equals("1")) {
                    iv_full_fees.setImageResource(R.drawable.ic_approved);
                    iv_confirm_full.setVisibility(View.GONE);
                } else {
                    if (studentCourseResultPOJO.getStuFullFees().length() > 0) {
                        iv_full_fees.setImageResource(R.drawable.ic_filled);
                        iv_confirm_full.setVisibility(View.VISIBLE);
                    } else {
                        if (studentCourseResultPOJO.getStuRemFees().length() > 0
                                && studentCourseResultPOJO.getStuRegFees().length() > 0) {
                            iv_full_fees.setImageResource(R.drawable.ic_filled);
                            iv_confirm_full.setVisibility(View.VISIBLE);
                        } else {
                            iv_full_fees.setImageResource(R.drawable.ic_not_filled);
                            iv_confirm_full.setVisibility(View.GONE);
                        }
                    }
                }
            }
        }
    }


    public void showOnlineDialog(String message) {
        final Dialog dialog1 = new Dialog(StudentCourseViewActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_fees_paid);
        dialog1.setTitle("Fees Paid");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tv_fees = (TextView) dialog1.findViewById(R.id.tv_fees);
        Button btn_ok = (Button) dialog1.findViewById(R.id.btn_ok);

        tv_fees.setText(message);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

    }

    public void setListenerNull() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateStudent(boolean deduct_seat) {

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("id", studentCourseResultPOJO.getId()));
        nameValuePairs.add(new BasicNameValuePair("c_id", studentCourseResultPOJO.getcId()));
        nameValuePairs.add(new BasicNameValuePair("stu_id", studentCourseResultPOJO.getStuId()));
        nameValuePairs.add(new BasicNameValuePair("stu_app_id", studentCourseResultPOJO.getStuAppId()));
        nameValuePairs.add(new BasicNameValuePair("a_stu_app_id", studentCourseResultPOJO.getaStuAppId()));
        nameValuePairs.add(new BasicNameValuePair("photo_upload", studentCourseResultPOJO.getPhotoUpload()));
        nameValuePairs.add(new BasicNameValuePair("a_photo_upload", studentCourseResultPOJO.getaPhotoUpload()));
        nameValuePairs.add(new BasicNameValuePair("cert_upload", studentCourseResultPOJO.getCertUpload()));
        nameValuePairs.add(new BasicNameValuePair("a_cert_upload", studentCourseResultPOJO.getaCertUpload()));
        nameValuePairs.add(new BasicNameValuePair("id_upload", studentCourseResultPOJO.getIdUpload()));
        nameValuePairs.add(new BasicNameValuePair("a_id_upload", studentCourseResultPOJO.getaIdUpload()));
        nameValuePairs.add(new BasicNameValuePair("stu_reg_fees", studentCourseResultPOJO.getStuRegFees()));
        nameValuePairs.add(new BasicNameValuePair("a_stu_reg_fees", studentCourseResultPOJO.getaStuRegFees()));
        nameValuePairs.add(new BasicNameValuePair("stu_rem_fees", studentCourseResultPOJO.getStuRemFees()));
        nameValuePairs.add(new BasicNameValuePair("a_stu_rem_fees", studentCourseResultPOJO.getaStuRemFees()));
        nameValuePairs.add(new BasicNameValuePair("stu_full_fees", studentCourseResultPOJO.getStuFullFees()));
        nameValuePairs.add(new BasicNameValuePair("a_stu_full_fees", studentCourseResultPOJO.getaStuFullFees()));
        nameValuePairs.add(new BasicNameValuePair("stu_date", studentCourseResultPOJO.getStuDate()));
        nameValuePairs.add(new BasicNameValuePair("stu_time", studentCourseResultPOJO.getStuTime()));
        new WebServiceBase(nameValuePairs, this, this, UPDATE_STUDENT, true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
//        if (deduct_seat) {
//            updateCourse();
//        }
    }

    private static final String UPDATE_COURCE = "update_course";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_confirm_app:
//                studentCourseResultPOJO.setaStuAppId("1");
//                updateStudent(false);
                updateStudent("update_a_app_id");
                break;
            case R.id.iv_confirm_photo:
//                studentCourseResultPOJO.setaPhotoUpload("1");
//                updateStudent(false);
                updateStudent("update_a_photo_upload");
                break;
            case R.id.iv_confirm_cert:
//                studentCourseResultPOJO.setaCertUpload("1");
//                updateStudent(false);
                updateStudent("update_a_cert_upload");
                break;
            case R.id.iv_confirm_id:
//                studentCourseResultPOJO.setaIdUpload("1");
//                updateStudent(false);
                updateStudent("update_a_id_upload");
                break;
            case R.id.iv_confirm_reg:
//                studentCourseResultPOJO.setaStuRegFees("1");
//                updateStudent(false);
                updateStudent("update_a_reg_fees");
                break;
            case R.id.iv_confirm_full:
//                studentCourseResultPOJO.setaStuFullFees("1");
//                updateStudent(true);
                updateStudent("update_a_full_fees");
                break;

            case R.id.iv_rem_confirm:
                if (studentCourseResultPOJO.getaStuRegFees().equals("0")) {
                    ToastClass.showShortToast(getApplicationContext(), "Please First Confirm the Registration Fees.");
                } else {
//                    studentCourseResultPOJO.setaStuRemFees("1");
//                    studentCourseResultPOJO.setaStuFullFees("1");
//                    updateStudent(true);
                    updateStudent("update_a_rem_fees");
                    updateStudent("update_a_full_fees");
                }
                break;
        }
    }

    public void updateStudent(String request_action) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action", request_action));
        nameValuePairs.add(new BasicNameValuePair("c_id", studentCourseResultPOJO.getcId()));
        nameValuePairs.add(new BasicNameValuePair("stu_id", studentCourseResultPOJO.getStuId()));
        new WebServiceBase(nameValuePairs, this, this, CALL_UPDATE_STUDENT, true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
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
            case CALL_UPDATE_STUDENT:
                parseUpdateStudentResponse(response);
                break;
        }
    }

    public void parseUpdateStudentResponse(String response) {
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


    public void parseUpdateCourseActivity(String response) {
        Log.d(TagUtils.getTag(), "response:-" + response);
        try {
            if (new JSONObject(response).optString("Success").equals("true")) {
            } else {
                ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
        }
    }

    public void parseUpdateStudent(String response) {
        Log.d(TagUtils.getTag(), "update response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                Gson gson = new Gson();
                studentCourseResultPOJO = gson.fromJson(jsonObject.optJSONObject("result").toString(), StudentCourseResultPOJO.class);
                checkStatus();
                ToastClass.showShortToast(getApplicationContext(), "Updated Successfully");
            } else {
                ToastClass.showShortToast(getApplicationContext(), "Updation Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(), "Something went wrong");
        }
    }
}
