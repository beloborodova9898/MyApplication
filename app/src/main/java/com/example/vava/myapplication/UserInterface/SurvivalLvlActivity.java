package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.vava.myapplication.Game3Stakana;
import com.example.vava.myapplication.R;

import static com.example.vava.myapplication.Game3Stakana.numberOfGlasses;
import static com.example.vava.myapplication.UserInterface.StatisticsPageActivity.STATIST;
import static com.example.vava.myapplication.UserInterface.StatisticsPageActivity.STATIST_SURV;

public class SurvivalLvlActivity extends Activity {
    private int [] data;
    private Game3Stakana game;
    private ToggleButton[] visualGlasses;
    private Drawable [] levelLists;
    private int activeGlass; // -1 = никакой
    private static final String KEY_GAME = "GAME";
    private static final String APP_PREFERENCES = "someValues";
    private static final String APP_PREFERENCES_SURV = "currentSurvLvl";
    private static final int maxSurvLvl = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_game_window);

        Button needHelp = (Button)findViewById(R.id.buttonNeedHelp);
        String newHelpLbl  =  getString (R.string.no_help_button);
        needHelp.setText(newHelpLbl);
        needHelp.setEnabled(false);

        if (savedInstanceState != null) {
            data = savedInstanceState.getIntArray(KEY_GAME);
        } else {
            SharedPreferences sValue;
            sValue = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            int toOpen = sValue.getInt(APP_PREFERENCES_SURV, 1);
            getData(toOpen);
        }

        game = new Game3Stakana(data);
        activeGlass = -1;

        TextView trgt = (TextView) findViewById(R.id.textViewTarget);
        String targetMessage = "";
        for (int i=0; i < numberOfGlasses; i++)
            targetMessage += " " + data[numberOfGlasses*2 + i];
        trgt.setText(trgt.getText() + targetMessage);

        visualGlasses = new ToggleButton[numberOfGlasses];
        levelLists = new Drawable[numberOfGlasses];
        for (int i=0; i < numberOfGlasses; i++) {
            int glassNumber = i + 1;
            String buttonName = "glass" + glassNumber;
            int id = getResources().getIdentifier(buttonName, "id", getPackageName());
            visualGlasses[i] = (ToggleButton) findViewById(id);
            final int tempI = i;
            visualGlasses[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { glassPressed(tempI); } });
            levelLists[i] = visualGlasses[i].getBackground();
        }
        refreshAll();

        Button giveUp = (Button) findViewById(R.id.buttonGiveUp);
        giveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { giveUpPressed(); } });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_GAME, game.toIntArray());
    }

    private void getData(int i) {
        switch (i) {
            case 1: data = getResources().getIntArray(R.array.su_lvl_1);
                break;
            case 2: data = getResources().getIntArray(R.array.su_lvl_2);
                break;
            case 3: data = getResources().getIntArray(R.array.su_lvl_3);
                break;
            case 4: data = getResources().getIntArray(R.array.su_lvl_4);
                break;
            case 5: data = getResources().getIntArray(R.array.su_lvl_5);
        }
    }

    private void refreshText() {
        for (int i = 0; i < numberOfGlasses; i++)
            refreshText(i);
    }

    private void refreshText(int i) {
            visualGlasses[i].setText(game.sostStakana(i));
    }

    private void refreshImages() {
        for (int i = 0; i < numberOfGlasses; i++) {
            levelLists[i].setLevel(game.getState(i));
        }
    }

    private void refreshAll() {
        refreshText();
        refreshImages();
    }

    private void glassPressed(int which) {
        if (activeGlass == -1) {
            activeGlass = which;
            refreshText(which);
            return;
        }
        if(activeGlass == which) {
            refreshText(which);
            activeGlass = -1;
            return;
        }
        visualGlasses[activeGlass].setChecked(false);
        if (game.transfuse(activeGlass, which)) {
            congr();
            recountNextLvl();
            recountStatistics();
            Intent intent = new Intent(this, SelectGameModActivity.class);
            startActivity(intent);
        }
        visualGlasses[which].setChecked(false);
        refreshAll();
        activeGlass = -1;
    }

    private void recountStatistics() {
        SharedPreferences stats;
        stats = getSharedPreferences(STATIST, Context.MODE_PRIVATE);
        int temp = stats.getInt(STATIST_SURV, 0);
        if (temp != maxSurvLvl) {
            int [] maxy = new int[5];
            maxy[0] = getResources().getIntArray(R.array.su_lvl_1)[0];
            maxy[1] = getResources().getIntArray(R.array.su_lvl_2)[0];
            maxy[2] = getResources().getIntArray(R.array.su_lvl_3)[0];
            maxy[3] = getResources().getIntArray(R.array.su_lvl_4)[0];
            maxy[4] = getResources().getIntArray(R.array.su_lvl_5)[0];
            int toCompare = game.getGlasses()[0].getMaxValue();

            for (int i=0; i < maxSurvLvl; i++)
                if ((toCompare == maxy[i]) && (temp < (i + 1))) temp = i + 1;

            SharedPreferences.Editor editor = stats.edit();
            editor.putInt(STATIST_SURV, temp);
            editor.apply();
        }
    }

    private void recountNextLvl() {
        SharedPreferences sValue;
        sValue = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        int currValue = sValue.getInt(APP_PREFERENCES_SURV, 1);
        if (currValue != maxSurvLvl) currValue++;
        SharedPreferences.Editor editor = sValue.edit();
        editor.putInt(APP_PREFERENCES_SURV, currValue);
        editor.apply();
    }

    private void congr() {
        Toast diffChangedMessage = Toast.makeText(getApplicationContext(),
                R.string.user_wins, Toast.LENGTH_SHORT);
        diffChangedMessage.show();
    }

    private void giveUpPressed() {
        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.user_gives_up, Toast.LENGTH_SHORT);
        toast.show();
    }
}
