package com.annguyen.android.eatsmart.libs.adapters;

import java.util.List;

/**
 * Created by annguyen on 10/08/2017.
 */

public interface OnActionCallbackListener {
    void removeItems(List<Integer> positions, List<Long> recipeIds);

    void destroyActionMode();
}
