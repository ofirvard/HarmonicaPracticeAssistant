package com.example.harmonicapracticeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private List<Song> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songs = SaveHandler.load(getApplicationContext());

        // TODO: 9/28/2020 load songs
        songs = SaveHandler.load(getApplicationContext());
    }

    public void newSong(View view)
    {
        Intent intent = new Intent(this, SongActivity.class);
        intent.putExtra(Keys.IS_NEW_SONG, true);
        intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
        startActivity(intent);

    }

    public void loadSongs(View view)
    {
        Intent intent = new Intent(this, SongListActivity.class);
        intent.putExtra("songs", songs.toArray());
        startActivity(intent);
    }
}
