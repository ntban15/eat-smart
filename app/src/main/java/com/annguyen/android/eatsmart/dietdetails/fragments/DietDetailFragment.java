package com.annguyen.android.eatsmart.dietdetails.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.DietDetailActivity;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;

import java.util.ArrayList;
import java.util.List;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DietDetailFragment extends Fragment {

    private boolean isCreated = false;

    @BindView(R.id.calorie_circle)
    CircleProgressView calorieCircle;
    @BindView(R.id.calorie_detail_info)
    TextView calorieDetailInfo;
    @BindView(R.id.fat_circle)
    CircleProgressView fatCircle;
    @BindView(R.id.fat_detail_info)
    TextView fatDetailInfo;
    @BindView(R.id.carbs_circle)
    CircleProgressView carbsCircle;
    @BindView(R.id.carbs_detail_info)
    TextView carbsDetailInfo;
    @BindView(R.id.protein_circle)
    CircleProgressView proteinCircle;
    @BindView(R.id.protein_detail_info)
    TextView proteinDetailInfo;
    @BindView(R.id.diet_detail_progress)
    ProgressBar dietDetailProgress;

    Unbinder unbinder;

    private Diet currentDiet;
    private List<Recipe> recipeList;
    private int curCal;
    private int curFat;
    private int curCarb;
    private int curProtein;

    public static DietDetailFragment newInstance() {
        return new DietDetailFragment();
    }

    public DietDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diet_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        // prepare data
        initData();

        // mark fragment as created
        isCreated = true;

        // calculate recipes
        calculateDietInfo();

        // init circles
        initCircles();

        return view;
    }

    private void initData() {
        // initialize current diet
        if (null == currentDiet) {
            currentDiet = new Diet();
            currentDiet.setDietKey(DietDetailActivity.DIET_DUMMY);
        }

        // init recipe list
        if (null == recipeList)
            recipeList = new ArrayList<>();
    }

    public void newData(Diet newDiet, Recipe recipe) {
        if (null != newDiet)
            currentDiet = newDiet;
        if (null != recipe) {
            if (null == recipeList)
                recipeList = new ArrayList<>();
            recipeList.add(recipe);
        }

        if (isCreated) {
            calculateDietInfo();
            initCircles();
        }
    }

    private void calculateDietInfo() {
        //reset values
        curCal = 0;
        curFat = 0;
        curProtein = 0;
        curCarb = 0;

        for (Recipe recipe : recipeList) {
            curCal += recipe.getCalories();
            curFat += recipe.getFatValue();
            curProtein += recipe.getProteinValue();
            curCarb += recipe.getCarbsValue();
        }
    }

    private void initCircles() {
        int maxCalories = currentDiet.getMaxCalories();
        int minCalories = currentDiet.getMinCalories();
        int maxFat = currentDiet.getMaxFat();
        int minFat = currentDiet.getMinFat();
        int maxCarb = currentDiet.getMaxCarbs();
        int minCarb = currentDiet.getMinCarbs();
        int maxProtein = currentDiet.getMaxProtein();
        int minProtein = currentDiet.getMinProtein();

        checkCircle(calorieCircle, maxCalories, minCalories, curCal);
        calorieDetailInfo.setText(String.format(getString(R.string.calories_detail), maxCalories, minCalories));

        checkCircle(fatCircle, maxFat, minFat, curFat);
        fatDetailInfo.setText(String.format(getString(R.string.fat_detail), maxFat, minFat));

        checkCircle(carbsCircle, maxCarb, minCarb, curCarb);
        carbsDetailInfo.setText(String.format(getString(R.string.carb_detail), maxCarb, minCarb));

        checkCircle(proteinCircle, maxProtein, minProtein, curProtein);
        proteinDetailInfo.setText(String.format(getString(R.string.protein_detail), maxProtein, minProtein));
    }

    private void checkCircle(CircleProgressView cpv, int max, int min, int current) {
        if (0 != max) {
            cpv.setTextMode(TextMode.VALUE);
            cpv.setMaxValue(max);

            if (current < min)
                setCircleBelow(cpv);
            else if (current < max)
                setCircleOk(cpv);
            else
                setCircleAbove(cpv);
            //cpv.setValue(0);
            cpv.setValueAnimated(current);
        }
        else
            setCircleNah(cpv);
    }

    private void setCircleBelow(CircleProgressView cpv) {
        cpv.setBarColor(ContextCompat.getColor(getContext(), R.color.circle_bar_below));
        cpv.setRimColor(ContextCompat.getColor(getContext(), R.color.circle_rim_below));
    }

    private void setCircleOk(CircleProgressView cpv) {
        cpv.setBarColor(ContextCompat.getColor(getContext(), R.color.circle_bar_ok));
        cpv.setRimColor(ContextCompat.getColor(getContext(), R.color.circle_rim_ok));
    }

    private void setCircleAbove(CircleProgressView cpv) {
        cpv.setBarColor(ContextCompat.getColor(getContext(), R.color.circle_bar_above));
        cpv.setRimColor(ContextCompat.getColor(getContext(), R.color.circle_rim_above));
    }

    private void setCircleNah(CircleProgressView cpv) {
        cpv.setTextMode(TextMode.TEXT);
        cpv.setText("N/A");
        cpv.setBarColor(ContextCompat.getColor(getContext(), R.color.circle_nah));
        cpv.setRimColor(ContextCompat.getColor(getContext(), R.color.circle_nah));
    }

    private void showProgressBar() {
        dietDetailProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        dietDetailProgress.setVisibility(View.GONE);
    }

    private void showContent() {

    }

    private void hideContent() {

    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
