package com.techease.gethelp.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       UsersDetailModel usersDetailModel = userList.get(position);
       holder.tvTitl.setText(usersDetailModel.getName());
       holder.tvTime.setText(usersDetailModel.getAway());

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitl,tvDistance,tvTime;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvTitl = itemView.findViewById(R.id.tv_Title);
            tvDistance = itemView.findViewById(R.id.tv_km);
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}
