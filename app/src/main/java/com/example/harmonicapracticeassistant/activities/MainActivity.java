package com.example.harmonicapracticeassistant.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.editor2.Editor2Activity;
import com.example.harmonicapracticeassistant.pitchdetector.PitchDetectorActivity;
import com.example.harmonicapracticeassistant.utils.AppSettings;
import com.example.harmonicapracticeassistant.utils.Constants;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.LoadUtils;

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

        HarmonicaUtils.setUp(getApplicationContext());
        songs = LoadUtils.loadSongs(this);// TODO: 22/09/2022 remove this 
        settings = LoadUtils.loadSettings(this);
    }

    public void newSong(View view)
    {
        new Intent(this, SongActivity.class);// TODO: 9/9/2022 delete
        Intent intent = new Intent(this, Editor2Activity.class);

        intent.putExtra(Constants.IS_NEW_SONG, true);
//        intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);
        intent.putExtra(Constants.SETTINGS, settings);

        startActivityForResult(intent, Constants.SONG_LIST_UPDATE_CODE);
    }

    public void loadSongs(View view)
    {
        Intent intent = new Intent(this, SongListActivity.class);

//        intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);// TODO: 21/09/2022 load songs inside 
        intent.putExtra(Constants.SETTINGS, settings);

        startActivityForResult(intent, Constants.SONG_LIST_UPDATE_CODE);
    }

    public void settings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);

//        intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);
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
                        songs = LoadUtils.loadSongs(getApplicationContext());
                }
                break;

        }
    }
}
