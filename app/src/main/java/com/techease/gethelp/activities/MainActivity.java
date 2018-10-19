package com.techease.gethelp.activities;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.techease.gethelp.fragments.OnBoardFragment;

import com.techease.gethelp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = new OnBoardFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

    }
}
