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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.fragments.HistoryFragment;
import com.techease.gethelp.utils.GeneralUtils;

import java.util.List;

/**
 * Created by eapple on 24/10/2018.
 */

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.MyViewHolder> {
   private Context context;
   private List<UsersDetailModel> userList;

   public AllUsersAdapter(Activity context, List<UsersDetailModel> userList){
       this.context = context;
       this.userList = userList;
   }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
       final UsersDetailModel usersDetailModel = userList.get(position);
       holder.tvTitl.setText(usersDetailModel.getName());
       holder.tvTime.setText(usersDetailModel.getAway());
       Log.d("zma",String.valueOf(usersDetailModel.getLanguages()));


       holder.layoutUsers.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Log.d("khan",String.valueOf(usersDetailModel.getId()));
               GeneralUtils.putIntegerValueInEditor(context,"user_id",usersDetailModel.getId());
               Fragment fragment = new HistoryFragment();
               ((AppCompatActivity)context).getFragmentManager().beginTransaction().replace(R.id.main_container,fragment).addToBackStack("").commit();
           }
       });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitl,tvDistance,tvTime;
        RelativeLayout layoutUsers;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitl = itemView.findViewById(R.id.tv_Title);
            tvDistance = itemView.findViewById(R.id.tv_km);
            tvTime = itemView.findViewById(R.id.tv_time);
            layoutUsers = itemView.findViewById(R.id.layout_users);
        }
    }
}
