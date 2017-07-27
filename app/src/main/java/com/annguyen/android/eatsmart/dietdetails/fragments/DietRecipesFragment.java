package com.annguyen.android.eatsmart.dietdetails.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annguyen.android.eatsmart.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DietRecipesFragment extends Fragment {

    public DietRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diet_recipes, container, false);
    }

}
