package com.example.harmonicapracticeassistant.pitchdetector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harmonicapracticeassistant.R;
import com.example.harmonicapracticeassistant.enums.MusicalNote;
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

public class PitchDetector extends AppCompatActivity
{
    private AudioDispatcher dispatcher;
    private TextView noteTextView;
    private TextView hertzTextView;
    private List<Note> notes;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private NoteListAdapter noteListAdapter;
    private RecyclerView noteListRecyclerView;
    private boolean isRecording = false;
    private LinearSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private Thread audioThread;
    private TextView middleTextView;
    private Note previousNote = new Note(NA_NOTE_FREQUENCY);

    // TODO: 10/11/2021 add save song button, add dropdown list to select key, with none option
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detector);

        hertzTextView = findViewById(R.id.hertz);
        noteTextView = findViewById(R.id.note);
        middleTextView = findViewById(R.id.middle);

        HarmonicaUtils.setUp(getApplicationContext());
        setupNoteListAdapter();

        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted)
                    {
//                        setUpDetector();
                    }
                    else
                        Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
                });

        checkPermission();
        test();
    }

    public void test()
    {
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.C, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.Db, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.D, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.Eb, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.E, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.F, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.Gb, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.G, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.Ab, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.A, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.Bb, 0)));
        notes.add(new Note(NoteFinder.getNoteById(MusicalNote.B, 0)));
//        noteListRecyclerView.scrollToPosition(1);
        noteListAdapter.notifyDataSetChanged();
        noteListRecyclerView.scrollToPosition(1);
    }

    public void checkPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        {
//            setUpDetector();
        }
        else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED)
        {
            Toast.makeText(this, R.string.recording_permission_denied, Toast.LENGTH_SHORT).show();
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
        else
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
    }

    public void processPitch(float frequency)
    {
        if (frequency <= MINIMUM_HERTZ_THRESHOLD)
        {
            hertzTextView.setText("" + frequency);
            noteTextView.setText(R.string.not_applicable);
            previousNote = new Note(NA_NOTE_FREQUENCY);
        }
        else
        {
            hertzTextView.setText("" + frequency);
            Note note = NoteFinder.getNoteByFrequency(frequency);
            if (!previousNote.isSameNote(note))
            {
                noteTextView.setText(String.format("%s", note.getMusicalNote()));
                previousNote = note;
                notes.add(note);
                // TODO: 24/11/2021 see why it goes to one before
                noteListRecyclerView.scrollToPosition(notes.size() - 1);
                // TODO: 23/11/2021 set middle?
                noteListAdapter.notifyDataSetChanged();
            }
        }
    }

    public void record(View view)
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
        if (noteListAdapter.switchVisual())
            ((Button) view).setText(R.string.notes);
        else
            ((Button) view).setText(R.string.frequency);

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