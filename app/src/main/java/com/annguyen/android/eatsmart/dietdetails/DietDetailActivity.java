package com.annguyen.android.eatsmart.dietdetails;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.adapters.DietDetailPagerAdapter;
import com.annguyen.android.eatsmart.dietdetails.fragments.DietDetailFragment;
import com.annguyen.android.eatsmart.dietdetails.fragments.DietRecipesFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DietDetailActivity extends FragmentActivity {

    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;
    private String dietKey;

    @BindView(R.id.diet_detail_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.diet_detail_pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_detail);
        ButterKnife.bind(this);

        //get diet key
        dietKey = getIntent().getStringExtra("dietKey");

        //set up Fragments
        setupFragments();

        //set up ViewPager
        setupViewPager();
    }

    private void setupFragments() {
        fragmentList = new ArrayList<>();
        DietDetailFragment dietDetailFragment = DietDetailFragment.newInstance(dietKey);
        DietRecipesFragment dietRecipesFragment = new DietRecipesFragment();
        fragmentList.add(dietDetailFragment);
        fragmentList.add(dietRecipesFragment);
    }

    private void setupViewPager() {
        pagerAdapter = new DietDetailPagerAdapter(getSupportFragmentManager(), fragmentList);
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
}
