package com.techease.gethelp.activities;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techease.gethelp.fragments.OnBoardFragment;

import com.techease.gethelp.R;
import com.techease.gethelp.utils.GeneralUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(GeneralUtils.getSharedPreferences(MainActivity.this).getBoolean("loggedIn",false)){
            startActivity(new Intent(MainActivity.this, NavigationDrawerActivity.class));
        }else {
            GeneralUtils.connectFragment(this,new OnBoardFragment());
        }


    }
}
