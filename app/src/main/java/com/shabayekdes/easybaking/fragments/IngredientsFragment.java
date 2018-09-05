package com.shabayekdes.easybaking.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shabayekdes.easybaking.R;
import com.shabayekdes.easybaking.adapters.IngredientsAdapter;
import com.shabayekdes.easybaking.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientsFragment extends Fragment {

    public static final String INGREDIENTS_KEY = "ingredients";
    List<Ingredient> ingredients;
    @BindView(R.id.ingredients_recycler)
    RecyclerView ingredientsRecycler;
    IngredientsAdapter ingredientsAdapter;
    public IngredientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments ;
        if (savedInstanceState == null) {
       arguments = getArguments();
        }else {
            arguments = savedInstanceState;
        }
        if (arguments != null) {
            ingredients = arguments.getParcelableArrayList(INGREDIENTS_KEY);
        }
        ingredientsAdapter = new IngredientsAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);
        ButterKnife.bind(this, view);

        ingredientsRecycler.setAdapter(ingredientsAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ingredientsAdapter.setIngredients(ingredients);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(INGREDIENTS_KEY, (ArrayList<? extends Parcelable>) ingredients);
    }
}
