package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vava.myapplication.Algorithms.GlassGraph;
import com.example.vava.myapplication.Algorithms.GlassGraphSolver;
import com.example.vava.myapplication.Algorithms.GlassSolution;
import com.example.vava.myapplication.Algorithms.Vert;
import com.example.vava.myapplication.Game3Stakana;
import com.example.vava.myapplication.R;

public class StoryGameActivity extends Activity {
    private int [] data;
    private Game3Stakana game;
    private Button [] visualGlasses;
    int activeGlass; // 3 = никакой
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
        switch (i) {
            case 1: data = getResources().getIntArray(R.array.st_lvl_1);
                    break;
            case 2: data = getResources().getIntArray(R.array.st_lvl_2);
                break;
            case 3: data = getResources().getIntArray(R.array.st_lvl_3);
                break;
            case 4: data = getResources().getIntArray(R.array.st_lvl_4);
                break;
            case 5: data = getResources().getIntArray(R.array.st_lvl_5);
                break;
            case 6: data = getResources().getIntArray(R.array.st_lvl_6);
                break;
            case 7: data = getResources().getIntArray(R.array.st_lvl_7);
                break;
            case 8: data = getResources().getIntArray(R.array.st_lvl_8);
                break;
            case 9: data = getResources().getIntArray(R.array.st_lvl_9);
                break;
            case 10: data = getResources().getIntArray(R.array.st_lvl_10);
                break;
            case 11: data = getResources().getIntArray(R.array.st_lvl_11);
                break;
            case 12: data = getResources().getIntArray(R.array.st_lvl_12);
                break;
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
            Intent intent = new Intent(this, SelectStoryLvlActivity.class);
            startActivity(intent);
        }
        refreshAll();
        activeGlass = 3;
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
        Toast diffChangedMessage = Toast.makeText(getApplicationContext(),
                R.string.user_gives_up, Toast.LENGTH_SHORT);
        diffChangedMessage.show();
    }
}