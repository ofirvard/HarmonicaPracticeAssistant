package com.example.harmonicapracticeassistant.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.editor.SpecialKeysFABHandler;
import com.example.harmonicapracticeassistant.editor.Tab;
import com.example.harmonicapracticeassistant.utils.AppSettings;
import com.example.harmonicapracticeassistant.utils.Constants;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class SongActivity extends AppCompatActivity
{

    SpecialKeysFABHandler specialKeysFABHandler;
    private boolean isRecording = false;
    private boolean isEditing = false;
    private EditText songName;
    private EditText songTabs;
    private List<Song> songs;
    private Song song;
    private boolean isNewSong;
    private int textSize;
    private boolean isBlow = true;
    private LinearLayout keyboard;
    private AppSettings settings;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_builder);

        specialKeysFABHandler = new SpecialKeysFABHandler(this);

        this.songs = getIntent().getExtras().getParcelableArrayList(Constants.SONGS);
        this.settings = getIntent().getExtras().getParcelable(Constants.SETTINGS);

        this.songName = findViewById(R.id.song_title);
        this.songTabs = findViewById(R.id.song_text);
        songTabs.setRawInputType(InputType.TYPE_NULL);//todo check if cursor can be moved
//        songTabs.setRawInputType(InputType.TYPE_CLASS_TEXT);
//        songTabs.setTextIsSelectable(true);

        songTabs.setShowSoftInputOnFocus(false);

        songTabs.setOnTouchListener((v, event) -> {
            boolean disabled = false;
            if (MotionEvent.ACTION_UP == event.getAction())
            {
                if (songTabs.hasSelection())
                {
                    songTabs.setInputType(InputType.TYPE_NULL);
                    disabled = true;
                }
                else
                    //Then we restore it, if previously unset.
                    if (disabled)
                    {
                        songTabs.setInputType(InputType.TYPE_CLASS_TEXT);
                        disabled = false;
                    }
            }
            return false;
        });

        isNewSong = getIntent().getExtras().getBoolean(Constants.IS_NEW_SONG);
        if (!isNewSong)
        {
            int songPosition = getIntent().getExtras().getInt(Constants.SONG_POSITION);
            song = songs.get(songPosition);

            songName.setText(song.getName());
            songTabs.setText(song.toString());

            // TODO: 10/1/2020 load recordings
        }
        else
            song = new Song();

        this.keyboard = (LinearLayout) setKeyboard();

        this.songTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getDefaultTextSize());
        this.textSize = settings.getDefaultTextSize();

        // TODO: 1/31/21 move to function
        this.keyboard.setVisibility(LinearLayout.GONE);
        findViewById(R.id.backspace).setVisibility(View.GONE);
        findViewById(R.id.new_line).setVisibility(View.GONE);
        findViewById(R.id.blowDraw).setVisibility(View.GONE);
        findViewById(R.id.space).setVisibility(View.GONE);
        findViewById(R.id.more_buttons).setVisibility(View.GONE);
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
        basicClick();

        if (!isRecording)
            switchKeyboard((FloatingActionButton) view);
    }

    public void zoomIn(View view)
    {
        basicClick();

        if (textSize < Constants.MAX_TEXT_SIZE)
        {
            textSize++;
            songTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    public void zoomOut(View view)
    {
        basicClick();

        if (textSize > Constants.MIN_TEXT_SIZE)
        {
            textSize--;
            songTabs.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    public void blowDraw(View view)
    {
        basicClick();

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

    public void note(View view)
    {
        basicClick();

        if (isEditing)
        {

//            SpannableString string = new SpannableString(".");
//            string.setSpan(new ImageSpan(this, R.mipmap.ic_launcher), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            songTabs.append(string);
//
            Tab tab = new Tab(1, false, Tab.BendOld.OLD);
            song.addNote(tab);
//            String note = Song.noteTranslator(noteNumber);
//            song.addNote(noteNumber);
            songTabs.append(tab.getNote());
        }
    }

    public void specialKey(View view)
    {
        basicClick();
        // TODO: 15/03/2021 make them alter the note to the left

        if (isEditing)
        {
            switch (view.getId())
            {
                case R.id.space:
//                    song.addNote(Keys.NOTE_SPACE);todo figure this out
                    songTabs.append(Song.noteTranslator(Constants.NOTE_SPACE));
                    break;

                case R.id.new_line:// TODO: 16/04/2021 this will create two line
//                    song.addNote(Keys.NOTE_ENTER);
//                    songTabs.append(Song.noteTranslator(Keys.NOTE_ENTER));
                    break;

                case R.id.backspace:
                    song.deleteLastNote();
                    songTabs.setText(song.toString());
                    break;

//                case R.id.bracket_open_fab:
//                    song.addNote(Keys.BRACKET_OPEN);
//                    songTabs.append(Song.noteTranslator(Keys.BRACKET_OPEN));
//                    break;
//
//                case R.id.bracket_close_fab:
//                    song.addNote(Keys.BRACKET_CLOSE);
//                    songTabs.append(Song.noteTranslator(Keys.BRACKET_CLOSE));
//                    break;

//                case R.id.half_bend_fab:
//                    song.addNote(Keys.HALF_BEND);
//                    songTabs.append(Song.noteTranslator(Keys.HALF_BEND));
//                    break;
//
//                case R.id.whole_bend_fab:
//                    song.addNote(Keys.WHOLE_BEND);
//                    songTabs.append(Song.noteTranslator(Keys.WHOLE_BEND));
//                    break;
//
//                case R.id.step_and_a_half_bend_fab:
//                    song.addNote(Keys.WHOLE_HALF_BEND);
//                    songTabs.append(Song.noteTranslator(Keys.WHOLE_HALF_BEND));
//
//                    break;

//                case R.id.over_fab:
//                    song.addNote(Keys.OVER);
//                    songTabs.append(Song.noteTranslator(Keys.OVER));
//                    break;
//
//                case R.id.wave_fab:
//                    song.addNote(Keys.WAVE);
//                    songTabs.append(Song.noteTranslator(Keys.WAVE));
//                    break;
            }
        }
    }

    @Override
    public void onBackPressed()
    {// TODO: 10/30/2020 is !isNewSong don't do discard
        // TODO: 10/10/2020 check why this is causing crash
        // TODO: 9/30/2020 stop recording if recording
        basicClick();

        if (isNewSong)
            if (!checkSongNameFree())
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.name_in_use);
                builder.setMessage(R.string.name_in_use_dialog);

                builder.setPositiveButton(R.string.rename, null);
                builder.setNegativeButton(R.string.discard, (dialogInterface, i) -> SongActivity.super.onBackPressed());

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            else
            {
                // TODO: 9/30/2020 add main recording and recordings once there done

                song.setName(songName.getText().toString());

                if (isNewSong)
                    songs.add(song);


                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);
                setResult(RESULT_OK, intent);
                finish();

//            super.onBackPressed();
            }
        else
        {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Constants.SONGS, (ArrayList<? extends Parcelable>) songs);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private boolean checkSongNameFree()
    {
        for (Song song : songs)
            if (song.getName().equals(songName.getText().toString()))
                return false;
        return true;
    }

    private void switchKeyboard(FloatingActionButton view)
    {
        // TODO: 1/31/21 separate to functions
        if (isEditing)/// TODO: 10/15/2020 debug this
        {
            view.setImageDrawable(getResources().getDrawable(R.drawable.pencil, getTheme()));
            isEditing = false;
            basicClick();

            keyboard.setVisibility(LinearLayout.GONE);
            findViewById(R.id.backspace).setVisibility(View.GONE);
            findViewById(R.id.new_line).setVisibility(View.GONE);
            findViewById(R.id.space).setVisibility(View.GONE);
            findViewById(R.id.more_buttons).setVisibility(View.GONE);
//            findViewById(R.id.bracketOpen).setVisibility(View.GONE);
//            findViewById(R.id.bracketClose).setVisibility(View.GONE);
            if (settings.isKeyboardSlim())
                findViewById(R.id.blowDraw).setVisibility(View.GONE);

            findViewById(R.id.record).setVisibility(View.VISIBLE);
            findViewById(R.id.zoom_in).setVisibility(View.VISIBLE);
            findViewById(R.id.zoom_out).setVisibility(View.VISIBLE);

            songName.setEnabled(false);
        }
        else
        {
            view.setImageDrawable(getResources().getDrawable(R.drawable.floppy_disk, getTheme()));
            isEditing = true;

            keyboard.setVisibility(LinearLayout.VISIBLE);
            findViewById(R.id.backspace).setVisibility(View.VISIBLE);
            findViewById(R.id.new_line).setVisibility(View.VISIBLE);
            findViewById(R.id.space).setVisibility(View.VISIBLE);
            findViewById(R.id.more_buttons).setVisibility(View.VISIBLE);
//            findViewById(R.id.bracketOpen).setVisibility(View.VISIBLE);
//            findViewById(R.id.bracketClose).setVisibility(View.VISIBLE);
            if (settings.isKeyboardSlim())
            {
                findViewById(R.id.blowDraw).setVisibility(View.VISIBLE);
            }

            findViewById(R.id.record).setVisibility(View.GONE);
            findViewById(R.id.zoom_in).setVisibility(View.GONE);
            findViewById(R.id.zoom_out).setVisibility(View.GONE);

            songName.setEnabled(true);
        }
    }

    public void clearFocus(View view)
    {
        songName.clearFocus();
    }

    private void basicClick()
    {
        songName.clearFocus();
        specialKeysFABHandler.closeFABMenu();
    }

    private View setKeyboard()
    {
        ViewStub stub = findViewById(R.id.layout_stub);
        if (settings.isKeyboardSlim())
            stub.setLayoutResource(R.layout.buttons_slim);
        else
            stub.setLayoutResource(R.layout.buttons_full);

        return stub.inflate();
    }
}
