package com.annguyen.android.eatsmart.recipedetails.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by annguyen on 19/08/2017.
 */

public class InstDecorator extends RecyclerView.ItemDecoration {

    private int spacing;

    public InstDecorator(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(spacing, spacing, spacing, spacing);
    }
}
