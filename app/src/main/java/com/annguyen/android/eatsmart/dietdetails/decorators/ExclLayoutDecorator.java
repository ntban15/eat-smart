package com.annguyen.android.eatsmart.dietdetails.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by annguyen on 31/07/2017.
 */

public class ExclLayoutDecorator extends RecyclerView.ItemDecoration {

    private int spacing;

    public ExclLayoutDecorator(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(spacing, spacing, spacing, spacing);
    }
}
