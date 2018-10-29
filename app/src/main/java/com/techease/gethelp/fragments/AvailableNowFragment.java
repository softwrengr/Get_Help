package com.techease.gethelp.fragments;

import android.content.Context;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.techease.gethelp.R;
import com.techease.gethelp.adapters.AllUsersAdapter;
import com.techease.gethelp.datamodels.allUsersModel.UserResponseModel;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AvailableNowFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    @BindView(R.id.rv_around_users)
    RecyclerView rvUsers;
    AllUsersAdapter allUsersAdapter;
    List<UsersDetailModel> usersDetailModelList;
    View view;
    double lattitude, longitude;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_available_now, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);

        RecyclerView.LayoutManager mLayoutManagerReviews = new LinearLayoutManager(getActivity());
        rvUsers.setLayoutManager(mLayoutManagerReviews);
        usersDetailModelList = new ArrayList<>();

        if (alertDialog == null) {
            alertDialog = AlertUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        getAllUsers();

    }


    private void getAllUsers() {
        lattitude = HomeFragment.lattitude;
        longitude = HomeFragment.longitude;
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserResponseModel> allUsers = services.allUsers(String.valueOf(lattitude), String.valueOf(longitude));
        allUsers.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if (response.body().getSuccess()) {

                    if (alertDialog != null)
                        alertDialog.dismiss();

                    usersDetailModelList.addAll(response.body().getData());
                    allUsersAdapter = new AllUsersAdapter(getActivity(), usersDetailModelList);
                    rvUsers.setAdapter(allUsersAdapter);
                    allUsersAdapter.notifyDataSetChanged();

                } else {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });
    }
}
