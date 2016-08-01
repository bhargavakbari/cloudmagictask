package com.example.bhargav.cloudmagicdemoapp.networkmanager;

import com.example.bhargav.cloudmagicdemoapp.models.DetailMessage;
import com.example.bhargav.cloudmagicdemoapp.models.Message;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Bhargav on 7/30/2016.
 */
public interface ApiInterface {

    @GET("message")
    Call<List<Message>> getAllEmail();

    @GET("message/{id}")
    Call<DetailMessage> getMessageById(@Path("id") String id);

    @DELETE("message/{id}")
    Call<String> deleteMessageById(@Path("id") String id);

}
