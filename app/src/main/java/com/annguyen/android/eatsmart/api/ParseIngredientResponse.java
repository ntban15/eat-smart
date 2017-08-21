package com.annguyen.android.eatsmart.api;

/**
 * Created by khoanguyen on 8/19/17.
 */

import com.annguyen.android.eatsmart.entities.Nutrition_Parse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ParseIngredientResponse {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("original")
    @Expose
    private String original;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("amount")
    @Expose
    private int amount;
    @SerializedName("unitShort")
    @Expose
    private String unitShort;
    @SerializedName("unitLong")
    @Expose
    private String unitLong;
    @SerializedName("consistency")
    @Expose
    private String consistency;
    @SerializedName("aisle")
    @Expose
    private String aisle;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("meta")
    @Expose
    private List<Object> meta = null;
    @SerializedName("nutrition")
    @Expose
    private Nutrition_Parse nutrition;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnitShort() {
        return unitShort;
    }

    public void setUnitShort(String unitShort) {
        this.unitShort = unitShort;
    }

    public String getUnitLong() {
        return unitLong;
    }

    public void setUnitLong(String unitLong) {
        this.unitLong = unitLong;
    }

    public String getConsistency() {
        return consistency;
    }

    public void setConsistency(String consistency) {
        this.consistency = consistency;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Object> getMeta() {
        return meta;
    }

    public void setMeta(List<Object> meta) {
        this.meta = meta;
    }

    public Nutrition_Parse getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition_Parse nutrition) {
        this.nutrition = nutrition;
    }
}

