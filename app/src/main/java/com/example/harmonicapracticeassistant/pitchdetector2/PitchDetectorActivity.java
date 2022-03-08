package com.example.harmonicapracticeassistant.pitchdetector2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.NaturalNote;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Note;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import java.util.Collections;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

public class PitchDetectorActivity extends AppCompatActivity
{
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private PitchDetectorHandler pitchDetectorHandler;
    private PitchDetectorProcessor pitchDetectorProcessor;
    private PitchDetectorAdapter pitchDetectorAdapter;
    private NotePairListHandler notePairListHandler;
    private Spinner keySpinner;
    private TextView hertz;
    private RecyclerView notesRecyclerView;

    // TODO: 10/11/2021 add save/record song button
    // TODO: 12/22/2021 add in settings default key
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detector2);

        HarmonicaUtils.setUp(getApplicationContext());
        pitchDetectorHandler = new PitchDetectorHandler();
        pitchDetectorProcessor = new PitchDetectorProcessor();
        notePairListHandler = new NotePairListHandler();

        hertz = findViewById(R.id.hertz);
        keySpinner = findViewById(R.id.key_spinner);
        notesRecyclerView = findViewById(R.id.notes_list_pitch_detector);

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted)
                        Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
                });

        checkPermission();
        setupRecyclerView();
        setupKeySpinner();
    }

    private void setupRecyclerView()
    {
        // TODO: 07/02/2022 add snapping

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        SnapHelper snapHelper = new LinearSnapHelper();
        pitchDetectorAdapter = new PitchDetectorAdapter(this, pitchDetectorProcessor, notePairListHandler, snapHelper);

        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        notesRecyclerView.setLayoutManager(layoutManager);
        notesRecyclerView.setAdapter(pitchDetectorAdapter);
        snapHelper.attachToRecyclerView(notesRecyclerView);
        notesRecyclerView.addOnScrollListener(pitchDetectorAdapter.getOnScrollListener());
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
//        test();// TODO: 21/02/2022 this is only for testing
//        notesRecyclerView.post(() -> notesRecyclerView.smoothScrollToPosition(notePairListHandler.getLastPosition()));

        if (!pitchDetectorHandler.isRunning())
            startRecording();
        else
            stopRecording();
    }

    private void test()
    {
        Note n1 = NoteFinder.getNoteById(NaturalNote.C, 4);
        Hole h1 = new Hole(n1);
        Pair<Note, List<Hole>> p1 = new Pair<>(n1, Collections.singletonList(h1));

        Note n2 = NoteFinder.getNoteById(NaturalNote.D, 4);
        Hole h2 = new Hole(n2);
        Pair<Note, List<Hole>> p2 = new Pair<>(n2, Collections.singletonList(h2));

        Note n3 = NoteFinder.getNoteById(NaturalNote.E, 4);
        Hole h3 = new Hole(n3);
        Pair<Note, List<Hole>> p3 = new Pair<>(n3, Collections.singletonList(h3));

        Note n4 = NoteFinder.getNoteById(NaturalNote.F, 4);
        Hole h4 = new Hole(n4);
        Pair<Note, List<Hole>> p4 = new Pair<>(n4, Collections.singletonList(h4));

        Note n5 = NoteFinder.getNoteById(NaturalNote.G, 4);
        Hole h5 = new Hole(n5);
        Pair<Note, List<Hole>> p5 = new Pair<>(n5, Collections.singletonList(h5));

        Note n6 = NoteFinder.getNoteById(NaturalNote.A, 4);
        Hole h6 = new Hole(n6);
        Pair<Note, List<Hole>> p6 = new Pair<>(n6, Collections.singletonList(h6));

        notePairListHandler.add(p1);
        notePairListHandler.add(p2);
        notePairListHandler.add(p3);
        notePairListHandler.add(p4);
        notePairListHandler.add(p5);
        notePairListHandler.add(p6);

        pitchDetectorAdapter.notifyDataSetChanged();
    }

    @SuppressLint("DefaultLocale")
    private void updateHertz()
    {
        Pair<Note, List<Hole>> noteWithHoles = pitchDetectorProcessor.processNewPitch(pitchDetectorHandler.getFrequency());
        notePairListHandler.add(noteWithHoles);

        if (noteWithHoles != null)
        {
            pitchDetectorAdapter.notifyDataSetChanged();
            notesRecyclerView.post(() -> notesRecyclerView.smoothScrollToPosition(notePairListHandler.getLastPosition()));
            hertz.setText(NoteTranslator.holesToString(pitchDetectorProcessor.getKey().isSharp(), noteWithHoles));
            Log.d("Hertz Update", "updated ui: " + noteWithHoles.first);
        }
        else
        {
            hertz.setText(R.string.not_applicable);
            Log.d("Hertz Update", "updated ui: na");
        }
    }

    public void switchVisual(View view)
    {
        ((Button) view).setText(NoteTranslator.switchVisual(pitchDetectorProcessor.getKey().getKeyName()));
        pitchDetectorAdapter.notifyDataSetChanged();
    }

    public void clear(View view)
    {
        notePairListHandler.clear();
        pitchDetectorAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause()
    {// TODO: 05/02/2022 test this
        stopRecording();
        super.onPause();
    }

    private void startRecording()
    {
        Log.d("Hertz Update", "starting recording");
        ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.recording_button));
        pitchDetectorHandler.start();
        startUpdateUIThread();
    }

    private void stopRecording()
    {
        Log.d("Hertz Update", "stopping recording");
        ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.microphone));
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
                notePairListHandler.clear();
                pitchDetectorAdapter.notifyDataSetChanged();

                if (pitchDetectorProcessor.isCurrentKeyNone())// TODO: 16/02/2022 only if its on hole
                    switchVisual(findViewById(R.id.visual_change));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }
}