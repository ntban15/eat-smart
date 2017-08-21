package com.annguyen.android.eatsmart.diets.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.annguyen.android.eatsmart.EatSmartApp;
import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.DietDetailActivity;
import com.annguyen.android.eatsmart.dietdetails.fragments.EditDietDialog;
import com.annguyen.android.eatsmart.diets.DietPresenter;
import com.annguyen.android.eatsmart.diets.adapters.DietListAdapter;
import com.annguyen.android.eatsmart.diets.adapters.OnDietItemClickListener;
import com.annguyen.android.eatsmart.diets.decorators.ListItemDecorator;
import com.annguyen.android.eatsmart.diets.di.DietFragmentComponent;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.login.ui.LoginActivity;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class DietFragment extends Fragment implements DietView, OnDietItemClickListener {

    private static String CREATE_DIET = "Create diet";

    @BindView(R.id.container_diet)
    CoordinatorLayout containerDiet;
    @BindView(R.id.diets_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.create_diet)
    FloatingActionButton createDietBtn;
    @BindView(R.id.diet_list)
    RecyclerView dietList;

    @Inject
    DietPresenter presenter;
    @Inject
    DietListAdapter adapter;

    //to unbind view from ButterKnife
    Unbinder unbinder;

    public static DietFragment newInstance() {
        return new DietFragment();
    }

    public DietFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diet, container, false);
        unbinder = ButterKnife.bind(this, view);
        //setup dependency injection
        setupInjection();
        //allow fragment to add options
        setHasOptionsMenu(true);
        //setup presenter
        presenter.start();
        //set up recycler view
        setupRecyclerView();
        //init UI with list of diets
        presenter.initUI();

        return view;
    }

    private void setupRecyclerView() {
        dietList.setAdapter(adapter);
        dietList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        dietList.addItemDecoration(new ListItemDecorator(25));
    }

    private void setupInjection() {
        EatSmartApp application = (EatSmartApp) getActivity().getApplication();
        DietFragmentComponent component = application.getDietFragmentComponent(this, this);
        component.inject(this);
    }

    @Override
    public void onDestroyView() {
        presenter.stop();
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.log_out) {
            presenter.onLogoutSelected();
        }

        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.diet_toolbar_options, menu);
    }

    @Override
    public void onError(String errMsg) {
        Snackbar.make(containerDiet, errMsg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void goToLogin() {
        Intent logoutIntent = new Intent(getContext(), LoginActivity.class);
        logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        createDietBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        createDietBtn.setVisibility(View.GONE);
    }

    @Override
    public void setDietContent(List<Diet> dietList) {
        adapter.setDietDataset(dietList);
    }

    @Override
    public void addNewDiet(Diet newDiet) {
        adapter.addNewDiet(newDiet);
    }

    @OnClick(R.id.create_diet)
    void onCreateDiet() {
        Diet newDiet = new Diet();
        EditDietDialog editDietDialog = EditDietDialog.newInstance(Parcels.wrap(newDiet), CREATE_DIET);
        editDietDialog.show(getChildFragmentManager(), CREATE_DIET);
    }

    @Override
    public void onItemClick(String dietKey, boolean active, int pos) {
        Intent goToDetail = new Intent(this.getContext(), DietDetailActivity.class);
        goToDetail.putExtra("dietKey", dietKey);
        goToDetail.putExtra("isActive", active);
        startActivity(goToDetail);
    }

    @Override
    public void onSwitchClick(String dietKey, int pos) {
        //TODO: SET ACTIVE OR INACTIVE
        presenter.setActive(dietKey);
        adapter.setActiveDiet(pos);
    }

    @Override
    public void OnItemLongClick() {
        //TODO: START CONTEXTUAL ACTION BAR
    }
}
