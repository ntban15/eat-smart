package com.annguyen.android.eatsmart.dietdetails.adapters;

import com.annguyen.android.eatsmart.entities.Recipe;

/**
 * Created by annguyen on 10/08/2017.
 */

public interface OnRecipeClickListener {

    void onRecipeClick(long id, int pos);

    void onRecipeLongClick(int pos);

    void onRemoveFromDietClick(long id);

    void onAddToDietClick(Recipe recipe);
}
