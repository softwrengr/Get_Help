package com.techease.gethelp.firebase;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.techease.gethelp.utils.GeneralUtils;


/**
 * Created by Asus on 10/4/2017.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "Firebase";
    public static String DEVICE_TOKEN;
    Context context;

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        DEVICE_TOKEN = refreshedToken;
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        if (refreshedToken.equals(null)) {
            // onTokenRefresh();
            Toast.makeText(this, "F, ID Null", Toast.LENGTH_SHORT).show();
            refreshedToken = FirebaseInstanceId.getInstance().getToken();

        } else {
//            GeneralUtils.putStringValueInEditor(context, "device_token", refreshedToken).commit();
        }

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private String sendRegistrationToServer(String token) {
        return token;
        // TODO: Implement this method to send token to your app server.
    }
}
