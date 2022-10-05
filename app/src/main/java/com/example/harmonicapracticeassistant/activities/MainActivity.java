package com.example.harmonicapracticeassistant.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.editor.EditorActivity;
import com.example.harmonicapracticeassistant.pitchdetector.PitchDetectorActivity;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.settings.SettingsActivity;
import com.example.harmonicapracticeassistant.songlist.SongListActivity;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.LoadUtils;
import com.example.harmonicapracticeassistant.utils.ParcelIds;

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
                        settings = data.getExtras().getParcelable(ParcelIds.SETTINGS_PARCEL_ID);
                    }
                });

        HarmonicaUtils.setUp(getApplicationContext());
        settings = LoadUtils.loadSettings(this);
    }

    public void newSong(View view)
    {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra(ParcelIds.IS_NEW_SONG_PARCEL_ID, true);
        intent.putExtra(ParcelIds.SETTINGS_PARCEL_ID, settings);
        startActivity(intent);
    }

    public void loadSongs(View view)
    {
        Intent intent = new Intent(this, SongListActivity.class);
        intent.putExtra(ParcelIds.SETTINGS_PARCEL_ID, settings);
        startActivity(intent);
    }

    public void settings(View view)
    {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(ParcelIds.SETTINGS_PARCEL_ID, settings);

        activityResultLauncher.launch(intent);
    }

    public void pitchDetector(View view)
    {
        Intent intent = new Intent(this, PitchDetectorActivity.class);
        intent.putExtra(ParcelIds.SETTINGS_PARCEL_ID, settings);
        startActivity(intent);
    }
}
