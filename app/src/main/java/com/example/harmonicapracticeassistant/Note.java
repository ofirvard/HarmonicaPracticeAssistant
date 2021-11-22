package com.example.harmonicapracticeassistant;

import com.example.harmonicapracticeassistant.utils.NoteFinder;

import androidx.annotation.NonNull;

public class Note
{
    private final String note;
    private final int octave;
    private final int frequency;
    private final int id;

    public Note(int id)
    {
        this.id = id;
        Note note = NoteFinder.getNoteById(id);
        this.note = note.getNote();
        this.octave = note.getOctave();
        this.frequency = note.getFrequency();
    }

    public String getNote()
    {
        return note;
    }

    public int getOctave()
    {
        return octave;
    }

    public int getFrequency()
    {
        return frequency;
    }

    public int getId()
    {
        return id;
    }

    public String getNoteWithOctave()
    {
        return note + octave;
    }

    @NonNull
    @Override
    public String toString()
    {
        return getNoteWithOctave();
    }
}
