package com.annguyen.android.eatsmart.dietdetails;

import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.adapters.DietDetailPagerAdapter;
import com.annguyen.android.eatsmart.dietdetails.fragments.DietDetailFragment;
import com.annguyen.android.eatsmart.dietdetails.fragments.DietRecipesFragment;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.FirebaseAuthentication;
import com.annguyen.android.eatsmart.libs.FirebaseRealtimeDatabase;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DietDetailActivity extends AppCompatActivity {

    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private String dietKey;

    @BindView(R.id.diet_detail_actionbar)
    Toolbar toolbar;
    @BindView(R.id.diet_detail_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.diet_detail_pager)
    ViewPager pager;

    private List<Parcelable> recipesParc;
    private Diet currentDiet;
    private RealtimeDatabase realtimeDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_detail);
        ButterKnife.bind(this);

        //set up action bar
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitle("Diet Details");
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //init firebase
        initFirebase();

        //get diet key
        dietKey = getIntent().getStringExtra("dietKey");

        //get diet
        initCurrentDiet();
    }

    private void initCurrentDiet() {
        ValueEventListener getDietListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentDiet = dataSnapshot.getValue(Diet.class);
                currentDiet.setDietKey(dietKey);
                getRecipesInfo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DietDetailActivity.this, databaseError.toString(), Toast.LENGTH_SHORT)
                        .show();
            }
        };

        realtimeDatabase.setValueListener(getDietListener);
        realtimeDatabase.getDietDetail(dietKey);
    }

    private void getRecipesInfo() {
        if (null == recipesParc)
            recipesParc = new ArrayList<>();
        ValueEventListener recipesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot recipe : dataSnapshot.getChildren()) {
                    Recipe temp = recipe.getValue(Recipe.class);
                    if (null != temp)
                        recipesParc.add(Parcels.wrap(temp));
                }
                //set up Fragments
                setupFragments();

                //set up ViewPager
                setupViewPager();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DietDetailActivity.this, databaseError.toString(), Toast.LENGTH_SHORT)
                        .show();
            }
        };

        realtimeDatabase.setValueListener(recipesListener);
        realtimeDatabase.getDietRecipes(dietKey);
    }

    private void setupFragments() {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();

        Parcelable dietParc = Parcels.wrap(currentDiet);
        Parcelable[] recipesParcArr = recipesParc.toArray(new Parcelable[0]);

        DietDetailFragment dietDetailFragment = DietDetailFragment.newInstance(dietParc, recipesParcArr);
        DietRecipesFragment dietRecipesFragment = DietRecipesFragment.newInstance(dietParc, recipesParcArr);
        fragmentList.add(dietDetailFragment);
        fragmentList.add(dietRecipesFragment);
        titleList.add("Overview");
        titleList.add("Diets");
    }

    private void setupViewPager() {
        pagerAdapter = new DietDetailPagerAdapter(getSupportFragmentManager(), fragmentList, titleList);
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public void onBackPressed() {
        if (0 == pager.getCurrentItem())
            super.onBackPressed();
        else
            pager.setCurrentItem(pager.getCurrentItem() - 1);
    }

    private void initFirebase() {
        realtimeDatabase = new FirebaseRealtimeDatabase(FirebaseDatabase.getInstance(), new FirebaseAuthentication(FirebaseAuth.getInstance()));
    }
}
