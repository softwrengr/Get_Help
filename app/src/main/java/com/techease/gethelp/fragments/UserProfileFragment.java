package com.techease.gethelp.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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


public class UserProfileFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    View view;

    @BindView(R.id.iv_user_profile)
    ImageView ivUserProfile;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.btn_save)
    Button btnSave;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_userprofile, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this,view);
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        getUserProfile();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void getUserProfile() {
        int userID = GeneralUtils.getMainUserID(getActivity());
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        final Call<UserProfileResponseModel> allUsers = services.userProfile(String.valueOf(userID));
        allUsers.enqueue(new Callback<UserProfileResponseModel>() {
            @Override
            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                alertDialog.dismiss();
                if(response.body().getSuccess()){
                    tvUserName.setText(response.body().getData().getName());
                    Glide.with(getActivity()).load(response.body().getData().getProfilePic()).into(ivUserProfile);
                    if(response.body().getData().getProfilePic().equals("")){
                        Glide.with(getActivity()).load(GeneralUtils.getFbPicture(getActivity())).into(ivUserProfile);
                    }



                }


            }

            @Override
            public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {

            }
        });
    }
}
