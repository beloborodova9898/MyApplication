package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vava.myapplication.Algorithms.GlassGraph;
import com.example.vava.myapplication.Algorithms.GlassGraphSolver;
import com.example.vava.myapplication.Algorithms.GlassSolution;
import com.example.vava.myapplication.Game3Stakana;
import com.example.vava.myapplication.R;

public class SurvivalLvlActivity extends Activity {
    private int [] data;
    private Game3Stakana game;
    private Button [] visualGlasses;
    int activeGlass; // 3 = никакой
    private static final String KEY_GAME = "GAME";
    public static final String APP_PREFERENCES = "someValues";
    public static final String APP_PREFERENCES_SURV = "currentSurvLvl";
    public static final String STATIST = "statistics";
    public static final String STATIST_SURV = "survival";
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
        activeGlass = 3;
        TextView trgt = (TextView) findViewById(R.id.textViewTarget);
        trgt.setText(trgt.getText() + " " + Integer.toString(data[6]) + " " + Integer.toString(data[7]) + " " + Integer.toString(data[8]));

        visualGlasses = new Button[3];
        visualGlasses[0] = (Button) findViewById(R.id.glass1);
        visualGlasses[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { glassPressed(0); } });
        visualGlasses[1] = (Button) findViewById(R.id.glass2);
        visualGlasses[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { glassPressed(1); } });
        visualGlasses[2] = (Button) findViewById(R.id.glass3);
        visualGlasses[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { glassPressed(2); } });
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
        for (int i = 0; i < 3; i++)
            visualGlasses[i].setText(game.sostStakana(i));
    }

    private void refreshImages() {
        for (int i = 0; i < 3; i++) {
            setNotSelImage(i);
        }
    }

    private void setSelImage(int i) {
        String name = "gl"+Integer.toString(i+1)+stateOf(i)+"_sel";
        int resID = getResources().getIdentifier(name, "drawable", getPackageName());
        visualGlasses[i].setBackground(getResources().getDrawable(resID));
    }

    private String stateOf(int i) {
        switch (game.getState(i)) {
            case 0: return "_empty";
            case 1: return "_fill1";
            case 2: return "_fill2";
            case 3: return "_full";
        }
        return null;
    }

    private void setNotSelImage(int i) {
        String name = "gl"+Integer.toString(i+1)+stateOf(i);
        int resID = getResources().getIdentifier(name, "drawable", getPackageName());
        visualGlasses[i].setBackground(getResources().getDrawable(resID));
    }

    private void refreshAll() {
        refreshText();
        refreshImages();
    }

    private void glassPressed(int which) {
        if (activeGlass==3) {
            setSelImage(which);
            activeGlass = which;
            return;
        }
        if(activeGlass==which) {
            setNotSelImage(which);
            activeGlass = 3;
            return;
        }
        if (game.transfuse(activeGlass, which)) {
            congr();
            recountNextLvl();
            recountStatistics();
            Intent intent = new Intent(this, SelectGameModActivity.class);
            startActivity(intent);
        }
        refreshAll();
        activeGlass = 3;
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
