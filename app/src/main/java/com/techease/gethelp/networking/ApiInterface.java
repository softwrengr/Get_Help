package com.techease.gethelp.networking;

import com.techease.gethelp.datamodels.addCardModel.AddCardResponse;
import com.techease.gethelp.datamodels.addLanguageModel.AddLanguageResponse;
import com.techease.gethelp.datamodels.allUsersModel.UserResponseModel;
import com.techease.gethelp.datamodels.availableSituationsModel.AvailableSituationResponse;
import com.techease.gethelp.datamodels.checkCard.CheckCardResponse;
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

import okhttp3.MultipartBody;
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
                                               @Field("longitude") String longitude,
                                               @Field("device_type") String deviceType);

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
    Call<DriverJobsModelResponse> driverJobs(@Field("driverid") int id);

    @FormUrlEncoded
    @POST("App/accepetRequest")
    Call<GenericResponseModel> acceptRejectJob(@Field("requestid") String id,
                                               @Field("status") String status);

    @FormUrlEncoded
    @POST("App/clientrequest")
    Call<ClientRequestResponse> clientRequests(@Field("clientid") int clientID);

    @FormUrlEncoded
    @POST("App/saveCarddetails")
    Call<AddCardResponse> addCard(@Field("userid") int id,
                                  @Field("name") String name,
                                  @Field("card_number") String cardNumber,
                                  @Field("cvv") String cvv,
                                  @Field("month") String month,
                                  @Field("year") String year);

    @FormUrlEncoded
    @POST("App/checkCard")
    Call<CheckCardResponse> checkCard(@Field("userid") int id);

    @FormUrlEncoded
    @POST("App/completeJobDriver")
    Call<GenericResponseModel> driverCompleteJob(@Field("requestid") String requestID,
                                                 @Field("userid") String userID);

    @FormUrlEncoded
    @POST("App/payAmount")
    Call<GenericResponseModel> releasePayment(@Field("card_no") String cardNumber,
                                              @Field("cvv") String cvv,
                                              @Field("month") String month,
                                              @Field("year") String year,
                                              @Field("userid") int id,
                                              @Field("helpid") int helpID,
                                              @Field("driverid") int driverID);

    @Multipart
    @POST("App/addlanguage")
    Call<AddLanguageResponse> addLanguage(@Part("userid") RequestBody id,
                                          @Part("country") RequestBody country,
                                          @Part MultipartBody.Part photo,
                                          @Part("flag") RequestBody file);

    @Multipart
    @POST("App/userImage")
    Call<UserProfileResponseModel> addProfilePic(@Part("userid") RequestBody id,
                                                 @Part MultipartBody.Part photo,
                                                 @Part("profile_pic") RequestBody file);
}
