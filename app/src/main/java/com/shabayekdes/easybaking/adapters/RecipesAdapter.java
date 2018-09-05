package com.shabayekdes.easybaking.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shabayekdes.easybaking.R;
import com.shabayekdes.easybaking.data.ImagesAssets;
import com.shabayekdes.easybaking.imageUtils.ImageHelper;
import com.shabayekdes.easybaking.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {
    private RecipesAdapter.OnClickHandler onClickHandler;
    private List<Recipe> recipes;
    private Resources resources;

    public RecipesAdapter(Context context, OnClickHandler onClickHandler) {
        this.onClickHandler = onClickHandler;
        this.resources = context.getResources();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.recipeNameTV.setText(recipe.getName());

        int imageResource = ImagesAssets.getRecipeImage(position);
        float cornerRadius = holder.recipeCard.getRadius();
        RoundedBitmapDrawable drawable = ImageHelper.getRoundedCornerBitmap(resources, imageResource, cornerRadius);
        holder.recipeImageIV.setImageDrawable(drawable);
    }

    @Override
    public int getItemCount() {
        if (recipes == null) {
            return 0;
        } else {
            return recipes.size();
        }
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    public interface OnClickHandler {
        void onRecipeItemClicked(Recipe recipe);
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipe_image_iv)
        ImageView recipeImageIV;
        @BindView(R.id.recipe_name_tv)
        TextView recipeNameTV;
        @BindView(R.id.recipe_card)
        CardView recipeCard;

        RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Recipe recipe = recipes.get(position);
            onClickHandler.onRecipeItemClicked(recipe);
        }
    }
}
