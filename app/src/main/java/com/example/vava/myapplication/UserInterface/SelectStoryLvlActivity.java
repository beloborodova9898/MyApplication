package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.vava.myapplication.R;

import static com.example.vava.myapplication.Game3Stakana.numberOfGlasses;

public class SelectStoryLvlActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_story_lvl);

        int numberOfLvls = 12;
        for (int i=1; i <= numberOfLvls; i++) {
            String buttonName = "lvl" + i;
            int id = getResources().getIdentifier(buttonName, "id", getPackageName());
            Button tempButton = (Button) findViewById(id);
            final int tempI = i;
            tempButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { buttonPressed(tempI); } });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, SelectGameModActivity.class);
        startActivity(intent);
    }

    private void buttonPressed(int i) {
        Intent intent = new Intent(this, StoryGameActivity.class);
        intent.putExtra("number", i);
        startActivity(intent);
    }
}
