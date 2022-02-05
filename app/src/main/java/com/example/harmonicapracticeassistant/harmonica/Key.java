package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.enums.MusicalNote;
import com.example.harmonicapracticeassistant.raw.models.HoleRaw;
import com.example.harmonicapracticeassistant.raw.models.KewRaw;
import com.example.harmonicapracticeassistant.utils.NoteFinder;

import java.util.ArrayList;
import java.util.List;

public class Key
{
    String keyName;
    List<Hole> holes;

    public Key(String keyName, List<Hole> holes)
    {
        this.keyName = keyName;
        this.holes = holes;
    }

    public Key(KewRaw rawKey)
    {
        keyName = rawKey.getKeyName();
        holes = new ArrayList<>();

        for (HoleRaw raw : rawKey.getHoles())
        {
            Hole newHole = new Hole(raw);
            holes.add(newHole);
        }

        holes.sort((o1, o2) -> Integer.compare(o1.getHoleNumber(), o2.getHoleNumber()));
    }

    public Hole getHole(MusicalNote musicalNote, int octave)
    {
        return holes.stream().findFirst().filter(hole -> hole.isSameNote(musicalNote, octave)).orElse(null);
    }

    public Hole getHole(int i)
    {
        return holes.get(i);
    }

    public void setHolesFrequencies()
    {
        for (Hole hole : holes)
        {
            hole.setFrequency(NoteFinder.getNoteById(hole.getMusicalNote(), hole.getOctave()).getFrequency());
        }
    }

    public String getKeyName()
    {
        return keyName;
    }
}
