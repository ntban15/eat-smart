package com.annguyen.android.eatsmart.dietdetails.fragments;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.dietdetails.DietDetailActivity;
import com.annguyen.android.eatsmart.dietdetails.adapters.ExcludedIngrListAdapter;
import com.annguyen.android.eatsmart.dietdetails.decorators.ExclLayoutDecorator;
import com.annguyen.android.eatsmart.dietdetails.listeners.OnExclClickListener;
import com.annguyen.android.eatsmart.entities.Diet;
import com.annguyen.android.eatsmart.libs.FirebaseAuthentication;
import com.annguyen.android.eatsmart.libs.FirebaseRealtimeDatabase;
import com.annguyen.android.eatsmart.libs.base.RealtimeDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditDietDialog extends DialogFragment implements OnExclClickListener {

    private static String DIET_PARC = "DIET_PARC";
    private static String DIALOG_TITLE = "DIALOG_TITLE";
    private RealtimeDatabase realtimeDatabase;

    @BindView(R.id.edit_diet_name)
    TextInputEditText editDietName;
    @BindView(R.id.edit_diet_type)
    Spinner editDietType;
    @BindView(R.id.edit_diet_exlc)
    TextInputEditText editDietExlc;
    @BindView(R.id.btn_add_excl_item)
    ImageButton btnAddExclItem;
    @BindView(R.id.edit_diet_excluded_list)
    RecyclerView editDietExcludedList;
    @BindView(R.id.edit_diet_calories_min)
    TextInputEditText editDietCaloriesMin;
    @BindView(R.id.edit_diet_calories_max)
    TextInputEditText editDietCaloriesMax;
    @BindView(R.id.edit_diet_fat_max)
    TextInputEditText editDietFatMax;
    @BindView(R.id.edit_diet_carbs_min)
    TextInputEditText editDietCarbsMin;
    @BindView(R.id.edit_diet_fat_min)
    TextInputEditText editDietFatMin;
    @BindView(R.id.edit_diet_protein_min)
    TextInputEditText editDietProteinMin;
    @BindView(R.id.edit_diet_carbs_max)
    TextInputEditText editDietCarbsMax;
    @BindView(R.id.edit_diet_protein_max)
    TextInputEditText editDietProteinMax;
    Unbinder unbinder;

    private Diet editDiet;
    private ArrayAdapter<CharSequence> typeArrayAdapter;
    private ExcludedIngrListAdapter exclAdapter;

    public EditDietDialog() {
        // Required empty public constructor
    }

    public static EditDietDialog newInstance(Parcelable dietParc, String title) {
        EditDietDialog editDietDialog = new EditDietDialog();
        Bundle args = new Bundle();
        args.putParcelable(DIET_PARC, dietParc);
        args.putString(DIALOG_TITLE, title);
        editDietDialog.setArguments(args);
        return editDietDialog;
    }

    @Override
    public void onResume() {
//        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.MATCH_PARENT;
//        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder
                .setTitle(getArguments().getString(DIALOG_TITLE))
                .setPositiveButton("SAVE", null)
                .setNegativeButton(android.R.string.cancel, null);

        // Inflate the layout for this fragment
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.fragment_edit_diet_dialog, null, false);
        unbinder = ButterKnife.bind(this, view);

        // init Firebase
        initFirebase();

        // init diet type spinner
        initSpinner();

        // init recycler view
        initRecyclerView();

        // Get given diet
        Parcelable dietParc = getArguments().getParcelable(DIET_PARC);

        if (null != dietParc) { // if dialog is an edit diet dialog
            editDiet = Parcels.unwrap(getArguments().getParcelable(DIET_PARC));
            editDietName.setText(editDiet.getTitle());
            editDietType.setSelection(typeArrayAdapter.getPosition(editDiet.getDietType()));
            exclAdapter.setList(editDiet.getExcls());
            editDietCaloriesMax.setText(String.valueOf(editDiet.getMaxCalories()));
            editDietCaloriesMin.setText(String.valueOf(editDiet.getMinCalories()));
            editDietFatMax.setText(String.valueOf(editDiet.getMaxFat()));
            editDietFatMin.setText(String.valueOf(editDiet.getMinFat()));
            editDietCarbsMax.setText(String.valueOf(editDiet.getMaxCarbs()));
            editDietCarbsMin.setText(String.valueOf(editDiet.getMinCarbs()));
            editDietProteinMax.setText(String.valueOf(editDiet.getMaxProtein()));
            editDietProteinMin.setText(String.valueOf(editDiet.getMinProtein()));
        }

        builder.setView(view);

        // override positive and negative button to disable dismiss after click
        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button posBtn = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button negBtn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                posBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (validateInput()) {
                            postDiet();
                            dismiss();
                        }
                    }
                });

                negBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                    }
                });
            }
        });

        //request focus on name field
        editDietName.requestFocus();

        return dialog;
    }

    private void postDiet() {
        String dietKey = editDiet.getDietKey();
        if (!dietKey.equals(DietDetailActivity.DIET_DUMMY)) {
            Diet newDiet = new Diet();
            newDiet.setTitle(editDietName.getText().toString());
            newDiet.setDietType(editDietType.getSelectedItem().toString());
            newDiet.setExcludedIngredients(concateExcls());
            newDiet.setMaxCalories(Integer.valueOf(editDietCaloriesMax.getText().toString()));
            newDiet.setMinCalories(Integer.valueOf(editDietCaloriesMin.getText().toString()));
            newDiet.setMaxFat(Integer.valueOf(editDietFatMax.getText().toString()));
            newDiet.setMinFat(Integer.valueOf(editDietFatMin.getText().toString()));
            newDiet.setMaxCarbs(Integer.valueOf(editDietCarbsMax.getText().toString()));
            newDiet.setMinCarbs(Integer.valueOf(editDietCarbsMin.getText().toString()));
            newDiet.setMaxProtein(Integer.valueOf(editDietProteinMax.getText().toString()));
            newDiet.setMinProtein(Integer.valueOf(editDietProteinMin.getText().toString()));
            if (dietKey.isEmpty()) {
                //create new diet
                realtimeDatabase.addNewDiet(newDiet);
            } else {
                realtimeDatabase.modifyDiet(dietKey, newDiet.toFullMap());
            }
        }
    }

    private boolean validateInput() {
        //check name
        if (!checkEmptyField(editDietName))
            return false;

        //check calories
        if (!checkEmptyField(editDietCaloriesMin) || !checkEmptyField(editDietCaloriesMax)
                || !checkValidNutrition(editDietCaloriesMin, editDietCaloriesMax))
            return false;

        //check fat
        if (!checkEmptyField(editDietFatMin) || !checkEmptyField(editDietFatMax)
                || !checkValidNutrition(editDietFatMin, editDietFatMax))
            return false;

        //check carbs
        if (!checkEmptyField(editDietCarbsMin) || !checkEmptyField(editDietCarbsMax)
                || !checkValidNutrition(editDietCarbsMin, editDietCarbsMax))
            return false;

        //check protein
        if (!checkEmptyField(editDietProteinMin) || !checkEmptyField(editDietProteinMax)
                || !checkValidNutrition(editDietProteinMin, editDietProteinMax))
            return false;

        return true;
    }

    private boolean checkValidNutrition(TextInputEditText editMin, TextInputEditText editMax) {
        int max = Integer.valueOf(editMax.getText().toString());
        int min = Integer.valueOf(editMin.getText().toString());
        if (min > max) {
            editMin.setError("Minimum value exceeds maximum value");
            editMin.requestFocus();
            return false;
        }
        return true;
    }

    private boolean checkEmptyField(TextInputEditText editText) {
        if (editText.getText().toString().isEmpty()) {
            editText.setError("Field must not be empty");
            editText.requestFocus();
            return false;
        }
        return true;
    }

    private void initFirebase() {
        realtimeDatabase = new FirebaseRealtimeDatabase(FirebaseDatabase.getInstance(),
                new FirebaseAuthentication(FirebaseAuth.getInstance()));
    }

    private void initRecyclerView() {
        exclAdapter = new ExcludedIngrListAdapter(this, true);
        editDietExcludedList.setAdapter(exclAdapter);
        editDietExcludedList.setLayoutManager(new StaggeredGridLayoutManager(1,
                StaggeredGridLayoutManager.HORIZONTAL));
        editDietExcludedList.addItemDecoration(new ExclLayoutDecorator(30));
    }

    private void initSpinner() {
        typeArrayAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.diet_type_list, android.R.layout.simple_spinner_item);
        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editDietType.setAdapter(typeArrayAdapter);
    }

    private String concateExcls() {
        List<String> excls = exclAdapter.getList();
        StringBuilder builder = new StringBuilder("");
        for (String excl : excls) {
            builder.append(excl).append(",");
        }
        if (0 != builder.length())
            builder.deleteCharAt(builder.length() - 1); //remove extra ,
        return builder.toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onRemoveClick(int position) {
        exclAdapter.removeItem(position);
    }

    @OnClick(R.id.btn_add_excl_item)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_add_excl_item: {
                String added = editDietExlc.getText().toString();
                if (added.isEmpty())
                        return;
                editDietExlc.setText(null);
                exclAdapter.addItem(added);
                editDietExcludedList.scrollToPosition(exclAdapter.getItemCount() - 1);
                break;
            }
        }
    }
}
