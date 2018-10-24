package com.techease.gethelp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.signupModel.SignupResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.AlertUtils;
import com.techease.gethelp.utils.GeneralUtils;

public class SignUpFragment extends Fragment {
    @BindView(R.id.tv_already_signin)
    TextView tvSignin;
    @BindView(R.id.et_userEmail)
    EditText etUserEmail;
    @BindView(R.id.et_userPassword)
    EditText etUserPassword;
    @BindView(R.id.et_UserName)
    EditText etName;
    @BindView(R.id.btn_signup)
    Button btnSignUp;

    View view;
    String strUserEmail,strUserPassword,strName,strDeviceID;
    Boolean validate=false;
    AlertDialog alertDialog;
    double lattitude, longitude;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 100;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initUI();
        return view;
    }

    private void initUI(){
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }

        ButterKnife.bind(this,view);
        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    userSignUp();
                }

            }
        });
    }

    private void userSignUp() {
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<SignupResponseModel> userLogin = services.userRegistration(strUserEmail, strUserPassword,strDeviceID,strName,String.valueOf(lattitude),String.valueOf(longitude));
        userLogin.enqueue(new Callback<SignupResponseModel>() {
            @Override
            public void onResponse(Call<SignupResponseModel> call, Response<SignupResponseModel> response) {
                alertDialog.dismiss();
                
                if(response.body().getMessage().equals("Registered Successfully")){
                    Toast.makeText(getActivity(), "sign up done successfully", Toast.LENGTH_SHORT).show();
                    GeneralUtils.connectFragmentWithBackStack(getActivity(),new LoginFragment());
                }
                else {
                    Toast.makeText(getActivity(), "you got some error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SignupResponseModel> call, Throwable t) {

            }
        });
    }

    private boolean validate(){
        validate = true;
        strUserEmail = etUserEmail.getText().toString();
        strUserPassword = etUserPassword.getText().toString();
        strName  = etName.getText().toString();
        strDeviceID = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        if(strUserEmail.equals("")){
            etUserEmail.setError("please enter your email");
            validate = false;
        }
        else if(strUserPassword.equals("")){
            etUserPassword.setError("please set your password");
            validate = false;
        }
        else if(strName.equals("")){
            etName.setError("please enter your name");
            validate = false;
        }


        return validate;
    }

    protected void buildAlertMessageNoGps() {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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
        final android.app.AlertDialog alert = builder.create();
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
}
