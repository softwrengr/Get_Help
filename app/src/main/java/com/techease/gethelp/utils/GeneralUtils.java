package com.techease.gethelp.utils;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.techease.gethelp.R;


public class GeneralUtils {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;



    public static Fragment connectFragment(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        return fragment;
    }

    public static Fragment connectFragmentWithBackStack(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("true").commit();
        return fragment;
    }
    public static SharedPreferences.Editor putStringValueInEditor(Context context, String key, String value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putString(key, value).commit();
        return editor;
    }

    public static SharedPreferences.Editor putIntegerValueInEditor(Context context, String key, int value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putInt(key, value).commit();
        return editor;
    }

    public static SharedPreferences.Editor putBooleanValueInEditor(Context context, String key, boolean value) {
        sharedPreferences = getSharedPreferences(context);
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value).commit();
        return editor;
    }


    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Configuration.MY_PREF, 0);
    }


}