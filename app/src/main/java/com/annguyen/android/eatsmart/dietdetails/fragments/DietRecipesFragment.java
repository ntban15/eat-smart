package com.annguyen.android.eatsmart.dietdetails.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.DietDetailActivity;
import com.annguyen.android.eatsmart.dietdetails.adapters.RecipeListAdapter;
import com.annguyen.android.eatsmart.dietdetails.adapters.OnRecipeClickListener;
import com.annguyen.android.eatsmart.dietdetails.decorators.RecipeGridLayoutDecorator;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.GlideImageLoader;
import com.annguyen.android.eatsmart.libs.adapters.ActionModeCallback;
import com.annguyen.android.eatsmart.libs.adapters.OnActionCallbackListener;
import com.annguyen.android.eatsmart.libs.base.ImageLoader;
import com.annguyen.android.eatsmart.recipedetails.RecipeDetailsActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DietRecipesFragment extends Fragment implements OnRecipeClickListener, OnActionCallbackListener {

    private boolean isCreated = false;

    @BindView(R.id.diet_detail_recipe_list)
    RecyclerView dietDetailRecipeList;
    Unbinder unbinder;

    private ImageLoader imageLoader;
    private Diet curDiet;
    private List<Recipe> recipeList;
    private RecipeListAdapter adapter;
    private ActionModeCallback modeCallback;
    private ActionMode actionMode;

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

        // set action mode callback
        modeCallback = new ActionModeCallback(adapter, this);

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
        adapter = new RecipeListAdapter(imageLoader, curDiet, recipeList, this, false);
        dietDetailRecipeList.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        dietDetailRecipeList.addItemDecoration(new RecipeGridLayoutDecorator(2, 60, true));
        dietDetailRecipeList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        actionMode = null;
        super.onDestroyView();
    }

    @Override
    public void onRecipeClick(long id, int pos) {
        if (null != actionMode)
            toggleSelection(pos);
        else {
            if (isConnected()) {
                Intent recipeDetail = new Intent(getContext(), RecipeDetailsActivity.class);
                recipeDetail.putExtra(RecipeDetailsActivity.RECIPE_ID, String.valueOf(id));
                startActivity(recipeDetail);
            }
            else {
                Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onRecipeLongClick(int pos) {
        if (null == actionMode) {
            actionMode = ((DietDetailActivity)getActivity()).startSupportActionMode(modeCallback);
        }

        toggleSelection(pos);
    }

    @Override
    public void onRemoveFromDietClick(long id) {
        //do nothing
    }

    @Override
    public void onAddToDietClick(Recipe recipe) {
        //do nothing
    }

    private void toggleSelection(int pos) {
        adapter.toggleSelection(pos);
        int count = adapter.getSelectedItemCount();

        if (count == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(count));
            actionMode.invalidate();
        }

    }

    @Override
    public void removeItems(List<Integer> positions, List<Long> recipeIds) {
        adapter.removeItems(positions);
        ((DietDetailActivity) getActivity()).removeRecipes(recipeIds);
    }

    @Override
    public void destroyActionMode() {
        actionMode = null;
    }
}
