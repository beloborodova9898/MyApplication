package com.example.vava.myapplication;

// Готово

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuCore extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button start = (Button) findViewById(R.id.buttonStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStartPressed();
            }
        });

        Button about = (Button) findViewById(R.id.buttonAbout);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAboutPressed();
            }
        });

        Button statistics = (Button) findViewById(R.id.buttonStat);
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonStatisticsPressed();
            }
        });

        Button options = (Button) findViewById(R.id.buttonOptions);
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOptionsPressed();
            }
        });

        Button help = (Button) findViewById(R.id.buttonHelp);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonHeipPressed();
            }
        });

    }

    public void buttonStartPressed () {
        Intent intent = new Intent(this, SelectGameModCore.class);
        startActivity(intent);
    }

    public void buttonAboutPressed () {
        Intent intent = new Intent(this, AboutPageCore.class);
        startActivity(intent);
    }

    public void buttonStatisticsPressed() {
        Intent intent = new Intent(this, StatisticsPageCore.class);
        startActivity(intent);
    }

    public void buttonOptionsPressed() {
        Intent intent = new Intent(this, OptionsPageCore.class);
        startActivity(intent);
    }

    public void buttonHeipPressed() {
        Intent intent = new Intent(this, HowToPlayPageCore.class);
        startActivity(intent);
    }

}
