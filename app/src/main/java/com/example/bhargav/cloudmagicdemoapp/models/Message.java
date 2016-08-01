package com.example.bhargav.cloudmagicdemoapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Bhargav on 7/30/2016.
 */
public class Message {
    @SerializedName("subject")
    String mSubject;

    @SerializedName("participants")
    List<String> mParticipants;

    @SerializedName("preview")
    String mPreview;

    @SerializedName("isRead")
    boolean isRead;

    @SerializedName("isStarred")
    boolean isStarred;

    @SerializedName("ts")
    long mTs;

    @SerializedName("id")
    long mMessageId;

    public String getSubject() {
        return mSubject;
    }

    public String getPreview() {
        return mPreview;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public long getTs() {
        return mTs;
    }

    public long getMessageId() {
        return mMessageId;
    }

    public List<String> getParticipants() {
        return mParticipants;
    }

    @Override
    public String toString() {
        return "Message{" +
                "mSubject='" + mSubject + '\'' +
                ", mParticipants=" + mParticipants +
                ", mPreview='" + mPreview + '\'' +
                ", isRead=" + isRead +
                ", isStarred=" + isStarred +
                ", mTs=" + mTs +
                ", mMessageId=" + mMessageId +
                '}';
    }
}
