
package com.annguyen.android.eatsmart.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Nutrient {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("amount")
    @Expose
    private float amount;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("percentOfDailyNeeds")
    @Expose
    private float percentOfDailyNeeds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getPercentOfDailyNeeds() {
        return percentOfDailyNeeds;
    }

    public void setPercentOfDailyNeeds(float percentOfDailyNeeds) {
        this.percentOfDailyNeeds = percentOfDailyNeeds;
    }

}
