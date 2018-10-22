package com.techease.gethelp.networking;

import com.techease.gethelp.datamodels.LoginModels.LoginResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by eapple on 29/08/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("user_authenthication.php")
    Call<LoginResponseModel> userLogin(@Field("username") String email,
                                       @Field("password") String password);



}
