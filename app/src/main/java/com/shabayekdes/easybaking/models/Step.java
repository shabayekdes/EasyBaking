
package com.shabayekdes.easybaking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable{

    @SerializedName("description")
    private String Description;
    @SerializedName("id")
    private Long Id;
    @SerializedName("shortDescription")
    private String ShortDescription;
    @SerializedName("thumbnailURL")
    private String ThumbnailURL;
    @SerializedName("videoURL")
    private String VideoURL;

    protected Step(Parcel in) {
        Description = in.readString();
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readLong();
        }
        ShortDescription = in.readString();
        ThumbnailURL = in.readString();
        VideoURL = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getThumbnailURL() {
        return ThumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        ThumbnailURL = thumbnailURL;
    }

    public String getVideoURL() {
        return VideoURL;
    }

    public void setVideoURL(String videoURL) {
        VideoURL = videoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Description);
        if (Id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(Id);
        }
        dest.writeString(ShortDescription);
        dest.writeString(ThumbnailURL);
        dest.writeString(VideoURL);
    }
}
