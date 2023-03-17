package com.example.harmonicapracticeassistant.pitchdetector;

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
import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.harmonica.Harmonica;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Note;
import com.example.harmonicapracticeassistant.settings.AppSettings;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.IntentBuilder;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import static com.example.harmonicapracticeassistant.utils.Constants.DEFAULT_FREQ;

public class PitchDetectorActivity extends AppCompatActivity
{
    private final List<List<Hole>> holesDetectedList = new ArrayList<>();
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private PitchDetectorHandler pitchDetectorHandler;
    private PitchDetectorProcessor pitchDetectorProcessor;
    private PitchDetectorRecyclerViewAdapter pitchDetectorRecyclerViewAdapter;
    private Spinner keySpinner;
    private TextView hertz;
    private RecyclerView notesRecyclerView;
    private Note lastNote = null;
    private AppSettings appSettings;

    // TODO: 10/11/2021 add save/record song button
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detector);

        appSettings = getIntent().getExtras().getParcelable(IntentBuilder.SETTINGS_PARCEL_ID);

        this.pitchDetectorHandler = new PitchDetectorHandler();
        this.pitchDetectorProcessor = new PitchDetectorProcessor(appSettings);
        this.hertz = findViewById(R.id.hertz);
        this.keySpinner = findViewById(R.id.key_spinner);
        this.notesRecyclerView = findViewById(R.id.notes_list_pitch_detector);

        this.requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted)
                        Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
                });

        checkPermission();
        setupRecyclerView();
        setupKeySpinner(appSettings);
    }

    private void setupRecyclerView()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        SnapHelper snapHelper = new LinearSnapHelper();
        pitchDetectorRecyclerViewAdapter = new PitchDetectorRecyclerViewAdapter(this,
                pitchDetectorProcessor,
                holesDetectedList,
                snapHelper);

        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        notesRecyclerView.setLayoutManager(layoutManager);
        notesRecyclerView.setAdapter(pitchDetectorRecyclerViewAdapter);
        snapHelper.attachToRecyclerView(notesRecyclerView);
        notesRecyclerView.addOnScrollListener(pitchDetectorRecyclerViewAdapter.getOnScrollListener());
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
            startRecording();
        else
            stopRecording();
    }

    public void switchVisual(View view)
    {
        ((Button) view).setText(NoteTranslator.switchVisual());
        pitchDetectorRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void clear(View view)
    {
        holesDetectedList.clear();
        pitchDetectorRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause()
    {
        stopRecording();
        super.onPause();
    }

    @SuppressLint("DefaultLocale")
    private void updateHertz()
    {
        List<Hole> holesDetected = pitchDetectorProcessor.processNewPitch(pitchDetectorHandler.getFrequency());

        if (!holesDetected.isEmpty())
        {
            if (lastNote == null || !lastNote.equals(holesDetected.get(0).getNote()))
            {
                lastNote = holesDetected.get(0).getNote();
                holesDetectedList.add(holesDetected);
                pitchDetectorRecyclerViewAdapter.notifyItemInserted(holesDetectedList.size() - 1);
                notesRecyclerView.post(() -> notesRecyclerView.smoothScrollToPosition(holesDetectedList.size() - 1));
                String translatedHolesDetected = NoteTranslator.holesToString(
                        pitchDetectorProcessor.getKey().isSharp(), holesDetected);
                hertz.setText(translatedHolesDetected);
                Log.d("Hertz Update", "updated ui: " + translatedHolesDetected);
            }
        }
        else
        {
            hertz.setText(R.string.not_applicable);
            lastNote = null;
            Log.d("Hertz Update", "updated ui: na");
        }
    }

    private void startRecording()
    {
        Log.d("Hertz Update", "starting recording");
        ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.shape_button_recording));
        pitchDetectorHandler.start();
        startUpdateUIThread();
    }

    private void stopRecording()
    {
        Log.d("Hertz Update", "stopping recording");
        ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.image_microphone));
        pitchDetectorHandler.stop();
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
                        Thread.sleep(200);
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

    private void startTestUIThread()
    {
        Handler handler = new Handler();
        Thread uiUpdaterThread = new Thread("UI updater test")
        {
            @Override
            public void run()
            {
                Harmonica harmonica = HarmonicaUtils.getHarmonica(appSettings.getDefaultTuningType(), appSettings.getDefaultKey());

                while (pitchDetectorHandler.isRunning())
                {
                    try
                    {
                        Thread.sleep(100);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    handler.post(() -> {
                        List<Hole> holesDetected = getHolesForTest(harmonica);

                        lastNote = holesDetected.get(0).getNote();
                        holesDetectedList.add(holesDetected);
                        pitchDetectorRecyclerViewAdapter.notifyItemInserted(holesDetectedList.size() - 1);


                        notesRecyclerView.post(() -> notesRecyclerView.smoothScrollToPosition(holesDetectedList.size() - 1)); //not too bad
                        String translatedHolesDetected = NoteTranslator.holesToString(
                                pitchDetectorProcessor.getKey().isSharp(), holesDetected);
                        hertz.setText(translatedHolesDetected);
                        Log.d("Hertz Update", "updated ui: " + translatedHolesDetected);
                    });
                }
                Log.d("Hertz Update", "stopped update ui thread");
            }
        };
        uiUpdaterThread.start();
        Log.d("Hertz Update", "started update ui thread");
    }

    private void setupKeySpinner(AppSettings appSettings)
    {
        // TODO: 12/20/2022 make this code more reusable
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item_text,
                HarmonicaUtils.getKeysName(appSettings.getDefaultTuningType()));
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_text);
        keySpinner.setAdapter(adapter);
        keySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                pitchDetectorProcessor.setHarmonica(appSettings.getDefaultTuningType(),
                        HarmonicaUtils.getKeys().get(i).getKeyName());
                holesDetectedList.clear();
                pitchDetectorRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
        keySpinner.setSelection(HarmonicaUtils.getPositionOfKey(appSettings));
    }

    private List<Hole> getHolesForTest(Harmonica harmonica)
    {
        Random random = new Random();
        return Collections.singletonList(harmonica.getHole(random.nextInt(10) + 1, Bend.NONE));
    }
}