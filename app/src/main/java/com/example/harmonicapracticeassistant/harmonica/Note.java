package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.MusicalNote;

import androidx.annotation.NonNull;

@Deprecated
public class Note
{
    private final MusicalNote musicalNote;
    private final int octave;
    private final float frequency;

    public Note(MusicalNote musicalNote, int octave, float frequency)
    {
        this.musicalNote = musicalNote;
        this.octave = octave;
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
