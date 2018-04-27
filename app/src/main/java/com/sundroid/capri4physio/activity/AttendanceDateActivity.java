package com.sundroid.capri4physio.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.adapter.AttendenceDateAdapter;
import com.sundroid.capri4physio.pojo.attendance.AttendanceDataPOJO;
import com.sundroid.capri4physio.pojo.attendance.AttendancePOJO;
import com.sundroid.capri4physio.pojo.attendance.AttendanceResultPOJO;
import com.sundroid.capri4physio.pojo.attendance.AttendenceListPOJO;
import com.sundroid.capri4physio.pojo.attendance.AttendencePOJO;
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import static java.util.Calendar.MONTH;

public class AttendanceDateActivity extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, WebServicesCallBack {
    private static final String CALL_ADD_DATE = "call_add_date";
    private static final String CALL_GET_ATTENDENCE = "call_get_attendence";
    private static final String CALL_ATTENDENCE_API = "call_attendence_api";
    @BindView(R.id.btn_add_attendance)
    Button btn_add_attendance;
    @BindView(R.id.btn_save_attendance)
    Button btn_save_attendance;

    @BindView(R.id.rv_attendence_date)
    RecyclerView rv_attendence_date;

    String course_id;
    AttendenceDateAdapter attendenceDateAdapter;
    List<AttendencePOJO> attendencePOJOList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_date);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            course_id = bundle.getString("course_id");
        }
        attachAdapter();
        btn_add_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateSelectionDialog();
            }
        });

        btn_save_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callCourseAttendance();
            }
        });
    }

    public void callCourseAttendance() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "get_all_student_attendance"));
        nameValuePairs.add(new BasicNameValuePair("c_id", course_id));
        new WebServiceBase(nameValuePairs, this,this, CALL_ATTENDENCE_API,true).execute(WebServicesUrls.STUDENT_ATTENDANCE_CRUD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        callGetAttendenceDate();
    }

    public void attachAdapter() {
        attendenceDateAdapter = new AttendenceDateAdapter(this, null, attendencePOJOList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_attendence_date.setHasFixedSize(true);
        rv_attendence_date.setAdapter(attendenceDateAdapter);
        rv_attendence_date.setLayoutManager(layoutManager);
        rv_attendence_date.setItemAnimator(new DefaultItemAnimator());
    }

    public void callGetAttendenceDate() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "get_all_attendance"));
        nameValuePairs.add(new BasicNameValuePair("c_id", course_id));
        new WebServiceBase(nameValuePairs, this,this, CALL_GET_ATTENDENCE,true).execute(WebServicesUrls.STUDENT_ATTENDANCE_CRUD);
    }


    EditText et_date;
    EditText et_time;

    public void showDateSelectionDialog() {
        final Dialog dialog1 = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
//        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog1.setContentView(R.layout.dialog_attendance_date);
        dialog1.setTitle("Select Date and Time");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Button btn_select = dialog1.findViewById(R.id.btn_select);
        Button btn_end_time = dialog1.findViewById(R.id.btn_to_hour);
        Button btn_start_time = dialog1.findViewById(R.id.btn_from_hour);
        Button btn_ok = dialog1.findViewById(R.id.btn_ok);
        et_date = dialog1.findViewById(R.id.et_date);
        final EditText et_start_time = dialog1.findViewById(R.id.et_from_hour);
        final EditText et_end_time = dialog1.findViewById(R.id.et_to_hour);

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AttendanceDateActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        btn_start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_time = et_start_time;
                TimePickerDialog tpd = TimePickerDialog
                        .newInstance(AttendanceDateActivity.this, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                tpd.show(getFragmentManager(), "Start Time");
            }
        });
        btn_end_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_time = et_end_time;
                TimePickerDialog tpd = TimePickerDialog
                        .newInstance(AttendanceDateActivity.this, Calendar.HOUR_OF_DAY, Calendar.MINUTE, true);
                tpd.show(getFragmentManager(), "End Time");
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_date.getText().toString().length() > 0 && et_start_time.getText().toString().length() > 0
                        && et_end_time.getText().toString().length() > 0) {
                    dialog1.dismiss();
                    callAddDateAPI(course_id, et_date.getText().toString(), et_start_time.getText().toString(), et_end_time.getText().toString());
                } else {
                    ToastClass.showShortToast(getApplicationContext(), "Please Select Date and Time Properly");
                }
            }
        });

    }

    public void callAddDateAPI(String course_id, String date, String start_time, String end_time) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "create_attendance"));
        nameValuePairs.add(new BasicNameValuePair("c_id", course_id));
        nameValuePairs.add(new BasicNameValuePair("date", date));
        nameValuePairs.add(new BasicNameValuePair("start_time", start_time));
        nameValuePairs.add(new BasicNameValuePair("end_time", end_time));
        new WebServiceBase(nameValuePairs, this,this, CALL_ADD_DATE,true).execute(WebServicesUrls.STUDENT_ATTENDANCE_CRUD);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        String day = "";
        String mon = "";
        if (dayOfMonth < 10) {
            day = "0" + dayOfMonth;
        } else {
            day = String.valueOf(dayOfMonth);
        }

        if ((monthOfYear + 1) < 10) {
            mon = "0" + (monthOfYear + 1);
        } else {
            mon = String.valueOf((monthOfYear + 1));
        }

        String date = year + "-" + mon + "-" + day;

        if (et_date != null) {
            et_date.setText(date);
        }
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hour = "";
        if (hourOfDay < 10) {
            hour = "0" + hourOfDay;
        } else {
            hour = String.valueOf(hourOfDay);
        }

        String minutes = "";

        if (minute < 10) {
            minutes = "0" + minute;
        } else {
            minutes = String.valueOf(minute);
        }

        String time = hour + ":" + minutes;
        Log.d(TagUtils.getTag(), "time selected:-" + time);

        if (et_time != null) {
            et_time.setText(time);
        }
    }

    private void exportToExcel(List<AttendanceResultPOJO> attendanceResultPOJOS, String course_Name) {

        final String fileName = course_Name + "_studentlist.xls";


        File file = new File(Environment.getExternalStorageDirectory() + File.separator + fileName);
        Log.d(TagUtils.getTag(), "file path:-" + file.toString());

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(course_Name, 0);

            try {
                sheet.addCell(new Label(0, 0, "Name")); // column and row

                for (int i = 0; i < attendanceResultPOJOS.get(0).getAttendanceDataPOJOList().size(); i++) {
                    AttendanceDataPOJO attendanceDataPOJO = attendanceResultPOJOS.get(0).getAttendanceDataPOJOList().get(i);
                    sheet.addCell(new Label(i + 1, 0, attendanceDataPOJO.getDate()));
                }
                for (int i = 0; i < attendanceResultPOJOS.size(); i++) {
                    AttendanceResultPOJO attendanceResultPOJO = attendanceResultPOJOS.get(i);
                    sheet.addCell(new Label(0, i + 1, attendanceResultPOJO.getStu_name()));
                    for (int j = 0; j < attendanceResultPOJO.getAttendanceDataPOJOList().size(); j++) {
                        AttendanceDataPOJO attendanceDataPOJO = attendanceResultPOJO.getAttendanceDataPOJOList().get(j);
                        sheet.addCell(new Label(j + 1, i + 1, attendanceDataPOJO.getStatus()));
                    }
                }
//                for (int i = 0; i < student_list.size(); i++) {
//                    String title =String.valueOf(i+1);
//                    String desc = student_list.get(i);
//
//                    sheet.addCell(new Label(0, i+1, title));
//                    sheet.addCell(new Label(1, i+1, desc));
//                }
                Toast.makeText(getApplicationContext(), "Student list has been exported", Toast.LENGTH_LONG).show();

                if (file != null) {
                    if (file.exists()) {
                        Log.d(TagUtils.getTag(), "file path:-" + file.getPath());
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                        String type = mime.getMimeTypeFromExtension(ext);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileProvider", file);
                            intent.setDataAndType(contentUri, type);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            grantAllUriPermissions(intent, contentUri);
                        } else {
                            intent.setDataAndType(Uri.fromFile(file), type);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            grantAllUriPermissions(intent, Uri.fromFile(file));
                        }


                        startActivity(intent);
                    }
                }
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void grantAllUriPermissions(Intent intent, Uri uri) {
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case CALL_ADD_DATE:
                parseADDDateResponse(response);
                break;
            case CALL_GET_ATTENDENCE:
                parseGetAttendence(response);
                break;
            case CALL_ATTENDENCE_API:
                parseAttendenceResponse(response);
                break;
        }
    }

    public void parseAttendenceResponse(String response) {
        Log.d(TagUtils.getTag(), "attendence response:-" + response);
        try {
            Gson gson = new Gson();
            AttendancePOJO attendencePOJO = gson.fromJson(response, AttendancePOJO.class);
            if(attendencePOJO.getSuccess().equals("true")){
                exportToExcel(attendencePOJO.getAttendanceResultPOJOList(), course_id);
            }else{
                ToastClass.showShortToast(getApplicationContext(),"Attendance Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseGetAttendence(String response) {
        Log.d(TagUtils.getTag(), "attendence dates:-" + response);
        attendencePOJOList.clear();
        try {
            Gson gson = new Gson();
            AttendenceListPOJO attendenceListPOJO = gson.fromJson(response, AttendenceListPOJO.class);
            if (attendenceListPOJO.getSuccess().equals("true")) {
                attendencePOJOList.addAll(attendenceListPOJO.getAttendencePOJOList());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        attendenceDateAdapter.notifyDataSetChanged();
    }

    public void parseADDDateResponse(String response) {
        Log.d(TagUtils.getTag(), "response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                Intent intent = new Intent(AttendanceDateActivity.this, CourseStudentListActivity.class);
                intent.putExtra("course_id", course_id);
                intent.putExtra("a_id", jsonObject.optJSONObject("result").optString("a_id"));
                intent.putExtra("date", jsonObject.optJSONObject("result").optString("date"));
                intent.putExtra("is_updating", false);
                startActivity(intent);
            } else {
                ToastClass.showShortToast(getApplicationContext(), jsonObject.optString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
