package com.example.harmonicapracticeassistant.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.pitchdetector.PitchDetectorActivity;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.settings.SettingsActivity;
import com.example.harmonicapracticeassistant.songlist.SongListActivity;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.IntentBuilder;
import com.example.harmonicapracticeassistant.utils.LoadUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    private AppSettings settings;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        settings = data.getExtras().getParcelable(IntentBuilder.SETTINGS_PARCEL_ID);
                    }
                });

        settings = LoadUtils.loadSettings(this);
        HarmonicaUtils.setUp(getApplicationContext(), settings);
    }

    public void newSong(View view)
    {
        startActivity(IntentBuilder.buildNewSongEditorIntent(getApplicationContext(), settings));
    }

    public void loadSongs(View view)
    {
        startActivity(IntentBuilder.buildBasicIntent(this,
                SongListActivity.class,
                settings));
    }

    public void settings(View view)
    {
        activityResultLauncher.launch(IntentBuilder.buildBasicIntent(this,
                SettingsActivity.class,
                settings));
    }

    public void pitchDetector(View view)
    {
        startActivity(IntentBuilder.buildBasicIntent(this,
                PitchDetectorActivity.class,
                settings));
    }
}
