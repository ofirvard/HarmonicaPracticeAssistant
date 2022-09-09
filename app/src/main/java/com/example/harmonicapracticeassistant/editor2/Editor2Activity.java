package com.example.harmonicapracticeassistant.editor2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.utils.AppSettings;
import com.example.harmonicapracticeassistant.utils.Constants;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.harmonicapracticeassistant.utils.Constants.BACKSPACE_LONG_CLICK_DELETE_DELAY;
import static com.example.harmonicapracticeassistant.utils.Constants.BACKSPACE_LONG_CLICK_INITIAL_DELAY;

public class Editor2Activity extends AppCompatActivity
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor2);

        AppSettings appSettings = getIntent().getExtras().getParcelable(Constants.SETTINGS);
        EditText editText = findViewById(R.id.edit_text_song_tabs);
        // TODO: 9/9/2022 add slim keyboard
        if (getIntent().getExtras().getBoolean(Constants.IS_NEW_SONG))
            song = new Song(String.format("%s %d", getResources().getString(R.string.new_song), appSettings.getSongNextId()),
                    "",
                    appSettings.getSongNextId());
        else
        {
            song = getIntent().getExtras().getParcelable(Constants.SONG);
            editText.setText(song.getNotes());
        }

        HarmonicaUtils.setUp(getApplicationContext());
        Objects.requireNonNull(getSupportActionBar()).setTitle("New Song");

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
        // Handle item selection
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
        editorUtil.writeNote(this, view.getId());
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

    private void renameSongMenuItem()
    {
        final EditText edittext = new EditText(getApplicationContext());
        edittext.setText(song.getName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.rename_title);
        builder.setView(edittext);
        builder.setPositiveButton(R.string.rename, (dialog, which) -> song.setName(edittext.getText().toString()));
        builder.setNegativeButton(R.string.discard, null);

        AlertDialog dialog = builder.create();
        dialog.show();
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
            song.setName(String.format("%s %d", getResources().getString(R.string.new_song), song.getId()));

        song.setNotes(editorUtil.getEditTextString());

        // TODO: 9/9/2022 use save util
    }
}