package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vava.myapplication.R;

public class OptionsPageActivity extends Activity {

    public static final String APP_PREFERENCES = "someValues";
    public static final String APP_PREFERENCES_DIFF = "endlessModeDifficulty";
    public static final String APP_PREFERENCES_SURV = "currentSurvLvl";
    private SharedPreferences dValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_page);

        Button low = (Button) findViewById(R.id.buttonLowDiff);
        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyChanged(false);
            }
        });

        Button high = (Button) findViewById(R.id.buttonHighDiff);
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficultyChanged(true);
            }
        });

        Button resetS = (Button) findViewById(R.id.buttonResetSurv);
        resetS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSurvivalMode();
            }
        });

        dValue = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void difficultyChanged (boolean highIsSelected) {
        SharedPreferences.Editor editor = dValue.edit();
        editor.putBoolean(APP_PREFERENCES_DIFF, highIsSelected);
        editor.apply();

        Toast diffChangedMessage = Toast.makeText(getApplicationContext(),
                R.string.diff_changed_message, Toast.LENGTH_SHORT);
        diffChangedMessage.show();
    }

    public void resetSurvivalMode () {
        SharedPreferences sValue;
        sValue = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sValue.edit();
        editor.putInt(APP_PREFERENCES_SURV, 1);
        editor.apply();

        Toast diffChangedMessage = Toast.makeText(getApplicationContext(),
                R.string.endl_mode_reset_finished, Toast.LENGTH_SHORT);
        diffChangedMessage.show();
    }
}
