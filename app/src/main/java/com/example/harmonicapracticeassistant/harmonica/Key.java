package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import java.util.List;

public class Key
{
    String key;
    List<Hole> holes;

    public Key(String key, List<Hole> holes)
    {
        this.key = key;
        this.holes = holes;
    }

    public Hole getHole(MusicalNote musicalNote, int octave)
    {
        return holes.stream().findFirst().filter(hole -> hole.isSameNote(musicalNote, octave)).orElse(null);
    }

    public Hole getHole(int i)
    {
        for (Hole hole : holes)
            if (hole.getHole() == i)
                return hole;

        return null;
    }

    public void setHolesFrequencies()
    {
        for (Hole hole : holes)
        {
            hole.setFrequency(NoteFinder.getNoteById(hole.getMusicalNote(), hole.getOctave()).getFrequency());
        }
    }

    public String getKey()
    {
        return key;
    }
}
