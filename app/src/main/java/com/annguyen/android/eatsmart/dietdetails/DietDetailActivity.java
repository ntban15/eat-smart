package com.annguyen.android.eatsmart.dietdetails;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.adapters.DietDetailPagerAdapter;
import com.annguyen.android.eatsmart.dietdetails.fragments.DietDetailFragment;
import com.annguyen.android.eatsmart.dietdetails.fragments.DietRecipesFragment;
import com.annguyen.android.eatsmart.dietdetails.fragments.EditDietDialog;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.libs.FirebaseAuthentication;
import com.annguyen.android.eatsmart.libs.FirebaseRealtimeDatabase;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

    private static String EDIT_DIET = "Edit diet";
    public static String DIET_DUMMY = "DUMMY";

    @BindView(R.id.diet_detail_actionbar)
    Toolbar toolbar;
    @BindView(R.id.diet_detail_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.diet_detail_pager)
    ViewPager pager;

    private RealtimeDatabase realtimeDatabase;

    private Diet currentDiet;
    private String dietKey;
    private List<Recipe> recipes;

    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private DietDetailFragment dietDetailFragment;
    private DietRecipesFragment dietRecipesFragment;

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

        //init first data
        initData();

        //setup fragments
        setupFragments();

        //setup view pager
        setupViewPager();

        //setup diet name
        setupDietName();

        //get diet
        initCurrentDiet();

        //get recipes
        initRecipes();
    }

    private void initData() {
        //create a dummy diet
        currentDiet = new Diet();
        currentDiet.setTitle("Diet");
        currentDiet.setDietKey(DIET_DUMMY);

        //create a dummy list
        recipes = new ArrayList<>();
    }

    private void initCurrentDiet() {
        ValueEventListener getDietListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                onGetDietSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                onGetDietFail();
            }
        };

        realtimeDatabase.setDietDetailListener(getDietListener);
        realtimeDatabase.getDietDetail(dietKey);
    }

    private void onGetDietFail() {
        currentDiet = new Diet();
        currentDiet.setTitle("Diet");
        currentDiet.setDietKey(DIET_DUMMY);

        setupDietName();
        dietDetailFragment.newData(currentDiet, null);
        dietRecipesFragment.newData(currentDiet);
    }

    private void onGetDietSuccess(DataSnapshot dataSnapshot) {
        currentDiet = dataSnapshot.getValue(Diet.class);
        currentDiet.setDietKey(dietKey);

        setupDietName();
        dietDetailFragment.newData(currentDiet, null);
        dietRecipesFragment.newData(currentDiet);
    }

    private void initRecipes() {
        ChildEventListener recipesListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Recipe newRecipe = dataSnapshot.getValue(Recipe.class);
                recipes.add(newRecipe);
                dietDetailFragment.newData(null, newRecipe);
                dietRecipesFragment.addNewRecipe(newRecipe);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        realtimeDatabase.setDietRecipesListener(recipesListener);
        realtimeDatabase.getDietRecipes(dietKey);
    }

    private void setupDietName() {
        toolbar.setTitle(currentDiet.getTitle());
    }

    private void setupFragments() {
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();

        dietDetailFragment = DietDetailFragment.newInstance();
        dietRecipesFragment = DietRecipesFragment.newInstance();
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
        realtimeDatabase = new FirebaseRealtimeDatabase(FirebaseDatabase.getInstance(),
                new FirebaseAuthentication(FirebaseAuth.getInstance()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.diet_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.edit_diet: {
                EditDietDialog editDietDialog = EditDietDialog
                        .newInstance(Parcels.wrap(currentDiet), EDIT_DIET);
                editDietDialog.show(getSupportFragmentManager(), EDIT_DIET);
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onDestroy() {
        realtimeDatabase.removeDietDetailListener(dietKey);
        realtimeDatabase.removeDietRecipesListenter(dietKey);
        super.onDestroy();
    }
}
