package com.example.harmonicapracticeassistant.pitchdetector2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
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
    private Spinner keySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detector2);

        HarmonicaUtils.setUp(getApplicationContext());
        pitchDetectorHandler = new PitchDetectorHandler();
        pitchDetectorProcessor = new PitchDetectorProcessor();
        hertz = findViewById(R.id.hertz);
        keySpinner = findViewById(R.id.key_spinner);

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted)
                        Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
                });

        checkPermission();
        setupKeySpinner();
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
            Log.d("Hertz Update", "starting recording");
            ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.recording_button));
            pitchDetectorHandler.start();
            startUpdateUIThread();
        }
        else
        {
            Log.d("Hertz Update", "stopping recording");
            ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.microphone));
            pitchDetectorHandler.stop();
        }
    }

    @SuppressLint("DefaultLocale")
    private void updateHertz()
    {
        hertz.setText(pitchDetectorProcessor.processNewPitch(pitchDetectorHandler.getFrequency()));
        Log.d("Hertz Update", "updated ui");
    }

    public void switchVisual(View view)
    {
        // TODO: 05/02/2022 send notice to processor and list adapter
        ((Button) view).setText(pitchDetectorProcessor.switchVisual());
    }

    @Override
    protected void onPause()
    {// TODO: 05/02/2022 test this
        pitchDetectorHandler.stop();
        super.onPause();
    }

    private void startUpdateUIThread()
    {

        Handler handler = new Handler();
        Thread uiUpdaterThread = new Thread("UI updater")
        {
            @Override
            public void run()
            {
                Log.d("Hertz Update", "ui updater started");
                while (pitchDetectorHandler.isRunning())
                {
                    try
                    {
                        Thread.sleep(100);
                        Log.d("Hertz Update", "slept for 100 millis");
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    handler.post(() -> updateHertz());
                }
                Log.d("Hertz Update", "stopped update ui thread");
            }
        };
        uiUpdaterThread.start();
        Log.d("Hertz Update", "started update ui thread");
    }

    private void setupKeySpinner()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                HarmonicaUtils.getKeysName());
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_text);
        keySpinner.setAdapter(adapter);
        keySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                pitchDetectorProcessor.setCurrentKey(HarmonicaUtils.getKeys().get(i));

                if (pitchDetectorProcessor.isCurrentKeyNone())
                    switchVisual(findViewById(R.id.visual_change));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }
}