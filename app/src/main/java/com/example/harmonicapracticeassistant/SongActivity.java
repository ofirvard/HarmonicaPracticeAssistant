package com.example.harmonicapracticeassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SongActivity extends AppCompatActivity
{
    private boolean isRecording = false;
    private boolean isEditing = false;
    private EditText songName;
    private EditText songTabs;
    private List<Song> songs;
    private Song song;
    private boolean isNewSong;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        songName = findViewById(R.id.song_title);
        songTabs = findViewById(R.id.song_edit_text);

        songs = getIntent().getExtras().getParcelableArrayList(Keys.SONGS);

        isNewSong = getIntent().getExtras().getBoolean(Keys.IS_NEW_SONG);
        if (!isNewSong)
        {
            int songPosition = getIntent().getExtras().getInt(Keys.SONG_POSITION);
            song = songs.get(songPosition);

            songName.setText(song.getName());
            songTabs.setText(song.getTabs());

            // TODO: 10/1/2020 load recordings 
        }
    }

    public void record(View view)
    {
        if (!isEditing)
            if (isRecording)
            {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.microphone, getTheme()));
                isRecording = false;
            }
            else
            {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.record, getTheme()));
                isRecording = true;
            }
    }

    public void editSave(View view)
    {
        if (!isRecording)
            if (isEditing)
            {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.pencil, getTheme()));
                isEditing = false;

                songName.setEnabled(false);
                songTabs.setEnabled(false);
            }
            else
            {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.floppy_disk, getTheme()));
                isEditing = true;

                songName.setEnabled(true);
                songTabs.setEnabled(true);
            }
    }

    @Override
    public void onBackPressed()
    {
        // TODO: 9/30/2020 stop recording if recording
        if (!checkSongNameFree())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.name_in_use);
            builder.setMessage(R.string.name_in_use_dialog);

            builder.setPositiveButton(R.string.rename, null);
            builder.setNegativeButton(R.string.discard, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    SongActivity.super.onBackPressed();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
        {
            // TODO: 9/30/2020 add main recording and recordings once there done 
            if (isNewSong)
                songs.add(new Song(songName.getText().toString(), songTabs.getText().toString(), false, new ArrayList<String>()));
            else
            {
                song.setName(songName.getText().toString());
                song.setTabs(songTabs.getText().toString());
            }

            SaveHandler.save(getApplicationContext(), songs);

            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
            setResult(RESULT_OK, intent);
            finish();

//            super.onBackPressed();
        }
    }

    private boolean checkSongNameFree()
    {
        for (Song song : songs)
            if (song.getName().equals(songName.getText().toString()))
                return false;
        return true;
    }
}
