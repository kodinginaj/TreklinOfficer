package com.example.treklinofficer.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retroserver {
    private static final String base_url = "https://treklin.yukseminar.id/api/";
    String url = "https://treklin.yukseminar.id/";

    private static Retrofit retrofit;

    public static Retrofit getClient(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public String url(){
        return url;
    }

}