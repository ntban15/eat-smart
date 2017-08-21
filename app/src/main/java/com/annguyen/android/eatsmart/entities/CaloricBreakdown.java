package com.annguyen.android.eatsmart.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by annguyen on 19/08/2017.
 */

public class CaloricBreakdown {

    @SerializedName("percentProtein")
    @Expose
    private double percentProtein;
    @SerializedName("percentFat")
    @Expose
    private double percentFat;
    @SerializedName("percentCarbs")
    @Expose
    private double percentCarbs;

    public double getPercentProtein() {
        return percentProtein;
    }

    public void setPercentProtein(double percentProtein) {
        this.percentProtein = percentProtein;
    }

    public double getPercentFat() {
        return percentFat;
    }

    public void setPercentFat(double percentFat) {
        this.percentFat = percentFat;
    }

    public double getPercentCarbs() {
        return percentCarbs;
    }

    public void setPercentCarbs(double percentCarbs) {
        this.percentCarbs = percentCarbs;
    }

}