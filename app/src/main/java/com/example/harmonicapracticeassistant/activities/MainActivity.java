package com.example.harmonicapracticeassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.pitchdetector.PitchDetectorActivity;
import com.example.harmonicapracticeassistant.utils.AppSettings;
import com.example.harmonicapracticeassistant.utils.Constants;
import com.example.harmonicapracticeassistant.utils.SaveHandler;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private List<Song> songs;
    private AppSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songs = SaveHandler.loadSongs(getApplicationContext());
        settings = SaveHandler.loadSettings(getApplicationContext());
    }

    public void newSong(View view)
    {
        Intent intent = new Intent(this, SongActivity.class);
        intent.putExtra(Constants.IS_NEW_SONG, true);
        intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);
        intent.putExtra(Constants.SETTINGS, settings);
        startActivityForResult(intent, Constants.SONG_LIST_UPDATE_CODE);
    }

    public void loadSongs(View view)
    {
        Intent intent = new Intent(this, SongListActivity.class);
        intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);
        intent.putExtra(Constants.SETTINGS, settings);
        startActivityForResult(intent, Constants.SONG_LIST_UPDATE_CODE);
    }

    public void settings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);
        intent.putExtra(Constants.SETTINGS, settings);
        startActivityForResult(intent, Constants.SETTINGS_REQUEST_CODE);
    }

    public void pitchDetector(View view)
    {
        // TODO: 08/03/2022 start pitch detector
        Intent intent = new Intent(this, PitchDetectorActivity.class);
        intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);// TODO: 08/03/2022 do i need this 
        intent.putExtra(Constants.SETTINGS, settings);
        startActivityForResult(intent, Constants.PITCH_DETECTOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case Constants.SONG_LIST_UPDATE_CODE:
                if (resultCode == RESULT_OK)
                    songs = data.getExtras().getParcelableArrayList(Constants.SONGS);
                break;

            case Constants.SETTINGS_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    settings = data.getExtras().getParcelable(Constants.SETTINGS);
                    if (data.getExtras().getBoolean(Constants.NEW_SONGS_IMPORTED))
                        songs = SaveHandler.loadSongs(getApplicationContext());
                }
                break;

        }
    }
}
