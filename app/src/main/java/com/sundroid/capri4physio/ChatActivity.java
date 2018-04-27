package com.sundroid.capri4physio;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.sundroid.capri4physio.Util.FileUtils;
import com.sundroid.capri4physio.Util.StringUtils;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.Util.UtilityFunction;
import com.sundroid.capri4physio.adapter.ChatUserAdapter;
import com.sundroid.capri4physio.pojo.ResponseListPOJO;
import com.sundroid.capri4physio.pojo.chat.ChatPOJO;
import com.sundroid.capri4physio.webservice.ResponseListCallback;
import com.sundroid.capri4physio.webservice.ResponseListWebservice;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 101;
    private static final int VIDEO_CAPTURE = 102;
    private int PICK_IMAGE_REQUEST = 103;
    private int PICK_VIDEO_REQUEST = 104;

    String user_id;
    String fri_id;
    String name;
    String is_admin = "";

    @BindView(R.id.et_chat)
    EditText et_chat;
    @BindView(R.id.btn_send)
    Button btn_send;
    @BindView(R.id.rv_chat)
    RecyclerView rv_chat;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    List<ChatPOJO> chatPOJOS = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Chat");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user_id = bundle.getString("user_id");
            fri_id = bundle.getString("fri_id");
            name = bundle.getString("name");
            is_admin = bundle.getString("is_admin");
            getSupportActionBar().setTitle(name);
        }

        inflateChats();
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_chat.getText().toString().length() > 0) {
                    sendMessage("", "", "1", et_chat.getText().toString());
                }
            }
        });

        getPreviousChats();
    }

    public void getPreviousChats(){
        ArrayList<NameValuePair> nameValuePairs=new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("request_action","get_previous_chats"));
        nameValuePairs.add(new BasicNameValuePair("user_id",user_id));
        nameValuePairs.add(new BasicNameValuePair("fri_id",fri_id));
        new ResponseListWebservice<ChatPOJO>(nameValuePairs,this, new ResponseListCallback<ChatPOJO>(){

            @Override
            public void onGetMsg(ResponseListPOJO<ChatPOJO> responseListPOJO) {
                if(responseListPOJO.isSuccess()){
                    Collections.reverse(responseListPOJO.getResultList());
                    chatPOJOS.addAll(responseListPOJO.getResultList());
                    chatUserAdapter.notifyDataSetChanged();
                }
            }
        },ChatPOJO.class,"GET_CHATS",false).execute(WebServicesUrls.CHAT_CRUD);
    }

    public void sendMessage(String image_path, String thumb, String type, String msg) {
        try {

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


            reqEntity.addPart("request_action", new StringBody("insert_chat"));
            reqEntity.addPart("user_id", new StringBody(user_id));
            reqEntity.addPart("fri_id", new StringBody(fri_id));
            reqEntity.addPart("date", new StringBody(UtilityFunction.getCurrentDate()));
            reqEntity.addPart("time", new StringBody(UtilityFunction.getCurrentTime()));
            reqEntity.addPart("msg", new StringBody(msg));
            reqEntity.addPart("type", new StringBody(type));

            Log.d(TagUtils.getTag(),"type:-"+type);
            if (type.equals("2")) {
                if (new File(image_path).exists()) {
                    FileBody fileBody = new FileBody(new File(image_path));
                    reqEntity.addPart("file", fileBody);
                    reqEntity.addPart("thumb", new StringBody(""));
                } else {
                    reqEntity.addPart("file", new StringBody(""));
                    reqEntity.addPart("thumb", new StringBody(""));
                }
            } else if (type.equals("3")) {
                if (new File(image_path).exists() && new File(thumb).exists()) {
                    FileBody fileBody = new FileBody(new File(image_path));
                    FileBody thumbfileBody = new FileBody(new File(thumb));
                    reqEntity.addPart("file", fileBody);
                    reqEntity.addPart("thumb", thumbfileBody);
                } else {
                    reqEntity.addPart("file", new StringBody(""));
                    reqEntity.addPart("thumb", new StringBody(""));
                }
            } else if (type.equals("1")) {
                reqEntity.addPart("file", new StringBody(""));
                reqEntity.addPart("thumb", new StringBody(""));
            }

            new WebUploadService(reqEntity, this, new WebServicesCallBack() {
                @Override
                public void onGetMsg(String[] msg) {
                    try {
                        JSONObject jsonObject = new JSONObject(msg[1]);
                        if (jsonObject.optBoolean("success")) {
                            chatPOJOS.add(new Gson().fromJson(jsonObject.optJSONObject("result").toString(), ChatPOJO.class));
                            chatUserAdapter.notifyDataSetChanged();
                            rv_chat.scrollToPosition(chatPOJOS.size() - 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, "CALL_INSERT_CHAT", false).execute(WebServicesUrls.CHAT_CRUD);

        } catch (Exception e) {
            e.printStackTrace();
        }
        et_chat.setText("");
    }


    @Override
    protected void onResume() {
        super.onResume();
        getApplicationContext().registerReceiver(mMessageReceiver, new IntentFilter(StringUtils.CHAT_CLASS));
    }

    @Override
    protected void onPause() {
        super.onPause();
        getApplicationContext().unregisterReceiver(mMessageReceiver);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String result = intent.getStringExtra("message");
            Log.d(TagUtils.getTag(), "chatresult:-" + result);
            try {
                Gson gson = new Gson();
                ChatPOJO chatResultPOJO = gson.fromJson(result, ChatPOJO.class);
                if(user_id.equals("A")){
                    if(chatResultPOJO.getUserId().equals(fri_id)||chatResultPOJO.getFriId().equals(fri_id)){
                        chatPOJOS.add(chatResultPOJO);
                        chatUserAdapter.notifyDataSetChanged();
                        rv_chat.scrollToPosition(chatPOJOS.size() - 1);
                    }
                }else if(fri_id.equals("A")){
                    if(chatResultPOJO.getUserId().equals(user_id)||chatResultPOJO.getFriId().equals(user_id)){
                        chatPOJOS.add(chatResultPOJO);
                        chatUserAdapter.notifyDataSetChanged();
                        rv_chat.scrollToPosition(chatPOJOS.size() - 1);
                    }
                }

            } catch (Exception e) {
                Log.d(TagUtils.getTag(), e.toString());
            }
        }
    };

    ChatUserAdapter chatUserAdapter;

    public void inflateChats() {
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        horizontalLayoutManagaer.setStackFromEnd(true);
        Log.d(TagUtils.getTag(), "user chats:-" + chatPOJOS.size());
        chatUserAdapter = new ChatUserAdapter(this, chatPOJOS, user_id, fri_id);
        rv_chat.setLayoutManager(horizontalLayoutManagaer);
        rv_chat.setHasFixedSize(true);
        rv_chat.setItemAnimator(new DefaultItemAnimator());
        rv_chat.setNestedScrollingEnabled(false);
        rv_chat.setAdapter(chatUserAdapter);
    }


    public void showAttachDialog() {
        final Dialog dialog1 = new Dialog(ChatActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        dialog1.setCancelable(true);
        dialog1.setContentView(R.layout.dialog_show_attachments);
        dialog1.setTitle("Select");
        dialog1.show();
        dialog1.setCancelable(true);
        Window window = dialog1.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout ll_camera = (LinearLayout) dialog1.findViewById(R.id.ll_camera);
        LinearLayout ll_gallery = (LinearLayout) dialog1.findViewById(R.id.ll_gallery);
        LinearLayout ll_video_camera = (LinearLayout) dialog1.findViewById(R.id.ll_video_camera);
        LinearLayout ll_video_gallery = (LinearLayout) dialog1.findViewById(R.id.ll_video_gallery);
        Button btn_cancel = (Button) dialog1.findViewById(R.id.btn_cancel);

        ll_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
                dialog1.dismiss();
            }
        });

        ll_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendImageMessageClick();
                dialog1.dismiss();
            }
        });

        ll_video_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openvideoCamera();
                dialog1.dismiss();
            }
        });

        ll_video_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openVideoGallery();
                dialog1.dismiss();
            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
    }

    public void openVideoGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }

    public void sendImageMessageClick() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    String video_file = "";

    public void openvideoCamera() {
        File mediaFile = new File(FileUtils.getVideoChatDir() + File.separator + System.currentTimeMillis() + ".mp4");
        video_file = mediaFile.toString();
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        Uri videoUri = Uri.fromFile(mediaFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".fileProvider", mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
        startActivityForResult(intent, VIDEO_CAPTURE);
    }


    String pictureImagePath = "";

    public void startCamera() {
        String strMyImagePath = FileUtils.getBaseFilePath() + File.separator + "temp.png";

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

    public void sendVideoMessage(String file_path) {
//        String file_name=FileUtil.getvideoChatDir()+ File.separator+"1490787148660.mp4";
        File f = new File(file_path);
        if (f.exists()) {
            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(f.toString(), MediaStore.Video.Thumbnails.MINI_KIND);
//            iv_image.setImageBitmap(thumb);

            String storage_file = FileUtils.getVideoChatDir() + File.separator + System.currentTimeMillis() + ".png";
            FileOutputStream fos = null;

            try {
                fos = new FileOutputStream(new File(storage_file));
                Log.d(TagUtils.getTag(), "taking photos");
                thumb.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();

                try {
                    File srcFile = new File(file_path);
                    String ext = Files.getFileExtension(file_path);
                    File destFile = new File(FileUtils.getVideoChatDir() + File.separator + System.currentTimeMillis() + "." + ext);
                    if (copyFile(srcFile, destFile)) {

                        sendMessage(destFile.toString(), storage_file, "3", "");
                    } else {
                        ToastClass.showShortToast(getApplicationContext(), "File Sending Failed");
                    }
                } catch (Exception e) {
                    ToastClass.showShortToast(getApplicationContext(), "File Sending Failed");
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TagUtils.getTag(), "file not exist");
        }
    }

    public boolean copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());

            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String image_path_string = "";

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
                        ChatActivity.this, selectedImageUri);
                Log.d("sun", "" + selectedImagePath);
                if (selectedImagePath != null && selectedImagePath != "") {
                    image_path_string = selectedImagePath;
                    Log.d(TagUtils.getTag(), "selected path:-" + selectedImagePath);
//                    sendImageMessage(selectedImagePath);

                    sendMessage(selectedImagePath, "", "2", "");
//                    Bitmap bmImg = BitmapFactory.decodeFile(image_path_string);
//                    iv_profile.setImageBitmap(bmImg);
//                    Preferences.setCardImagePath(getApplicationContext(),selectedImagePath);

//                    startActivity(new Intent(SelectImageActivity.this,CardActivity.class));
                } else {
                    Toast.makeText(ChatActivity.this, "File Selected is corrupted", Toast.LENGTH_LONG).show();
                }
                System.out.println("Image Path =" + selectedImagePath);
            }
        }
        if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, "Video saved to:\n" +
//                        data.getData(), Toast.LENGTH_LONG).show();
                Log.d(TagUtils.getTag(), "video file path:-" + video_file);

                sendVideoMessage(video_file);
                video_file = "";
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
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
                    sendMessage(file_name.toString(), "", "2", "");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == PICK_VIDEO_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                if (null == data)
                    return;
                Uri selectedImageUri = data.getData();
                System.out.println(selectedImageUri.toString());
                // MEDIA GALLERY
                String selectedImagePath = getPath(
                        ChatActivity.this, selectedImageUri);
                Log.d("sun", "" + selectedImagePath);
                if (selectedImagePath != null && selectedImagePath != "") {
                    image_path_string = selectedImagePath;
                    Log.d(TagUtils.getTag(), "selected path:-" + selectedImagePath);
//                    sendFileMessage("video", selectedImagePath);

                    sendVideoMessage(selectedImagePath);
//                    Bitmap bmImg = BitmapFactory.decodeFile(image_path_string);
//                    iv_profile.setImageBitmap(bmImg);
//                    Preferences.setCardImagePath(getApplicationContext(),selectedImagePath);

//                    startActivity(new Intent(SelectImageActivity.this,CardActivity.class));
                } else {
                    Toast.makeText(ChatActivity.this, "File Selected is corrupted", Toast.LENGTH_LONG).show();
                }
                System.out.println("Image Path =" + selectedImagePath);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_file_attach:
                showAttachDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

}
