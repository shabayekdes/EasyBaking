
package com.shabayekdes.easybaking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Ingredient implements Parcelable{

    @SerializedName("ingredient")
    private String Ingredient;
    @SerializedName("measure")
    private String Measure;
    @SerializedName("quantity")
    private Double Quantity;

    protected Ingredient(Parcel in) {
        Ingredient = in.readString();
        Measure = in.readString();
        if (in.readByte() == 0) {
            Quantity = null;
        } else {
            Quantity = in.readDouble();
        }
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getIngredient() {
        return Ingredient;
    }

    public void setIngredient(String ingredient) {
        Ingredient = ingredient;
    }

    public String getMeasure() {
        return Measure;
    }

    public void setMeasure(String measure) {
        Measure = measure;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Ingredient);
        dest.writeString(Measure);
        if (Quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(Quantity);
        }
    }
}
