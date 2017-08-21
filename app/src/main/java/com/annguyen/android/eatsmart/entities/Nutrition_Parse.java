package com.annguyen.android.eatsmart.entities;

/**
 * Created by khoanguyen on 8/21/17.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nutrition_Parse {

    @SerializedName("nutrients")
    @Expose
    private List<Nutrient> nutrients = null;
    @SerializedName("caloricBreakdown")
    @Expose
    private CaloricBreakdown caloricBreakdown;

    public List<Nutrient> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<Nutrient> nutrients) {
        this.nutrients = nutrients;
    }

    public CaloricBreakdown getCaloricBreakdown() {
        return caloricBreakdown;
    }

    public void setCaloricBreakdown(CaloricBreakdown caloricBreakdown) {
        this.caloricBreakdown = caloricBreakdown;
    }

}

