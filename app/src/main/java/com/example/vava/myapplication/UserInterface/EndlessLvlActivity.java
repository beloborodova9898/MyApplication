package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.vava.myapplication.R;


public class EndlessLvlActivity extends Activity {
    boolean highDifficulty;
    public static final String APP_PREFERENCES = "diffValue";
    public static final String APP_PREFERENCES_DIFF = "endlessModeDifficulty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_game_window);

        Button help = (Button) findViewById(R.id.buttonNeedHelp);
        help.setText(R.string.no_help_button);
        help.setEnabled(false);

        SharedPreferences dValue;
        dValue = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if (dValue.contains(APP_PREFERENCES_DIFF)) {
            System.out.println("JOIdjashasuihasf sdf sdf dsf");
            highDifficulty = dValue.getBoolean(APP_PREFERENCES_DIFF, false);

            TextView lbl = (TextView) findViewById(R.id.textViewTarget);
            if (highDifficulty) lbl.setText("True!");
        }
    }
}