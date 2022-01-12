package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import androidx.annotation.NonNull;

import static com.example.harmonicapracticeassistant.utils.Constants.NA_NOTE_FREQUENCY;

public class Note
{
    private MusicalNote musicalNote;
    private int octave;
    private float frequency;

    public Note(MusicalNote musicalNote, int octave, float frequency)
    {
        this.musicalNote = musicalNote;
        this.octave = octave;
        this.frequency = frequency;
    }

    public Note(float frequency)
    {
        if (frequency != NA_NOTE_FREQUENCY)
        {
            Note note = NoteFinder.getNoteByFrequency(frequency);

            this.musicalNote = note.getMusicalNote();
            this.octave = note.getOctave();
            this.frequency = note.getFrequency();
        }
        else
            this.frequency = frequency;
    }

    public Note(Note note)
    {
        this.musicalNote = note.getMusicalNote();
        this.octave = note.getOctave();
        this.frequency = note.getFrequency();
    }

    public void setFrequency(float frequency)
    {
        this.frequency = frequency;
    }

    public MusicalNote getMusicalNote()
    {
        return musicalNote;
    }

    public int getOctave()
    {
        return octave;
    }

    public float getFrequency()
    {
        return frequency;
    }

    public String getNoteWithOctave(boolean isSharp)
    {
        return getNoteString(isSharp) + octave;
    }

    public boolean isSameNote(MusicalNote note, int octave)
    {
        return this.octave == octave && this.musicalNote == note;
    }

    public boolean isSameNote(Note note)
    {
        return isSameNote(note.getMusicalNote(), note.getOctave());
    }

    public String getNoteString(boolean isSharp)
    {
        return musicalNote.toString(isSharp);
    }

    @NonNull
    @Override
    public String toString()
    {
        return getNoteWithOctave(false);
    }
}
