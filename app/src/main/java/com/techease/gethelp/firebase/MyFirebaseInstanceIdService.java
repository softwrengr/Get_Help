package com.techease.gethelp.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.techease.gethelp.activities.MainActivity;
import com.techease.gethelp.utils.GeneralUtils;


/**
 * Created by Asus on 10/4/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "Firebase";
    public static String DEVICE_TOKEN;
    Context context = MyFirebaseInstanceIdService.this;


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        DEVICE_TOKEN = refreshedToken;

        Log.d(TAG, "Refreshed token: " + refreshedToken);
        if (refreshedToken.equals(null)) {
            Toast.makeText(this, "F, ID Null", Toast.LENGTH_SHORT).show();
            refreshedToken = FirebaseInstanceId.getInstance().getToken();

        } else {
            GeneralUtils.putStringValueInEditor(context, "deviceID", refreshedToken).commit();
        }


        sendRegistrationToServer(refreshedToken);
    }

    private String sendRegistrationToServer(String token) {
        return token;
        // TODO: Implement this method to send token to your app server.
    }
}
