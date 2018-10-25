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

import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.datamodels.languagesDataModels.LanguageDetailModel;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileDetailModel;
import com.techease.gethelp.fragments.HistoryFragment;
import com.techease.gethelp.utils.GeneralUtils;

import java.util.List;

/**
 * Created by eapple on 25/10/2018.
 */

public class AllLanguagesAdapter extends RecyclerView.Adapter<AllLanguagesAdapter.MyViewHolder> {
    private Context context;
    private List<UsersDetailModel> userList;

    public AllLanguagesAdapter(Activity context, List<UsersDetailModel> userList){
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public AllLanguagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_language_layout, parent, false);

        return new AllLanguagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllLanguagesAdapter.MyViewHolder holder, int position) {
        final UsersDetailModel usersDetailModel = userList.get(position);
        holder.tvLanguage.setText(usersDetailModel.getName());
        Log.d("zma",String.valueOf(usersDetailModel.getLanguages()));


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvLanguage;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvLanguage = itemView.findViewById(R.id.tv_languageName);

        }
    }
}
