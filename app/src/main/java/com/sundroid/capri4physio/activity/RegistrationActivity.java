package com.sundroid.capri4physio.activity;

import android.annotation.TargetApi;
import android.app.Activity;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.Constants;
import com.sundroid.capri4physio.Util.FileUtils;
import com.sundroid.capri4physio.Util.Pref;
import com.sundroid.capri4physio.Util.StringUtils;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.pojo.user.StudentUserPOJO;
import com.sundroid.capri4physio.views.CircleImageView;
import com.sundroid.capri4physio.webservice.WebServicesCallBack;
import com.sundroid.capri4physio.webservice.WebServicesUrls;
import com.sundroid.capri4physio.webservice.WebUploadService;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrationActivity extends AppCompatActivity implements WebServicesCallBack{

    private static final String CALL_PROFILE_SAVE_API = "call_profile_save_api";
    private int PICK_IMAGE_REQUEST = 101;
    private static final int CAMERA_REQUEST = 103;

    @BindView(R.id.img_profile)
    CircleImageView img_profile;
    @BindView(R.id.et_first_name)
    EditText et_first_name;
    @BindView(R.id.et_last_name)
    EditText et_last_name;
    @BindView(R.id.rg_gender)
    RadioGroup rg_gender;
    @BindView(R.id.et_password)
    EditText et_password;
    @BindView(R.id.btn_submit)
    Button btn_submit;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_mobile)
    EditText et_mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupMenu menu = new PopupMenu(RegistrationActivity.this, view);

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuitem) {
                        switch (menuitem.getItemId()) {
                            case R.id.popup_camera:
                                startCamera();
                                break;
                            case R.id.popup_gallery:
                                selectProfilePic();
                                break;
                        }
                        return false;
                    }
                });
                menu.inflate(R.menu.menu_profile_pic_option);
                menu.show();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callRegisterAPI();
            }
        });
    }

    public void callRegisterAPI() {

        if(et_first_name.getText().toString().length()>0
                &&et_last_name.getText().toString().length()>0
                &&et_password.getText().toString().length()>0
                &&et_mobile.getText().toString().length()>0
                &&et_email.getText().toString().length()>0){
            String gender = "";
            if (rg_gender.getCheckedRadioButtonId() != -1) {
                switch (((RadioButton) findViewById(rg_gender.getCheckedRadioButtonId())).getText().toString().toLowerCase()) {
                    case "male":
                        gender = String.valueOf(Constants.GENDER_MALE);
                        break;
                    case "female":
                        gender = String.valueOf(Constants.GENDER_FEMALE);
                        break;
                    case "other":
                        gender = String.valueOf(Constants.GENDER_OTHER);
                        break;
                    default:
                        gender = String.valueOf(Constants.GENDER_DEFAULT);
                }
            }


            Log.d(TagUtils.getTag(), "profile image:-" + image_path_string);
            try {
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                if (image_path_string.length() > 0 && new File(image_path_string).exists()) {
                    FileBody bin1 = new FileBody(new File(image_path_string));
                    reqEntity.addPart("profile_pic", bin1);
                } else {
                    reqEntity.addPart("profile_pic", new StringBody(""));
                }
                reqEntity.addPart("request_action", new StringBody("create_user"));
                reqEntity.addPart("first_name", new StringBody(et_first_name.getText().toString()));
                reqEntity.addPart("last_name", new StringBody(et_last_name.getText().toString()));
                reqEntity.addPart("email", new StringBody(et_email.getText().toString()));
                reqEntity.addPart("mobile", new StringBody(et_mobile.getText().toString()));
                reqEntity.addPart("user_type", new StringBody("5"));
                reqEntity.addPart("password", new StringBody(et_password.getText().toString()));
                reqEntity.addPart("gender", new StringBody(gender));
                reqEntity.addPart("address", new StringBody(""));
                reqEntity.addPart("city", new StringBody(""));
                reqEntity.addPart("state", new StringBody(""));
                reqEntity.addPart("country", new StringBody(""));
                reqEntity.addPart("pin_code", new StringBody(""));
                reqEntity.addPart("device_type", new StringBody("android"));
                reqEntity.addPart("device_token", new StringBody(Pref.GetDeviceToken(getApplicationContext(),"")));

                new WebUploadService(reqEntity, this, this, CALL_PROFILE_SAVE_API, false).execute(WebServicesUrls.USER_CRUD);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            ToastClass.showShortToast(getApplicationContext(),"Please Enter Details Properly");
        }
    }


    String pictureImagePath = "";

    public void startCamera() {
        String strMyImagePath = Environment.getExternalStorageDirectory() + File.separator + "temp.png";

        pictureImagePath = strMyImagePath;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cameraIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileProvider", file);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        } else {
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

        }
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }


    public void selectProfilePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    String image_path_string="";
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
                        RegistrationActivity.this, selectedImageUri);
                Log.d("sun", "" + selectedImagePath);
                if (selectedImagePath != null && selectedImagePath != "") {
                    image_path_string = selectedImagePath;
                    Log.d(TagUtils.getTag(), "selected path:-" + selectedImagePath);
                    setProfilePic();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Selected File is Corrupted", Toast.LENGTH_LONG).show();
                }
                System.out.println("Image Path =" + selectedImagePath);
            }
            return;
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                Bitmap bmp = BitmapFactory.decodeFile(pictureImagePath);
                bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 4, bmp.getHeight() / 4, false);
                String strMyImagePath = FileUtils.getChatDir();
                File file_name = new File(strMyImagePath + File.separator + System.currentTimeMillis() + ".png");
                FileOutputStream fos = null;

                try {
                    fos = new FileOutputStream(file_name);
                    Log.d(TagUtils.getTag(), "taking photos");
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    image_path_string = file_name.toString();
                    setProfilePic();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
    }

    public void setProfilePic() {
        Glide.with(getApplicationContext())
                .load(image_path_string)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .dontAnimate()
                .into(img_profile);
    }

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

    @Override
    public void onGetMsg(String[] msg) {
        String apicall=msg[0];
        String response=msg[1];
        Log.d(TagUtils.getTag(),apicall+":-"+response);
        switch (apicall){
            case CALL_PROFILE_SAVE_API:
                parseResponse(response);
                break;
        }
    }

    public void parseResponse(String response){
        try{
            JSONObject jsonObject=new JSONObject(response);
            if(jsonObject.optString("success").equals("true")){

                    StudentUserPOJO studentUserPOJO=new Gson().fromJson(jsonObject.optJSONObject("result").toString(),StudentUserPOJO.class);
                    Pref.saveStudentUser(getApplicationContext(),studentUserPOJO);
                    Pref.SetBooleanPref(getApplicationContext(), StringUtils.IS_STUDENT_LOGIN,true);
                    startActivity(new Intent(RegistrationActivity.this,StudentDashboardActivity.class));
                    finishAffinity();

            }else{
                ToastClass.showShortToast(getApplicationContext(),jsonObject.optString("message"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
