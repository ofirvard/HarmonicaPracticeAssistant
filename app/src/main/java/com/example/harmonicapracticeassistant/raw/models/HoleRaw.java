package com.example.harmonicapracticeassistant.raw.models;

import com.example.harmonicapracticeassistant.enums.Bend;
import com.example.harmonicapracticeassistant.enums.MusicalNote;

@Deprecated
public class HoleRaw
{
    private int hole;
    private Bend bend;
    private MusicalNote musicalNote;
    private int octave;

    public int getHole()
    {
        return hole;
    }

    public Bend getBend()
    {
        return bend;
    }

    public MusicalNote getMusicalNote()
    {
        return musicalNote;
    }

    public int getOctave()
    {
        return octave;
    }
}
