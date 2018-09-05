package com.shabayekdes.easybaking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.shabayekdes.easybaking.fragments.IngredientsFragment;

public class IngredientsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle arguments;
            arguments = intent.getBundleExtra(RecipeDetailsActivity.INGREDIENTS_BUNDLE_KEY);

            String recipeName = arguments.getString(RecipeDetailsActivity.RECIPE_NAME_KEY);
            recipeName = getString(R.string.recipe_ingredients, recipeName);
            getSupportActionBar().setTitle(recipeName);


            IngredientsFragment fragment = new IngredientsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ingredients_container, fragment)
                    .commit();
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
