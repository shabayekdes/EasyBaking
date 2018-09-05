package com.shabayekdes.easybaking.data;

import com.shabayekdes.easybaking.R;

import java.util.ArrayList;
import java.util.List;

public class ImagesAssets {

    private static final List<Integer> recipesImages = new ArrayList<Integer>(){{
        add(R.drawable.nutella_pie);
        add(R.drawable.brownies);
        add(R.drawable.yellow_cake);
        add(R.drawable.cheese_cake);
    }};

    public static int getRecipeImage(int position){
        return recipesImages.get(position);
    }
}
