package com.example.harmonicapracticeassistant.editor;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.utils.AppSettings;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.ParcelIds;
import com.example.harmonicapracticeassistant.utils.SaveUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.harmonicapracticeassistant.utils.Constants.BACKSPACE_LONG_CLICK_DELETE_DELAY;
import static com.example.harmonicapracticeassistant.utils.Constants.BACKSPACE_LONG_CLICK_INITIAL_DELAY;

public class EditorActivity extends AppCompatActivity
{
    private final Handler handler = new Handler();
    private final Runnable backspaceRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            if (backspacePressed)
            {
                editorUtil.smartDelete();
                handler.postDelayed(this, BACKSPACE_LONG_CLICK_DELETE_DELAY);
            }
        }
    };

    private EditorUtil editorUtil;
    private boolean backspacePressed = false;
    private Song song;
    private FloatingActionButton blowDrawSwitch;
    private boolean isBlow = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        AppSettings appSettings = getIntent().getExtras().getParcelable(ParcelIds.SETTINGS_PARCEL_ID);
        applySettings(appSettings);

        EditText editText = findViewById(R.id.edit_text_song_tabs);

        if (getIntent().getExtras().getBoolean(ParcelIds.IS_NEW_SONG_PARCEL_ID))
            song = new Song(getResources().getString(R.string.new_song),
                    "");
        else
        {
            song = getIntent().getExtras().getParcelable(ParcelIds.SONG_PARCEL_ID);
            editText.setText(song.getNotes());
        }

        HarmonicaUtils.setUp(getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).setTitle(song.getName());

        editText.setShowSoftInputOnFocus(false);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        editText.setTextSize(appSettings.getDefaultTextSize());

        editorUtil = new EditorUtil(editText);

        findViewById(R.id.backspace).setOnTouchListener((v, event) -> {
            v.performClick();

            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    backspacePressed = true;

                    editorUtil.smartDelete();

                    handler.postDelayed(() -> {
                        if (backspacePressed)
                        {
                            handler.postDelayed(backspaceRunnable, BACKSPACE_LONG_CLICK_DELETE_DELAY);
                        }
                    }, BACKSPACE_LONG_CLICK_INITIAL_DELAY);
                    break;

                case MotionEvent.ACTION_UP:
                    backspacePressed = false;
                    break;
            }

            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.edit_song_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.edit_song_save)
        {
            saveSongMenuItem();
        }
        else if (item.getItemId() == R.id.edit_song_name)
        {
            renameSongMenuItem();
        }

        return super.onOptionsItemSelected(item);
    }

    public void writeNote(View view)
    {
        editorUtil.writeNote(this, isBlow, view.getId());
    }

    public void halfStepBend(View view)
    {
        editorUtil.bend(Bend.HALF_STEP);
    }

    public void wholeStepBend(View view)
    {
        editorUtil.bend(Bend.WHOLE_STEP);
    }

    public void stepAndAHalfBend(View view)
    {
        editorUtil.bend(Bend.STEP_AND_A_HALF);
    }

    public void newLine(View view)
    {
        editorUtil.newLine();
    }

    public void space(View view)
    {
        editorUtil.space();
    }

    public void switchBlowDraw(View view)
    {
        if (isBlow)
        {
            isBlow = false;
            blowDrawSwitch.setImageResource(R.drawable.image_minus);
        }
        else
        {
            isBlow = true;
            blowDrawSwitch.setImageResource(R.drawable.image_plus);
        }

        updateSlimKeyboard();
    }

    @Override
    public void onBackPressed()
    {
        if (!editorUtil.isEmpty())
            new AlertDialog.Builder(this)
                    .setTitle(R.string.save_song)
                    .setMessage(R.string.song_editor_dialog_back_press)
                    .setPositiveButton(R.string.save, (dialog, which) -> {
                        saveSong();
                    }).
                    setNegativeButton(R.string.discard, (dialog, which) -> super.onBackPressed())
                    .create().show();
        else
            super.onBackPressed();
    }

    private void updateSlimKeyboard()
    {
        if (isBlow)
        {
            ((Button) findViewById(R.id.note_1)).setText(R.string.keyboard_blow_1);
            ((Button) findViewById(R.id.note_1)).setText(R.string.keyboard_blow_1);
            ((Button) findViewById(R.id.note_2)).setText(R.string.keyboard_blow_2);
            ((Button) findViewById(R.id.note_3)).setText(R.string.keyboard_blow_3);
            ((Button) findViewById(R.id.note_4)).setText(R.string.keyboard_blow_4);
            ((Button) findViewById(R.id.note_5)).setText(R.string.keyboard_blow_5);
            ((Button) findViewById(R.id.note_6)).setText(R.string.keyboard_blow_6);
            ((Button) findViewById(R.id.note_7)).setText(R.string.keyboard_blow_7);
            ((Button) findViewById(R.id.note_8)).setText(R.string.keyboard_blow_8);
            ((Button) findViewById(R.id.note_9)).setText(R.string.keyboard_blow_9);
            ((Button) findViewById(R.id.note_10)).setText(R.string.keyboard_blow_10);
        }
        else
        {
            ((Button) findViewById(R.id.note_1)).setText(R.string.keyboard_draw_1);
            ((Button) findViewById(R.id.note_1)).setText(R.string.keyboard_draw_1);
            ((Button) findViewById(R.id.note_2)).setText(R.string.keyboard_draw_2);
            ((Button) findViewById(R.id.note_3)).setText(R.string.keyboard_draw_3);
            ((Button) findViewById(R.id.note_4)).setText(R.string.keyboard_draw_4);
            ((Button) findViewById(R.id.note_5)).setText(R.string.keyboard_draw_5);
            ((Button) findViewById(R.id.note_6)).setText(R.string.keyboard_draw_6);
            ((Button) findViewById(R.id.note_7)).setText(R.string.keyboard_draw_7);
            ((Button) findViewById(R.id.note_8)).setText(R.string.keyboard_draw_8);
            ((Button) findViewById(R.id.note_9)).setText(R.string.keyboard_draw_9);
            ((Button) findViewById(R.id.note_10)).setText(R.string.keyboard_draw_10);
            ;
        }
    }

    private void renameSongMenuItem()
    {
        final EditText edittext = new EditText(getApplicationContext());
        edittext.setText(song.getName());

        new AlertDialog.Builder(this)
                .setTitle(R.string.rename_title)
                .setView(edittext)
                .setPositiveButton(R.string.rename, (dialog, which) -> {
                    song.setName(edittext.getText().toString());
                    Objects.requireNonNull(getSupportActionBar()).setTitle(song.getName());
                })
                .setNegativeButton(R.string.discard, null).create().show();
    }

    private void saveSongMenuItem()
    {
        if (!HarmonicaUtils.findIllegalTabs(editorUtil.getNotesStringList()).isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.warning);
            builder.setMessage(R.string.illegal_tabs_warning);
            builder.setPositiveButton(R.string.yes, (dialog, which) -> saveSong());
            builder.setNegativeButton(R.string.no, (dialog, which) -> editorUtil.colorIllegalTabs());

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
            saveSong();
    }

    private void saveSong()
    {
        if (song.getName().equals(""))
            song.setName(String.format("%s %s", getResources().getString(R.string.new_song), song.getId()));

        song.setNotes(editorUtil.getEditTextString());

        SaveUtils.saveSong(this, song);
        Intent data = new Intent();
        data.putExtra(ParcelIds.SONG_PARCEL_ID, song);
        setResult(RESULT_OK, data);

        finish();
    }

    private void applySettings(AppSettings appSettings)
    {
        ViewStub keyboardStub = findViewById(R.id.keyboard_stub);

        if (appSettings.isKeyboardSlim())
        {
            blowDrawSwitch = findViewById(R.id.blow_draw_switch);
            blowDrawSwitch.setVisibility(View.VISIBLE);

            keyboardStub.setLayoutResource(R.layout.buttons_slim_2);
        }
        else
            keyboardStub.setLayoutResource(R.layout.buttons_full_2);

        keyboardStub.inflate();
    }
}