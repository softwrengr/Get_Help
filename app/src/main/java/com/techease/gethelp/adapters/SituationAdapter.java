package com.techease.gethelp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.datamodels.availableSituationsModel.AvailableSituationDataModel;
import com.techease.gethelp.fragments.CreateRequestFragment;
import com.techease.gethelp.fragments.HomeFragment;
import com.techease.gethelp.utils.GeneralUtils;

import java.util.List;

/**
 * Created by eapple on 24/10/2018.
 */

public class SituationAdapter extends RecyclerView.Adapter<SituationAdapter.MyViewHolder> {
    private Context context;
    private List<AvailableSituationDataModel> userList;


    public SituationAdapter(Activity context, List<AvailableSituationDataModel> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_situation_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final AvailableSituationDataModel usersDetailModel = userList.get(position);

        holder.tvTitl.setText(usersDetailModel.getTitle());
//        holder.tvTime.setText(usersDetailModel.getAway());
        holder.tvNo.setText(String.valueOf(position));



        holder.layoutUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new CreateRequestFragment();
                Bundle args = new Bundle();
                args.putString("helpID", usersDetailModel.getId());
                fragment.setArguments(args);
                ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack("tag").commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitl, tvDistance, tvTime, tvOnline, tvNo;
        RelativeLayout layoutUsers;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitl = itemView.findViewById(R.id.tv_Title);
            tvOnline = itemView.findViewById(R.id.tv_online);
            layoutUsers = itemView.findViewById(R.id.layout_users);
            tvNo = itemView.findViewById(R.id.tv_no);
        }
    }
}
