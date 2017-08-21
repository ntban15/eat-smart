
package com.annguyen.android.eatsmart.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CaloricBreakdown {

    @SerializedName("percentProtein")
    @Expose
    private float percentProtein;
    @SerializedName("percentFat")
    @Expose
    private float percentFat;
    @SerializedName("percentCarbs")
    @Expose
    private float percentCarbs;

    public float getPercentProtein() {
        return percentProtein;
    }

    public void setPercentProtein(float percentProtein) {
        this.percentProtein = percentProtein;
    }

    public float getPercentFat() {
        return percentFat;
    }

    public void setPercentFat(float percentFat) {
        this.percentFat = percentFat;
    }

    public float getPercentCarbs() {
        return percentCarbs;
    }

    public void setPercentCarbs(float percentCarbs) {
        this.percentCarbs = percentCarbs;
    }

}
