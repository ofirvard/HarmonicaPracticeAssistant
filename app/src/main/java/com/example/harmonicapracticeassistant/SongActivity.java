package com.example.harmonicapracticeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SongActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        if (getIntent().getExtras().getBoolean(Keys.IS_NEW_SONG))
        {
        }
    }
}
