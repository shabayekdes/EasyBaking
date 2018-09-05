package com.shabayekdes.easybaking.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
    @SerializedName("id")
    private Long Id;
    @SerializedName("image")
    private String Image;
    @SerializedName("ingredients")
    private List<Ingredient> Ingredients;
    @SerializedName("name")
    private String Name;
    @SerializedName("servings")
    private Long Servings;
    @SerializedName("steps")
    private List<Step> Steps;

    public Recipe(Long id, String image, String name, Long servings) {
        Id = id;
        Image = image;
        Name = name;
        Servings = servings;
    }

    protected Recipe(Parcel in) {
        if (in.readByte() == 0) {
            Id = null;
        } else {
            Id = in.readLong();
        }
        Image = in.readString();
        Name = in.readString();
        if (in.readByte() == 0) {
            Servings = null;
        } else {
            Servings = in.readLong();
        }
        Ingredients = new ArrayList<>();
        in.readTypedList(Ingredients, Ingredient.CREATOR);
        Steps = new ArrayList<>();
        in.readTypedList(Steps, Step.CREATOR);
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public List<Ingredient> getIngredients() {
        return Ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        Ingredients = ingredients;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Long getServings() {
        return Servings;
    }

    public void setServings(Long servings) {
        Servings = servings;
    }

    public List<Step> getSteps() {
        return Steps;
    }

    public void setSteps(List<Step> steps) {
        Steps = steps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (Id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(Id);
        }
        dest.writeString(Image);
        dest.writeString(Name);
        if (Servings == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(Servings);
        }
        dest.writeTypedList(Ingredients);
        dest.writeTypedList(Steps);
    }
}
