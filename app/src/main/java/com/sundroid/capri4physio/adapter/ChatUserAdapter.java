package com.sundroid.capri4physio.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.sundroid.capri4physio.R;
import com.sundroid.capri4physio.Util.FileUtils;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.pojo.chat.ChatPOJO;
import com.sundroid.capri4physio.webservice.WebServicesUrls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by sunil on 26-05-2017.
 */

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.MyViewHolder> {

    private List<ChatPOJO> horizontalList;
    private Activity activity;
    private final String TAG = getClass().getSimpleName();
    private String user_id;
    private String fri_id;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_chat;
        public TextView tv_time;
        public TextView tv_admin;
        public ImageView iv_image;
        public ImageView iv_video_image;
        public ImageView iv_video_download;
        public LinearLayout ll_chat;
        public LinearLayout ll_main;
        public FrameLayout frame_video;


        public MyViewHolder(View view) {
            super(view);
            tv_chat = (TextView) view.findViewById(R.id.tv_chat);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_admin = (TextView) view.findViewById(R.id.tv_admin);
            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            iv_video_image = (ImageView) view.findViewById(R.id.iv_video_image);
            iv_video_download = (ImageView) view.findViewById(R.id.iv_video_download);
            ll_chat = (LinearLayout) view.findViewById(R.id.ll_chat);
            ll_main = (LinearLayout) view.findViewById(R.id.ll_main);
            frame_video = (FrameLayout) view.findViewById(R.id.frame_video);
        }
    }


    public ChatUserAdapter(Activity activity, List<ChatPOJO> horizontalList, String user_id, String fri_id) {
        this.horizontalList = horizontalList;
        this.activity = activity;
        this.user_id = user_id;
        this.fri_id = fri_id;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_chats, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.tv_chat.setText(horizontalList.get(position).getMsg());
        holder.tv_time.setText(horizontalList.get(position).getTime());
        Log.d(TagUtils.getTag(), "chat pojo:-" + horizontalList.get(position).toString());
//        if (horizontalList.get(position).getIsAdmin().equals("1")) {
//            holder.ll_main.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            holder.ll_main.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
//            holder.ll_chat.setBackgroundResource(R.drawable.chat_receive_linear_back);
//            holder.tv_admin.setVisibility(View.VISIBLE);
//        } else {
            holder.tv_admin.setVisibility(View.GONE);
            if (user_id.equals(horizontalList.get(position).getUserId())) {
                holder.ll_main.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                holder.ll_main.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                holder.ll_chat.setBackgroundResource(R.drawable.chat_send_linear_back);
            } else {
                holder.ll_main.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                holder.ll_main.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
                holder.ll_chat.setBackgroundResource(R.drawable.chat_receive_linear_back);
            }
//        }

//        holder.ll_main.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                holder.ll_main.setBackgroundColor(Color.parseColor("#000000"));
//                holder.ll_chat.setBackgroundColor(Color.parseColor("#000000"));
//            }
//        });

//        setMesage(holder,horizontalList.get(position));
        ChatPOJO chatPOJO = horizontalList.get(position);

        if (chatPOJO.getType().equals("1")) {
            holder.iv_image.setVisibility(View.GONE);
            holder.frame_video.setVisibility(View.GONE);
            holder.tv_chat.setText(chatPOJO.getMsg());
            holder.tv_chat.setVisibility(View.VISIBLE);
        } else {
            if (chatPOJO.getType().equals("2")) {
                holder.iv_image.setVisibility(View.VISIBLE);
                holder.frame_video.setVisibility(View.GONE);
                holder.tv_chat.setVisibility(View.GONE);
                if (horizontalList.get(position).getFile().contains("chatupload")) {
                    loadImagewithGlide(activity, holder.iv_image, horizontalList.get(position).getFile());
                    holder.ll_chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openImageFile(WebServicesUrls.CHAT_MEDIA_URL + horizontalList.get(position).getFile());
                        }
                    });
                } else {
                    loadImagewithGlide(activity, holder.iv_image, WebServicesUrls.CHAT_MEDIA_URL + horizontalList.get(position).getFile());
                    holder.ll_chat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            openImageFile(horizontalList.get(position).getFile());
                        }
                    });
                }

            } else {
                if (chatPOJO.getType().equals("3")) {
                    holder.iv_image.setVisibility(View.GONE);
                    holder.frame_video.setVisibility(View.VISIBLE);
                    holder.tv_chat.setVisibility(View.GONE);
                    if (horizontalList.get(position).getThumb().contains("chatupload")) {
                        loadImagewithGlide(activity, holder.iv_video_image, WebServicesUrls.CHAT_MEDIA_URL + horizontalList.get(position).getThumb());
                        try {
                            final String file_name = horizontalList.get(position).getFile().split("/")[1];
                            final File video_file = new File(FileUtils.getVideoChatDir() + File.separator + file_name);
                            if (video_file.exists()) {
                                holder.iv_video_download.setVisibility(View.GONE);
                            } else {
                                holder.iv_video_download.setVisibility(View.VISIBLE);
                            }

                            holder.ll_chat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (video_file.exists()) {
                                        openVideoFile(FileUtils.getVideoChatDir() + File.separator + file_name);
                                    } else {
                                        downloadVideoFile(holder.iv_video_download, FileUtils.getVideoChatDir() + File.separator + file_name, "http://caprispine.in/chat/chatupload/" + file_name);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        loadImagewithGlide(activity, holder.iv_video_image, WebServicesUrls.CHAT_MEDIA_URL +horizontalList.get(position).getThumb());
                        holder.ll_chat.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (new File(horizontalList.get(position).getFile()).exists()) {
                                    openVideoFile(horizontalList.get(position).getFile());
                                }
                            }
                        });
                    }
                }
            }
        }
        holder.itemView.setTag(horizontalList.get(position));
    }

    public void downloadVideoFile(final ImageView imageView, final String destination_path, String url) {
        new AsyncTask<String, Integer, Long>() {
            ProgressDialog mProgressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(activity);
                mProgressDialog.setMessage("Downloading");
                mProgressDialog.setIndeterminate(false);
                mProgressDialog.setMax(100);
                mProgressDialog.setCancelable(true);
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                mProgressDialog.show();
            }

            @Override
            protected Long doInBackground(String... params) {
                int count;
                try {
                    Log.d(TagUtils.getTag(), "url:-" + params[0]);
                    URL url = new URL(params[0]);
                    URLConnection conexion = url.openConnection();
                    conexion.connect();
                    int lenghtOfFile = conexion.getContentLength();
                    InputStream input = new BufferedInputStream(url.openStream());
                    OutputStream output = new FileOutputStream(destination_path);
                    byte data[] = new byte[1024];
                    long total = 0;
                    while ((count = input.read(data)) != -1) {
                        total += count;
                        publishProgress((int) (total * 100 / lenghtOfFile));
                        output.write(data, 0, count);
                    }
                    output.flush();
                    output.close();
                    input.close();
                } catch (Exception e) {
                }
                return null;
            }

            protected void onProgressUpdate(Integer... progress) {
                mProgressDialog.setProgress(progress[0]);
                if (mProgressDialog.getProgress() == mProgressDialog.getMax()) {
                    mProgressDialog.dismiss();
                    Toast.makeText(activity.getApplicationContext(), "File Downloaded", Toast.LENGTH_SHORT).show();
                    imageView.setVisibility(View.GONE);
                }
            }

        }.execute(url);
    }

    public void openVideoFile(String file_name) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        File file = new File(file_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(activity.getApplicationContext(), "com.capri4physio.fileProvider", file);
            intent.setDataAndType(contentUri, "video/*");

        } else {
            intent.setDataAndType(Uri.fromFile(file), "video/*");
        }
        activity.startActivity(intent);
    }
    public void openImageFile(String file_url) {
//        Intent intent=new Intent(activity, ChatImageViewActivity.class);
//        intent.putExtra("url",file_url);
//        activity.startActivity(intent);
    }
    public void loadImagewithGlide(Activity activity, ImageView imageView, String url) {
        Glide.with(activity)
                .load(url)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                        makescrolldown();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                        makescrolldown();
                        return false;
                    }
                })
                .into(imageView);
    }

    public void setMesage(final MyViewHolder holder, ChatPOJO chatPOJO) {

    }

    @Override
    public int getItemCount() {
        if (horizontalList != null) {
            return horizontalList.size();
        } else {
            return 0;
        }
    }
}
