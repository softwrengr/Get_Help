package com.techease.gethelp.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.techease.gethelp.R;
import com.techease.gethelp.activities.NavigationDrawerActivity;
import com.techease.gethelp.datamodels.genricResponseModel.GenericResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.GeneralUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRequestFragment extends Fragment implements View.OnClickListener {
    private View view;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.et_current_location)
    EditText etCurrentLocation;
    @BindView(R.id.et_destination)
    EditText etDestination;
    @BindView(R.id.et_contact)
    EditText etContact;
    @BindView(R.id.btn_request)
    Button btnRequest;
    String description,currentLocation, destination, contact, helpID;
    int driverID, userID;
    private boolean valid = false;

    public CreateRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_request, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        btnRequest.setOnClickListener(this);
        helpID = getArguments().getString("helpID");
        userID = GeneralUtils.getUserID(getActivity());

//        ((NavigationDrawerActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_request:
                if (validate()) {
                    GeneralUtils.putStringValueInEditor(getActivity(), "description", description).apply();
                    GeneralUtils.putStringValueInEditor(getActivity(), "cLocation", currentLocation).apply();
                    GeneralUtils.putStringValueInEditor(getActivity(), "destination", destination).apply();
                    GeneralUtils.putStringValueInEditor(getActivity(), "contact", contact).apply();
                    GeneralUtils.putStringValueInEditor(getActivity(), "helpID", helpID).apply();
                    Fragment fragment = new HomeFragment();
                    Bundle args = new Bundle();
                    args.putBoolean("fromRequest", true);
                    getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack("tag").commit();

                    break;
                }
        }
    }


    private boolean validate(){
        description = etDescription.getText().toString();
        currentLocation = etCurrentLocation.getText().toString();
        destination = etDestination.getText().toString();
        contact = etContact.getText().toString();
        valid = true;
        if (description.isEmpty() || description.length()<5){
            etDescription.setError("Please write a valid description");
            valid = false;
        }else {
            etDescription.setError(null);
        }
        if (currentLocation.isEmpty() || currentLocation.length()<5){
            etCurrentLocation.setError("Please enter your current location");
            valid = false;
        }else {
            etCurrentLocation.setError(null);
        }
        if (destination.isEmpty() || destination.length()<5){
            etDestination.setError("Please write a valid destination address");
            valid = false;
        }else {
            etDestination.setError(null);
        }
        if (contact.isEmpty() || contact.length()<5){
            etContact.setError("Please write a valid contact number");
            valid = false;
        }else {
            etContact.setError(null);
        }
        return valid;
    }
}
