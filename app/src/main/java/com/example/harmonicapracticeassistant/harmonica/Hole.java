package com.example.harmonicapracticeassistant.harmonica;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.utils.HarmonicaUtils;

public class Hole
{
    private Note note;
    private int hole;
    private Bend bend;

    public Hole(Note note)
    {
        this.note = note;
        this.hole = 0;
        this.bend = Bend.NONE;
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
    {
        return hole + HarmonicaUtils.getBendString(context, bend);
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
        return String.format("%s, %d, %d", note, hole, bend);
    }

    public void setFrequency(float frequency)
    {
        note.setFrequency(frequency);
    }
}
