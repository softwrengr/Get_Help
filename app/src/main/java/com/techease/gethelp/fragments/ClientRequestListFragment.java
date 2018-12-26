package com.techease.gethelp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.techease.gethelp.R;
import com.techease.gethelp.adapters.ClientRequestsAdapter;
import com.techease.gethelp.adapters.DriversRequestAdapter;
import com.techease.gethelp.datamodels.clientRequestsModel.ClientRequestDataModel;
import com.techease.gethelp.datamodels.clientRequestsModel.ClientRequestResponse;
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
public class ClientRequestListFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.rv_client_request)
    RecyclerView rvDriverJobs;
    ClientRequestsAdapter adapter;
    List<ClientRequestDataModel> list;
    RecyclerView.LayoutManager layoutManager;
    private View view;

    public ClientRequestListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_client_request_list, container, false);
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
        Call<ClientRequestResponse> call = service.clientRequests(GeneralUtils.getUserID(getActivity()));
        call.enqueue(new Callback<ClientRequestResponse>() {
            @Override
            public void onResponse(Call<ClientRequestResponse> call, Response<ClientRequestResponse> response) {
                if (response.body().getSuccess()) {
                    list.addAll(response.body().getData());
                    adapter = new ClientRequestsAdapter(getActivity(), list);
                    rvDriverJobs.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClientRequestResponse> call, Throwable t) {
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
