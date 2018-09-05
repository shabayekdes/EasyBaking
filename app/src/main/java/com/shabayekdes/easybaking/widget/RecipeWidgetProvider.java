package com.shabayekdes.easybaking.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.shabayekdes.easybaking.MainActivity;
import com.shabayekdes.easybaking.R;
import com.shabayekdes.easybaking.models.Ingredient;
import com.shabayekdes.easybaking.models.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    private static String getIngredientsString(List<Ingredient> ingredients) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Ingredient ingredient : ingredients) {
            String ingredientDetails = ingredient.getQuantity() + " "
                    + ingredient.getMeasure() + " "
                    + ingredient.getIngredient();
            stringBuilder
                    .append(ingredientDetails)
                    .append(".\n");
        }
        return stringBuilder.toString();
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, String recipeName, String ingredientsList) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        views.setTextViewText(R.id.widget_ingredients_label, recipeName);
        views.setTextViewText(R.id.widget_ingredients_list, ingredientsList);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, null, null);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        Bundle recipeBundle = intent.getBundleExtra(MainActivity.RECIPE_BUNDLE_KEY);
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);


        if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE) && recipeBundle != null) {
            Recipe recipe = recipeBundle.getParcelable(MainActivity.RECIPE_KEY);

            String recipeName =  recipe.getName().toUpperCase() + " Ingredients";
            List<Ingredient> ingredients = recipe.getIngredients();
            String ingredientsList = getIngredientsString(ingredients);
            for (int widgetId : widgetIds) {
                updateAppWidget(context, widgetManager, widgetId, recipeName, ingredientsList);
            }
        }
    }
}

