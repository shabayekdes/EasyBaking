package com.shabayekdes.easybaking;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import com.shabayekdes.easybaking.adapters.RecipesAdapter;
import com.shabayekdes.easybaking.connection.ApiClient;
import com.shabayekdes.easybaking.connection.ApiInterface;
import com.shabayekdes.easybaking.connection.ConnectionManager;
import com.shabayekdes.easybaking.idlingResources.SimpleIdlingResource;
import com.shabayekdes.easybaking.models.Recipe;
import com.shabayekdes.easybaking.widget.RecipeWidgetProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements Callback<List<Recipe>>, RecipesAdapter.OnClickHandler {
    public static final String RECIPE_BUNDLE_KEY = "recipeBundle";
    public static final String RECIPE_KEY = "recipe";
    public static final String RECIPES_LIST_KEY = "recipesList";
    public static final String RECIPES_RECYCLER_STATE_KEY = "recipesRecyclerState";
    private static final String TAG = MainActivity.class.getSimpleName();
    static List<Recipe> recipes;
    @BindView(R.id.recipes_recycler)
    RecyclerView recipesRecyclerView;
    Parcelable recipesRecyclerState;
    private RecipesAdapter recipesAdapter;
    @Nullable
    private SimpleIdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        recipes = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        recipesRecyclerState = recipesRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECIPES_RECYCLER_STATE_KEY, recipesRecyclerState);

        outState.putParcelableArrayList(RECIPES_LIST_KEY, (ArrayList<? extends Parcelable>) recipes);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        recipes = savedInstanceState.getParcelableArrayList(RECIPES_LIST_KEY);
        getData();

        recipesRecyclerState = savedInstanceState.getParcelable(RECIPES_RECYCLER_STATE_KEY);
        recipesRecyclerView.getLayoutManager().onRestoreInstanceState(recipesRecyclerState);
    }

    private void getData() {

        int noOfColumns = calculateNoOfColumns();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, noOfColumns);
        recipesRecyclerView.setLayoutManager(gridLayoutManager);

        recipesAdapter = new RecipesAdapter(this, this);
        recipesRecyclerView.setAdapter(recipesAdapter);
        if (!recipes.isEmpty()) {
            recipesAdapter.setRecipes(recipes);
            return;
        }
        if (ConnectionManager.isConnected(this)) {
            if (idlingResource != null) {
                idlingResource.setIdleState(false);
            }
            Retrofit retrofit = ApiClient.getApiClient();
            ApiInterface apiInterface = retrofit.create(ApiInterface.class);
            Call<List<Recipe>> getRecipes = apiInterface.getRecipes();
            getRecipes.enqueue(this);

        } else {
            ConnectionManager.noConnectionAlert(this);
        }
    }

    @Override
    public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
        recipes = response.body();
        recipesAdapter.setRecipes(recipes);
        if (idlingResource != null) {
            idlingResource.setIdleState(true);
        }
    }

    @Override
    public void onFailure(Call<List<Recipe>> call, Throwable t) {
        Log.w(TAG, t.getMessage());
    }

    @Override
    public void onRecipeItemClicked(Recipe recipe) {
        Bundle arguments = new Bundle();
        arguments.putParcelable(RECIPE_KEY, recipe);
        Intent recipeDetailsActivityIntent = new Intent(this, RecipeDetailsActivity.class);
        recipeDetailsActivityIntent.putExtra(RECIPE_BUNDLE_KEY, arguments);
        startActivity(recipeDetailsActivityIntent);

        Intent widgetIntent = new Intent(this, RecipeWidgetProvider.class);
        widgetIntent.putExtra(RECIPE_BUNDLE_KEY, arguments);
        widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int[] widgetIds = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(getApplication(), RecipeWidgetProvider.class));
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
        sendBroadcast(widgetIntent);
    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns;
        if (dpWidth >= 600) {
            noOfColumns = (int) (dpWidth / 200);
        } else {
            noOfColumns = 1;
        }
        return noOfColumns;
    }

    @NonNull
    @VisibleForTesting
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }
}
