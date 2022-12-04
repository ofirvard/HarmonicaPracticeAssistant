package com.example.harmonicapracticeassistant.practice;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.Song;
import com.example.harmonicapracticeassistant.pitchdetector.PitchDetectorHandler;
import com.example.harmonicapracticeassistant.pitchdetector.PitchDetectorProcessor;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.IntentBuilder;
import com.example.harmonicapracticeassistant.utils.TextUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class PracticeActivity extends AppCompatActivity
{
    private boolean isPracticing = false;
    private Song song;
    private PitchDetectorHandler pitchDetectorHandler;
    private PitchDetectorProcessor pitchDetectorProcessor;
    private AppSettings appSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);

        appSettings = getIntent().getExtras().getParcelable(IntentBuilder.SETTINGS_PARCEL_ID);
        song = getIntent().getExtras().getParcelable(IntentBuilder.SONG_PARCEL_ID);

        TextView textView = findViewById(R.id.practice_text_view);
        textView.setTextSize(appSettings.getDefaultTextSize());
        textView.setText(TextUtil.getColorIllegalTabsSpannable(song.getNotes()));
        getSupportActionBar().setTitle(song.getName());
        // TODO: 23/11/2022 load the song, color it

        // TODO: 23/11/2022 this will be where i show the song and check how well you play it, it will show a text view that will change color as you progress 

        // TODO: 11/27/2022 can i put the spinner in the action bar?
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // TODO: 12/4/2022 disable if in the middle of recording
        getMenuInflater().inflate(R.menu.practice_bar_spinner, menu);

        MenuItem item = menu.findItem(R.id.practice_key_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item_text,
                HarmonicaUtils.getKeysName());
        Spinner spinner = (Spinner) item.getActionView();

        adapter.setDropDownViewResource(R.layout.spinner_dropdown_text);
        spinner.setAdapter(adapter);
        spinner.setSelection(HarmonicaUtils.getPositionOfKey(appSettings.getDefaultKey()));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
//                pitchDetectorProcessor.setCurrentKey(HarmonicaUtils.getKeys().get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        return true;
    }

    public void practiceSong(View view)
    {
        if (isPracticing)
        {
            isPracticing = false;
            ((FloatingActionButton) view).setImageResource(R.drawable.image_microphone);
        }
        else
        {
            isPracticing = true;
            ((FloatingActionButton) view).setImageResource(R.drawable.shape_red_square);
        }
    }
}