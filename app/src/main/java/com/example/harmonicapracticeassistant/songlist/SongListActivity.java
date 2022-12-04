package com.example.harmonicapracticeassistant.songlist;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.IntentBuilder;
import com.example.harmonicapracticeassistant.utils.LoadUtils;
import com.example.harmonicapracticeassistant.utils.SaveUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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

        AppSettings settings = getIntent().getExtras().getParcelable(IntentBuilder.SETTINGS_PARCEL_ID);
        selectableSongs = setSelectableSongs(LoadUtils.loadSongs(this));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        songListAdapter = new SongListAdapter(selectableSongs, this, settings);
        recyclerView.setAdapter(songListAdapter);

        getSupportActionBar().setTitle(R.string.song_list);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.song_list_action_bar_menu, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        menu.findItem(R.id.delete_selected).setVisible(songListAdapter.isSelect());
        menu.findItem(R.id.stop_select).setVisible(songListAdapter.isSelect());
        menu.findItem(R.id.select).setVisible(!songListAdapter.isSelect());
        menu.findItem(R.id.unselect_all).setVisible(songListAdapter.isSelect());
        menu.findItem(R.id.favourite_selected).setVisible(songListAdapter.isSelect() &&
                !areAllSelectedSongsFavourite(getSelectedSongs()));
        menu.findItem(R.id.unfavourite_selected).setVisible(songListAdapter.isSelect() &&
                areAllSelectedSongsFavourite(getSelectedSongs()));

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.select)
        {
            songListAdapter.setSelect(true);
        }
        else if (item.getItemId() == R.id.stop_select)
        {
            songListAdapter.setSelect(false);
        }
        else if (item.getItemId() == R.id.select_all)
        {
            setAllSongsSelect(true);
        }
        else if (item.getItemId() == R.id.unselect_all)
        {
            setAllSongsSelect(false);
        }
        else if (item.getItemId() == R.id.delete_selected)
        {
            deleteSelected();
        }
        else if (item.getItemId() == R.id.delete_all)
        {
            deleteAll();
        }
        else if (item.getItemId() == R.id.favourite_selected || item.getItemId() == R.id.unfavourite_selected)
        {
            setFavouriteSelected();
        }
        else if (item.getItemId() == R.id.test_add_song)
        {
            testMakeSong();
        }
        else if (item.getItemId() == R.id.test_add_song_10)
        {
            for (int i = 0; i < 10; i++)
                testMakeSong();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFavouriteSelected()
    {
        List<SelectableSong> selectedSongs = getSelectedSongs();
        boolean favouriteValue = !areAllSelectedSongsFavourite(selectedSongs);

        selectedSongs.forEach(selectedSong -> selectedSong.getSong()
                .setFavourite(favouriteValue));
        SaveUtils.saveSongs(this, selectableSongs);
        songListAdapter.sort();
        songListAdapter.notifyDataSetChanged();
    }

    private boolean areAllSelectedSongsFavourite(List<SelectableSong> selectedSongs)
    {
        return selectedSongs.stream().allMatch(selectableSong ->
                selectableSong.getSong().isFavourite());
    }

    private void testMakeSong()
    {
        Song song = new Song("Song " + new Random().nextInt(10000), "");
        SaveUtils.saveSong(this, song);
        selectableSongs.add(new SelectableSong(song));

        songListAdapter.notifyDataSetChanged();
    }

    private void setAllSongsSelect(boolean select)
    {
        selectableSongs.forEach(selectableSong -> selectableSong.setSelected(select));
        songListAdapter.setSelect(select);
    }

    private void deleteAll()
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_all_title)
                .setMessage(R.string.delete_all_message_dialog)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    SaveUtils.removeAllSong(this);
                    selectableSongs.clear();

                    songListAdapter.setSelect(false);
                    songListAdapter.notifyDataSetChanged();
                }).
                setNegativeButton(R.string.no, (dialog, which) -> {
                })
                .create().show();
    }

    private void deleteSelected()
    {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_selected_title)
                .setMessage(R.string.delete_selected_message_dialog)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    List<String> songUUIDsToDelete = getSelectedSongs().stream()
                            .map(SelectableSong::getSong)
                            .map(Song::getId)
                            .collect(Collectors.toList());
                    SaveUtils.removeSongs(this, songUUIDsToDelete);

                    selectableSongs.removeIf(selectableSong ->
                            songUUIDsToDelete.contains(selectableSong.getSong().getId()));

                    songListAdapter.setSelect(false);
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

    private List<SelectableSong> getSelectedSongs()
    {

        return selectableSongs.stream()
                .filter(SelectableSong::isSelected)
                .collect(Collectors.toList());
    }
}
