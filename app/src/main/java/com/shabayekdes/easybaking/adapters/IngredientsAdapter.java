package com.shabayekdes.easybaking.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shabayekdes.easybaking.R;
import com.shabayekdes.easybaking.models.Ingredient;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredients;


    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.ingredient.setText(ingredient.getIngredient());
        double quantity = ingredient.getQuantity();
        DecimalFormat df = new DecimalFormat("###.#");
        String quantityToString = df.format(quantity);
        String quantityString = quantityToString + " " + ingredient.getMeasure();
        holder.quantity.setText(quantityString);
    }

    @Override
    public int getItemCount() {
        if (ingredients == null) {
            return 0;
        } else {
            return ingredients.size();
        }
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        notifyDataSetChanged();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ingredient)
        TextView ingredient;
        @BindView(R.id.quantity)
        TextView quantity;

        IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
