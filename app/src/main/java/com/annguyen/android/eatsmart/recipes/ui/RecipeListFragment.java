package com.annguyen.android.eatsmart.recipes.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annguyen.android.eatsmart.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment {

    public static RecipeListFragment newInstance() {
        return new RecipeListFragment();
    }

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_list, container, false);
    }

}
