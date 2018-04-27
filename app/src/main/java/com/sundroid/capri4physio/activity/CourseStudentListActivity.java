package com.sundroid.capri4physio.activity;

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
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.adapter.StudentCheckAdapter;
import com.sundroid.capri4physio.pojo.attendedstudent.AttendedStudentResultPOJO;
import com.sundroid.capri4physio.pojo.attendedstudent.AttendedUserPOJO;
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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

public class CourseStudentListActivity extends AppCompatActivity implements WebServicesCallBack {

    private static final String CALL_ALL_STUDENTS = "call_all_students";
    private static final String CALL_MARK_ATTENDANCE_API = "call_mark_attendance_api";
    private static final String CALL_ATTENDENCE_LIST = "call_attendence_list";

    @BindView(R.id.rv_student_list)
    RecyclerView rv_student_list;
    @BindView(R.id.btn_mark)
    Button btn_mark;
    @BindView(R.id.tv_save)
    TextView tv_save;

    public List<String> checkedStudents = new ArrayList<>();
    List<AttendedStudentResultPOJO> studentPOJOList = new ArrayList<>();
    String course_id = "", a_id = "", date = "";
    boolean is_updating;
    StudentCheckAdapter studentCheckAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_student_list);
        ButterKnife.bind(this);

        course_id = getIntent().getExtras().getString("course_id");
        a_id = getIntent().getExtras().getString("a_id");
        date = getIntent().getExtras().getString("date");
        is_updating = getIntent().getExtras().getBoolean("is_updating");

        attachAdapter();
        if (is_updating) {
//            callAttendedUser();
            btn_mark.setText("Update Attendance");
        } else {
            btn_mark.setText("Mark Attendance");
        }
        callAllStudents();

        btn_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(is_updating){
//                    callUpdateAttendance();
//                }else {
                    callMarkAttendance();
//                }
            }
        });


    }

    public void callUpdateAttendance(){
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("request_action", "UPDATE_MARK_ATTENDANCE"));
            nameValuePairs.add(new BasicNameValuePair("a_id", a_id));
            nameValuePairs.add(new BasicNameValuePair("c_id", course_id));
            nameValuePairs.add(new BasicNameValuePair("date", date));

            if (checkedStudents.size() > 0) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (String student_id : checkedStudents) {
                    JSONObject idjJsonObject = new JSONObject();
                    idjJsonObject.put("id", student_id);
                    jsonArray.put(idjJsonObject);
                }

                jsonObject.put("students", jsonArray);
                Log.d(TagUtils.getTag(), "students obj:-" + jsonObject.toString());
                nameValuePairs.add(new BasicNameValuePair("students", jsonObject.toString()));
//                new WebServiceBase(nameValuePairs, this,this, CALL_MARK_ATTENDANCE_API,true).execute(ApiConfig.STUDENT_ATTENDANCE);
            } else {
                ToastClass.showShortToast(getApplicationContext(), "please mark students first");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callAttendedUser(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "STUDENTS_BY_ATTENDANCE"));
        nameValuePairs.add(new BasicNameValuePair("c_id", course_id));
        nameValuePairs.add(new BasicNameValuePair("date", date));
//        new WebServiceBase(nameValuePairs,this,this,CALL_ATTENDENCE_LIST,true).execute(ApiConfig.STUDENT_ATTENDANCE);
    }

    public void callMarkAttendance() {
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("request_action", "update_attendance"));
            nameValuePairs.add(new BasicNameValuePair("a_id", a_id));
            nameValuePairs.add(new BasicNameValuePair("c_id", course_id));
            nameValuePairs.add(new BasicNameValuePair("date", date));

            if (isAnyMarked()) {
                JSONObject jsonObject = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                for (AttendedStudentResultPOJO attendedStudentResultPOJO: studentPOJOList) {
                    if(attendedStudentResultPOJO.getAttended()) {
                        JSONObject idjJsonObject = new JSONObject();
                        idjJsonObject.put("id", attendedStudentResultPOJO.getStuId());
                        jsonArray.put(idjJsonObject);
                    }
                }

                jsonObject.put("students", jsonArray);
                Log.d(TagUtils.getTag(), "students obj:-" + jsonObject.toString());
                nameValuePairs.add(new BasicNameValuePair("students", jsonObject.toString()));
                new WebServiceBase(nameValuePairs, this,this, CALL_MARK_ATTENDANCE_API,true).execute(WebServicesUrls.ATTENDANCE_TAKEN_CRUD);
            } else {
                ToastClass.showShortToast(getApplicationContext(), "please mark students first");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isAnyMarked(){
        for(AttendedStudentResultPOJO attendedStudentResultPOJO:studentPOJOList){
            if(attendedStudentResultPOJO.getAttended()){
                return true;
            }
        }
        return false;
    }

    public void callAllStudents() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "get_attended_student_list"));
        nameValuePairs.add(new BasicNameValuePair("date",date));
        nameValuePairs.add(new BasicNameValuePair("c_id", course_id));
        new WebServiceBase(nameValuePairs, this,this, CALL_ALL_STUDENTS,true).execute(WebServicesUrls.ATTENDANCE_TAKEN_CRUD);
    }

    public void attachAdapter() {
        studentCheckAdapter = new StudentCheckAdapter(this, null, studentPOJOList,is_updating);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv_student_list.setHasFixedSize(true);
        rv_student_list.setAdapter(studentCheckAdapter);
        rv_student_list.setLayoutManager(layoutManager);
        rv_student_list.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall = msg[0];
        String response = msg[1];
        switch (apicall) {
            case CALL_ALL_STUDENTS:
                parseALLStudents(response);
                break;
            case CALL_MARK_ATTENDANCE_API:
                parseMarkResponse(response);
                break;
            case CALL_ATTENDENCE_LIST:
                parseALLStudents(response);
                break;
        }
    }

    public void parseMarkResponse(String response) {
        Log.d(TagUtils.getTag(), "mark response:-" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optString("success").equals("true")) {
                ToastClass.showShortToast(getApplicationContext(), jsonObject.optString("message"));
                finish();
            } else {
                ToastClass.showShortToast(getApplicationContext(), jsonObject.optString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void parseALLStudents(String response) {
        studentPOJOList.clear();
        Log.d(TagUtils.getTag(), "response:-" + response);
        try {
            final AttendedUserPOJO attendedUserPOJO = new Gson().fromJson(response, AttendedUserPOJO.class);
            studentPOJOList.addAll(attendedUserPOJO.getAttendedUserPOJOS());
            tv_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> stringList=new ArrayList<>();
                    for(AttendedStudentResultPOJO studentPOJO:attendedUserPOJO.getAttendedUserPOJOS()){
                        stringList.add(studentPOJO.getFirstName()+" "+studentPOJO.getLastName());
                    }
                    exportToExcel(stringList,"Course Attendance");
                }
            });
            //
        } catch (Exception e) {
            e.printStackTrace();
        }
        studentCheckAdapter.notifyDataSetChanged();
    }

    private void exportToExcel(List<String> student_list,String course_Name) {

        final String fileName = course_Name+"_studentlist.xls";


        File file = new File(Environment.getExternalStorageDirectory()+File.separator+fileName);
        Log.d(TagUtils.getTag(),"file path:-"+file.toString());

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;

        try {
            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet(course_Name, 0);

            try {
                sheet.addCell(new Label(0, 0, "No")); // column and row
                sheet.addCell(new Label(1, 0, "Name"));
                for (int i = 0; i < student_list.size(); i++) {
                    String title =String.valueOf(i+1);
                    String desc = student_list.get(i);

                    sheet.addCell(new Label(0, i+1, title));
                    sheet.addCell(new Label(1, i+1, desc));
                }
                Toast.makeText(getApplicationContext(),"Student list has been exported",Toast.LENGTH_LONG).show();

                if (file != null) {
                    if (file.exists()) {
                        Log.d(TagUtils.getTag(), "file path:-" + file.getPath());
                        MimeTypeMap mime = MimeTypeMap.getSingleton();
                        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                        String type = mime.getMimeTypeFromExtension(ext);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileProvider", file);
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
}
