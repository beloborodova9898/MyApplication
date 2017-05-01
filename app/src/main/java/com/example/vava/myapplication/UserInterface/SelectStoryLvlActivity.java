package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vava.myapplication.R;

public class SelectStoryLvlActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_story_lvl);

        Button lvl1 = (Button) findViewById(R.id.lvl1);
        lvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(1);
            }
        });

        Button lvl2 = (Button) findViewById(R.id.lvl2);
        lvl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(2);
            }
        });

        Button lvl3 = (Button) findViewById(R.id.lvl3);
        lvl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(3);
            }
        });

        Button lvl4 = (Button) findViewById(R.id.lvl4);
        lvl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(4);
            }
        });

        Button lvl5 = (Button) findViewById(R.id.lvl5);
        lvl5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(5);
            }
        });

        Button lvl6 = (Button) findViewById(R.id.lvl6);
        lvl6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(6);
            }
        });

        Button lvl7 = (Button) findViewById(R.id.lvl7);
        lvl7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(7);
            }
        });

        Button lvl8 = (Button) findViewById(R.id.lvl8);
        lvl8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(8);
            }
        });

        Button lvl9 = (Button) findViewById(R.id.lvl9);
        lvl9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(9);
            }
        });

        Button lvl10 = (Button) findViewById(R.id.lvl10);
        lvl10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(10);
            }
        });

        Button lvl11 = (Button) findViewById(R.id.lvl11);
        lvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(11);
            }
        });

        Button lvl12 = (Button) findViewById(R.id.lvl12);
        lvl12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPressed(12);
            }
        });
    }

    public void buttonPressed(int i) {
        Intent intent = new Intent(this, StoryGameActivity.class);
        intent.putExtra("number", i);
        startActivity(intent);
    }
}
