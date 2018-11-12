package com.techease.gethelp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.techease.gethelp.R;
import com.techease.gethelp.utils.GeneralUtils;

public class CheckUserFragment extends Fragment {
    View view;
    Button btnGoLogin,btnGoSignup;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_check_user, container, false);
        btnGoLogin = view.findViewById(R.id.btn_go_login);
        btnGoSignup = view.findViewById(R.id.btn_go_signup);

        btnGoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showDialog();
            }
        });
        btnGoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           showDialog();
            }
        });
        return view;
    }

    private void showDialog(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.check_user_layout);
        Button btnClient,btnDriver;
        btnClient = dialog.findViewById(R.id.client);
        btnDriver = dialog.findViewById(R.id.driver);

        btnClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragment(getActivity(),new OnBoardFragment());
                GeneralUtils.putStringValueInEditor(getActivity(),"type","Client");
//                Fragment fragment = new OnBoardFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("type", "Client");
//                fragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("").commit();
                dialog.dismiss();
            }
        });

        btnDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.connectFragment(getActivity(),new OnBoardFragment());
                GeneralUtils.putStringValueInEditor(getActivity(),"type","Driver");
//                Fragment fragment = new OnBoardFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("type", "Driver");
//                fragment.setArguments(bundle);
//                getFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack("").commit();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
