package com.example.bhargav.cloudmagicdemoapp.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Bhargav on 7/30/2016.
 */
public class Participants {

    @SerializedName("name")
    String mName;

    @SerializedName("email")
    String mEmail;

    public String getName() {
        return mName;
    }

    public String getEmail() {
        return mEmail;
    }

    @Override
    public String toString() {
        return "Participants{" +
                "mName='" + mName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                '}';
    }
}
