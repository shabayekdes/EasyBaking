package com.shabayekdes.easybaking.connection;

import com.shabayekdes.easybaking.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

@GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
