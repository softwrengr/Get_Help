package com.techease.gethelp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techease.gethelp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddCardFragment extends Fragment {

    private View view;


    public AddCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_card, container, false);
        initUI();
        return view;
    }

    private void initUI() {

    }

}
