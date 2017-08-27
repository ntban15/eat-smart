package com.annguyen.android.eatsmart.recipes.ui;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.adapters.OnRecipeClickListener;
import com.annguyen.android.eatsmart.dietdetails.adapters.RecipeListAdapter;
import com.annguyen.android.eatsmart.dietdetails.decorators.RecipeGridLayoutDecorator;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.GlideImageLoader;
import com.annguyen.android.eatsmart.libs.base.ImageLoader;
import com.annguyen.android.eatsmart.recipedetails.RecipeDetailsActivity;
import com.annguyen.android.eatsmart.recipes.RecipeListPresenter;
import com.annguyen.android.eatsmart.recipes.RecipeListPresenterImpl;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeListFragment extends Fragment implements RecipeListView, OnRecipeClickListener,
        android.support.v7.widget.SearchView.OnQueryTextListener{

    @BindView(R.id.suggested_recipes_list)
    RecyclerView suggestedRecipesList;
    @BindView(R.id.search_progress_bar)
    ProgressBar searchProgressBar;
    @BindView(R.id.search_recipes_container)
    RelativeLayout searchRecipesContainer;
    Unbinder unbinder;

    private RecipeListAdapter adapter;
    private ImageLoader imageLoader;
    private RecipeListPresenter presenter;
    private SearchView searchViewHolder = null;

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
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        prepareImageLoader();
        unbinder = ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        presenter = new RecipeListPresenterImpl(this);
        presenter.start();
        presenter.onRecipesRequest();
        return view;
    }

    private void prepareImageLoader() {
        imageLoader = new GlideImageLoader(Glide.with(this));
    }

    @Override
    public void prepareUI(Diet curDiet) {
        List<Recipe> recipes = new ArrayList<>();
        adapter = new RecipeListAdapter(imageLoader, curDiet, recipes, this, true);
        suggestedRecipesList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        suggestedRecipesList.addItemDecoration(new RecipeGridLayoutDecorator(1, 30, true));
        suggestedRecipesList.setAdapter(adapter);
    }

    @Override
    public void onSuccess(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgressBar() {
        searchProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        searchProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void addNewRecipe(Recipe recipe) {
        adapter.addRecipe(recipe);
    }

    @Override
    public void clearRecipes() {
        if (null != adapter)
            adapter.clearRecipes();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.stop();
        searchViewHolder = null;
    }

    @Override
    public void onRecipeClick(long id, int pos) {
        Intent recipeDetail = new Intent(getContext(), RecipeDetailsActivity.class);
        recipeDetail.putExtra(RecipeDetailsActivity.RECIPE_ID, String.valueOf(id));
        startActivity(recipeDetail);
    }

    @Override
    public void onRecipeLongClick(int pos) {
        //do nothing
    }

    @Override
    public void onRemoveFromDietClick(long id) {
        presenter.onRecipeRemoveClick(id);
    }

    @Override
    public void onAddToDietClick(Recipe recipe) {
        presenter.onRecipeAddClick(recipe);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recipe_search_menu, menu);
        SearchView searchView = new SearchView(getActivity());
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_for_recipes));
        MenuItem searchItem = menu.findItem(R.id.search_recipe);
        searchItem.setActionView(searchView);
        searchViewHolder = searchView;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (null != searchViewHolder)
            searchViewHolder.clearFocus();
        presenter.onNewQuery(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
