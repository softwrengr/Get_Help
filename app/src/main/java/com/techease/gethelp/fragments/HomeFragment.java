package com.techease.gethelp.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.LinkAddress;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techease.gethelp.R;
import com.techease.gethelp.activities.NavigationDrawerActivity;
import com.techease.gethelp.adapters.AllUsersAdapter;
import com.techease.gethelp.datamodels.allUsersModel.UserResponseModel;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.datamodels.onlineStatusDatamodel.OnlineStatusDataModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.AlertUtils;
import com.techease.gethelp.utils.GeneralUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    android.support.v7.app.AlertDialog alertDialog;
    @BindView(R.id.rv_users)
    RecyclerView rvUsers;
    @BindView(R.id.ll_need)
    LinearLayout llNeed;
    AllUsersAdapter allUsersAdapter;
    List<UsersDetailModel> usersDetailModelList;
    View view;
    public static double lattitude, longitude;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        showOnlineStatus();
        initUI();

        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        llNeed.setOnClickListener(this);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }


        RecyclerView.LayoutManager mLayoutManagerReviews = new LinearLayoutManager(getActivity());
        rvUsers.setLayoutManager(mLayoutManagerReviews);
        usersDetailModelList = new ArrayList<>();


        if (alertDialog == null) {
            alertDialog = AlertUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        getAllUsers();
        hideKeyboard(getActivity());

    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private void getAllUsers() {
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
              Log.d("fail",t.getMessage());
            }
        });
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    //getting current location
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();

                lattitude = latti;
                longitude = longi;

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();

                lattitude = latti;
                longitude = longi;

            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = latti;
                longitude = longi;

            } else {
                Toast.makeText(getActivity(), "Unble to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showOnlineStatus(){
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<OnlineStatusDataModel> allUsers = services.onlineStatus(String.valueOf(GeneralUtils.getMainUserID(getActivity())));
        allUsers.enqueue(new Callback<OnlineStatusDataModel>() {
            @Override
            public void onResponse(Call<OnlineStatusDataModel> call, Response<OnlineStatusDataModel> response) {
                if (response.body().getSuccess()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<OnlineStatusDataModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_need:
                GeneralUtils.connectFragmentWithBackStack(getActivity(), new CreateRequestFragment());
        }
    }
}
