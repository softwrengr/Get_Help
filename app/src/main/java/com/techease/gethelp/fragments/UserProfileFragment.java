package com.techease.gethelp.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.techease.gethelp.R;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileResponseModel;
import com.techease.gethelp.networking.ApiClient;
import com.techease.gethelp.networking.ApiInterface;
import com.techease.gethelp.networking.HTTPMultiPartEntity;
import com.techease.gethelp.utils.AlertUtils;
import com.techease.gethelp.utils.Configuration;
import com.techease.gethelp.utils.GeneralUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class UserProfileFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
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
        alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        getUserProfile();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
                    new UploadFileToServer().execute();
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
        final Call<UserProfileResponseModel> allUsers = services.userProfile(String.valueOf(userID));
        allUsers.enqueue(new Callback<UserProfileResponseModel>() {
            @Override
            public void onResponse(Call<UserProfileResponseModel> call, Response<UserProfileResponseModel> response) {
                alertDialog.dismiss();
                if (response.body().getSuccess()) {
                    tvUserName.setText(response.body().getData().getName());
                    etUserProfileEmail.setText(response.body().getData().getEmail());
                    etUserProfileName.setText(response.body().getData().getName());
                    Glide.with(getActivity()).load(response.body().getData().getProfilePic()).into(ivUserProfile);
                    if (response.body().getData().getProfilePic().equals("")) {
                        Glide.with(getActivity()).load(GeneralUtils.getFbPicture(getActivity())).into(ivUserProfile);
                    }

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

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Changing Profile Image");
            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Configuration.ChangeProfile);
            try {
                HTTPMultiPartEntity entity = new HTTPMultiPartEntity(
                        new HTTPMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) 100) * 100));

                            }
                        });
                Log.d("id", String.valueOf(userID));
                entity.addPart("profile_pic", new FileBody(file));
                Looper.prepare();
                entity.addPart("userid", new StringBody(String.valueOf(userID)));

                httppost.setEntity(entity);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                responseString = EntityUtils.toString(r_entity);

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                responseString = e.toString();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
            Log.d("zma", "api response " + responseString);
            return responseString;
        }

        @Override
        protected void onPostExecute(String message) {
            super.onPostExecute(message);
            Log.d("resp", message);
            try {
                Toast.makeText(getActivity(), "Profile Changed Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Server Response");
                builder.setMessage("you got some error");
                builder.setCancelable(true);
                builder.show();
            }
        }
    }
}
