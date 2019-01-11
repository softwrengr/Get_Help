package com.techease.gethelp.fragments;

import android.Manifest;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.techease.gethelp.R;
import com.techease.gethelp.adapters.LanguageAdapter;
import com.techease.gethelp.datamodels.addLanguageModel.AddLanguageResponse;
import com.techease.gethelp.datamodels.languagesDataModels.LanguageModel;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileLanguage;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.AlertUtils;
import com.techease.gethelp.utils.Configuration;
import com.techease.gethelp.utils.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class LanguagesFragment extends Fragment {
    @BindView(R.id.tv_add_languages)
    TextView tvAddLanguages;
    @BindView(R.id.rv_languages)
    RecyclerView rvLanguages;
    LanguageAdapter languagesAdapter;
    ArrayList<UserProfileLanguage> languageModelList;
    View view;

    int userID;
    String strCountry,strFile;
    File file;
    Uri uri_path;
    ImageView ivFlag;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_languages, container, false);
        userID = GeneralUtils.getMainUserID(getActivity());
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }

        }).check();

        initUI();
        return view;

    }

    private void initUI(){
        ButterKnife.bind(this,view);

        tvAddLanguages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             addLanguage();
            }
        });

        rvLanguages.setLayoutManager(new LinearLayoutManager(getActivity()));
        languageModelList = new ArrayList<>();
        getLanguages();
        GeneralUtils.acProgressPieDialog(getActivity());
        languagesAdapter = new LanguageAdapter(getActivity(), languageModelList);
        rvLanguages.setAdapter(languagesAdapter);

    }

    private void getLanguages() {
        ApiInterface service = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserProfileResponseModel> call = service.userProfile(String.valueOf(GeneralUtils.getUserID(getActivity())));
        call.enqueue(new Callback<UserProfileResponseModel>() {
            @Override
            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                GeneralUtils.progress.dismiss();
                if (response.body().getSuccess()){
                  languageModelList.addAll(response.body().getData().getLanguages());
                  languagesAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                GeneralUtils.progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addLanguage(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_dialog_layout);
        final CountryCodePicker countryCodePicker;
        countryCodePicker = dialog.findViewById(R.id.country_code);
        strCountry = countryCodePicker.getSelectedCountryName();
        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                strCountry = countryCodePicker.getSelectedCountryName();
            }
        });

        Button btnSelectPicture = dialog.findViewById(R.id.diload_btn_upload);
        btnSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentGalleryPic();
            }
        });

        Button btnAddLanguage = dialog.findViewById(R.id.dailog_add_language);
        btnAddLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralUtils.acProgressPieDialog(getActivity());
              addLanguageAPI();
                dialog.dismiss();
            }
        });
        ivFlag = dialog.findViewById(R.id.addFlag);
        dialog.show();
    }

    private void intentGalleryPic() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case 1:
                if (resultCode == RESULT_OK) {

                    uri_path = data.getData();
                    ivFlag.setImageURI(uri_path);
                    strFile = getImagePath(uri_path);
                    Log.d("path",strFile);
                    file = new File(strFile);

                    if (strFile == null) {
                        Toast.makeText(getActivity(), "please select a file", Toast.LENGTH_SHORT).show();
                    } else {

                    }

                } else {
                    Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }

    public String getImagePath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);

    }

    private void addLanguageAPI(){
        ApiInterface service = ApiClient.getApiClient().create(ApiInterface.class);
        RequestBody userID = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(GeneralUtils.getUserID(getActivity())));
        RequestBody countryName = RequestBody.create(MediaType.parse("multipart/form-data"), strCountry);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        final MultipartBody.Part docFile = MultipartBody.Part.createFormData("flag", file.getName(), requestFile);
        RequestBody bodyName = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        Call<AddLanguageResponse> call = service.addLanguage(userID, countryName, docFile, bodyName);
        call.enqueue(new Callback<AddLanguageResponse>() {
            @Override
            public void onResponse(Call<AddLanguageResponse> call, Response<AddLanguageResponse> response) {
                GeneralUtils.progress.dismiss();
                if (response.body().getSuccess()){
                    GeneralUtils.connectFragmentInDrawerWOB(getActivity(), new LanguagesFragment());
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddLanguageResponse> call, Throwable t) {
                GeneralUtils.progress.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
