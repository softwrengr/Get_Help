package com.techease.gethelp.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.techease.gethelp.R;
import com.techease.gethelp.adapters.DriversRequestAdapter;
import com.techease.gethelp.datamodels.driversRequestModel.DriverJobsDataModel;
import com.techease.gethelp.datamodels.driversRequestModel.DriverJobsModelResponse;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverJobsListFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rv_driver_request)
    RecyclerView rvDriverJobs;
    DriversRequestAdapter adapter;
    List<DriverJobsDataModel> list;
    RecyclerView.LayoutManager layoutManager;
    private View view;

    public DriverJobsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_driver_jobs_list, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getActivity());
        rvDriverJobs.setLayoutManager(layoutManager);

        getJobList();

    }

    private void getJobList() {
        ApiInterface service = ApiClient.getApiClient().create(ApiInterface.class);
        Call<DriverJobsModelResponse> call = service.driverJobs(GeneralUtils.getUserID(getActivity()));
        call.enqueue(new Callback<DriverJobsModelResponse>() {
            @Override
            public void onResponse(Call<DriverJobsModelResponse> call, Response<DriverJobsModelResponse> response) {
                if (response.body().getSuccess()) {
                    list.addAll(response.body().getData());
                    adapter = new DriversRequestAdapter(getActivity(), list);
                    rvDriverJobs.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DriverJobsModelResponse> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }
}
