package com.example.harmonicapracticeassistant.songlist;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.LoadUtils;
import com.example.harmonicapracticeassistant.utils.ParcelIds;
import com.example.harmonicapracticeassistant.utils.SaveUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SongListActivity extends AppCompatActivity
{
    private List<SelectableSong> selectableSongs;
    private SongListAdapter songListAdapter;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);

        AppSettings settings = getIntent().getExtras().getParcelable(ParcelIds.SETTINGS_PARCEL_ID);
        selectableSongs = setSelectableSongs(LoadUtils.loadSongs(this));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        songListAdapter = new SongListAdapter(selectableSongs, SongListActivity.this, settings);
        recyclerView.setAdapter(songListAdapter);

        getSupportActionBar().setTitle(R.string.song_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.song_list_menu, menu);

        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu)
    {
        menu.getItem(R.id.delete_selected).setVisible(songListAdapter.isSelect());

        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        // TODO: 03/10/2022 use this 
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.select)
        {
            // TODO: 9/27/2022 start select
        }
        else if (item.getItemId() == R.id.select_all)
        {
            // TODO: 9/27/2022 select all
            selectAllSongs();
        }
        else if (item.getItemId() == R.id.delete_all)
        {
            deleteAll();
        }
        else if (item.getItemId() == R.id.test_add_song)
        {
            for (int i = 0; i < 5; i++)
            {
                Song song = new Song("Song " + new Random().nextInt(100), "");
                SaveUtils.saveSong(this, song);
                selectableSongs.add(new SelectableSong(song));
            }

            songListAdapter.notifyDataSetChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
//                songs = data.getExtras().getParcelableArrayList(Constants.SONGS);
//                mAdapter.setSongs(selectableSongs);
//                SaveUtils.saveSongs(getApplicationContext(), songs);
            }
        }
    }

    private void selectAllSongs()
    {
        selectableSongs.forEach(selectableSong -> selectableSong.setSelected(true));
        songListAdapter.setSelect(true);
    }

    private void deleteAll()
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_all_title)
                .setMessage(R.string.delete_all_message_dialog)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    SaveUtils.removeAllSong(this);
                    selectableSongs.clear();
                    songListAdapter.notifyDataSetChanged();
                }).
                setNegativeButton(R.string.no, (dialog, which) -> {
                })
                .create().show();
    }

    private List<SelectableSong> setSelectableSongs(List<Song> loadSongs)
    {
        List<SelectableSong> selectableSongs = new ArrayList<>();
        for (Song song : loadSongs)
            selectableSongs.add(new SelectableSong(song));

        return selectableSongs;
    }

    @Override
    public void onBackPressed()
    {
        if (songListAdapter.isSelect())
            songListAdapter.setSelect(false);
        else
            super.onBackPressed();
    }
}
