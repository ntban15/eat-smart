package com.annguyen.android.eatsmart.recipedetails;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.api.RetrofitClient;
import com.annguyen.android.eatsmart.entities.Nutrient;
import com.annguyen.android.eatsmart.entities.Nutrition;
import com.annguyen.android.eatsmart.entities.Recipe;
import com.annguyen.android.eatsmart.entities.Step;
import com.annguyen.android.eatsmart.libs.GlideImageLoader;
import com.annguyen.android.eatsmart.libs.base.ImageLoader;
import com.annguyen.android.eatsmart.recipedetails.adapters.InstructionAdapter;
import com.annguyen.android.eatsmart.recipedetails.decorators.InstDecorator;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailsActivity extends AppCompatActivity {

    public static String RECIPE_ID = "RECIPE_ID";
    private String recipeId;
    private Recipe curRecipe;
    private InstructionAdapter instructionAdapter;
    private RetrofitClient client;
    private ImageLoader imageLoader;

    @BindView(R.id.recipe_detail_toolbar)
    Toolbar recipeDetailToolbar;
    @BindView(R.id.recipe_detail_img)
    ImageView recipeDetailImg;
    @BindView(R.id.recipe_detail_title)
    TextView recipeDetailTitle;
    @BindView(R.id.recipe_detail_source)
    TextView recipeDetailSource;
    @BindView(R.id.recipe_detail_carbs_value)
    TextView recipeDetailCarbsValue;
    @BindView(R.id.recipe_detail_fat_value)
    TextView recipeDetailFatValue;
    @BindView(R.id.recipe_detail_calories_value)
    TextView recipeDetailCaloriesValue;
    @BindView(R.id.recipe_detail_protein_value)
    TextView recipeDetailProteinValue;
    @BindView(R.id.recipe_detail_intruct_list)
    RecyclerView recipeDetailIntructList;
    @BindView(R.id.recipe_detail_progress)
    ProgressBar recipeDetailProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        setSupportActionBar(recipeDetailToolbar);
        recipeDetailToolbar.setTitle("");
        initRetrofit();
        initImageLoader();
        initRecyclerView();
        initUI();

        recipeId = getIntent().getStringExtra(RECIPE_ID);
        getRecipeDetail();
    }

    private void initUI() {
        recipeDetailTitle.setText("Recipe");
        recipeDetailSource.setText("Source");
        recipeDetailCaloriesValue.setText(String.valueOf(0));
        recipeDetailFatValue.setText(String.valueOf(0));
        recipeDetailCarbsValue.setText(String.valueOf(0));
        recipeDetailProteinValue.setText(String.valueOf(0));
    }

    private void initRetrofit() {
        client = new RetrofitClient();
    }

    private void initImageLoader() {
        imageLoader = new GlideImageLoader(Glide.with(this));
    }

    private void initRecyclerView() {
        instructionAdapter = new InstructionAdapter(imageLoader);
        recipeDetailIntructList.setAdapter(instructionAdapter);
        recipeDetailIntructList.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recipeDetailIntructList.addItemDecoration(new InstDecorator(25));
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recipeDetailIntructList);
    }

    private void getRecipeDetail() {
        recipeDetailProgress.setVisibility(View.VISIBLE);
        client.getRecipeInformationService().getInfo(recipeId, true)
                .enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, Response<Recipe> response) {
                onCallSuccess(response);
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                onCallFailure();
            }
        });
    }

    private void onCallFailure() {
        recipeDetailProgress.setVisibility(View.GONE);
        Toast.makeText(this, "Cannot receive recipe", Toast.LENGTH_SHORT).show();
    }

    private void onCallSuccess(Response<Recipe> response) {
        recipeDetailProgress.setVisibility(View.GONE);
        if (null == response.body()) {
            onCallFailure();
            return;
        }
        curRecipe = response.body();
        initInfo();
    }

    private void initInfo() {
        imageLoader.load(recipeDetailImg, curRecipe.getImage());
        recipeDetailTitle.setText(curRecipe.getTitle());
        recipeDetailSource.setText(curRecipe.getSourceName());
        int calories = 0;
        int fat = 0;
        int carbs = 0;
        int protein = 0;
        int counter = 0;
        Nutrition nutrition = curRecipe.getNutrition();
        if (null != nutrition) {
            for (Nutrient nutrient : nutrition.getNutrients()) {
                if (4 == counter)
                    break;
                switch (nutrient.getTitle()) {
                    case "Calories":
                        calories = (int) nutrient.getAmount();
                        ++counter;
                        break;
                    case "Fat":
                        fat = (int) nutrient.getAmount();
                        ++counter;
                        break;
                    case "Carbohydrates":
                        carbs = (int) nutrient.getAmount();
                        ++counter;
                        break;
                    case "Protein":
                        protein = (int) nutrient.getAmount();
                        ++counter;
                        break;
                }
            }
        }
        recipeDetailCaloriesValue.setText(String.valueOf(calories));
        recipeDetailFatValue.setText(String.valueOf(fat));
        recipeDetailCarbsValue.setText(String.valueOf(carbs));
        recipeDetailProteinValue.setText(String.valueOf(protein));
        if (!curRecipe.getAnalyzedInstructions().isEmpty()) {
            for (Step step : curRecipe.getAnalyzedInstructions().get(0).getSteps()) {
                instructionAdapter.addStep(step);
            }
        }
    }

    @OnClick(R.id.recipe_detail_source)
    public void onViewClicked() {
        if (null != curRecipe) {
            Intent sourceIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(curRecipe.getSourceUrl()));
            startActivity(sourceIntent);
        }
    }
}