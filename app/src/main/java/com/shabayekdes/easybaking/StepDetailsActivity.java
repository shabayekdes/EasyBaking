package com.shabayekdes.easybaking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.shabayekdes.easybaking.fragments.StepDetailsFragment;

public class StepDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (savedInstanceState == null) {
            Intent intent = getIntent();
            Bundle arguments;
            arguments = intent.getBundleExtra(RecipeDetailsActivity.STEPS_BUNDLE_KEY);


            int stepPosition = arguments.getInt(StepDetailsFragment.CURRENT_STEP_POSITION_KEY) + 1;
            String recipeName = arguments.getString(RecipeDetailsActivity.RECIPE_NAME_KEY);
            recipeName = getString(R.string.recipe_step, recipeName, stepPosition);
            getSupportActionBar().setTitle(recipeName);


            StepDetailsFragment fragment = new StepDetailsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_details_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
