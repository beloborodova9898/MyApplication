package com.example.vava.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class SelectGameModCore extends Activity {
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
        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonEndlessPressed();
            }
        });

        Button survival = (Button) findViewById(R.id.buttonSurvival);
        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonSurvivalPressed();
            }
        });
    }

    public void buttonStoryPressed () {
        Intent intent = new Intent(this, SelectStoryLvlCore.class);
        startActivity(intent);
    }

    public void buttonEndlessPressed () {
        Intent intent = new Intent(this, EndlessLvlCore.class);
        startActivity(intent);
    }

    public void buttonSurvivalPressed () {
        Intent intent = new Intent(this, SurvivalLvlCore.class);
        startActivity(intent);
    }
}
