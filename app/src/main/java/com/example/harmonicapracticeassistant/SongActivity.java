package com.example.harmonicapracticeassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SongActivity extends AppCompatActivity
{
    private boolean isRecording = false;
    private boolean isEditing = false;
    private EditText songName;
    private EditText songTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        songName = findViewById(R.id.song_title);
        songTabs = findViewById(R.id.song_edit_text);

        if (getIntent().getExtras().getBoolean(Keys.IS_NEW_SONG))
        {
//            getActionBar().setTitle("New Song");
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
}
