package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.raw.models.HoleRaw;
import com.example.harmonicapracticeassistant.raw.models.KewRaw;

import java.util.ArrayList;
import java.util.List;

public class Key
{
    private final String keyName;
    private final boolean isSharp;
    private final List<Hole> holes;

    public Key(String keyName, boolean isSharp, List<Hole> holes)
    {
        this.keyName = keyName;
        this.isSharp = isSharp;
        this.holes = holes;
    }

    public Key(KewRaw rawKey)
    {
        keyName = rawKey.getKeyName();
        isSharp = rawKey.isSharp();
        holes = new ArrayList<>();

        for (HoleRaw raw : rawKey.getHoles())
        {
            Hole newHole = new Hole(raw);
            holes.add(newHole);
        }
    }

    public List<Hole> findHoles(float frequency)
    {
        List<Hole> matchingHoles = new ArrayList<>();

        for (Hole hole : holes)
            if (hole.isWithinFrequencyRange(frequency))
                matchingHoles.add(hole);

        return matchingHoles;
    }

    public String getKeyName()
    {
        return keyName;
    }

    public boolean isSharp()
    {
        return isSharp;
    }
}
