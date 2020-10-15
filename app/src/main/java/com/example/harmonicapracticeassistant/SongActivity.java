package com.example.harmonicapracticeassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SongActivity extends AppCompatActivity
{
    private boolean isRecording = false;
    private boolean isEditing = false;
    private EditText songName;
    private TextView songTabs;
    private List<Song> songs;
    private Song song;
    private boolean isNewSong;
    private int textSize;
    private boolean isBlow = true;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_builder);

        songName = findViewById(R.id.song_title);
        songTabs = findViewById(R.id.song_text);
        linearLayout = findViewById(R.id.linearLayout);

        songs = getIntent().getExtras().getParcelableArrayList(Keys.SONGS);
        AppSettings settings = getIntent().getExtras().getParcelable(Keys.SETTINGS);

        isNewSong = getIntent().getExtras().getBoolean(Keys.IS_NEW_SONG);
        if (!isNewSong)
        {
            int songPosition = getIntent().getExtras().getInt(Keys.SONG_POSITION);
            song = songs.get(songPosition);

            songName.setText(song.getName());
            songTabs.setText(song.getTabs());

            // TODO: 10/1/2020 load recordings 
        }

        songTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getDefaultTextSize());
        textSize = settings.getDefaultTextSize();

        linearLayout.setVisibility(LinearLayout.GONE);
        findViewById(R.id.backspace).setVisibility(View.GONE);
        findViewById(R.id.enter).setVisibility(View.GONE);
        findViewById(R.id.blowDraw).setVisibility(View.GONE);
        findViewById(R.id.space).setVisibility(View.GONE);
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
            if (isEditing)/// TODO: 10/15/2020 debug this
            {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.pencil, getTheme()));
                isEditing = false;

                linearLayout.setVisibility(LinearLayout.GONE);
                findViewById(R.id.backspace).setVisibility(View.GONE);
                findViewById(R.id.enter).setVisibility(View.GONE);
                findViewById(R.id.blowDraw).setVisibility(View.GONE);
                findViewById(R.id.space).setVisibility(View.GONE);

                findViewById(R.id.record).setVisibility(View.VISIBLE);
                findViewById(R.id.zoomIn).setVisibility(View.VISIBLE);
                findViewById(R.id.zoomOut).setVisibility(View.VISIBLE);

                songName.setEnabled(false);
            }
            else
            {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.floppy_disk, getTheme()));
                isEditing = true;

                findViewById(R.id.record).setVisibility(View.GONE);
                findViewById(R.id.zoomIn).setVisibility(View.GONE);
                findViewById(R.id.zoomOut).setVisibility(View.GONE);

                linearLayout.setVisibility(LinearLayout.VISIBLE);
                findViewById(R.id.backspace).setVisibility(View.VISIBLE);
                findViewById(R.id.enter).setVisibility(View.VISIBLE);
                findViewById(R.id.blowDraw).setVisibility(View.VISIBLE);
                findViewById(R.id.space).setVisibility(View.VISIBLE);

                songName.setEnabled(true);
            }
    }

    public void zoomIn(View view)
    {
        if (textSize < Keys.MAX_TEXT_SIZE)
        {
            textSize++;
            songTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    public void zoomOut(View view)
    {
        if (textSize > Keys.MIN_TEXT_SIZE)
        {
            textSize--;
            songTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    public void blowDraw(View view)
    {
        if (isEditing)
            if (isBlow)
            {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.minus, getTheme()));
                isBlow = false;
            }
            else
            {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.plus, getTheme()));
                isBlow = true;
            }
    }

    public void space(View view)
    {
        if (isEditing)
            songTabs.append(" _ ");
    }

    public void enter(View view)
    {
        if (isEditing)
            songTabs.append("\n");
    }

    public void backspace(View view)
    {
        if (isEditing)
        {
            // TODO: 10/15/2020
        }
    }

    public void note(View view)
    {
        if (isEditing)
        {
            String note = "";
            if (isBlow)
                note += "+";
            else
                note += "-";

            note += ((TextView) view).getText().toString();
            note += " ";

            songTabs.append(note);
        }
    }

    @Override
    public void onBackPressed()
    {
        // TODO: 10/10/2020 check why this is causing crash
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

            SaveHandler.saveSongs(getApplicationContext(), songs);

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
