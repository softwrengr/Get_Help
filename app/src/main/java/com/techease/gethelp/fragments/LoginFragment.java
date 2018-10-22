package com.techease.gethelp.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
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
import com.techease.gethelp.activities.NavigationDrawerActivity;
import com.techease.gethelp.datamodels.loginModels.LoginResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.AlertUtils;
import com.techease.gethelp.utils.GeneralUtils;

public class LoginFragment extends Fragment {
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_new_user)
    TextView tvNewUser;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    View view;
    private boolean valid = false;
    private String strEmail, strPassword;
    android.support.v7.app.AlertDialog alertDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    alertDialog = AlertUtils.createProgressDialog(getActivity());
                    alertDialog.show();
                    userLogin();
                }
            }
        });

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragmentWithBackStack(getActivity(), new SignUpFragment());
            }
        });
    }

    private void userLogin() {
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LoginResponseModel> userLogin = services.userLogin(strEmail, strPassword);
        userLogin.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                alertDialog.dismiss();
                if(response.body().getMessage().equals("Logged in")){
                    startActivity(new Intent(getActivity(), NavigationDrawerActivity.class));
                }
                else {
                    Toast.makeText(getActivity(), "you got some error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {

            }
        });
    }

    private boolean validate() {
        valid = true;

        strEmail = etEmail.getText().toString();
        strPassword = etPassword.getText().toString();

        if (strEmail.isEmpty()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }
        if (strPassword.isEmpty()) {
            etPassword.setError("Please enter a valid password");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }

    //!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()
}
