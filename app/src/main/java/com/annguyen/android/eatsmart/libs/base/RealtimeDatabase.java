package com.annguyen.android.eatsmart.libs.base;

import com.annguyen.android.eatsmart.entities.Diet;

import java.util.List;

/**
 * Created by khoanguyen on 6/7/17.
 */

public interface RealtimeDatabase {
    void addNewDiet(Diet newDiet);
    void modifyDiet(String dietKey, Diet modifiedDiet);
    void removeDiet(String dietKey);
    void setDietListener(Object listener);
}
