package com.techease.gethelp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.allUsersModel.UserLanguage;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.datamodels.languagesDataModels.LanguageModel;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileDetailModel;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileLanguage;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileResponseModel;
import com.techease.gethelp.fragments.HistoryFragment;
import com.techease.gethelp.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

/**
 * Created by eapple on 31/10/2018.
 */

public class UserProfileAdapter extends RecyclerView.Adapter<UserProfileAdapter.MyViewHolder> {
    private Context context;
    private List<UserProfileLanguage> flagList;

    public UserProfileAdapter(Activity context, List<UserProfileLanguage> userList) {
        this.context = context;
        this.flagList = userList;

    }


    @NonNull
    @Override
    public UserProfileAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_flag_layout, parent, false);

        return new UserProfileAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final UserProfileAdapter.MyViewHolder holder, int position) {
        final UserProfileLanguage model = flagList.get(position);
        Glide.with(context).load(model.getFlag()).into(holder.ivflag);

    }

    @Override
    public int getItemCount() {
        return flagList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivflag;
        public MyViewHolder(View itemView) {
            super(itemView);

            ivflag =  itemView.findViewById(R.id.ivProfileFlag);

        }
    }
}


