package com.example.harmonicapracticeassistant.harmonica;

import android.content.Context;

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

    public String getNoteWithOctave()
    {
        return note.getNoteWithOctave();
    }

    public String getHoleWithBend(Context context)
    {// TODO: 05/02/2022 remove contect, make the string part of enum
        return holeNumber + HarmonicaUtils.getBendString(context, bend);
    }

    public int getHoleNumber()
    {
        return holeNumber;
    }

    public MusicalNote getMusicalNote()
    {
        return note.getMusicalNote();
    }

    public int getOctave()
    {
        return note.getOctave();
    }

    public float getFrequency()
    {
        return note.getFrequency();
    }

    @NonNull
    @Override
    public String toString()
    {
        return note + "," + holeNumber + "," + bend;
    }

    public void setFrequency(float frequency)
    {
        note.setFrequency(frequency);
    }
}
