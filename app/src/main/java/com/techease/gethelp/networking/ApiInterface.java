package com.techease.gethelp.networking;

import com.techease.gethelp.datamodels.forgotpasswordmodel.ChangePasswordModel;
import com.techease.gethelp.datamodels.forgotpasswordmodel.CodeVerifiedModel;
import com.techease.gethelp.datamodels.forgotpasswordmodel.ResetPaswordModel;
import com.techease.gethelp.datamodels.loginModels.LoginResponseModel;
import com.techease.gethelp.datamodels.signupModel.SignupResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by eapple on 29/08/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponseModel> userLogin(@Field("email") String email,
                                       @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<SignupResponseModel> userRegistration(@Field("email") String email,
                                               @Field("password") String password,
                                               @Field("device_id") String device_id,
                                               @Field("name") String name);

    @FormUrlEncoded
    @POST("forgot")
    Call<ResetPaswordModel> resetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("CheckCode")
    Call<CodeVerifiedModel> codeVerify(@Field("code") String code);

    @FormUrlEncoded
    @POST("Resetpassword")
    Call<ChangePasswordModel> changePassword(@Field("password") String changePassword,
                                           @Field("code") String strCode);

}
