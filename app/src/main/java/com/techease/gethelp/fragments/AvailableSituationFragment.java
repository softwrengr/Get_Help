package com.techease.gethelp.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techease.gethelp.R;
import com.techease.gethelp.adapters.AllUsersAdapter;
import com.techease.gethelp.adapters.SituationAdapter;
import com.techease.gethelp.datamodels.availableSituationsModel.AvailableSituationResponse;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.datamodels.availableSituationsModel.AvailableSituationDataModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AvailableSituationFragment extends Fragment implements View.OnClickListener {

    private View view;
    @BindView(R.id.rv_users)
    RecyclerView rvUsers;
    @BindView(R.id.ll_need)
    LinearLayout llNeed;
    SituationAdapter allUsersAdapter;
    List<AvailableSituationDataModel> situationDataModelList;
    public static double lattitude, longitude;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 100;


    public AvailableSituationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_available_situation, container, false);
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
        situationDataModelList = new ArrayList<>();


       GeneralUtils.acProgressPieDialog(getActivity());
        allUsersAdapter = new SituationAdapter(getActivity(), situationDataModelList);
        rvUsers.setAdapter(allUsersAdapter);
        getAllUsers();

    }
    private void getAllUsers() {
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<AvailableSituationResponse> situation = services.situation();
        situation.enqueue(new Callback<AvailableSituationResponse>() {
            @Override
            public void onResponse(Call<AvailableSituationResponse> call, Response<AvailableSituationResponse> response) {
                GeneralUtils.progress.dismiss();
                if (response.body().getSuccess()) {
                    situationDataModelList.addAll(response.body().getData());
                    allUsersAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<AvailableSituationResponse> call, Throwable t) {
                GeneralUtils.progress.dismiss();
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

    @Override
    public void onClick(View v) {

    }
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
                Toast.makeText(getActivity(), "Unable to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
