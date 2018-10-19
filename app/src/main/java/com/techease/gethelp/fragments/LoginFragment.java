package com.techease.gethelp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.techease.gethelp.activities.NavigationDrawerActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.techease.gethelp.R;

public class LoginFragment extends Fragment {
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_new_user)
    TextView tvNewUser;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);
        initUI();
        return view;
    }

    private void initUI(){
        ButterKnife.bind(this,view);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NavigationDrawerActivity.class));
            }
        });

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new SignUpFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("zxc").commit();
            }
        });
    }
}
