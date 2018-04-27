package com.sundroid.capri4physio.webservice;

/**
 * Created by sunil on 29-12-2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sundroid.capri4physio.Util.TagUtils;
import com.sundroid.capri4physio.Util.ToastClass;
import com.sundroid.capri4physio.pojo.ResponsePOJO;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by sunil on 28-12-2016.
 */

public class ResponseWebservice<T> extends AsyncTask<String, Void, ResponsePOJO<T>> {
    ArrayList<NameValuePair> nameValuePairs;
    String jResult;
    Context context;
    Object object;
    static HttpClient httpClient;
    static HttpPost httppost;
    static HttpResponse response;
    static BufferedReader bufferedReader;
    InputStream is;
    ProgressDialog progressDialog;
    boolean isdialog = true;
    private final String TAG = getClass().getName();
    String msg;
    public ResponseWebservice(ArrayList<NameValuePair> nameValuePairs, Context context, Object object, String msg, boolean isdialog) {
        this.nameValuePairs = nameValuePairs;
//        this.nameValuePairs.addAll(UtilityFunction.getNameValuePairs(context));
        this.object = object;
        this.context = context;
        this.isdialog = isdialog;
        this.msg=msg;
        String nmv = "";
        for (NameValuePair nameValuePair : nameValuePairs) {
            nmv = nmv + nameValuePair.getName() + " : " + nameValuePair.getValue() + "\n";
        }
        Log.d(TagUtils.getTag(), "nmv:-" + nmv);

        Log.d(TAG, this.toString());
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isdialog) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(true);
            try {
                progressDialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected ResponsePOJO<T> doInBackground(String... params) {
        try {
            Log.d(TagUtils.getTag(),"url:-"+params[0]);
            jResult = httpCall(params[0], nameValuePairs);
            Log.d(TagUtils.getTag(),msg+":-"+jResult);
            if(jResult!=null&&jResult.length()>0){
                ResponsePOJO<T> responsePOJO = new Gson().fromJson(jResult, new TypeToken<ResponsePOJO<T>>() {}.getType());
                return responsePOJO;
            }
        } catch (Exception e) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(ResponsePOJO<T> responsePOJO) {
        super.onPostExecute(responsePOJO);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (responsePOJO != null) {
            ResponseCallBack<T> mcallback = (ResponseCallBack<T>) object;
            mcallback.onGetMsg(responsePOJO);
        }else{
            ToastClass.showShortToast(context,"No response from server");
        }

    }


    public static String httpCall(String url, ArrayList<NameValuePair> postParameters) {
        String result = "";
        try {
            httpClient = new DefaultHttpClient();
            httppost = new HttpPost(url);

            httppost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));

            // Execute HTTP Post Request
            response = httpClient.execute(httppost);

            //converting response into string
            result = convertToString(response);
            return result;
        } catch (IOException e) {
            Log.i("Io", e.toString());

            return "";
        }
    }

    private static String convertToString(HttpResponse response) {

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer stringBuffer = new StringBuffer("");
            String line = "";
            String LineSeparator = System.getProperty("line.separator");
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + LineSeparator);
            }
            bufferedReader.close();
            return stringBuffer.toString();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

    @Override
    public String toString() {
        return "WebServiceBase{" +
                "nameValuePairs=" + nameValuePairs +
                ", jResult='" + jResult + '\'' +
                ", context=" + context +
                ", object=" + object +
                ", is=" + is +
                ", progressDialog=" + progressDialog +
                ", isdialog=" + isdialog +
                ", TAG='" + TAG + '\'' +
                '}';
    }
}
