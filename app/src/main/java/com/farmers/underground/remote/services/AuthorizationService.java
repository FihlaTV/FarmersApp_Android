package com.farmers.underground.remote.services;

import com.farmers.underground.remote.models.SuccessMsg;
import com.farmers.underground.remote.models.UserCredentials;
import com.farmers.underground.remote.models.UserProfile;
import com.farmers.underground.remote.models.UserRegistration;
import com.farmers.underground.remote.models.UserSignUpFB;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by tZpace
 * on 28-Sep-15.
 */
public interface AuthorizationService {

    @POST("users/register")
    Call<SuccessMsg> registerViaEmail(@Body UserRegistration user);

    @POST("users/signUpFb")
    Call<SuccessMsg> signUpFb(@Body UserSignUpFB user);

    @POST("users/signIn")
    Call<SuccessMsg> loginViaEmail(@Body UserCredentials user);

    @POST("users/signOut")
    Call<SuccessMsg> signOut();

    @FormUrlEncoded
    @POST("users/forgotPass")
    Call<SuccessMsg> forgotPass(@Field("email") String email);

    @GET("users/profile")
    Call<UserProfile> getUserProfileBySession();

    @DELETE ("users/dellAccountBySession")
    Call<SuccessMsg> dellAccountBySession();

    @FormUrlEncoded
    @POST("users/favorites")
    Call<SuccessMsg> addCropsToFavorites(@Field("favorites") String favoritesID);

    @DELETE("users/favorites/{displayName}")
    Call<SuccessMsg> deleteCropsFromFavorites(@Path("displayName") String displayName);

}
