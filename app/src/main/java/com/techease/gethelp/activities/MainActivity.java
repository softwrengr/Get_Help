package com.techease.gethelp.activities;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.techease.gethelp.R;
import com.techease.gethelp.firebase.MyFirebaseInstanceIdService;
import com.techease.gethelp.fragments.CheckUserFragment;
import com.techease.gethelp.utils.GeneralUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        MultiDex.install(this);
//        GeneralUtils.putStringValueInEditor(MainActivity.this, "deviceID", MyFirebaseInstanceIdService.DEVICE_TOKEN).commit();
        if (GeneralUtils.getSharedPreferences(MainActivity.this).getBoolean("loggedIn", false)) {
            startActivity(new Intent(MainActivity.this, NavigationDrawerActivity.class));
            finish();
        } else {
            Fragment fragment = new CheckUserFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }
}
