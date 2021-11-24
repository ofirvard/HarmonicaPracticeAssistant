package com.example.harmonicapracticeassistant.pitchdetector;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.harmonicapracticeassistant.Note;
import com.example.harmonicapracticeassistant.R;
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

public class PitchDetector extends AppCompatActivity
{
    private final int MINIMUM_HERTZ_THRESHOLD = 10;

    private AudioDispatcher dispatcher;
    private TextView noteTextView;
    private TextView hertzTextView;
    private List<Note> notes;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private NoteListAdapter noteListAdapter;
    private RecyclerView noteListRecyclerView;
    private int previousNoteId = NoteFinder.NA_NOTE_ID;
    private boolean isRecording = false;
    private LinearSnapHelper snapHelper;
    private LinearLayoutManager layoutManager;
    private Thread audioThread;
    private TextView middleTextView;

    // TODO: 10/11/2021 add save song button, add dropdown list to select key, with none option
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pitch_detector);

        hertzTextView = findViewById(R.id.hertz);
        noteTextView = findViewById(R.id.note);
        middleTextView = findViewById(R.id.middle);

        NoteFinder.setUp(getApplicationContext());
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
//        notes.add(new Note(PADDING_NOTE_ID));
        notes.add(new Note(0));
        notes.add(new Note(1));
        notes.add(new Note(2));
        notes.add(new Note(3));
        notes.add(new Note(4));
        notes.add(new Note(5));
        notes.add(new Note(6));
        notes.add(new Note(7));
        notes.add(new Note(8));
        notes.add(new Note(9));
        notes.add(new Note(10));
        notes.add(new Note(11));
        notes.add(new Note(12));
        notes.add(new Note(13));
        notes.add(new Note(14));
        notes.add(new Note(15));
        notes.add(new Note(16));
        notes.add(new Note(17));
        notes.add(new Note(18));
        notes.add(new Note(19));
        notes.add(new Note(20));
        notes.add(new Note(21));
        notes.add(new Note(22));
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
        {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    public void processPitch(float pitchInHz)
    {
        if (pitchInHz <= MINIMUM_HERTZ_THRESHOLD)
        {
            hertzTextView.setText("" + pitchInHz);
            noteTextView.setText(R.string.not_applicable);
            previousNoteId = NoteFinder.NA_NOTE_ID;
        }
        else
        {
            hertzTextView.setText("" + pitchInHz);
            Note note = NoteFinder.getNoteByFrequency((int) pitchInHz);
            if (previousNoteId != note.getId())
            {
                noteTextView.setText(String.format("%s", note.getNote()));
                previousNoteId = note.getId();
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