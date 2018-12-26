package com.techease.gethelp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.driversRequestModel.DriverJobsDataModel;
import com.techease.gethelp.datamodels.genricResponseModel.GenericResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by eapple on 24/10/2018.
 */

public class DriversRequestAdapter extends RecyclerView.Adapter<DriversRequestAdapter.MyViewHolder> {
    private Context context;
    private List<DriverJobsDataModel> userList;
    boolean isAccepted = false;


    public DriversRequestAdapter(Context context, List<DriverJobsDataModel> userList) {
        this.context = context;
        this.userList = userList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_driver_jobs_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final DriverJobsDataModel jobsDataModel = userList.get(position);
        holder.tvClientName.setText("" + jobsDataModel.getClient());
        holder.tvRequest.setText("" + jobsDataModel.getRequested());
        holder.tvMessage.setText("" + jobsDataModel.getMessage());
        holder.tvCurLoc.setText("" + jobsDataModel.getCurrentLocation());
        holder.tvDestination.setText("" + jobsDataModel.getDestination());
        holder.tvStatus.setText("" + jobsDataModel.getStatus());
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest(jobsDataModel.getRequestId(), "1");
                isAccepted = true;
            }
        });
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequest(jobsDataModel.getRequestId(), "2");
                isAccepted = false;
            }
        });


    }

    private void makeRequest(String requestID, String status) {
        ApiInterface service = ApiClient.getApiClient().create(ApiInterface.class);
        Call<GenericResponseModel> call = service.acceptRejectJob(requestID, status);
        call.enqueue(new Callback<GenericResponseModel>() {
            @Override
            public void onResponse(Call<GenericResponseModel> call, Response<GenericResponseModel> response) {

                if (response.body().getSuccess()) {
                    if (isAccepted) {
                        Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
                    }
                }else {
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
        private TextView tvClientName, tvRequest, tvMessage, tvCurLoc, tvDestination, tvStatus;
        private Button btnAccept, btnReject;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvClientName = itemView.findViewById(R.id.tv_client_name);
            tvRequest = itemView.findViewById(R.id.tv_request);
            tvMessage = itemView.findViewById(R.id.tv_message);
            tvCurLoc = itemView.findViewById(R.id.tv_c_location);
            tvDestination = itemView.findViewById(R.id.tv_destination);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnAccept = itemView.findViewById(R.id.btn_accept);
            btnReject = itemView.findViewById(R.id.btn_reject);

        }
    }
}
