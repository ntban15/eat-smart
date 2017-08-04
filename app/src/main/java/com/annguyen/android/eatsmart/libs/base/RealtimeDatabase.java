package com.annguyen.android.eatsmart.libs.base;

import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;

import java.util.List;
import java.util.Map;

/**
 * Created by khoanguyen on 6/7/17.
 */

public interface RealtimeDatabase {

    void addNewDiet(Diet newDiet);
    void modifyDiet(String dietKey, Map<String, Object> properties);
    void removeDiet(String dietKey, List<String> recipeKeys);
    void setActiveDiet(String dietKey); //null to disable currently active diet

    void removeRecipeFromDiet(String dietKey, String recipeId);
    void addRecipeToDiet(String dietKey, Recipe recipe);

    void getDietDetail(String dietKey);
    void getRecipeDetail(String recipeId);

    void getDietRecipeRecord(String dietKey, String recipeId);

    void getDietRecipes(String dietKey); //get list of recipe keys the diet is using
    void getActiveDiet();
    void getDietMeta(); //for getting list of diets (meta info) to populate UI

    void setOnCompleteListener(Object onCompleteListener);
    void setValueListener(Object valueListener);

    void setOnDietMetaListListener(Object childListener);

    void removeOnDietMetaListListener();

    void setActiveDietListener(Object activeDietListener);

    void removeActiveDietListener();

    void setDietDetailListener(Object currentDietListener);

    void removeDietDetailListener(String dietKey);

    void setDietRecipesListener(Object dietRecipesListener);

    void removeDietRecipesListenter(String dietKey);
}
