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

import com.example.vava.myapplication.Algorithms.GlassGraph;
import com.example.vava.myapplication.Algorithms.GlassGraphSolver;
import com.example.vava.myapplication.Algorithms.GlassSolution;
import com.example.vava.myapplication.Algorithms.Vert;
import com.example.vava.myapplication.Game3Stakana;
import com.example.vava.myapplication.R;

import static com.example.vava.myapplication.Game3Stakana.numberOfGlasses;
import static com.example.vava.myapplication.UserInterface.StatisticsPageActivity.STATIST;
import static com.example.vava.myapplication.UserInterface.StatisticsPageActivity.STATIST_STORY;

public class StoryGameActivity extends Activity {
    private int [] data;
    private Game3Stakana game;
    private ToggleButton[] visualGlasses;
    private Drawable [] levelLists;
    private int activeGlass; // -1 = никакой
    private static final String KEY_GAME = "GAME";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_game_window);

        if (savedInstanceState != null) {
            data = savedInstanceState.getIntArray(KEY_GAME);
        } else {
        int whichLvl = getIntent().getIntExtra("number", 0);
        getData(whichLvl); }
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
        String as = "st_lvl_" + i;
        int id = getResources().getIdentifier(as, "array", getPackageName());
        data = getResources().getIntArray(id);
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
            Intent intent = new Intent(this, SelectStoryLvlActivity.class);
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
        editor.putInt(STATIST_STORY, stats.getInt(STATIST_STORY, 0) + 1);
        editor.apply();
    }

    private void congr() {
        Toast diffChangedMessage = Toast.makeText(getApplicationContext(),
                R.string.user_wins, Toast.LENGTH_SHORT);
        diffChangedMessage.show();
    }

    private void showHelp() {
        GlassGraph temp = new GlassGraph(game.getGlasses(), 0);
        GlassSolution toShow = GlassGraphSolver.breadthFirstSearch(temp, game.getFinish());
        AlertDialog.Builder builder = new AlertDialog.Builder(StoryGameActivity.this);
        builder.setTitle(R.string.help_header);
        builder.setMessage(toShow.toString());
        AlertDialog message = builder.create();
        message.show();
    }

    private void giveUpPressed() {
        Toast toast = Toast.makeText(getApplicationContext(),
                R.string.user_gives_up, Toast.LENGTH_SHORT);
        toast.show();
    }
}