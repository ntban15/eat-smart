package com.annguyen.android.eatsmart.dietdetails.fragments;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.adapters.DietRecipeListAdapter;
import com.annguyen.android.eatsmart.dietdetails.decorators.GridLayoutDecorator;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.GlideImageLoader;
import com.annguyen.android.eatsmart.libs.base.ImageLoader;
import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DietRecipesFragment extends Fragment {

    private static String DIET_PARC = "DIET_PARC";
    private static String RECIPES_PARC = "RECIPES_PARC";

    @BindView(R.id.diet_detail_recipe_list)
    RecyclerView dietDetailRecipeList;
    Unbinder unbinder;

    private ImageLoader imageLoader;
    private Diet curDiet;
    private List<Recipe> recipeList;
    private DietRecipeListAdapter adapter;

    public static DietRecipesFragment newInstance(Parcelable diet, Parcelable[] recipes) {
        DietRecipesFragment instance = new DietRecipesFragment();
        Bundle args = new Bundle();
        args.putParcelable(DIET_PARC, diet);
        args.putParcelableArray(RECIPES_PARC, recipes);
        instance.setArguments(args);
        return instance;
    }

    public DietRecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diet_recipes, container, false);
        unbinder = ButterKnife.bind(this, view);

        // get current diet
        curDiet = Parcels.unwrap(getArguments().getParcelable(DIET_PARC));

        // get list of recipes
        recipeList = new ArrayList<>();
        Parcelable[] recipesParc = getArguments().getParcelableArray(RECIPES_PARC);
        if (recipesParc != null) {
            Recipe recipeTemp;
            for (Parcelable recipeParc : recipesParc) {
                recipeTemp = Parcels.unwrap(recipeParc);
                recipeList.add(recipeTemp);
            }
        }

        // set up recycler view
        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        imageLoader = new GlideImageLoader(Glide.with(this));
        adapter = new DietRecipeListAdapter(imageLoader, curDiet, recipeList);

        dietDetailRecipeList.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        dietDetailRecipeList.addItemDecoration(new GridLayoutDecorator(2, 60, true));
        dietDetailRecipeList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
