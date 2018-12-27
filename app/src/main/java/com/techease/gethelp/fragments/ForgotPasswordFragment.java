package com.techease.gethelp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.techease.gethelp.R;
import com.techease.gethelp.activities.NavigationDrawerActivity;
import com.techease.gethelp.datamodels.forgotpasswordmodel.ChangePasswordModel;
import com.techease.gethelp.datamodels.forgotpasswordmodel.CodeVerifiedModel;
import com.techease.gethelp.datamodels.forgotpasswordmodel.ResetPaswordModel;
import com.techease.gethelp.datamodels.loginModels.LoginResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.AlertUtils;
import com.techease.gethelp.utils.GeneralUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordFragment extends Fragment {
    @BindView(R.id.et_sendEmail)
    EditText etSendEmail;
    @BindView(R.id.btn_sendEmail)
    Button btnSendEmail;
    @BindView(R.id.et_verifyCode)
    EditText etVerifyCode;
    @BindView(R.id.btn_verifyCode)
    Button btnVerify;
    @BindView(R.id.et_newPassword)
    EditText etSetNewPassword;
    @BindView(R.id.btn_changePassword)
    Button btnChangePassword;
    @BindView(R.id.layoutVerification)
    LinearLayout layoutVerify;
    @BindView(R.id.layoutSendEmail)
    LinearLayout layoutSendEmail;
    @BindView(R.id.layoutChangePassword)
    LinearLayout layoutChangePassword;

    String strResetPassword, strVerifycode,strNewPassword;
    Boolean valid = false, verify = false,check =false;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendEmail()) {
                GeneralUtils.acProgressPieDialog(getActivity());
                    resetPassword();
                }
            }
        });

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify()) {
                    GeneralUtils.acProgressPieDialog(getActivity());

                    verifyCode();
                }
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changePassword()) {
                    GeneralUtils.acProgressPieDialog(getActivity());

                    setNewPassword();
                }
            }
        });
    }


    //networking call for reset password
    private void resetPassword() {
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        final Call<ResetPaswordModel> resetPassword = services.resetPassword(strResetPassword);
        resetPassword.enqueue(new Callback<ResetPaswordModel>() {
            @Override
            public void onResponse(Call<ResetPaswordModel> call, Response<ResetPaswordModel> response) {
               GeneralUtils.progress.dismiss();
                if (response.body().getStatus() == 200) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    layoutSendEmail.setVisibility(View.GONE);
                    layoutVerify.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<ResetPaswordModel> call, Throwable t) {
                Toast.makeText(getActivity(), "you got some error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //networking call for verify code
    private void verifyCode() {
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        final Call<CodeVerifiedModel> resetPassword = services.codeVerify(strVerifycode);
        resetPassword.enqueue(new Callback<CodeVerifiedModel>() {
            @Override
            public void onResponse(Call<CodeVerifiedModel> call, Response<CodeVerifiedModel> response) {
                GeneralUtils.progress.dismiss();
                if (response.body().getStatus() == 200) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    layoutVerify.setVisibility(View.GONE);
                    layoutChangePassword.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<CodeVerifiedModel> call, Throwable t) {
                Toast.makeText(getActivity(), "you got some error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //networking call for set new Password
    private void setNewPassword() {
        Log.d("check",strNewPassword);
        Log.d("check",strVerifycode);
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ChangePasswordModel> setNewPassword = services.changePassword(strNewPassword, strVerifycode);
        setNewPassword.enqueue(new Callback<ChangePasswordModel>() {
            @Override
            public void onResponse(Call<ChangePasswordModel> call, Response<ChangePasswordModel> response) {
                GeneralUtils.progress.dismiss();
                if(response.body().getStatus()==200){
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    GeneralUtils.connectFragmentWithBackStack(getActivity(),new LoginFragment());
                }

            }
            @Override
            public void onFailure(Call<ChangePasswordModel> call, Throwable t) {

            }
        });
    }


    private boolean sendEmail() {
        valid = true;
        strResetPassword = etSendEmail.getText().toString().trim();

        if (strResetPassword.isEmpty()) {
            etSendEmail.setError("you enter a wrong code");
            valid = false;
        }
        return valid;
    }

    private boolean verify() {
        verify = true;
        strVerifycode = etVerifyCode.getText().toString().trim();

        if (strVerifycode.isEmpty()) {
            etSendEmail.setError("you enter a wrong code");
            verify = false;
        }
        return verify;
    }

    private boolean changePassword(){
        check = true;
        strNewPassword = etVerifyCode.getText().toString().trim();

        if (strNewPassword.isEmpty()) {
            etSetNewPassword.setError("please set a strong password");
            check = false;
        }
        return check;
    }
}
