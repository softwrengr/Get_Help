package com.techease.gethelp.utils;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import com.techease.gethelp.R;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cc.cloudist.acplibrary.ACProgressPie;


public class GeneralUtils {
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static ACProgressFlower progress;




    public static Fragment connectFragment(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("tag").commit();
        return fragment;
    }

    public static Fragment connectFragmentWithBackStack(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack("true").commit();
        return fragment;
    }

    public static Fragment connectFragmentInDrawerActivity(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack("abc").commit();
        return fragment;
    }

    public static Fragment connectFragmentInDrawerWOB(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).commit();
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


    public static String getApiToken(Context context){
        return getSharedPreferences(context).getString("api_token","");
    }

    public static String getFbPicture(Context context){
        return getSharedPreferences(context).getString("facebook_profile","");
    }

    public static int getMainUserID(Context context){
        return getSharedPreferences(context).getInt("main_id",0);
    }

    public static int getUserID(Context context){
        return getSharedPreferences(context).getInt("userID",0);
    }

    public static String getType(Context context){
        return getSharedPreferences(context).getString("type","");
    }
    public static ACProgressFlower acProgressPieDialog(Context context) {
        progress =  new ACProgressFlower.Builder(context)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();
        progress.show();
        return progress;

    }

}
