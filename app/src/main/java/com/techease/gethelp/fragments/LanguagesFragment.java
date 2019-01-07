package com.techease.gethelp.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Looper;
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
import com.techease.gethelp.datamodels.languagesDataModels.LanguageModel;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

public class LanguagesFragment extends Fragment {
    android.support.v7.app.AlertDialog alertDialog;
    @BindView(R.id.tv_add_languages)
    TextView tvAddLanguages;
    @BindView(R.id.rv_languages)
    RecyclerView rvLanguages;
    LanguageAdapter languagesAdapter;
    ArrayList<LanguageModel> languageModelList;
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
        if (alertDialog == null)
            alertDialog = AlertUtils.createProgressDialog(getActivity());
        alertDialog.show();
        languagesAdapter = new LanguageAdapter(getActivity(), languageModelList);
        rvLanguages.setAdapter(languagesAdapter);

    }

    private void getLanguages() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Configuration.Languages
                , new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                alertDialog.dismiss();
                Log.d("response",response);
                try {
                    if (alertDialog != null)
                        alertDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    JSONArray jsonArray = jsonObject1.getJSONArray("languages");
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        LanguageModel model = new LanguageModel();
                        String language = jsonObject3.getString("language");
                        String flag = jsonObject3.getString("flag");
                        Log.d("zma",language);
                        model.setLanguages(language);
                        model.setFlag(flag);
                        languageModelList.add(model);
                    }
                    languagesAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (alertDialog != null)
                        alertDialog.dismiss();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id",String.valueOf(userID));
                return params;

            }
        };
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(stringRequest);

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
                new UploadFileToServer().execute();
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

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog =new ProgressDialog(getActivity());
            progressDialog.setMessage("We are adding language...");
            progressDialog.show();

        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

        }

        @Override
        protected String doInBackground(Void... params) {
            String responseString;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Configuration.ADDLanguage);
            try {
                HTTPMultiPartEntity entity = new HTTPMultiPartEntity(
                        new HTTPMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) 100) * 100));

                            }
                        });
                Log.d("id",String.valueOf(userID));
                entity.addPart("flag", new FileBody(file));
                Looper.prepare();
                entity.addPart("userid", new StringBody(String.valueOf(userID)));
                entity.addPart("country",new StringBody(strCountry));

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
                Toast.makeText(getActivity(), "Language Added Successfully", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                GeneralUtils.connectFragmentInDrawerActivity(getActivity(),new LanguagesFragment());

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
