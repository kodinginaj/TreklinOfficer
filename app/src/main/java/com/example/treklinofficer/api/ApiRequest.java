package com.example.treklinofficer.api;

import com.example.treklinofficer.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRequest {

    @FormUrlEncoded
    @POST("loginOfficer")
    Call<ResponseModel> loginOfficer(
            @Field("email") String email,
            @Field("password") String password,
            @Field("latitude") Double latitude,
            @Field("longitude") Double longitude
    );

    @FormUrlEncoded
    @POST("officer/getComplaintByIdOfficer")
    Call<ResponseModel> getComplaint(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("officer/updateLocation")
    Call<ResponseModel> updateLocation(
            @Field("id") String id,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );
}
