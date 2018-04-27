package com.sundroid.capri4physio.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.sundroid.capri4physio.Util.Pref;
import com.sundroid.capri4physio.Util.TagUtils;


/**
 * Created by Emobi-Android-002 on 9/19/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    Context ctx;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        PreferenceData.setDevice_Token(getApplicationContext(),refreshedToken);
        //Displaying token on logcat
        Log.d(TagUtils.getTag(), "Refreshed token: " + refreshedToken);
        Pref.SaveDeviceToken(getApplicationContext(), refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }
}