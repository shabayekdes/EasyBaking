package com.shabayekdes.easybaking;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.shabayekdes.easybaking.adapters.StepsAdapter;
import com.shabayekdes.easybaking.fragments.IngredientsFragment;
import com.shabayekdes.easybaking.fragments.StepDetailsFragment;
import com.shabayekdes.easybaking.models.Recipe;
import com.shabayekdes.easybaking.models.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeDetailsActivity extends AppCompatActivity implements StepsAdapter.OnStepClickListener {
    public static final String STEPS_BUNDLE_KEY = "stepBundle";
    public static final String INGREDIENTS_BUNDLE_KEY = "ingredientBundle";
    public static final String STEPS_LIST_KEY = "stepsList";
    public static final String RECIPE_NAME_KEY = "recipeName";


    private static List<Step> steps;
    Recipe recipe;
    @BindView(R.id.steps_recycler)
    RecyclerView stepsRecyclerView;
    boolean twoPane;
    private StepsAdapter stepsAdapter;
    String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        stepsAdapter = new StepsAdapter(this);
        stepsRecyclerView.setAdapter(stepsAdapter);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle arguments = intent.getBundleExtra(MainActivity.RECIPE_BUNDLE_KEY);
            recipe = arguments.getParcelable(MainActivity.RECIPE_KEY);
            setupInfo();
        }

        if (findViewById(R.id.details_container) != null) {
            twoPane = true;

            if (savedInstanceState == null){
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList(IngredientsFragment.INGREDIENTS_KEY, (ArrayList<? extends Parcelable>) recipe.getIngredients());
                IngredientsFragment fragment = new IngredientsFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.details_container, fragment)
                        .commit();
            }
        }

    }

    private void setupInfo() {
        recipeName = recipe.getName();
        getSupportActionBar().setTitle(recipeName);
        steps = recipe.getSteps();
        stepsAdapter.setSteps(steps);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MainActivity.RECIPE_KEY, recipe);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = savedInstanceState.getParcelable(MainActivity.RECIPE_KEY);
        setupInfo();
    }

    @OnClick(R.id.recipe_ingredient_parent)
    public void ingredient(View view) {
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(IngredientsFragment.INGREDIENTS_KEY, (ArrayList<? extends Parcelable>) recipe.getIngredients());
        arguments.putString(RECIPE_NAME_KEY, recipeName);
        if (twoPane) {
            IngredientsFragment fragment = new IngredientsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, IngredientsActivity.class);
            intent.putExtra(INGREDIENTS_BUNDLE_KEY, arguments);
            startActivity(intent);
        }
    }

    @Override
    public void onStepClicked(int position) {
        Bundle arguments = new Bundle();
        arguments.putParcelableArrayList(STEPS_LIST_KEY, (ArrayList<? extends Parcelable>) steps);
        arguments.putInt(StepDetailsFragment.CURRENT_STEP_POSITION_KEY, position);
        arguments.putString(RECIPE_NAME_KEY, recipeName);
        if (twoPane) {
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            stepDetailsFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_container, stepDetailsFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(STEPS_BUNDLE_KEY, arguments);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
