package com.annguyen.android.eatsmart.recipes;

import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.GreenRobotEventBus;
import com.annguyen.android.eatsmart.libs.base.EventBus;
import com.annguyen.android.eatsmart.recipes.events.RecipesEvent;
import com.annguyen.android.eatsmart.recipes.ui.RecipeListView;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by annguyen on 24/08/2017.
 */

public class RecipeListPresenterImpl implements RecipeListPresenter {

    private EventBus eventBus;
    private RecipeListView view;
    private RecipeListModel model;

    public RecipeListPresenterImpl(RecipeListView view) {
        eventBus = new GreenRobotEventBus(org.greenrobot.eventbus.EventBus.getDefault());
        this.view = view;
        this.model = new RecipeListModelImpl();
    }

    @Override
    public void start() {
        eventBus.register(this);
    }

    @Override
    public void stop() {
        eventBus.unregister(this);
        view = null;
    }

    @Override
    public void onRecipesRequest() {
        view.showProgressBar();
        model.getRecipes();
    }

    @Subscribe
    @Override
    public void onRecipesEvent(RecipesEvent recipesEvent) {
        view.hideProgressBar();
        switch(recipesEvent.getEventCode()) {
            case RecipesEvent.GET_RECIPES_COMPLETE: {
                view.prepareUI(recipesEvent.getCurDiet());
                for (Recipe recipe : recipesEvent.getRecipes())
                    view.addNewRecipe(recipe);
                view.hideProgressBar();
                view.onSuccess("Showing suggestions for " + recipesEvent.getCurDiet().getTitle());
                break;
            }
            case RecipesEvent.GET_RECIPES_FAIL: {
                view.onError("Cannot get suggestions");
                break;
            }
            case RecipesEvent.GET_NEW_RECIPES_COMPLETE: {
                view.clearRecipes();
                for (Recipe recipe : recipesEvent.getRecipes())
                    view.addNewRecipe(recipe);
                view.hideProgressBar();
                break;
            }
        }
    }

    @Override
    public void onSearchQueryEvent(String query) {
        onNewQuery(query);
    }

    @Override
    public void onRecipeRemoveClick(long id) {
        model.removeFromDiet(id);
    }

    @Override
    public void onRecipeAddClick(Recipe recipe) {
        view.onSuccess("Added to diet");
        model.addToDiet(recipe);
    }

    @Override
    public void onNewQuery(String query) {
        view.clearRecipes();
        view.showProgressBar();
        model.getNewRecipes(query);
    }
}