package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.app.AlertDialog;
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

import com.example.vava.myapplication.Algorithms.Glass;
import com.example.vava.myapplication.Algorithms.GlassGraph;
import com.example.vava.myapplication.Algorithms.GlassGraphSolver;
import com.example.vava.myapplication.Algorithms.GlassSolution;
import com.example.vava.myapplication.Algorithms.Vert;
import com.example.vava.myapplication.Game3Stakana;
import com.example.vava.myapplication.R;

import java.util.NoSuchElementException;
import java.util.Random;

import static com.example.vava.myapplication.Game3Stakana.numberOfGlasses;
import static com.example.vava.myapplication.UserInterface.OptionsPageActivity.APP_PREFERENCES;
import static com.example.vava.myapplication.UserInterface.OptionsPageActivity.APP_PREFERENCES_DIFF;
import static com.example.vava.myapplication.UserInterface.StatisticsPageActivity.STATIST;
import static com.example.vava.myapplication.UserInterface.StatisticsPageActivity.STATIST_ENDL;


public class EndlessLvlActivity extends Activity {
    private int [] data;
    private Game3Stakana game;
    private int activeGlass; // -1 = никакой
    private ToggleButton[] visualGlasses;
    private Drawable[] levelLists;
    private static final String KEY_GAME = "GAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_game_window);

        if (savedInstanceState != null) {
            data = savedInstanceState.getIntArray(KEY_GAME);
        } else {
            SharedPreferences dValue;
            dValue = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            boolean highDifficulty = dValue.getBoolean(APP_PREFERENCES_DIFF, false);
            createData(highDifficulty);
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

        Button helper = (Button) findViewById(R.id.buttonNeedHelp);
        helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { showHelp(); } });

        Button giveUp = (Button) findViewById(R.id.buttonGiveUp);
        giveUp.setText(getString(R.string.no_solution));
        giveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { giveUpPressed(); } });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntArray(KEY_GAME, game.toIntArray());
    }

    private void createData(boolean isDifficult) {
        data = new int [numberOfGlasses*3];
        data = new int [numberOfGlasses*3];
        int min = 2;
        int max = 14;

        if(isDifficult) {
            min = 3;
            max = 30;
        }

        Random r = new Random();

        for (int i = 0; i < numberOfGlasses; i++)
        {
            int tempMax = min + r.nextInt(max);
            if (i==0) tempMax /= 2;
            int tempCurr = r.nextInt(tempMax);
            tempCurr *= 0.7;
            data [i] = tempMax;
            data [numberOfGlasses+i] = tempCurr;
        }

        int [] maxy = new int[numberOfGlasses];
        System.arraycopy(data, 0, maxy, 0, numberOfGlasses);

        int [] curry = new int[numberOfGlasses];
        System.arraycopy(data, numberOfGlasses, curry, 0, numberOfGlasses);

        GlassGraph temp = new GlassGraph(maxy, new Vert(curry), 0);
        Vert finish = temp.getRandomVert();

        for (int i = 0; i < numberOfGlasses; i++)
        data[i+numberOfGlasses*2] = finish.getValue(i);
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
            addStatistics();
            Intent intent = new Intent(this, SelectGameModActivity.class);
            startActivity(intent);
        }
        visualGlasses[which].setChecked(false);
        refreshAll();
        activeGlass = -1;
    }

    private void addStatistics() {
        SharedPreferences stats;
        stats = getSharedPreferences(STATIST, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = stats.edit();
        editor.putInt(STATIST_ENDL, stats.getInt(STATIST_ENDL, 0) + 1);
        editor.apply();
    }

    private void congr() {
        Toast diffChangedMessage = Toast.makeText(getApplicationContext(),
                R.string.user_wins, Toast.LENGTH_SHORT);
        diffChangedMessage.show();
    }

    private void showHelp() {
        try {
            GlassGraph temp = new GlassGraph(game.getGlasses(), 0);
            GlassSolution toShow = GlassGraphSolver.breadthFirstSearch(temp, game.getFinish());
            AlertDialog.Builder builder = new AlertDialog.Builder(EndlessLvlActivity.this);
            builder.setTitle(R.string.help_header);
            builder.setMessage(toShow.toString());
            AlertDialog message = builder.create();
            message.show();
        } catch (NoSuchElementException n) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.really_no_sol, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void giveUpPressed() {
        try {
            GlassGraph temp = new GlassGraph(game.getGlasses(), 0);
            GlassGraphSolver.breadthFirstSearch(temp, game.getFinish());
        } catch (NoSuchElementException n) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    R.string.really_no_sol, Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(this, SelectGameModActivity.class);
            startActivity(intent);
        }
        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.user_gives_up, Toast.LENGTH_SHORT);
        toast.show();
    }
}