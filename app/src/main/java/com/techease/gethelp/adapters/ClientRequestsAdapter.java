package com.techease.gethelp.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import com.techease.gethelp.datamodels.clientRequestsModel.ClientRequestDataModel;
import com.techease.gethelp.fragments.CreateRequestFragment;

import java.util.List;

/**
 * Created by kashif on 24/10/2018.
 */

public class ClientRequestsAdapter extends RecyclerView.Adapter<ClientRequestsAdapter.MyViewHolder> {
    private Context context;
    private List<ClientRequestDataModel> userList;


    public ClientRequestsAdapter(Activity context, List<ClientRequestDataModel> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_client_requests_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final ClientRequestDataModel usersDetailModel = userList.get(position);

        holder.tvClientName.setText("" + usersDetailModel.getDriver());
        holder.tvRequest.setText("" + usersDetailModel.getRequested());
        holder.tvMessage.setText("" + usersDetailModel.getMessage());
        holder.tvCurLoc.setText("" + usersDetailModel.getCurrentLocation());
        holder.tvDestination.setText("" + usersDetailModel.getDestination());
        holder.tvStatus.setText("" + usersDetailModel.getStatus());



    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvClientName, tvRequest, tvMessage, tvCurLoc, tvDestination, tvStatus;
        public MyViewHolder(View itemView) {
            super(itemView);

            tvClientName = itemView.findViewById(R.id.tv_driver_name);
            tvRequest = itemView.findViewById(R.id.tv_request);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvCurLoc = itemView.findViewById(R.id.tv_c_location);
            tvDestination = itemView.findViewById(R.id.tv_destination);
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}
