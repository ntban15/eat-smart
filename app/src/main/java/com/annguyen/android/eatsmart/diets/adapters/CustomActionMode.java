package com.annguyen.android.eatsmart.diets.adapters;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by annguyen on 6/15/2017.
 */

public class CustomActionMode implements ActionMode.Callback {

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }
}
