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

import com.example.vava.myapplication.Algorithms.Glass;
import com.example.vava.myapplication.Algorithms.GlassGraph;
import com.example.vava.myapplication.Algorithms.GlassGraphSolver;
import com.example.vava.myapplication.Algorithms.GlassSolution;
import com.example.vava.myapplication.Algorithms.Vert;
import com.example.vava.myapplication.Game3Stakana;
import com.example.vava.myapplication.R;

import java.util.NoSuchElementException;
import java.util.Random;


public class EndlessLvlActivity extends Activity {
    boolean highDifficulty;
    public static final String APP_PREFERENCES = "someValues";
    public static final String APP_PREFERENCES_DIFF = "endlessModeDifficulty";
    public static final String STATIST = "statistics";
    public static final String STATIST_ENDL = "endless";
    private int [] data;
    private Game3Stakana game;
    int activeGlass; // 3 = никакой
    private Button [] visualGlasses;
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
            highDifficulty = dValue.getBoolean(APP_PREFERENCES_DIFF, false);
            createData(highDifficulty);
        }

        game = new Game3Stakana(data);

        TextView trgt = (TextView) findViewById(R.id.textViewTarget);
        trgt.setText(trgt.getText() + " " + Integer.toString(data[6]) + " " + Integer.toString(data[7]) + " " + Integer.toString(data[8]));

        activeGlass = 3;

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
        data = new int [9];
        int min = 2;
        int max = 14;

        if(isDifficult) {
            min = 3;
            max = 30;
        }

        Random r = new Random();

        for (int i = 0; i < 3; i++)
        {
            int tempMax = min + r.nextInt(max);
            if (i==0) tempMax /= 2;
            int tempCurr = r.nextInt(tempMax);
            tempCurr *= 0.7;
            data [i] = tempMax;
            data [3+i] = tempCurr;
        }

        int [] maxy = new int[]{data[0], data[1], data[2]};
        int [] curry = new int[]{data[3], data[4], data[5]};
        GlassGraph temp = new GlassGraph(maxy, new Vert(curry), 0);

        Vert finish = temp.getRandomVert();
        for (int i = 0; i < 3; i++)
        data[i+6] = finish.getValue(i);
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
            addStatistics();
            Intent intent = new Intent(this, SelectGameModActivity.class);
            startActivity(intent);
        }
        refreshAll();
        activeGlass = 3;
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