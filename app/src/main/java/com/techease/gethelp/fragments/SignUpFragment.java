package com.techease.gethelp.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view  = inflater.inflate(R.layout.fragment_sign_up, container, false);
        initUI();
        return view;
    }

    private void initUI(){
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
        Call<SignupResponseModel> userLogin = services.userRegistration(strUserEmail, strUserPassword,strDeviceID,strName);
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
}
