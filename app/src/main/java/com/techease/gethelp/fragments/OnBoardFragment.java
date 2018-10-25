package com.techease.gethelp.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.techease.gethelp.R;
import com.techease.gethelp.activities.NavigationDrawerActivity;
import com.techease.gethelp.datamodels.signupModel.SignupResponseModel;
import com.techease.gethelp.datamodels.socialModels.SocialResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.AlertUtils;
import com.techease.gethelp.utils.GeneralUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;


public class OnBoardFragment extends Fragment implements View.OnClickListener {
    AlertDialog alertDialog;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.btn_email)
    Button btnEmail;
    @BindView(R.id.sign_in_button)
    Button signInButton;
    @BindView(R.id.btn_fb)
    Button btnFacebook;

    Unbinder unbinder;
    View view;
    CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final String EMAIL = "email";
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 200;
    String strEmail, strName, strDeviceID, strProviderID, strProvider,strToken,strImageUrl;
    double lattitude, longitude;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_onboard, container, false);
        unbinder = ButterKnife.bind(this, view);
        loginButton = view.findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setFragment(this);

        initUI();

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();

                // Callback registration for facebook login
                loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        final GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    strProviderID = object.getString("id");
                                    strName = object.getString("first_name") + object.getString("last_name");
                                    strEmail = object.getString("email");
                                    strProvider = "Facebook";

                                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                                    alertDialog.show();
                                    Bundle bundle=getFacebookData(object);
                                    socialLoginApiCall();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("zmaFbE", e.getMessage().toString());
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                        request.setParameters(parameters);
                        request.executeAsync();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
                //end
            }
        });



        //google Sign in code
        signInButton.setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getResources().getString(R.string.client_id))
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        //end

        return view;
    }

    private void initUI() {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }


        btnEmail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Fragment fragment = new SignUpFragment();
                getFragmentManager().beginTransaction().setCustomAnimations(R.animator.fade_in, R.animator.fade_out).replace(R.id.fragment_container, fragment).addToBackStack("abc").commit();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new LoginFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("abc").commit();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            updateUI(null);
        }
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check for existing Google Sign In account, if the user is already signed in
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
//        updateUI(account);
//    }


    private void updateUI(GoogleSignInAccount account) {

        if (account != null) {
            alertDialog = AlertUtils.createProgressDialog(getActivity());
            alertDialog.show();
            strName = account.getDisplayName();
            strEmail = account.getEmail();
            strProviderID = account.getId();
            strProvider = "Google";
            strImageUrl = account.getPhotoUrl().toString();
            GeneralUtils.putStringValueInEditor(getActivity(),"facebook_profile",strImageUrl);
            socialLoginApiCall();

        } else {
           Log.d("googleError","you got some error");
        }
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");
            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                GeneralUtils.putStringValueInEditor(getActivity(),"facebook_profile",String.valueOf(profile_pic));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("zmaE",String.valueOf(e.getCause()));
                return null;
            }

            return bundle;
        }
        catch(JSONException e) {
            Log.d("Error","Error parsing JSON");
        }
        return null;
    }

    //networking call for social login
    private void socialLoginApiCall() {
        strDeviceID = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<SocialResponseModel> userLogin = services.socialLogin(strEmail, strName, strDeviceID, strProviderID, strProvider,String.valueOf(lattitude),String.valueOf(longitude));
        userLogin.enqueue(new Callback<SocialResponseModel>() {
            @Override
            public void onResponse(Call<SocialResponseModel> call, Response<SocialResponseModel> response) {
                alertDialog.dismiss();
//                if(response.body().getSuccess()){
                    startActivity(new Intent(getActivity(), NavigationDrawerActivity.class));
                    strToken = response.body().getUser().getToken();
                GeneralUtils.putIntegerValueInEditor(getActivity(),"main_id",response.body().getUser().getUserId());
                    GeneralUtils.putStringValueInEditor(getActivity(),"api_token",strToken);
                    GeneralUtils.putBooleanValueInEditor(getActivity(), "loggedIn", true).commit();
//
//                }else {
//
//                }
            }

            @Override
            public void onFailure(Call<SocialResponseModel> call, Throwable t) {

            }
        });
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
