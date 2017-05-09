package com.example.vava.myapplication.UserInterface;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vava.myapplication.R;

public class StatisticsPageActivity extends Activity {
    public static final String STATIST = "statistics";
    public static final String STATIST_ENDL = "endless";
    public static final String STATIST_STORY = "story";
    public static final String STATIST_SURV = "survival";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_page);

        SharedPreferences stats;
        stats = getSharedPreferences(STATIST, Context.MODE_PRIVATE);

        TextView story = (TextView) findViewById(R.id.textStoryCount);
        story.setText(Integer.toString(stats.getInt(STATIST_STORY, 0)));

        TextView endl = (TextView) findViewById(R.id.textEndlCount);
        endl.setText(Integer.toString(stats.getInt(STATIST_ENDL, 0)));

        TextView surv = (TextView) findViewById(R.id.textMaxSurv);
        surv.setText(Integer.toString(stats.getInt(STATIST_SURV, 0)));
    }
}
