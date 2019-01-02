package com.techease.gethelp.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.techease.gethelp.R;
import com.techease.gethelp.activities.MapsActivity;
import com.techease.gethelp.utils.GeneralUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRequestFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_LOCATION = 100;
    public static boolean fromRequest = false;
    public static double lattitude, longitude;
    @BindView(R.id.title)
    TextView tvTitle;
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
    String description, currentLocation, destination, contact, helpID;
    int driverID, userID;
    LocationManager locationManager;
    int PLACE_PICKER_REQUEST = 1;
    private View view;
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

    @SuppressLint("ClickableViewAccessibility")
    private void initUI() {
        ButterKnife.bind(this, view);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
        btnRequest.setOnClickListener(this);
        helpID = getArguments().getString("helpID");
        userID = GeneralUtils.getUserID(getActivity());
        tvTitle.setText(getArguments().getString("title"));
        etDestination.setOnClickListener(this);
//        ((NavigationDrawerActivity) getActivity()).getSupportActionBar().hide();
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request:
                if (validate()) {
                    GeneralUtils.putStringValueInEditor(getActivity(), "description", description).apply();
                    GeneralUtils.putStringValueInEditor(getActivity(), "cLocation", currentLocation).apply();
                    GeneralUtils.putStringValueInEditor(getActivity(), "destination", destination).apply();
                    GeneralUtils.putStringValueInEditor(getActivity(), "contact", contact).apply();
                    GeneralUtils.putStringValueInEditor(getActivity(), "helpID", helpID).apply();
                    Fragment fragment = new AvailableDriverFragment();
                    Bundle args = new Bundle();
                    fromRequest = true;
                    getFragmentManager().beginTransaction().replace(R.id.main_container, fragment).addToBackStack("tag").commit();
                    break;
                }
//            case R.id.et_destination:
//                    startActivity(new Intent(getActivity(), MapsActivity.class));


        }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();


                Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses;
                try {
                    addresses = gcd.getFromLocation(latti, longi, 1);
                    if (addresses.size() > 0) {
                        System.out.println(addresses.get(0).getSubLocality());

                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                        String city = addresses.get(0).getLocality();
//                        String state = addresses.get(0).getAdminArea();
//                        String country = addresses.get(0).getCountryName();
//                        String postalCode = addresses.get(0).getPostalCode();
//                        String knownName = addresses.get(0).getFeatureName();
                        Log.d("zma address", address);
                        etCurrentLocation.setText(address);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lattitude = latti;
                longitude = longi;

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();

                lattitude = latti;
                longitude = longi;

            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = latti;
                longitude = longi;

            } else {
                Toast.makeText(getActivity(), "Unable to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private boolean validate() {
        description = etDescription.getText().toString();
        currentLocation = etCurrentLocation.getText().toString();
        destination = etDestination.getText().toString();
        contact = etContact.getText().toString();
        valid = true;
        if (description.isEmpty() || description.length() < 5) {
            etDescription.setError("Please write a valid description");
            valid = false;
        } else {
            etDescription.setError(null);
        }
        if (currentLocation.isEmpty() || currentLocation.length() < 5) {
            etCurrentLocation.setError("Please enter your current location");
            valid = false;
        } else {
            etCurrentLocation.setError(null);
        }
        if (destination.isEmpty() || destination.length() < 5) {
            etDestination.setError("Please write a valid destination address");
            valid = false;
        } else {
            etDestination.setError(null);
        }
        if (contact.isEmpty() || contact.length() < 5) {
            etContact.setError("Please write a valid contact number");
            valid = false;
        } else {
            etContact.setError(null);
        }
        return valid;
    }
     public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
