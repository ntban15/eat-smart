package com.annguyen.android.eatsmart.dietdetails.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.DietDetailActivity;
import com.annguyen.android.eatsmart.dietdetails.adapters.DietRecipeListAdapter;
import com.annguyen.android.eatsmart.dietdetails.decorators.RecipeGridLayoutDecorator;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.GlideImageLoader;
import com.annguyen.android.eatsmart.libs.base.ImageLoader;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DietRecipesFragment extends Fragment {

    private boolean isCreated = false;

    @BindView(R.id.diet_detail_recipe_list)
    RecyclerView dietDetailRecipeList;
    Unbinder unbinder;

    private ImageLoader imageLoader;
    private Diet curDiet;
    private List<Recipe> recipeList;
    private DietRecipeListAdapter adapter;

    public static DietRecipesFragment newInstance() {
        return new DietRecipesFragment();
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

        // init data
        initData();

        // setup recycler view
        setupRecyclerView();

        // mark fragment as created
        isCreated = true;

        return view;
    }

    private void initData() {
        if (null == curDiet) {
            curDiet = new Diet();
            curDiet.setDietKey(DietDetailActivity.DIET_DUMMY);
        }

        if (null == recipeList)
            recipeList = new ArrayList<>();
    }

    public void newData(Diet curDiet) {
        if (null != curDiet)
            this.curDiet = curDiet;
        if (isCreated)
            adapter.onDietChanged(curDiet);
    }

    public void addNewRecipe(Recipe recipe) {
        if (isCreated)
            adapter.addRecipe(recipe);
        else {
            if (null == adapter) {
                if (null == recipeList)
                    recipeList = new ArrayList<>();
                recipeList.add(recipe);
            }
        }
    }

    private void setupRecyclerView() {
        imageLoader = new GlideImageLoader(Glide.with(this));
        adapter = new DietRecipeListAdapter(imageLoader, curDiet, recipeList);
        dietDetailRecipeList.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        dietDetailRecipeList.addItemDecoration(new RecipeGridLayoutDecorator(2, 60, true));
        dietDetailRecipeList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
