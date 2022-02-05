package com.example.harmonicapracticeassistant.harmonica;

import com.example.harmonicapracticeassistant.raw.models.HoleRaw;
import com.example.harmonicapracticeassistant.raw.models.KewRaw;

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
    }

    public String getKeyName()
    {
        return keyName;
    }
}
