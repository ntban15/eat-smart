package com.annguyen.android.eatsmart.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.camera.ui.CameraFragment;
import com.annguyen.android.eatsmart.diets.ui.DietFragment;
import com.annguyen.android.eatsmart.recipes.ui.RecipeListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bottom_nav)
    BottomNavigationView bottomNav;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    private DietFragment dietFragment;
    private RecipeListFragment recipeListFragment;
    private CameraFragment cameraFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //create 3 fragments for the app
        dietFragment = DietFragment.newInstance();
        recipeListFragment = RecipeListFragment.newInstance();
        cameraFragment = CameraFragment.newInstance();

        //setup toolbar
        toolbar.setTitle("");   //enable set title later
        setSupportActionBar(toolbar);

        //set listener for bottom nav bar
        initBottomNav();

        //initialize first view
        initFirstView();
    }

    private void initBottomNav() {

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment chosenFragment = null;

                //get current selected item
                int currentItem = bottomNav.getSelectedItemId();

                //current item = selected item
                if (currentItem == item.getItemId())
                    return true;

                switch(item.getItemId()) {
                    case R.id.your_diet_option:
                        setupDietUI();
                        chosenFragment = dietFragment;
                        break;
                    case R.id.recipe_list_option:
                        setupRecipeListUI();
                        chosenFragment = recipeListFragment;
                        break;
                    case R.id.food_camera_option:
                        setupCameraUI();
                        chosenFragment = cameraFragment;
                        break;
                }

                addFragment(chosenFragment);

                return true;
            }
        });
    }

    private void addFragment(Fragment chosenFragment) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, chosenFragment)
                    .commit();
    }

    private void setupCameraUI() {
        setToolbarTitle(getString(R.string.camera));
    }

    private void setupRecipeListUI() {
        setToolbarTitle(getString(R.string.recipes));
    }

    private void setupDietUI() {
        setToolbarTitle(getString(R.string.diets));
    }

    private void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    private void initFirstView() {
        //always go into diet first
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, dietFragment)
                .commit();

        setupDietUI();
    }
}
