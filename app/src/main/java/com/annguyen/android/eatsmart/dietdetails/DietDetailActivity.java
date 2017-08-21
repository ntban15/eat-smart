package com.annguyen.android.eatsmart.dietdetails;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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
import com.annguyen.android.eatsmart.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    private boolean isActive;
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

        //get active state
        isActive = getIntent().getBooleanExtra("isActive", false);

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
                Recipe removedRecipe = dataSnapshot.getValue(Recipe.class);
                recipes.remove(removedRecipe);
                dietDetailFragment.removeRecipe(removedRecipe);
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
            case R.id.edit_delete_diet: {
                showDeleteAlert();
                return true;
            }
            case R.id.edit_add_dummy: {
                createDummy();
                return true;
            }
            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }

    private void createDummy() {
        Recipe newRecipe = new Recipe();
        //Long id = (long) Math.floor(Math.random() * 10001);
        long id = 479101;
        newRecipe.setId(id);
        newRecipe.setTitle(String.valueOf(id));
        newRecipe.setImage("http://images.all-free-download.com/images/graphicthumb/chicken_picture_5_167115.jpg");
        newRecipe.setCalories(100);
        newRecipe.setCarbs("123g");
        newRecipe.setFat("50g");
        newRecipe.setProtein("60g");
        newRecipe.setServings(2);
        newRecipe.setReadyInMinutes(15);
        realtimeDatabase.addRecipeToDiet(dietKey, newRecipe);
    }

    private void showDeleteAlert() {
        new AlertDialog.Builder(this)
                .setTitle("Delete this diet?")
                .setMessage("All data related to this diet will be removed")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //get list of recipe ids
                        List<String> recipeIds = new ArrayList<>();
                        for (Recipe recipe : recipes) {
                            recipeIds.add(String.valueOf(recipe.getId()));
                        }
                        //remove diet detail listener before removing
                        realtimeDatabase.removeDietDetailListener(dietKey);
                        realtimeDatabase.removeDietRecipesListenter(dietKey);
                        //if diet is active -> set inactive
                        if (isActive)
                            realtimeDatabase.setActiveDiet(null);
                        realtimeDatabase.removeDiet(dietKey, recipeIds);
                        goBackToMain();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
    }

    private void goBackToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        realtimeDatabase.removeDietDetailListener(dietKey);
        realtimeDatabase.removeDietRecipesListenter(dietKey);
        super.onDestroy();
    }

    public void removeRecipes(List<Long> recipeIds) {
        for (Long recipeId : recipeIds) {
            realtimeDatabase.removeRecipeFromDiet(dietKey, String.valueOf(recipeId));
        }
    }
}
