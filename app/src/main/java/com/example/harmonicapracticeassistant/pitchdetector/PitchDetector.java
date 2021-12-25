package com.example.harmonicapracticeassistant.pitchdetector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.harmonica.Hole;
import com.example.harmonicapracticeassistant.harmonica.Key;
import com.example.harmonicapracticeassistant.harmonica.Note;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchProcessor;

import static com.example.harmonicapracticeassistant.utils.Constants.MINIMUM_HERTZ_THRESHOLD;
import static com.example.harmonicapracticeassistant.utils.Constants.NA_NOTE_FREQUENCY;
import static com.example.harmonicapracticeassistant.utils.Constants.NO_KEY;

public class PitchDetector extends AppCompatActivity
{
    private AudioDispatcher dispatcher;
    private TextView noteTextView;
    private TextView hertzTextView;
    private List<Hole> notes;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private NoteListAdapter noteListAdapter;
    private RecyclerView noteListRecyclerView;
    private boolean isRecording = false;
    private LinearSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private Thread audioThread;
    private TextView middleTextView;
    private Note previousNote = new Note(NA_NOTE_FREQUENCY);
    private Spinner keySpinner;

    // TODO: 10/11/2021 add save/record song button
    // TODO: 12/22/2021 add in settings default key
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detector);

        hertzTextView = findViewById(R.id.hertz);
        noteTextView = findViewById(R.id.note);
        middleTextView = findViewById(R.id.middle);
        keySpinner = findViewById(R.id.key_spinner);

        HarmonicaUtils.setUp(getApplicationContext());
        setupNoteListAdapter();
        setupKeySpinner();

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted)
                        Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
                });

        checkPermission();
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
                noteListAdapter.setCurrentKey(HarmonicaUtils.getKeys().get(i));

                if (noteListAdapter.isCurrentKeyNone())
                {
                    switchVisual(findViewById(R.id.visual_change));
                    testAll();
                }
                else
                    testKey();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }

    public void testAll()
    {
        notes.clear();
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.C, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.Db, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.D, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.Eb, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.E, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.F, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.Gb, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.G, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.Ab, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.A, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.Bb, 0)));
        notes.add(new Hole(NoteFinder.getNoteById(MusicalNote.B, 0)));
        noteListAdapter.notifyDataSetChanged();
        noteListRecyclerView.scrollToPosition(1);
    }

    public void testKey()
    {
        notes.clear();
        notes.add(noteListAdapter.getCurrentKey().getHole(10));
        notes.add(noteListAdapter.getCurrentKey().getHole(-10));
        notes.add(noteListAdapter.getCurrentKey().getHole(9));
        notes.add(noteListAdapter.getCurrentKey().getHole(-9));
        notes.add(noteListAdapter.getCurrentKey().getHole(8));
        notes.add(noteListAdapter.getCurrentKey().getHole(-8));
        notes.add(noteListAdapter.getCurrentKey().getHole(7));
        notes.add(noteListAdapter.getCurrentKey().getHole(-7));
        notes.add(noteListAdapter.getCurrentKey().getHole(6));
        notes.add(noteListAdapter.getCurrentKey().getHole(-6));
        notes.add(noteListAdapter.getCurrentKey().getHole(5));
        notes.add(noteListAdapter.getCurrentKey().getHole(-5));
        notes.add(noteListAdapter.getCurrentKey().getHole(4));
        notes.add(noteListAdapter.getCurrentKey().getHole(-4));
        notes.add(noteListAdapter.getCurrentKey().getHole(3));
        notes.add(noteListAdapter.getCurrentKey().getHole(-3));
        notes.add(noteListAdapter.getCurrentKey().getHole(2));
        notes.add(noteListAdapter.getCurrentKey().getHole(-2));
        notes.add(noteListAdapter.getCurrentKey().getHole(1));
        notes.add(noteListAdapter.getCurrentKey().getHole(-1));

        noteListAdapter.notifyDataSetChanged();
        noteListRecyclerView.scrollToPosition(1);
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

    public void processPitch(float frequency)
    {
        if (frequency <= MINIMUM_HERTZ_THRESHOLD)
        {
            hertzTextView.setText(String.format("%s", frequency));
            noteTextView.setText(R.string.not_applicable);
            previousNote = new Note(NA_NOTE_FREQUENCY);
        }
        else
        {
            if (noteListAdapter.isCurrentKeyNone())
            {
                hertzTextView.setText(String.format("%s", frequency));
                Note note = NoteFinder.getNoteByFrequency(frequency);
                if (!previousNote.isSameNote(note))
                {
                    noteTextView.setText(String.format("%s", note.getMusicalNote()));
                    previousNote = note;
//                    notes.add(note);todo get hole, create detector handler to hold all this data
                    // TODO: 24/11/2021 see why it goes to one before
                    noteListRecyclerView.scrollToPosition(notes.size() - 1);
                    // TODO: 23/11/2021 set middle?
                    noteListAdapter.notifyDataSetChanged();
                }
            }
            else
            {
                // TODO: 12/23/2021 check that note is in key

//                NoteFinder.getNoteByFrequency()
                noteListAdapter.getCurrentKey();
            }
        }
    }

    public void recordNotes(View view)
    {
        if (isRecording)
        {
            isRecording = false;
            ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.microphone));
            hertzTextView.setText(R.string.not_applicable);
            noteTextView.setText(R.string.not_applicable);
            stopDetector();
        }
        else
        {
            isRecording = true;
            notes.clear();
            noteListAdapter.notifyDataSetChanged();
            ((ImageButton) findViewById(R.id.record_pitch_detector)).setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.recording_button));
            startDetector();
        }
    }

    public void setupNoteListAdapter()
    {
        noteListRecyclerView = findViewById(R.id.notes_list_pitch_detector);
        notes = new ArrayList<>();

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        noteListRecyclerView.setLayoutManager(layoutManager);

        snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(noteListRecyclerView);
        // TODO: 28/11/2021 move this into adapter LinearSnapHelper
        noteListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            int snapPosition = RecyclerView.NO_POSITION;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                int newSnapPosition = recyclerView.getChildAdapterPosition(snapHelper.findSnapView(layoutManager));
                if (newSnapPosition != RecyclerView.NO_POSITION && newSnapPosition != snapPosition)
                {
                    snapPosition = newSnapPosition;
                    middleTextView.setText("" + newSnapPosition);
                    noteListAdapter.setCenterNote(newSnapPosition);
                }
            }
        });

        noteListAdapter = new NoteListAdapter(notes, getApplicationContext());
        noteListRecyclerView.setAdapter(noteListAdapter);
    }

    private void startDetector()
    {
        PitchDetectionHandler pdh = (res, e) -> {
            final float pitchInHz = res.getPitch();
            runOnUiThread(() -> processPitch(pitchInHz));
        };
        AudioProcessor pitchProcessor = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        dispatcher.addAudioProcessor(pitchProcessor);

        audioThread = new Thread(dispatcher, "Audio Thread");
        audioThread.start();
    }

    private void stopDetector()
    {
        if (dispatcher != null)
            dispatcher.stop();
        if (audioThread != null)
            audioThread.interrupt();
    }

    public void switchVisual(View view)
    {
        ((Button) view).setText(noteListAdapter.switchVisual());
        noteListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        stopDetector();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if (isRecording)
            startDetector();
    }
}