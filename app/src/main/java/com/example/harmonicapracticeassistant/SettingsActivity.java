package com.example.harmonicapracticeassistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity
{
    private AppSettings settings;
    private EditText textSizePreview;
    private boolean newSongsImported;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settings = getIntent().getExtras().getParcelable(Keys.SETTINGS);

        textSizePreview = findViewById(R.id.text_size_preview);
        setPreviewText();
    }

    public void increase(View view)
    {// TODO: 10/10/2020 why dose this seem smaller? 
        if (settings.getDefaultTextSize() < Keys.MAX_TEXT_SIZE)
        {
            settings.setDefaultTextSize(settings.getDefaultTextSize() + 1);
            setPreviewText();
        }
    }

    public void decrease(View view)
    {
        if (settings.getDefaultTextSize() > Keys.MIN_TEXT_SIZE)
        {
            settings.setDefaultTextSize(settings.getDefaultTextSize() - 1);
            setPreviewText();
        }
    }

    public void importSongs(View view)
    {
        // TODO: 10/10/2020 file picker
    }

    public void exportSongs(View view)
    {
        // TODO: 10/10/2020 file saver? figure out how to let user save file
    }

    public void save(View view)
    {
        SaveHandler.saveSettings(getApplicationContext(), settings);

        Intent intent = new Intent();
        intent.putExtra(Keys.SETTINGS, settings);
        intent.putExtra(Keys.NEW_SONGS_IMPORTED, newSongsImported);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setPreviewText()
    {
        textSizePreview.setText(String.format("%d", settings.getDefaultTextSize()));
        textSizePreview.setTextSize(TypedValue.COMPLEX_UNIT_SP, settings.getDefaultTextSize());
    }
}