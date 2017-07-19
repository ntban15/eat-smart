package com.annguyen.android.eatsmart.api;

/**
 * Created by annguyen on 6/3/2017.
 */

import java.util.List;

import com.annguyen.android.eatsmart.entities.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchComplexResponse {

    @SerializedName("results")
    @Expose
    private List<Recipe> recipes = null;
    @SerializedName("baseUri")
    @Expose
    private String baseUri;
    @SerializedName("offset")
    @Expose
    private int offset;
    @SerializedName("number")
    @Expose
    private int number;
    @SerializedName("totalResults")
    @Expose
    private int totalResults;
    @SerializedName("processingTimeMs")
    @Expose
    private int processingTimeMs;

    public List<Recipe> getResults() {
        return recipes;
    }

    public void setResults(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        this.baseUri = baseUri;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getProcessingTimeMs() {
        return processingTimeMs;
    }

    public void setProcessingTimeMs(int processingTimeMs) {
        this.processingTimeMs = processingTimeMs;
    }

}
