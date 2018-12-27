package com.techease.gethelp.activities;

import android.app.Fragment;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.techease.gethelp.firebase.MyFirebaseInstanceIdService;
import com.techease.gethelp.fragments.CheckUserFragment;
import com.techease.gethelp.fragments.OnBoardFragment;

import com.techease.gethelp.R;
import com.techease.gethelp.utils.GeneralUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        MultiDex.install(this);
        GeneralUtils.putStringValueInEditor(MainActivity.this, "deviceID", MyFirebaseInstanceIdService.DEVICE_TOKEN).apply();
        if(GeneralUtils.getSharedPreferences(MainActivity.this).getBoolean("loggedIn",false)){
            startActivity(new Intent(MainActivity.this, NavigationDrawerActivity.class));
        }else {
            GeneralUtils.connectFragment(this,new CheckUserFragment());
        }


    }
}
