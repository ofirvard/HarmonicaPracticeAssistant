package com.example.harmonicapracticeassistant.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import java.util.Objects;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

public class PitchDetector extends AppCompatActivity
{
    AudioDispatcher dispatcher;
    TextView noteTextView;
    TextView hertzTextView;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detector);

        hertzTextView = findViewById(R.id.hertz);
        noteTextView = findViewById(R.id.note);

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted)
                        setUpDetector();
                    else
                        Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
                });

        checkPermission();
    }

    public boolean checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        {
            setUpDetector();
            return true;
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
        {
            Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
        else
        {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
        return false;
    }

    private void setUpDetector()
    {
        NoteFinder.setUp(getApplicationContext());

        PitchDetectionHandler pdh = (res, e) -> {
            final float pitchInHz = res.getPitch();
            runOnUiThread(() -> processPitch(pitchInHz));
        };
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        dispatcher.addAudioProcessor(pitchProcessor);

        Thread audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    public void processPitch(float pitchInHz)
    {
        hertzTextView.setText("" + pitchInHz);
        noteTextView.setText(String.format("%s", Objects.requireNonNull(NoteFinder.getNoteByFrequency((int) pitchInHz)).getNote()));
    }
}