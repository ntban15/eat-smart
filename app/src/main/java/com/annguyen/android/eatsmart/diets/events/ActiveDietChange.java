package com.annguyen.android.eatsmart.diets.events;

/**
 * Created by annguyen on 6/27/2017.
 */

public class ActiveDietChange {
    private String dietKey;

    public ActiveDietChange(String dietKey) {
        this.dietKey = dietKey;
    }

    public String getDietKey() {
        return dietKey;
    }
}
