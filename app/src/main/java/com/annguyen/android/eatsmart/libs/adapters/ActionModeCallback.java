package com.annguyen.android.eatsmart.libs.adapters;

import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.adapters.DietRecipeListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Source: https://enoent.fr/blog/2015/01/18/recyclerview-basics/
 */

public class ActionModeCallback implements ActionMode.Callback {
    @SuppressWarnings("unused")
    private final String TAG = ActionModeCallback.class.getSimpleName();

    private SelectableAdapter adapter;
    private OnActionCallbackListener listener;

    public ActionModeCallback(SelectableAdapter adapter, OnActionCallbackListener listener) {

        this.adapter = adapter;
        this.listener = listener;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.selected_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_remove:
                // TODO: actually remove items
                //get list of recipes id
                List<Long> recipeIds = new ArrayList<>();
                if (adapter instanceof DietRecipeListAdapter) {
                    recipeIds.addAll(((DietRecipeListAdapter) adapter).getSelectedRecipeIds());
                }
                listener.removeItems(adapter.getSelectedItems(), recipeIds);

                mode.finish();
                return true;

            default:
                return false;
        }
    }
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        listener.destroyActionMode();
        adapter.clearSelection();
    }

}
