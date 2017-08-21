package com.annguyen.android.eatsmart.camera;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.annguyen.android.eatsmart.BuildConfig;
import com.annguyen.android.eatsmart.R;
import com.annguyen.android.eatsmart.api.ParseIngredientResponse;
import com.annguyen.android.eatsmart.api.RetrofitClientParseIngredient;

import java.text.DecimalFormat;
import java.util.List;

import clarifai2.api.ClarifaiBuilder;
import clarifai2.api.ClarifaiClient;
import clarifai2.dto.input.ClarifaiInput;
import clarifai2.dto.input.image.ClarifaiImage;
import clarifai2.dto.model.output.ClarifaiOutput;
import clarifai2.dto.prediction.Concept;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by khoanguyen on 7/31/17.
 */

public class ResultDialog extends DialogFragment {

    /**
     * Variable definitions for Clarifai API
     */
    public static final String CLARIFAI_ID = BuildConfig.CLARIFAI_ID;
    public static final String CLARIFAI_SECRET = BuildConfig.CLARIFAI_SECRET;
    private ClarifaiClient clarifaiClient;

    /**
     * Varaible definition for Spoonacular API
     */
    RetrofitClientParseIngredient retrofitClientParseIngredient;

    /**
     * Varaible definitions for fragment
     */
    private final static String ARGUMENT_BYTES_ARRAY = "bytes_array";
    private static byte[] bytes;
    private LinearLayout nutritionFacts;
    private TextView calo, fat, sugar, carb, error;
    private ProgressBar progressBar;
    private String responseString = "";

    public static ResultDialog newInstance(byte[] bytes) {
        Bundle args = new Bundle();
        args.putByteArray(ARGUMENT_BYTES_ARRAY, bytes);

        final ResultDialog resultDialog = new ResultDialog();
        resultDialog.setArguments(args);

        return resultDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialogfragment_camera_result, container, false);

        nutritionFacts = (LinearLayout) view.findViewById(R.id.nutritionFacts);
        calo = (TextView) view.findViewById(R.id.calories);
        fat = (TextView) view.findViewById(R.id.fat);
        sugar = (TextView) view.findViewById(R.id.sugar);
        carb = (TextView) view.findViewById(R.id.carb);
        error = (TextView) view.findViewById(R.id.errorNoti);

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final Bundle args = getArguments();
        bytes = args.getByteArray(ARGUMENT_BYTES_ARRAY);

        if (clarifaiClient == null) {
            clarifaiClient = new ClarifaiBuilder(CLARIFAI_ID, CLARIFAI_SECRET).buildSync();
        }

        new ClarifaiResultAsync().execute(bytes);

        return view;
    }

    private class ClarifaiResultAsync extends AsyncTask<byte[], Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            nutritionFacts.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            error.setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(byte[]... params) {
            try {
                byte[] bytes = params[0];
                responseString = "";
                final List<ClarifaiOutput<Concept>> predictionResults =
                        clarifaiClient.getDefaultModels().foodModel() // You can also do Clarifai.getModelByID("id") to get custom models
                                .predict()
                                .withInputs(
                                        ClarifaiInput.forImage(ClarifaiImage.of(bytes))
                                )
                                .executeSync() // optionally, pass a ClarifaiClient parameter to override the default client instance with another one
                                .get();

                ClarifaiOutput<Concept> output = predictionResults.get(0);


                //we just take the top 3 recognized ingredients to calculate their nutrition facts
                int count = 0;
                for (Concept concepts : output.data()) {
                    if (count != 3) {
                        responseString = responseString + concepts.name() + "\n";
                        count += 1;
                    }
                }
                return responseString;
            }
            catch (Exception e) {
                return e.getLocalizedMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);

            //after Clarifai has detected the ingredients, their nutrition facts will be evaluated
            //retrofit for parse_ingredient request is used
            retrofitClientParseIngredient = new RetrofitClientParseIngredient();
            retrofitClientParseIngredient.getParseIngredientService().parseIngredient(true, s, 1)
                    .enqueue(new Callback<List<ParseIngredientResponse>>() {
                        @Override
                        public void onResponse(Call<List<ParseIngredientResponse>> call, Response<List<ParseIngredientResponse>> response) {
                            if (response.isSuccessful()) {

                                nutritionFacts.setVisibility(View.VISIBLE);

                                double _calories = 0;
                                double _fat = 0;
                                double _sugar = 0;
                                double _carb = 0;

                                List<ParseIngredientResponse> parseIngredientResponse = response.body();
                                for (ParseIngredientResponse parseResponse: parseIngredientResponse) {
                                    if (parseResponse.getNutrition() != null) {
                                        if (parseResponse.getNutrition().getNutrients() != null) {
                                            _calories = _calories + parseResponse.getNutrition().getNutrients().get(0).getAmount();
                                            _fat = _fat + parseResponse.getNutrition().getNutrients().get(1).getAmount();
                                            _sugar = _sugar + parseResponse.getNutrition().getNutrients().get(4).getAmount();
                                            _carb = _carb + parseResponse.getNutrition().getNutrients().get(3).getAmount();
                                        }
                                    }
                                }

//                                DecimalFormat df = new DecimalFormat("#.00");
//                                df.format(_calories);
//                                df.format(_fat);
//                                df.format(_sugar);
//                                df.format(_carb);

                                calo.setText(String.format(java.util.Locale.US,"%.2f",_calories));
                                fat.setText(String.format(java.util.Locale.US,"%.2f",_fat));
                                sugar.setText(String.format(java.util.Locale.US,"%.2f",_sugar));
                                carb.setText(String.format(java.util.Locale.US,"%.2f",_carb));
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ParseIngredientResponse>> call, Throwable t) {
                            error.setVisibility(View.VISIBLE);
                            error.setText(t.getLocalizedMessage());
                        }
                    });
        }
    }
}
