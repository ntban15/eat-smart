package com.annguyen.android.eatsmart.dietdetails.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.libs.FirebaseAuthentication;
import com.annguyen.android.eatsmart.libs.FirebaseRealtimeDatabase;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.grabner.circleprogress.CircleProgressView;
import at.grabner.circleprogress.TextMode;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DietDetailFragment extends Fragment {

    private static String DIET_KEY = "DIET_KEY";

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
    @BindView(R.id.btn_edit_detail)
    Button btnEditDetail;
    @BindView(R.id.diet_detail_progress)
    ProgressBar dietDetailProgress;

    Unbinder unbinder;

    private String dietKey;
    private Diet currentDiet;
    private RealtimeDatabase realtimeDatabase;
    private int curCal = 0;
    private int curFat = 0;
    private int curCarb = 0;
    private int curProtein = 0;

    public static DietDetailFragment newInstance(String dietKey) {

        DietDetailFragment detailFragment = new DietDetailFragment();

        Bundle dietDetails = new Bundle();
        dietDetails.putString(DIET_KEY, dietKey);
        detailFragment.setArguments(dietDetails);

        return detailFragment;
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

        // init Firebase
        initFirebase();

        // initialize current dietKey
        dietKey = getArguments().getString(DIET_KEY);

        // get current Diet information
        initCurrentDiet();

        return view;
    }

    private void initFirebase() {
        realtimeDatabase = new FirebaseRealtimeDatabase(FirebaseDatabase.getInstance(), new FirebaseAuthentication(FirebaseAuth.getInstance()));
    }

    private void initCurrentDiet() {
        showProgressBar();
        hideContent();

        ValueEventListener getDietListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentDiet = dataSnapshot.getValue(Diet.class);
                getRecipesInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DietDetailFragment.this.getContext(), databaseError.toString(), Toast.LENGTH_SHORT)
                        .show();
                hideProgressBar();
                showContent();
            }
        };

        realtimeDatabase.setValueListener(getDietListener);
        realtimeDatabase.getDietDetail(dietKey);
    }

    private void getRecipesInfo() {
        ValueEventListener recipesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot recipe : dataSnapshot.getChildren()) {
                    curCal += (long) recipe.child("calories").getValue();
                    curCarb += (long) recipe.child("carbs").getValue();
                    curFat += (long) recipe.child("fat").getValue();
                    curProtein += (long) recipe.child("protein").getValue();
                }
                showContent();
                hideProgressBar();
                initCircles();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DietDetailFragment.this.getContext(), databaseError.toString(), Toast.LENGTH_SHORT)
                        .show();
                hideProgressBar();
                showContent();
            }
        };

        realtimeDatabase.setValueListener(recipesListener);
        realtimeDatabase.getDietRecipes(dietKey);
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
            cpv.setValue(0);
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
        btnEditDetail.setEnabled(true);
    }

    private void hideContent() {
        btnEditDetail.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnClick(R.id.btn_edit_detail)
    public void onViewClicked() {
    }
}
