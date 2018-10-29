package com.techease.gethelp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.AlertUtils;
import com.techease.gethelp.utils.GeneralUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HistoryFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    View view;

    @BindView(R.id.iv_user_profile)
    ImageView ivUserProfile;
    @BindView(R.id.iv_flagone)
    ImageView ivFlagOne;
    @BindView(R.id.iv_flagtwo)
    ImageView ivFlagTwo;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.tv_user_away)
    TextView tvUserAway;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.btn_call)
    Button btnCall;

    String strContactNo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_history, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this,view);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        getAllUsers();

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", strContactNo, null));
                startActivity(phoneIntent);
            }
        });
    }

    private void getAllUsers() {
        int userID = GeneralUtils.getUserID(getActivity());
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        final Call<UserProfileResponseModel> allUsers = services.userProfile(String.valueOf(userID));
        allUsers.enqueue(new Callback<UserProfileResponseModel>() {
            @Override
            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                alertDialog.dismiss();
                if(response.body().getSuccess()){
                    tvUserName.setText(response.body().getData().getName());
                    tvUserAway.setText(response.body().getData().getEmail());
                    tvHistory.setText(response.body().getData().getHistory());
                    Glide.with(getActivity()).load(response.body().getData().getProfilePic()).into(ivUserProfile);
                    strContactNo = response.body().getData().getContact();

                }
                else {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {

            }
        });
    }
}