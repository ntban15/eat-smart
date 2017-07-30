package com.annguyen.android.eatsmart.diets.decorators;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by annguyen on 30/07/2017.
 */

public class ListItemDecorator extends RecyclerView.ItemDecoration {

    private int spacing;

    public ListItemDecorator(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = spacing;
        }

        outRect.left = spacing;
        outRect.bottom = spacing;
        outRect.right = spacing;
    }
}
