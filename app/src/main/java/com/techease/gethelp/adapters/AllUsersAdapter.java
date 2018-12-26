package com.techease.gethelp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.datamodels.genricResponseModel.GenericResponseModel;
import com.techease.gethelp.fragments.AvailableSituationFragment;
import com.techease.gethelp.fragments.CreateRequestFragment;
import com.techease.gethelp.fragments.HistoryFragment;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.GeneralUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eapple on 24/10/2018.
 */

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.MyViewHolder> {
    private Context context;
    private List<UsersDetailModel> userList;


    public AllUsersAdapter(Activity context, List<UsersDetailModel> userList) {
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

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final UsersDetailModel usersDetailModel = userList.get(position);

        holder.tvTitl.setText(usersDetailModel.getName());
//        holder.tvTime.setText(usersDetailModel.getAway());
        holder.tvNo.setText(String.valueOf(position));
        holder.tvDistance.setText(usersDetailModel.getAway());
        String online = usersDetailModel.getIsOnline();
        if (online.equals("1") || online.equals("1")) {
            holder.tvOnline.setText("Online");
            holder.tvOnline.setTextColor(Color.parseColor("#00b300"));
        } else {
            holder.tvOnline.setText(usersDetailModel.getActiveSince());
        }

        holder.layoutUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CreateRequestFragment.fromRequest) {
                    makeRequest(usersDetailModel.getId());
                }
//                GeneralUtils.putIntegerValueInEditor(context, "user_id", usersDetailModel.getId()).apply();
//                Fragment fragment = new CreateRequestFragment();
//                Bundle args = new Bundle();
//                args.putInt("driver_id", usersDetailModel.getId());
//                fragment.setArguments(args);
//                ((AppCompatActivity) context).getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack("tag").commit();
            }
        });

    }

    private void makeRequest(int driverID) {
        ApiInterface service = ApiClient.getApiClient().create(ApiInterface.class);
        Call<GenericResponseModel> call = service.createRequest(driverID, GeneralUtils.getUserID(context),
                Integer.parseInt(GeneralUtils.getSharedPreferences(context).getString("helpID", "")),
                GeneralUtils.getSharedPreferences(context).getString("description", ""),
                GeneralUtils.getSharedPreferences(context).getString("cLocation", ""),
                GeneralUtils.getSharedPreferences(context).getString("destination", ""));
        call.enqueue(new Callback<GenericResponseModel>() {
            @Override
            public void onResponse(Call<GenericResponseModel> call, Response<GenericResponseModel> response) {
                if (response.body().getSuccess()) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    GeneralUtils.connectFragmentInDrawerActivity(context, new HistoryFragment());
                } else {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GenericResponseModel> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
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
            tvDistance = itemView.findViewById(R.id.tv_km);
//            tvTime = itemView.findViewById(R.id.tv_time);
            tvOnline = itemView.findViewById(R.id.tv_online);
            layoutUsers = itemView.findViewById(R.id.layout_users);
            tvNo = itemView.findViewById(R.id.tv_no);
        }
    }
}
