package com.techease.gethelp.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.techease.gethelp.R;
import com.techease.gethelp.adapters.UserProfileAdapter;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileLanguage;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.utils.GeneralUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class UserProfileFragment extends Fragment implements View.OnClickListener {

    View view;

    @BindView(R.id.iv_user_profile)
    ImageView ivUserProfile;
    @BindView(R.id.tv_userProfileName)
    TextView tvUserName;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.et_userProfileName)
    EditText etUserProfileName;
    @BindView(R.id.et_userProfileEmail)
    EditText etUserProfileEmail;
    @BindView(R.id.rvProfileFlag)
    RecyclerView rvProfileFlag;
    @BindView(R.id.tv_change_password)
    TextView tvChangePassword;
    List<UserProfileLanguage> userProfileLanguageList;
    UserProfileAdapter userProfileAdapter;

    File file;
    Uri uri_path;
    private String strFile;
    private int userID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_userprofile, container, false);
        userID = GeneralUtils.getMainUserID(getActivity());
        permission();
        initUI();
        return view;
    }

    private void initUI() {
        ButterKnife.bind(this, view);
        tvChangePassword.setOnClickListener(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(rvProfileFlag.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvProfileFlag.setLayoutManager(layoutManager);
        userProfileLanguageList = new ArrayList<>();
        userProfileAdapter = new UserProfileAdapter(getActivity(), userProfileLanguageList);
        GeneralUtils.acProgressPieDialog(getActivity());
        getUserProfile();
        ivUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraBuilder();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strFile == null || strFile.equals("")) {
                    Toast.makeText(getActivity(), "please select image", Toast.LENGTH_SHORT).show();
                } else {
                    GeneralUtils.acProgressPieDialog(getActivity());
                    addProfilePic();
                }
            }
        });
    }

    private void permission() {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
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
    }

    private void getUserProfile() {
        int userID = GeneralUtils.getMainUserID(getActivity());
        ApiInterface services = ApiClient.getApiClient().create(ApiInterface.class);
        Call<UserProfileResponseModel> allUsers = services.userProfile(String.valueOf(GeneralUtils.getUserID(getActivity())));
        allUsers.enqueue(new Callback<UserProfileResponseModel>() {
            @Override
            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                GeneralUtils.progress.dismiss();
                if (response.body().getSuccess()) {

                    tvUserName.setText(response.body().getData().getName());
                    etUserProfileEmail.setText(response.body().getData().getEmail());
                    etUserProfileName.setText(response.body().getData().getName());

                    userProfileLanguageList.addAll(response.body().getData().getLanguages());
                    rvProfileFlag.setAdapter(userProfileAdapter);
                    userProfileAdapter.notifyDataSetChanged();
                    Picasso.with(getActivity()).load(response.body().getData().getProfilePic()).into(ivUserProfile);

                } else {
                    GeneralUtils.progress.dismiss();
                    Toast.makeText(getActivity(), "No Data Found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                Log.d("fail", t.getMessage());
            }
        });
    }

    public void cameraBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Open");
        String[] pictureDialogItems = {
                "\tGallery",
                "\tCamera"};
        builder.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        galleryIntent();
                        break;
                    case 1:
                        cameraIntent();
                        break;
                }
            }
        });
        builder.show();
    }

    public void cameraIntent() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(captureIntent, 10);
    }

    public void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 20);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 10:
                if (resultCode == RESULT_OK) {


                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                    File sourceFile = new File(Environment.getExternalStorageDirectory(),
                            System.currentTimeMillis() + ".jpg");


                    FileOutputStream fo;
                    try {
                        sourceFile.createNewFile();
                        fo = new FileOutputStream(sourceFile);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ivUserProfile.setImageBitmap(bm);
                    strFile = sourceFile.getAbsolutePath().toString();
                    file = new File(strFile);

                } else {
                    Toast.makeText(getActivity(), "No Image Selected", Toast.LENGTH_SHORT).show();
                }
                break;

            case 20:
                if (resultCode == RESULT_OK) {

                    uri_path = data.getData();
                    ivUserProfile.setImageURI(uri_path);
                    strFile = getImagePath(uri_path);
                    Log.d("path", strFile);
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

    private void addProfilePic() {
        ApiInterface service = ApiClient.getApiClient().create(ApiInterface.class);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(GeneralUtils.getUserID(getActivity())));
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        final MultipartBody.Part docFile = MultipartBody.Part.createFormData("profile_pic", file.getName(), requestFile);
        RequestBody bodyName = RequestBody.create(MediaType.parse("text/plain"), "upload_test");
        Call<UserProfileResponseModel> call = service.addProfilePic(tokenBody, docFile, bodyName);
        call.enqueue(new Callback<UserProfileResponseModel>() {
            @Override
            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                GeneralUtils.acProgressPieDialog(getActivity());
                if (response.body().getSuccess()) {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<UserProfileResponseModel> call, Throwable t) {
                GeneralUtils.acProgressPieDialog(getActivity());
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_change_password:
                GeneralUtils.connectFragmentInDrawerActivity(getActivity(), new ForgotPasswordFragment());
                break;
        }
    }
}
