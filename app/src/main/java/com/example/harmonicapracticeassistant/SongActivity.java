package com.example.harmonicapracticeassistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SongActivity extends AppCompatActivity {
    private boolean isRecording = false;
    private boolean isEditing = false;
    private EditText songName;
    private TextView songTabs;
    private List<Song> songs;
    private Song song;
    private boolean isNewSong;
    private int textSize;
    private boolean isBlow = true;
    private LinearLayout keyboard;
    private AppSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_builder);

        this.songs = getIntent().getExtras().getParcelableArrayList(Keys.SONGS);
        this.settings = getIntent().getExtras().getParcelable(Keys.SETTINGS);

        this.songName = findViewById(R.id.song_title);
        this.songTabs = findViewById(R.id.song_text);

        isNewSong = getIntent().getExtras().getBoolean(Keys.IS_NEW_SONG);
        if (!isNewSong) {
            int songPosition = getIntent().getExtras().getInt(Keys.SONG_POSITION);
            song = songs.get(songPosition);

            songName.setText(song.getName());
            songTabs.setText(song.toString());

            // TODO: 10/1/2020 load recordings 
        } else {
            song = new Song();
        }

        this.keyboard = (LinearLayout) setKeyboard();

        this.songTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getDefaultTextSize());
        this.textSize = settings.getDefaultTextSize();

        this.keyboard.setVisibility(LinearLayout.GONE);
        findViewById(R.id.backspace).setVisibility(View.GONE);
        findViewById(R.id.enter).setVisibility(View.GONE);
        findViewById(R.id.blowDraw).setVisibility(View.GONE);
        findViewById(R.id.space).setVisibility(View.GONE);
    }

    private View setKeyboard() {
        ViewStub stub = findViewById(R.id.layout_stub);
        if (settings.isKeyboardSlim())
            stub.setLayoutResource(R.layout.buttons_slim);
        else
            stub.setLayoutResource(R.layout.buttons_full);

        return stub.inflate();
    }

    public void record(View view) {
        if (!isEditing)
            if (isRecording) {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.microphone, getTheme()));
                isRecording = false;
            } else {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.record, getTheme()));
                isRecording = true;
            }
    }

    public void editSave(View view) {
        if (!isRecording)
            if (isEditing)/// TODO: 10/15/2020 debug this
            {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.pencil, getTheme()));
                isEditing = false;

                keyboard.setVisibility(LinearLayout.GONE);
                findViewById(R.id.backspace).setVisibility(View.GONE);
                findViewById(R.id.enter).setVisibility(View.GONE);
                findViewById(R.id.space).setVisibility(View.GONE);
                if (settings.isKeyboardSlim())
                    findViewById(R.id.blowDraw).setVisibility(View.GONE);

                findViewById(R.id.record).setVisibility(View.VISIBLE);
                findViewById(R.id.zoomIn).setVisibility(View.VISIBLE);
                findViewById(R.id.zoomOut).setVisibility(View.VISIBLE);

                songName.setEnabled(false);
            } else {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.floppy_disk, getTheme()));
                isEditing = true;

                keyboard.setVisibility(LinearLayout.VISIBLE);
                findViewById(R.id.backspace).setVisibility(View.VISIBLE);
                findViewById(R.id.enter).setVisibility(View.VISIBLE);
                findViewById(R.id.space).setVisibility(View.VISIBLE);
                if (settings.isKeyboardSlim()) {
                    findViewById(R.id.blowDraw).setVisibility(View.VISIBLE);
                }

                findViewById(R.id.record).setVisibility(View.GONE);
                findViewById(R.id.zoomIn).setVisibility(View.GONE);
                findViewById(R.id.zoomOut).setVisibility(View.GONE);

                songName.setEnabled(true);
            }
    }

    public void zoomIn(View view) {
        if (textSize < Keys.MAX_TEXT_SIZE) {
            textSize++;
            songTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    public void zoomOut(View view) {
        if (textSize > Keys.MIN_TEXT_SIZE) {
            textSize--;
            songTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    public void blowDraw(View view) {
        if (isEditing)
            if (isBlow) {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.minus, getTheme()));
                isBlow = false;
            } else {
                ((FloatingActionButton) view).setImageDrawable(getResources().getDrawable(R.drawable.plus, getTheme()));
                isBlow = true;
            }
    }

    public void space(View view) {
        if (isEditing) {
            song.addNote(Keys.NOTE_SPACE);
            songTabs.append(Song.noteTranslator(Keys.NOTE_SPACE));
        }
    }

    public void enter(View view) {
        if (isEditing) {
            song.addNote(Keys.NOTE_ENTER);
            songTabs.append(Song.noteTranslator(Keys.NOTE_ENTER));
        }
    }

    public void backspace(View view) {
        if (isEditing) {
            song.deleteLastNote();
            songTabs.setText(song.toString());
        }
    }

    public void note(View view) {
        if (isEditing) {
            int noteNumber = Integer.parseInt(((TextView) view).getText().toString().replaceAll(" ", ""));
            if (settings.isKeyboardSlim() && !isBlow)
                noteNumber *= -1;

            String note = Song.noteTranslator(noteNumber);
            song.addNote(noteNumber);
            songTabs.append(note);
        }
    }

    @Override
    public void onBackPressed() {// TODO: 10/30/2020 is !isNewSong dont do discard
        // TODO: 10/10/2020 check why this is causing crash
        // TODO: 9/30/2020 stop recording if recording
        if (isNewSong)
            if (!checkSongNameFree()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.name_in_use);
                builder.setMessage(R.string.name_in_use_dialog);

                builder.setPositiveButton(R.string.rename, null);
                builder.setNegativeButton(R.string.discard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SongActivity.super.onBackPressed();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                // TODO: 9/30/2020 add main recording and recordings once there done

                song.setName(songName.getText().toString());

                if (isNewSong)
                    songs.add(song);



                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
                setResult(RESULT_OK, intent);
                finish();

//            super.onBackPressed();
            }
        else {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Keys.SONGS, (ArrayList<? extends Parcelable>) songs);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean checkSongNameFree() {
        for (Song song : songs)
            if (song.getName().equals(songName.getText().toString()))
                return false;
        return true;
    }
}
