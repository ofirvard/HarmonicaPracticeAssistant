package com.example.harmonicapracticeassistant.editor2;

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
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // TODO: 8/31/2022 load song
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor2);

        HarmonicaUtils.setUp(getApplicationContext());

        getSupportActionBar().setTitle("New Song");

        EditText editText = findViewById(R.id.edit_text_song_tabs);
        editText.setShowSoftInputOnFocus(false);
        editText.setRawInputType(InputType.TYPE_CLASS_TEXT);

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
//            saveSong();// TODO: 8/31/2022 save song
            if (!HarmonicaUtils.findIllegalTabs(editorUtil.getNotesStringList()).isEmpty())
            {
                // TODO: 9/1/2022 show popup warning that there are illegal tabs
            }

            editorUtil.colorIllegalTabs();


            return true;
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

    public void save(MenuItem menuItem)
    {
        // TODO: 9/1/2022 check if all legal
    }
}