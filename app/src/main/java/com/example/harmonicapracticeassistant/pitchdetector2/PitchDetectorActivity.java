package com.example.harmonicapracticeassistant.pitchdetector2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

public class PitchDetectorActivity extends AppCompatActivity
{
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private PitchDetectorHandler pitchDetectorHandler;
    private PitchDetectorProcessor pitchDetectorProcessor;
    private TextView hertz;

    // TODO: 1/29/2022 have here a run on ui loop to read the handler
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detector2);

        HarmonicaUtils.setUp(getApplicationContext());
        pitchDetectorHandler = new PitchDetectorHandler();
        pitchDetectorProcessor = new PitchDetectorProcessor();
        hertz = findViewById(R.id.hertz);

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted)
                        Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
                });

        checkPermission();
    }

    public void checkPermission()
    {
        switch (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO))
        {
            case PackageManager.PERMISSION_GRANTED:
                break;
            case PackageManager.PERMISSION_DENIED:
                Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                break;
            default:
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
                break;
        }
    }

    public void recordNotes(View view)
    {
        if (!pitchDetectorHandler.isRunning())
        {
            ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.microphone));
            pitchDetectorHandler.start();// TODO: 02/02/2022 test this 
            runOnUiThread(() -> updateHertz());
        }
        else
        {
            ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.recording_button));
            pitchDetectorHandler.stop();
        }
    }

    @SuppressLint("DefaultLocale")
    private void updateHertz()
    {
        // TODO: 1/30/2022 ask the prosseser what is going on
        hertz.setText(String.format("%f", pitchDetectorHandler.getFrequency()));
    }
}