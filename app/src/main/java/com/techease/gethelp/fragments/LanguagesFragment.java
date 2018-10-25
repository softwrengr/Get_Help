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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.techease.gethelp.R;
import com.techease.gethelp.adapters.AllLanguagesAdapter;
import com.techease.gethelp.adapters.AllUsersAdapter;
import com.techease.gethelp.datamodels.allUsersModel.UserResponseModel;
import com.techease.gethelp.datamodels.allUsersModel.UsersDetailModel;
import com.techease.gethelp.datamodels.languagesDataModels.LanguageDetailModel;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileDetailModel;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileResponseModel;
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

public class LanguagesFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    @BindView(R.id.tv_add_languages)
    TextView tvAddLanguages;
    @BindView(R.id.rv_languages)
    RecyclerView rvLanguages;
    AllLanguagesAdapter allLanguagesAdapter;
    List<UsersDetailModel> usersDetailModelList;
    View view;
    double lattitude,longitude;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_languages, container, false);
        initUI();
        return view;

    }

    private void initUI(){
        ButterKnife.bind(this,view);

        tvAddLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        RecyclerView.LayoutManager mLayoutManagerReviews = new LinearLayoutManager(getActivity());
        rvLanguages.setLayoutManager(mLayoutManagerReviews);
        usersDetailModelList = new ArrayList<>();

        if (alertDialog == null) {
            alertDialog = AlertUtils.createProgressDialog(getActivity());
            alertDialog.show();
        }
        getAllLanguages();

    }

    private void getAllLanguages() {
        lattitude = HomeFragment.lattitude;
        longitude = HomeFragment.longitude;
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserResponseModel> allUsers = services.allUsers(String.valueOf(lattitude),String.valueOf(longitude));
        allUsers.enqueue(new Callback<UserResponseModel>() {
            @Override
            public void onResponse(Call<UserResponseModel> call, Response<UserResponseModel> response) {
                if (response.body().getSuccess())
                {

                if (alertDialog != null)
                    alertDialog.dismiss();

                usersDetailModelList.addAll(response.body().getData());
                allLanguagesAdapter=new AllLanguagesAdapter(getActivity(),usersDetailModelList);
                rvLanguages.setAdapter(allLanguagesAdapter);
                allLanguagesAdapter.notifyDataSetChanged();

                }
                else
                {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<UserResponseModel> call, Throwable t) {

            }
        });
    }
}
