package com.example.harmonicapracticeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void newSong(View view)
    {
        Intent intent = new Intent(this, SongActivity.class);
        intent.putExtra(Keys.IS_NEW_SONG, true);
        startActivity(intent);

    }

    public void loadSongs(View view)
    {
        Intent intent = new Intent(this, SongListActivity.class);
        startActivity(intent);
    }
}
