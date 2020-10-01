package com.example.harmonicapracticeassistant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
    }

    public void newSong(View view)
    {
        Intent intent = new Intent(this, SongActivity.class);
        intent.putExtra(Keys.IS_NEW_SONG, true);
        intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
        startActivityForResult(intent, 1);
    }

    public void loadSongs(View view)
    {
        Intent intent = new Intent(this, SongListActivity.class);
        intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                songs = data.getExtras().getParcelableArrayList(Keys.SONGS);
            }
        }
    }
}
