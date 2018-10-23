package com.techease.gethelp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.techease.gethelp.R;
import com.techease.gethelp.activities.NavigationDrawerActivity;
import com.techease.gethelp.utils.GeneralUtils;

import java.util.Arrays;


public class OnBoardFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.btn_email)
    Button btnEmail;
    @BindView(R.id.sign_in_button)
    Button signInButton;

    Unbinder unbinder;
    View view;
    CallbackManager callbackManager;
    private LoginButton loginButton;
    private static final String EMAIL = "email";
    GoogleSignInClient mGoogleSignInClient;
    //SignInButton signInButton;
    int RC_SIGN_IN = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_onboard, container, false);
        unbinder = ButterKnife.bind(this, view);

        callbackManager = CallbackManager.Factory.create();
        loginButton = view.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setFragment(this);

        initUI();

        // Callback registration for facebook login
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity(), "login success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), NavigationDrawerActivity.class));
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

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                Fragment fragment = new SignUpFragment();
                getFragmentManager().beginTransaction().setCustomAnimations(R.drawable.left_in,R.drawable.left_out).replace(R.id.fragment_container, fragment).addToBackStack("abc").commit();
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

    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        updateUI(account);
    }


    private void updateUI(GoogleSignInAccount account) {

        if (account == null) {
            Toast.makeText(getActivity(), "you are not authenthic", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), account.getEmail(), Toast.LENGTH_SHORT).show();
            GeneralUtils.connectFragment(getActivity(), new HomeFragment());
        }
    }
}
