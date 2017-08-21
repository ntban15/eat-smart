package com.annguyen.android.eatsmart.entities;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by annguyen on 19/08/2017.
 */

@IgnoreExtraProperties
public class Nutrient {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("amount")
    @Expose
    private double amount;
    @SerializedName("unit")
    @Expose
    private String unit;
    @SerializedName("percentOfDailyNeeds")
    @Expose
    private double percentOfDailyNeeds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getPercentOfDailyNeeds() {
        return percentOfDailyNeeds;
    }

    public void setPercentOfDailyNeeds(double percentOfDailyNeeds) {
        this.percentOfDailyNeeds = percentOfDailyNeeds;
    }
}
