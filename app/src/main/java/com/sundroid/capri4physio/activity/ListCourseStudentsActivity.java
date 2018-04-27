package com.sundroid.capri4physio.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.adapter.StudentAdapter;
import com.sundroid.capri4physio.pojo.cources.CourcesResultPOJO;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCoursePOJO;
import com.sundroid.capri4physio.pojo.studentcourse.StudentCourseResultPOJO;
import com.sundroid.capri4physio.webservice.WebServiceBase;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ListCourseStudentsActivity extends AppCompatActivity implements WebServicesCallBack {
    private static final String GET_ALL_STUDENTS = "get_all_students";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_students)
    RecyclerView rv_students;
    @BindView(R.id.tv_total)
    TextView tv_total;
    @BindView(R.id.btn_save)
    Button btn_save;

    CourcesResultPOJO courcesResultPOJO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_course_students);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        courcesResultPOJO= (CourcesResultPOJO) getIntent().getSerializableExtra("cource");

        if(courcesResultPOJO!=null){
            getAllStudents();
        }else{
            finish();
        }

//        btn_print.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(ListCourseStudentsActivity.this,IncomeReportPrintActivity.class);
//                intent.putExtra("type","studentlist");
//                intent.putExtra("course_id",courcesResultPOJO.getC_id());
//                startActivity(intent);
//            }
//        });
    }


    public void ViewStudentDetails(StudentCourseResultPOJO studentCourseResultPOJO){
        Intent intent=new Intent(this,StudentCourseViewActivity.class);
        intent.putExtra("studentcourse",studentCourseResultPOJO);
        intent.putExtra("coursepojo",courcesResultPOJO);
        startActivity(intent);
    }

    public void getAllStudents(){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("request_action", "list_all_students"));
        nameValuePairs.add(new BasicNameValuePair("c_id", courcesResultPOJO.getId()));
        new WebServiceBase(nameValuePairs, this, this,GET_ALL_STUDENTS,true).execute(WebServicesUrls.STUDENT_COURSE_CRUD);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_attendance:
//                startActivity(new Intent(ListCourseStudentsActivity.this,CourseAttendanceReportActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        switch (apicall){
            case GET_ALL_STUDENTS:
                parseGetAllStudents(response);
                break;
        }
    }

    public void parseGetAllStudents(String response){
        Log.d(TagUtils.getTag(),"student response:-"+response);
        try{
            Gson gson=new Gson();
            StudentCoursePOJO studentCoursePOJO=gson.fromJson(response,StudentCoursePOJO.class);
            if(studentCoursePOJO.getSuccess().equals("true")){
                List<StudentCourseResultPOJO> studentCourseResultPOJOList=studentCoursePOJO.getStudentCourseResultPOJOList();

                StudentAdapter courceAdapter = new StudentAdapter(this,courcesResultPOJO, studentCourseResultPOJOList);
                LinearLayoutManager horizontalLayoutManagaer
                        = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                rv_students.setLayoutManager(horizontalLayoutManagaer);
                rv_students.setHasFixedSize(true);
                rv_students.setItemAnimator(new DefaultItemAnimator());
                rv_students.setAdapter(courceAdapter);

                final List<String> studentList=new ArrayList<>();
                for(StudentCourseResultPOJO studentCourseResultPOJO:studentCoursePOJO.getStudentCourseResultPOJOList()){
                    studentList.add(studentCourseResultPOJO.getFirstName()+" "+studentCourseResultPOJO.getLastName());
                }

                btn_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exportToExcel(studentList,courcesResultPOJO.getName());
                    }
                });
            }else{
                ToastClass.showShortToast(getApplicationContext(),"No Student Found");
                finish();
            }
        }catch (Exception e){
            e.printStackTrace();
            ToastClass.showShortToast(getApplicationContext(),"No Student Found");
            finish();
        }
    }


    private void exportToExcel(List<String> student_list,String course_Name) {
//
//        final String fileName = course_Name+"_studentlist.xls";
//
//
//        File file = new File(FileUtil.getBaseFilePath()+ File.separator+fileName);
//        Log.d(TagUtils.getTag(),"file path:-"+file.toString());
//
//        WorkbookSettings wbSettings = new WorkbookSettings();
//        wbSettings.setLocale(new Locale("en", "EN"));
//        WritableWorkbook workbook;
//
//        try {
//            workbook = Workbook.createWorkbook(file, wbSettings);
//            //Excel sheet name. 0 represents first sheet
//            WritableSheet sheet = workbook.createSheet(course_Name, 0);
//
//            try {
//                sheet.addCell(new Label(0, 0, "No")); // column and row
//                sheet.addCell(new Label(1, 0, "Name"));
//                for (int i = 0; i < student_list.size(); i++) {
//                    String title =String.valueOf(i+1);
//                    String desc = student_list.get(i);
//
//                    sheet.addCell(new Label(0, i+1, title));
//                    sheet.addCell(new Label(1, i+1, desc));
//                }
//                Toast.makeText(getApplicationContext(),"Student list has been exported",Toast.LENGTH_LONG).show();
//
//                if (file != null) {
//                    if (file.exists()) {
//                        Log.d(TagUtils.getTag(), "file path:-" + file.getPath());
//                        MimeTypeMap mime = MimeTypeMap.getSingleton();
//                        String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
//                        String type = mime.getMimeTypeFromExtension(ext);
//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName()+".fileProvider", file);
//                            intent.setDataAndType(contentUri, type);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            grantAllUriPermissions(intent, contentUri);
//                        } else {
//                            intent.setDataAndType(Uri.fromFile(file), type);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                            grantAllUriPermissions(intent, Uri.fromFile(file));
//                        }
//
//
//                        startActivity(intent);
//                    }
//                }
//            } catch (RowsExceededException e) {
//                e.printStackTrace();
//            } catch (WriteException e) {
//                e.printStackTrace();
//            }
//            workbook.write();
//            try {
//                workbook.close();
//            } catch (WriteException e) {
//                e.printStackTrace();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    private void grantAllUriPermissions(Intent intent, Uri uri) {
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_list, menu);//Menu Resource, Menu
        return true;
    }

}
