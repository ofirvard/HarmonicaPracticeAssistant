package com.example.harmonicapracticeassistant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

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
        intent.putExtra(Keys.IS_NEW_SONG, true);
        intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
        intent.putExtra(Keys.SETTINGS, settings);
        startActivityForResult(intent, Keys.NEW_SONG_REQUEST_CODE);
    }

    public void loadSongs(View view)
    {
        Intent intent = new Intent(this, SongListActivity.class);
        intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
        intent.putExtra(Keys.SETTINGS, settings);
        startActivity(intent);
    }

    public void settings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(Keys.SETTINGS, settings);
        startActivityForResult(intent, Keys.SETTINGS_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case Keys.NEW_SONG_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                    songs = data.getExtras().getParcelableArrayList(Keys.SONGS);
                break;

            case Keys.SETTINGS_REQUEST_CODE:
                if (resultCode == RESULT_OK)
                {
                    settings = data.getExtras().getParcelable(Keys.SETTINGS);
                    if (data.getExtras().getBoolean(Keys.NEW_SONGS_IMPORTED))
                        songs = SaveHandler.loadSongs(getApplicationContext());
                }
                break;

        }
    }
}
