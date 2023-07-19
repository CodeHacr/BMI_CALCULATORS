package com.rishav.bmicalci;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextWeight, editTextHeightCM, editTextHeightFeet, editTextHeightInches;
    private Button buttonCalculate, buttonReset;
    private TextView textViewResult;
    private RadioButton radioButtonCM, radioButtonFeetInches;
    private LinearLayout linearLayoutHeightCM, linearLayoutHeightFeetInches;

    private InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the views
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeightCM = findViewById(R.id.editTextHeightCM);
        editTextHeightFeet = findViewById(R.id.editTextHeightFeet);
        editTextHeightInches = findViewById(R.id.editTextHeightInches);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        textViewResult = findViewById(R.id.textViewResult);
        buttonReset = findViewById(R.id.buttonReset);
        radioButtonCM = findViewById(R.id.radioButtonCM);
        radioButtonFeetInches = findViewById(R.id.radioButtonFeetInches);
        linearLayoutHeightCM = findViewById(R.id.linearLayoutHeightCM);
        linearLayoutHeightFeetInches = findViewById(R.id.linearLayoutHeightFeetInches);

        editTextWeight.setFilters(new InputFilter[]{ new InputFilterMinMax("1","300")});
        editTextHeightCM.setFilters(new InputFilter[]{ new InputFilterMinMax("1","243")});
        editTextHeightFeet.setFilters(new InputFilter[]{ new InputFilterMinMax("1","9")});
        editTextHeightInches.setFilters(new InputFilter[]{ new InputFilterMinMax("0","11")});
        // Set click listeners
        buttonCalculate.setOnClickListener(this);
        buttonReset.setOnClickListener(this);
        radioButtonCM.setOnClickListener(this);
        radioButtonFeetInches.setOnClickListener(this);

        // Hide the height in feet and inches layout by default
        linearLayoutHeightFeetInches.setVisibility(View.GONE);
        radioButtonCM.setChecked(true);
        toggleHeightLayout(true);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-6926702628956925/6297578053", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });



    }

    @Override
    public void onClick(View v) {
        if (v == buttonCalculate) {
            calculateBMI();
        } else if (v == buttonReset) {
            resetFields();
        } else if (v == radioButtonCM) {
            toggleHeightLayout(true);
        } else if (v == radioButtonFeetInches) {
            toggleHeightLayout(false);
        }
    }

    private void calculateBMI() {
        String weightStr = editTextWeight.getText().toString().trim();
        String heightCMStr = editTextHeightCM.getText().toString().trim();
        String heightFeetStr = editTextHeightFeet.getText().toString().trim();
        String heightInchesStr = editTextHeightInches.getText().toString().trim();
        if (weightStr.isEmpty()) {
          //  Toast.makeText(this, "Please enter weight and height", Toast.LENGTH_SHORT).show();
            editTextWeight.setError("Please enter weight");
            return;
        }
        if((radioButtonCM.isChecked() && heightCMStr.isEmpty())){
            editTextHeightCM.setError("Please enter height");
            return;
        }
        if( radioButtonFeetInches.isChecked() && heightFeetStr.isEmpty()){
            editTextHeightFeet.setError("Please enter feet");
            return;
        }
        if( (radioButtonFeetInches.isChecked() && heightInchesStr.isEmpty())){
            editTextHeightInches.setError("Please enter inches");
            return;
        }

        float weight = Float.parseFloat(weightStr);
        float heightCM = 0f;
        int feet = 0;
        int inches = 0;

        if (radioButtonCM.isChecked()) {
            heightCM = Float.parseFloat(heightCMStr);
        } else if (radioButtonFeetInches.isChecked()) {
            feet = Integer.parseInt(heightFeetStr);
            inches = Integer.parseInt(heightInchesStr);
        }

        // Convert height to cm if necessary
        if (radioButtonFeetInches.isChecked()) {
            int heightInCm = (int) ((feet * 12 + inches) * 2.54);
            heightCM = heightInCm;
        }

        // Calculate BMI
        float heightInM = heightCM / 100f;
        float bmi = weight / (heightInM * heightInM);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-6926702628956925/6297578053", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });


        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                @Override
                public void onAdClicked() {
                    // Called when a click is recorded for an ad.
                    Log.d("ADMOB", "Ad was clicked.");
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    // Called when ad is dismissed.
                    // Set the ad reference to null so you don't show the ad a second time.
                    Log.d("ADMOB", "Ad dismissed fullscreen content.");
                    mInterstitialAd = null;
                    displayBMIResult(bmi);

                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextWeight.getWindowToken(), 0);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(AdError adError) {
                    // Called when ad fails to show.
                    Log.e("ADMOB", "Ad failed to show fullscreen content.");
                    displayBMIResult(bmi);

                    // Hide the keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editTextWeight.getWindowToken(), 0);

                }

                @Override
                public void onAdImpression() {
                    // Called when an impression is recorded for an ad.
                    Log.d("ADMOB", "Ad recorded an impression.");
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    // Called when ad is shown.
                    Log.d("ADMOB", "Ad showed fullscreen content.");
                }
            });

            mInterstitialAd.show(MainActivity.this);

        }
        else{
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
            displayBMIResult(bmi);

            // Hide the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editTextWeight.getWindowToken(), 0);
        }


    }

    private void displayBMIResult(float bmi) {
        String bmiCategory;

       if (bmi < 18.5) {
            bmiCategory = "Underweight";
        } else if (bmi < 25) {
            bmiCategory = "Normal Weight";
        } else if (bmi < 30) {
            bmiCategory = "Overweight";
        } else {
            bmiCategory = "Obese";
        }

        String result = "BMI : " + String.format("%.2f", bmi) + "\nCategory: " + bmiCategory;
        textViewResult.setText(result);
        Toast.makeText(this, "BMI calculated!", Toast.LENGTH_SHORT).show();
    }

    private void resetFields() {
        editTextWeight.setText("");
        editTextHeightCM.setText("");
        editTextHeightFeet.setText("");
        editTextHeightInches.setText("");
        textViewResult.setText("");
        Toast.makeText(this, "Fields reset!", Toast.LENGTH_SHORT).show();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-6926702628956925/6297578053", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });

    }

    private void toggleHeightLayout(boolean useCM) {
        if (useCM) {
            linearLayoutHeightCM.setVisibility(View.VISIBLE);
            linearLayoutHeightFeetInches.setVisibility(View.GONE);
        } else {
            linearLayoutHeightCM.setVisibility(View.GONE);
            linearLayoutHeightFeetInches.setVisibility(View.VISIBLE);
        }
    }
}
