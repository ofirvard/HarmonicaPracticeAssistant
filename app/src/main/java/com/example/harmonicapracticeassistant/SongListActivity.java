package com.example.harmonicapracticeassistant;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SongListActivity extends AppCompatActivity
{
    private List<Song> songs;
    private RecyclerView recyclerView;
    private SongListAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AppSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        songs = getIntent().getExtras().getParcelableArrayList(Keys.SONGS);
        settings = getIntent().getExtras().getParcelable(Keys.SETTINGS);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new SongListAdapter(songs, SongListActivity.this, settings);
        recyclerView.setAdapter(mAdapter);
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
                mAdapter.setSongs(songs);
                SaveHandler.saveSongs(getApplicationContext(), songs);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
        setResult(RESULT_OK, intent);
        finish();
    }
}
