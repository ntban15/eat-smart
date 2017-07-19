package com.annguyen.android.eatsmart.entities;

/**
 * Created by annguyen on 6/3/2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@IgnoreExtraProperties
public class Time {

    @SerializedName("number")
    @Expose
    private int number;
    @SerializedName("unit")
    @Expose
    private String unit;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}