package com.techease.gethelp.networking;

import android.widget.CalendarView;

import com.techease.gethelp.datamodels.addLanguageModels.AddLanguageResponseModel;
import com.techease.gethelp.datamodels.allUsersModel.UserResponseModel;
import com.techease.gethelp.datamodels.availableSituationsModel.AvailableSituationResponse;
import com.techease.gethelp.datamodels.clientRequestsModel.ClientRequestResponse;
import com.techease.gethelp.datamodels.driversRequestModel.DriverJobsModelResponse;
import com.techease.gethelp.datamodels.forgotpasswordmodel.ChangePasswordModel;
import com.techease.gethelp.datamodels.forgotpasswordmodel.CodeVerifiedModel;
import com.techease.gethelp.datamodels.forgotpasswordmodel.ResetPaswordModel;
import com.techease.gethelp.datamodels.genricResponseModel.GenericResponseModel;
import com.techease.gethelp.datamodels.loginModels.LoginResponseModel;
import com.techease.gethelp.datamodels.onlineStatusDatamodel.OnlineStatusDataModel;
import com.techease.gethelp.datamodels.signupModel.SignupResponseModel;
import com.techease.gethelp.datamodels.socialModels.SocialResponseModel;
import com.techease.gethelp.datamodels.userProfileModel.UserProfileResponseModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by eapple on 29/08/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("register/login")
    Call<LoginResponseModel> userLogin(@Field("email") String email,
                                       @Field("password") String password,
                                       @Field("type") String type);

    @FormUrlEncoded
    @POST("register/register")
    Call<SignupResponseModel> userRegistration(@Field("email") String email,
                                               @Field("password") String password,
                                               @Field("device_id") String device_id,
                                               @Field("name") String name,
                                               @Field("type") String type,
                                               @Field("latitude") String latitude,
                                               @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("register/forgot")
    Call<ResetPaswordModel> resetPassword(@Field("email") String email);

    @FormUrlEncoded
    @POST("register/CheckCode")
    Call<CodeVerifiedModel> codeVerify(@Field("code") String code);

    @FormUrlEncoded
    @POST("register/Resetpassword")
    Call<ChangePasswordModel> changePassword(@Field("password") String changePassword,
                                             @Field("code") String strCode);

    @FormUrlEncoded
    @POST("register/sociallogin")
    Call<SocialResponseModel> socialLogin(@Field("name") String name,
                                          @Field("email") String email,
                                          @Field("device_id") String deviceID,
                                          @Field("provider_id") String providerID,
                                          @Field("provider") String provider,
                                          @Field("latitude") String latitude,
                                          @Field("longitude") String longitude,
                                          @Field("type") String type);

    @FormUrlEncoded
    @POST("App/getUsers")
    Call<UserResponseModel> allUsers(@Field("latitude") String latitude,
                                     @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("App/login_status")
    Call<OnlineStatusDataModel> onlineStatus(@Field("user_id") String userID);

    @FormUrlEncoded
    @POST("App/getCompleteProfile")
    Call<UserProfileResponseModel> userProfile(@Field("user_id") String userID);

    @Multipart
    @POST("App/addlanguage")
    Call<AddLanguageResponseModel> addLanguage(@Part("userid") String userID,
                                               @Part("country") String gradudationYear,
                                               @Part RequestBody photo);

    @FormUrlEncoded
    @POST("App/makerequest")
    Call<GenericResponseModel> createRequest(@Field("driverid") Integer driverID,
                                             @Field("userid") Integer userID,
                                             @Field("helpid") Integer helpID,
                                             @Field("message") String message,
                                             @Field("current_location") String location,
                                             @Field("destination") String destination);

    @POST("App/situations")
    Call<AvailableSituationResponse> situation();

    @FormUrlEncoded
    @POST("App/driverrequest")
    Call<DriverJobsModelResponse>driverJobs(@Field("driverid") int id);

    @FormUrlEncoded
    @POST("App/accepetRequest")
    Call<GenericResponseModel> acceptRejectJob(@Field("requestid") String id,
                                               @Field("status") String status);
    @FormUrlEncoded
    @POST("App/clientrequest")
    Call<ClientRequestResponse> clientRequests(@Field("clientid") int clientID);

}
