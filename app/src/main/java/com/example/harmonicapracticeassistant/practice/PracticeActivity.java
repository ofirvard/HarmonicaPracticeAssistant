package com.example.harmonicapracticeassistant.practice;

import android.os.Bundle;
import android.view.View;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.ParcelIds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class PracticeActivity extends AppCompatActivity
{
    private boolean isPracticing = false;
    private Song song;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        AppSettings appSettings = getIntent().getExtras().getParcelable(ParcelIds.SETTINGS_PARCEL_ID);
//        applySettings(appSettings);// TODO: 11/27/2022  add key and size from settings
        song = getIntent().getExtras().getParcelable(ParcelIds.SONG_PARCEL_ID);
        // TODO: 23/11/2022 load the song 

        // TODO: 23/11/2022 this will be where i show the song and check how well you play it, it will show a text view that will change color as you progress 

        // TODO: 11/27/2022 can i put the spinner in the action bar?
    }

    public void practiceSong(View view)
    {
        if (isPracticing)
        {
            isPracticing = false;
            ((FloatingActionButton) view).setImageResource(R.drawable.image_microphone);
        }
        else
        {
            isPracticing = true;
            ((FloatingActionButton) view).setImageResource(R.drawable.shape_red_square);
        }
    }
}