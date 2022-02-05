package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.raw.models.HoleRaw;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import androidx.annotation.NonNull;

public class Hole
{
    private final Note note;
    private final int holeNumber;
    private final Bend bend;

    public Hole(Note note)
    {
        this.note = note;
        this.holeNumber = 0;
        this.bend = Bend.NONE;
    }

    public Hole(HoleRaw raw)
    {
        holeNumber = raw.getHole();
        bend = raw.getBend();
        note = NoteFinder.getNoteById(raw.getMusicalNote(), raw.getOctave());
    }

    public boolean isSameNote(MusicalNote musicalNote, int octave)
    {
        return this.note.isSameNote(musicalNote, octave);
    }

    public String getNoteWithOctave(boolean isSharp)
    {
        return note.getNoteWithOctave(isSharp);
    }


    public String getHoleWithBend()
    {
        return holeNumber + HarmonicaUtils.getBendString(bend);
    }

    public MusicalNote getMusicalNote()
    {
        return note.getMusicalNote();
    }

    public float getFrequency()
    {
        return note.getFrequency();
    }

    public int getHole()
    {
        return holeNumber;
    }

    @NonNull
    @Override
    public String toString()
    {
        return note + "," + holeNumber + "," + bend;
    }
}
