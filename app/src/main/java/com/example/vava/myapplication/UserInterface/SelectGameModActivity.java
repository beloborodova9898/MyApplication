package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vava.myapplication.R;


public class SelectGameModActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_game_mod);

        Button story = (Button) findViewById(R.id.buttonStory);
        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStoryPressed();
            }
        });

        Button endless = (Button) findViewById(R.id.buttonEndless);
        endless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEndlessPressed();
            }
        });

        Button survival = (Button) findViewById(R.id.buttonSurvival);
        survival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSurvivalPressed();
            }
        });
    }

    public void buttonStoryPressed () {
        Intent intent = new Intent(this, SelectStoryLvlActivity.class);
        startActivity(intent);
    }

    public void buttonEndlessPressed () {
        Intent intent = new Intent(this, EndlessLvlActivity.class);
        startActivity(intent);
    }

    public void buttonSurvivalPressed () {
        Intent intent = new Intent(this, SurvivalLvlActivity.class);
        startActivity(intent);
    }
}
