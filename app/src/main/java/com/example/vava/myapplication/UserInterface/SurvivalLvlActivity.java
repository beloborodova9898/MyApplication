package com.example.vava.myapplication.UserInterface;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.example.vava.myapplication.R;

public class SurvivalLvlActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.story_game_window);

        Button needHelp = (Button)findViewById(R.id.buttonNeedHelp);
        String newHelpLbl  =  getString (R.string.no_help_button);
        needHelp.setText(newHelpLbl);
    }
}
