package com.techease.gethelp.adapters;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileLanguage;
import com.techease.gethelp.fragments.HistoryFragment;
import com.techease.gethelp.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eapple on 26/10/2018.
 */

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<UserProfileLanguage> languageModelArrayList;

    public LanguageAdapter(Activity context, ArrayList<UserProfileLanguage> userList){
        this.context = context;
        this.languageModelArrayList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_language_layout, parent, false);
        return new LanguageAdapter.MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UserProfileLanguage model = languageModelArrayList.get(position);
        holder.tvLanguage.setText(model.getLanguage());
        Glide.with(context).load(model.getFlag()).into(holder.ivflag);

    }

    @Override
    public int getItemCount() {
        return languageModelArrayList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvLanguage;
        ImageView ivflag;
        RelativeLayout layoutUsers;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvLanguage = itemView.findViewById(R.id.tv_languageName);
            ivflag =  itemView.findViewById(R.id.iv_flagName);

        }
    }
}

