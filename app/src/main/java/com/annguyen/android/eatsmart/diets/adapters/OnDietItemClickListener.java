package com.annguyen.android.eatsmart.diets.adapters;

/**
 * Created by annguyen on 6/12/2017.
 */

public interface OnDietItemClickListener {

    void onItemClick(String dietKey, int pos);

    void OnItemLongClick();

    void onSwitchClick(String dietKey, int pos);

}
